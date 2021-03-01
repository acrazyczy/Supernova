package LLVMIR;

import LLVMIR.Instruction.br;
import LLVMIR.Instruction.jump;
import LLVMIR.Instruction.statement;
import LLVMIR.Instruction.terminalStmt;
import Util.error.internalError;
import Util.position;

import java.util.ArrayList;

public class basicBlock {
	private ArrayList<statement> stmts = new ArrayList<>();
	private terminalStmt tailStmt = null;

	public basicBlock() {}

	public void push_back(statement stmt) {
		stmts.add(stmt);
		if (stmt instanceof terminalStmt) {
			if (tailStmt != null)
				throw new internalError("multiple tails of a block", new position(0, 0));
			tailStmt = (terminalStmt) stmt;
		}
	}

	public ArrayList<basicBlock> successors() {
		ArrayList<basicBlock> ret = new ArrayList<>();
		if (tailStmt instanceof br) {
			ret.add(((br) tailStmt).trueBranch);
			ret.add(((br) tailStmt).falseBranch);
		} else if (tailStmt instanceof jump) {
			ret.add(((jump) tailStmt).destination);
		}
		return ret;
	}
}
