package LLVMIR;

import LLVMIR.Instruction.br;
import LLVMIR.Instruction.statement;
import LLVMIR.Instruction.terminalStmt;
import LLVMIR.Operand.register;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class basicBlock {
	public ArrayList<statement> stmts = new ArrayList<>();
	private terminalStmt tailStmt = null;
	public final int loopDepth;
	public String name;

	public basicBlock(String name, function currentFunction, int loopDepth) {
		this.name = name;
		this.loopDepth = loopDepth;
		if (currentFunction != null) this.name = this.name + currentFunction.getBlockNameIndex(name);
	}

	public void push_back(statement stmt) {
		if (tailStmt != null) return;
		stmts.add(stmt);
		if (stmt instanceof terminalStmt) tailStmt = (terminalStmt) stmt;
	}

	public ArrayList<basicBlock> successors() {
		ArrayList<basicBlock> ret = new ArrayList<>();
		if (tailStmt instanceof br) {
			ret.add(((br) tailStmt).trueBranch);
			if (((br) tailStmt).falseBranch != null) ret.add(((br) tailStmt).falseBranch);
		}
		return ret;
	}

	public boolean hasNoTerminalStmt() {return tailStmt == null;}

	public Set<register> variables() {
		Set<register> ret = new HashSet<>();
		stmts.forEach(stmt -> ret.addAll(stmt.variables()));
		return ret;
	}

	@Override public String toString() {return name;}
}
