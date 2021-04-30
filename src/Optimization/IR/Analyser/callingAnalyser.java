package Optimization.IR.Analyser;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.call;
import LLVMIR.function;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class callingAnalyser implements pass {
	private final IREntry programIREntry;

	public Map<function, Set<function>> caller, callee;
	public Map<function, Map<function, Set<call>>> callInst;

	public callingAnalyser(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		caller = new LinkedHashMap<>();
		callee = new LinkedHashMap<>();
		callInst = new LinkedHashMap<>();
		programIREntry.functions.forEach(func -> {
			caller.put(func, new LinkedHashSet<>()); callee.put(func, new LinkedHashSet<>()); callInst.put(func, new LinkedHashMap<>());
		});
		programIREntry.functions.stream()
			.filter(func -> func.blocks != null)
			.forEach(func -> func.blocks.forEach(blk -> blk.stmts.stream().filter(stmt -> stmt instanceof call)
				.forEach(stmt -> {
					call stmt_ = (call) stmt;
					caller.get(stmt_.callee).add(func);
					callee.get(func).add(stmt_.callee);
					if (!callInst.get(func).containsKey(stmt_.callee)) callInst.get(func).put(stmt_.callee, new LinkedHashSet<>());
					callInst.get(func).get(stmt_.callee).add(stmt_);
				})
			));
		return false;
	}
}
