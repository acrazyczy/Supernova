package LLVMIR;

import LLVMIR.Instruction.br;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Instruction.terminalStmt;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.*;

public class basicBlock {
	public final Map<register, phi> phiCollections = new HashMap<>();
	public final Map<phi, register> phiMapping = new HashMap<>();
	public LinkedList<statement> stmts = new LinkedList<>();
	public terminalStmt tailStmt = null;
	public String name;

	public basicBlock() {}

	public basicBlock(String name, function currentFunction) {
		this.name = name;
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

	public boolean replaceSuccessor(basicBlock oldSuc, basicBlock newSuc) {
		assert tailStmt instanceof br;
		boolean ret = false;
		if (((br) tailStmt).trueBranch == oldSuc) {
			((br) tailStmt).trueBranch = newSuc;
			ret = true;
		}
		if (((br) tailStmt).falseBranch == oldSuc) ((br) tailStmt).falseBranch = newSuc;
		return ret;
	}

	public void replacePredecessor(basicBlock oldPre, basicBlock newPre) {
		phiCollections.values().forEach(phiInst -> {
			for (ListIterator<basicBlock> blkItr = phiInst.blocks.listIterator();blkItr.hasNext();)
				if (blkItr.next() == oldPre) {
					blkItr.set(newPre);
					break;
				}
		});
	}

	public void removeAllPhi() {
		phiCollections.clear();
		phiMapping.clear();
		for (ListIterator<statement> instItr = stmts.listIterator();instItr.hasNext();) {
			if (!(instItr.next() instanceof phi)) break;
			instItr.remove();
		}
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

	public void replaceInstruction(statement oldInst, statement newInst) {
		assert !(oldInst instanceof phi) && !(oldInst instanceof terminalStmt);
		newInst.belongTo = this;
		for (int i = 0;i < stmts.size();++ i)
			if (stmts.get(i) == oldInst)
				stmts.set(i, newInst);
	}

	public void removeInstruction(statement stmt) {
		if (stmt instanceof terminalStmt) tailStmt = null;
		stmts.remove(stmt);
		if (stmt instanceof phi) {
			phiCollections.remove(phiMapping.get(stmt));
			phiMapping.remove(stmt);
		}
	}

	public void insertInstructionAfter(statement inst, statement afterInst) {
		assert !(inst instanceof phi) || afterInst instanceof phi;
		if (afterInst == null) insertInstructionAfterPhi(inst);
		else if (afterInst instanceof phi) insertInstructionAfterPhi(inst);
		else {
			inst.belongTo = this;
			stmts.add(stmts.indexOf(afterInst) + 1, inst);
			if (inst instanceof terminalStmt) {
				assert tailStmt == null;
				tailStmt = (terminalStmt) inst;
			}
		}
	}

	public void insertInstructionAfterPhi(statement inst) {
		inst.belongTo = this;
		stmts.add(phiCollections.size(), inst);
		if (inst instanceof phi) {
			phiCollections.put((register) inst.dest, (phi) inst);
			phiMapping.put((phi) inst, (register) inst.dest);
		} else if (inst instanceof terminalStmt) {
			assert tailStmt == null;
			tailStmt = (terminalStmt) inst;
		}
	}

	public void insertInstructionBeforeTail(statement inst) {
		assert !(inst instanceof phi) || !(inst instanceof terminalStmt);
		inst.belongTo = this;
		stmts.add(stmts.size() - 1, inst);
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
		phiMapping.put(phiInst, v);
		stmts.addFirst(phiInst);
	}
}
