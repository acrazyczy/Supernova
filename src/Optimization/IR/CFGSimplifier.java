package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.br;
import LLVMIR.Operand.booleanConstant;
import LLVMIR.basicBlock;
import LLVMIR.function;

public class CFGSimplifier implements pass {
	private IREntry programIREntry;

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

	private boolean removeRedundantBranch(function func) {
		boolean changed = false;
		for (basicBlock blk: func.blocks)
			if (blk.tailStmt instanceof br && ((br) blk.tailStmt).cond != null)
				if (((br) blk.tailStmt).trueBranch == ((br) blk.tailStmt).falseBranch) {
					((br) blk.tailStmt).cond = null;
					((br) blk.tailStmt).falseBranch = null;
					changed = true;
				}
		return changed;
	}

	private boolean run(function func) {
		boolean changed = constantConditionReplacement(func);
		changed |= removeRedundantBranch(func);
		return changed;
	}

	@Override public boolean run() {return programIREntry.functions.stream().filter(func -> func.blocks != null).anyMatch(this::run);}
}
