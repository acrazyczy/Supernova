package LLVMIR;

import LLVMIR.Instruction.br;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Instruction.terminalStmt;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.*;

public class basicBlock {
	public Map<register, phi> phiCollections = new HashMap<>();
	public LinkedList<statement> stmts = new LinkedList<>();
	private terminalStmt tailStmt = null;
	public final int loopDepth;
	public String name;

	public basicBlock(String name, function currentFunction, int loopDepth) {
		this.name = name;
		this.loopDepth = loopDepth;
		if (currentFunction != null) this.name = this.name + currentFunction.getBlockNameIndex(name);
	}

	public void push_front(statement stmt) {
		assert !(stmt instanceof terminalStmt);
		stmts.addFirst(stmt);
		stmt.belongTo = this;
	}

	public void push_back(statement stmt) {
		if (tailStmt != null) return;
		stmts.add(stmt);
		stmt.belongTo = this;
		if (stmt instanceof terminalStmt) tailStmt = (terminalStmt) stmt;
	}

	public ArrayList<basicBlock> successors() {
		ArrayList<basicBlock> ret = new ArrayList<>();
		if (tailStmt instanceof br) {
			ret.add(((br) tailStmt).trueBranch);
			if (((br) tailStmt).falseBranch != null && ((br) tailStmt).trueBranch != ((br) tailStmt).falseBranch) ret.add(((br) tailStmt).falseBranch);
		}
		return ret;
	}

	public void removeSuccessor(basicBlock sucBlk) {
		assert tailStmt instanceof br;
		if (((br) tailStmt).trueBranch == sucBlk)
			((br) tailStmt).trueBranch = ((br) tailStmt).falseBranch;
		((br) tailStmt).falseBranch = null;
		((br) tailStmt).cond = null;
	}

	public void removeBlockFromPhi(basicBlock preBlk) {
		phiCollections.values().forEach(phiInst -> {
			Iterator<basicBlock> blkItr = phiInst.blocks.iterator();
			Iterator<entity> valItr = phiInst.values.iterator();
			for (int i = 0;i < phiInst.blocks.size();++ i) {
				valItr.next();
				if (blkItr.next() == preBlk) {
					valItr.remove();
					blkItr.remove();
					break;
				}
			}
		});
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

	@Override public String toString() {return "%" + name;}

	public void addPhiFunction(register v, LinkedList<basicBlock> blocks) {
		assert !phiCollections.containsKey(v);
		LinkedList<entity> values = new LinkedList<>();
		for (int i = 0;i < blocks.size();++ i) values.add(v);
		phi phiInst = new phi(blocks, values, v);
		phiInst.belongTo = this;
		phiCollections.put(v, phiInst);
		stmts.addFirst(phiInst);
	}
}
