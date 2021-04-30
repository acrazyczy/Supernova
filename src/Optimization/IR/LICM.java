package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.IRLoop;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.register;
import LLVMIR.function;
import Optimization.IR.Analyser.loopAnalyzer;

import java.util.*;

public class LICM implements pass {
	private final IREntry programIREntry;

	public LICM(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private void collectInvariance(statement stmt, Set<register> varInLoop, Queue<statement> workList) {
		if (stmt instanceof load) {
			// TODO: 2021/4/30 alias analysis
		} else if (stmt instanceof call) {
			// TODO: 2021/4/30 more precise sideEffectChecker
		} else {
			if (stmt instanceof binary && (((binary) stmt).inst == binary.instCode.sdiv || ((binary) stmt).inst == binary.instCode.srem))
				return;
			if (stmt instanceof br || stmt instanceof phi || stmt instanceof ret || stmt instanceof store)
				return;
			Set<register> uses = stmt.uses();
			uses.retainAll(varInLoop);
			if (uses.isEmpty()) {
				varInLoop.remove((register) stmt.dest);
				workList.offer(stmt);
			}
		}
	}

	private boolean run(IRLoop loop) {
		boolean changed = false;
		if (!loop.children.isEmpty()) changed = loop.children.stream().map(this::run).reduce(changed, (a, b) -> a || b);

		LinkedHashSet<register> defInLoop = new LinkedHashSet<>();
		LinkedHashMap<register, Set<statement>> useInLoop = new LinkedHashMap<>();
		Queue<statement> workList = new ArrayDeque<>();

		loop.blocks.forEach(blk -> blk.variablesAnalysis(null, null, defInLoop, useInLoop, null));
		loop.blocks.forEach(blk -> blk.stmts.forEach(stmt -> collectInvariance(stmt, defInLoop, workList)));

		changed |= !workList.isEmpty();
		while (!workList.isEmpty()) {
			statement stmt = workList.poll();
			stmt.belongTo.removeInstruction(stmt, true);
			loop.preHead.insertInstructionBeforeTail(stmt);
			if (useInLoop.containsKey((register) stmt.dest))
				useInLoop.get((register) stmt.dest).forEach(u -> {
					if (defInLoop.contains((register) u.dest))
						collectInvariance(u, defInLoop, workList);
				});
		}

		return changed;
	}

	private boolean run(function func) {
		loopAnalyzer loopProperty = new loopAnalyzer(func, true);
		loopProperty.run();
		return loopProperty.rootLoops.stream().map(this::run).reduce(false, (a, b) -> a || b);
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
