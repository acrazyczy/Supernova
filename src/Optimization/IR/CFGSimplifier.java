package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.br;
import LLVMIR.Operand.booleanConstant;
import LLVMIR.basicBlock;
import LLVMIR.function;
import Optimization.IR.Analyser.callingAnalyser;

import java.util.*;

public class CFGSimplifier implements pass {
	private final IREntry programIREntry;

	public CFGSimplifier(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private boolean constantConditionReplacement(function func) {
		boolean changed = false;
		for (basicBlock blk: func.blocks)
			if (blk.tailStmt instanceof br && ((br) blk.tailStmt).cond instanceof booleanConstant) {
				if (((booleanConstant) ((br) blk.tailStmt).cond).val == 0) {
					basicBlock tmp = ((br) blk.tailStmt).trueBranch;
					((br) blk.tailStmt).trueBranch = ((br) blk.tailStmt).falseBranch;
					((br) blk.tailStmt).falseBranch = tmp;
				}
				((br) blk.tailStmt).falseBranch.removeBlockFromPhi(blk);
				((br) blk.tailStmt).cond = null;
				((br) blk.tailStmt).falseBranch = null;
				changed = true;
			}
		return changed;
	}

	private boolean checkSuccessor(Map<basicBlock, Set<basicBlock>> predecessors, basicBlock blk) {
		return predecessors.get(blk).size() == 1 && (!(blk.tailStmt instanceof br) || ((br) blk.tailStmt).cond == null);
	}

	private boolean removeRedundantJump(function func) {
		boolean changed = false;
		List<basicBlock> blocks = new ArrayList<>(func.blocks);
		Map<basicBlock, Set<basicBlock>> predecessors = new LinkedHashMap<>();
		blocks.forEach(blk -> predecessors.put(blk, new LinkedHashSet<>()));
		blocks.forEach(blk -> blk.successors().forEach(sucBlk -> predecessors.get(sucBlk).add(blk)));
		for (basicBlock blk: blocks) {
			if (blk.stmts == null) continue;
			while (blk.successors().size() == 1 && checkSuccessor(predecessors, blk.successors().iterator().next())) {
				basicBlock sucBlk = blk.successors().iterator().next();
				blk.mergeBlock(sucBlk, func);
				func.blocks.remove(sucBlk);
				blk.successors().forEach(b -> {
					predecessors.get(b).remove(sucBlk); predecessors.get(b).add(blk);
					b.replacePredecessor(sucBlk, blk);
				});
				changed = true;
			}
		}
		return changed;
	}

	private boolean removeRedundantBranch(function func) {
		boolean changed = false;
		List<basicBlock> blocks = new ArrayList<>(func.blocks);
		for (basicBlock blk: blocks)
			if (blk.tailStmt instanceof br && ((br) blk.tailStmt).cond != null)
				if (((br) blk.tailStmt).trueBranch == ((br) blk.tailStmt).falseBranch) {
					((br) blk.tailStmt).cond = null;
					((br) blk.tailStmt).falseBranch = null;
					changed = true;
				} else if (((br) blk.tailStmt).trueBranch.tailStmt instanceof br && ((br) blk.tailStmt).falseBranch.tailStmt instanceof br) {
					basicBlock tb = ((br) blk.tailStmt).trueBranch, fb = ((br) blk.tailStmt).falseBranch;
					br br1 = (br) tb.tailStmt, br2 = (br) fb.tailStmt;
					if (((br) blk.tailStmt).trueBranch.stmts.size() == 1 && ((br) blk.tailStmt).falseBranch.stmts.size() == 1)
						if (br1.cond == null && br2.cond == null && br1.trueBranch == br2.trueBranch) {
							basicBlock dest = br1.trueBranch;
							if (dest.phiCollections.isEmpty()) {
								((br) blk.tailStmt).cond = null;
								((br) blk.tailStmt).trueBranch = dest;
								((br) blk.tailStmt).falseBranch = null;
								func.blocks.remove(tb);
								func.blocks.remove(fb);
							}
						}
				}
		return changed;
	}

	private void dfs(function func, Set<function> isVisited, callingAnalyser callingProperty) {
		if (isVisited.contains(func)) return;
		isVisited.add(func);
		callingProperty.callee.get(func).forEach(callee -> dfs(callee, isVisited, callingProperty));
	}

	private boolean removeUnusedFunction() {
		callingAnalyser callingProperty = new callingAnalyser(programIREntry);
		callingProperty.run();
		Set<function> isVisited = new LinkedHashSet<>();
		dfs(programIREntry.functions.iterator().next(), isVisited, callingProperty);
		ArrayList<function> funcs = new ArrayList<>(programIREntry.functions);
		boolean changed = funcs.stream().anyMatch(func -> func.blocks != null && !isVisited.contains(func));
		funcs.stream()
			.filter(func -> func.blocks != null && !isVisited.contains(func))
			.forEach(programIREntry.functions::remove);
		return changed;
	}

	private boolean run(function func) {
		boolean changed = constantConditionReplacement(func);
		changed |= removeRedundantBranch(func);
		changed |= removeRedundantJump(func);
		return changed;
	}

	@Override public boolean run() {
		boolean changed = removeUnusedFunction();
		changed |= programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
		return changed;
	}
}
