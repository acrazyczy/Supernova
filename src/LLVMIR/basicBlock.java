package LLVMIR;

import LLVMIR.Instruction.br;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Instruction.terminalStmt;
import LLVMIR.Operand.register;

import java.util.*;

public class basicBlock {
	public Map<register, phi> phiCollections = new HashMap<>();
	public List<statement> stmts = new LinkedList<>();
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

	public void variablesAnalysis(Set<register> variables, Set<register> uses, Set<register> defs, Map<register, Set<basicBlock>> usePoses, Map<register, Set<basicBlock>> defPoses) {
		stmts.forEach(stmt -> {
			if (variables != null) variables.addAll(stmt.variables());
			if (uses != null) uses.addAll(stmt.uses());
			if (defs != null) defs.addAll(stmt.defs());
			if (usePoses != null) stmt.uses().forEach(r -> {
				if (usePoses.containsKey(r)) usePoses.get(r).add(this);
				else usePoses.put(r, new HashSet<>(Collections.singleton(this)));
			});
			if (defPoses != null) stmt.defs().forEach(r -> {
				if (defPoses.containsKey(r)) defPoses.get(r).add(this);
				else defPoses.put(r, new HashSet<>(Collections.singleton(this)));
			});
		});
	}

	@Override public String toString() {return name;}

	public void addPhiFunction(register v, basicBlock x) {
		if (!phiCollections.containsKey(v)) {
			phiCollections.put(v, new phi(this, new ArrayList<>(), new ArrayList<>(), v));
		}
		phiCollections.get(v).add(v, x);
	}
}
