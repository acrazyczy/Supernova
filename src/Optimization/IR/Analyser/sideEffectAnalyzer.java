package Optimization.IR.Analyser;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.*;
import LLVMIR.function;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class sideEffectAnalyzer implements pass {
	private final IREntry programIREntry;

	public Set<function> sideEffectFunctions;
	public Set<statement> sideEffectStatements;

	private Map<function, Set<function>> callerFunctionLists;
	private Map<function, Set<statement>> callerInstructionLists;

	public sideEffectAnalyzer(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private void sideEffectComputation(function func) {
		func.blocks.forEach(blk -> {
			blk.stmts.forEach(stmt -> {
				if (stmt instanceof store || stmt instanceof ret) sideEffectStatements.add(stmt);
				else if (stmt instanceof call) {
					callerFunctionLists.get(((call) stmt).callee).add(func);
					callerInstructionLists.get(((call) stmt).callee).add(stmt);
				}
			});
			if (!sideEffectFunctions.contains(func) && blk.stmts.stream().anyMatch(stmt -> sideEffectStatements.contains(stmt)))
				sideEffectFunctions.add(func);
		});
	}

	private Set<function> isVisited;

	private void sideEffectPropagation(function func) {
		isVisited.add(func);
		sideEffectFunctions.add(func);
		callerFunctionLists.get(func).stream().filter(caller -> !isVisited.contains(caller)).forEach(this::sideEffectPropagation);
	}

	@Override
	public boolean run() {
		sideEffectFunctions = new LinkedHashSet<>();
		sideEffectStatements = new LinkedHashSet<>();
		callerFunctionLists = new LinkedHashMap<>();
		callerInstructionLists = new LinkedHashMap<>();
		programIREntry.functions.forEach(func -> {
			callerFunctionLists.put(func, new LinkedHashSet<>());
			callerInstructionLists.put(func, new LinkedHashSet<>());
		});
		programIREntry.functions.stream().filter(func -> func.blocks != null).forEach(this::sideEffectComputation);
		isVisited = new LinkedHashSet<>();
		for (function func: programIREntry.functions) {
			if (func.blocks == null
				&& !func.functionName.equals("print")
				&& !func.functionName.equals("println")
				&& !func.functionName.equals("printInt")
				&& !func.functionName.equals("printlnInt")
				&& !func.functionName.equals("getString")
				&& !func.functionName.equals("getInt")
				|| isVisited.contains(func) || func.blocks != null && !sideEffectFunctions.contains(func)) continue;
			sideEffectPropagation(func);
		}
		programIREntry.functions.stream().filter(func -> sideEffectFunctions.contains(func)).forEach(func -> sideEffectStatements.addAll(callerInstructionLists.get(func)));
		return true;
	}
}
