package LLVMIR;

import LLVMIR.Instruction.*;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.*;

public class basicBlock {
	// The register may be incorrect during optimization, but it works. So let's just ignore this minor mistake.
	public final Map<register, phi> phiCollections = new LinkedHashMap<>();
	public final Map<phi, register> phiMapping = new LinkedHashMap<>();
	public LinkedList<statement> stmts = new LinkedList<>();
	public terminalStmt tailStmt = null;
	public String name;
	public int loopDepth;

	public basicBlock() {}

	public basicBlock(String name, function currentFunction) {
		this.name = name;
		if (currentFunction != null) this.name = this.name + "." + currentFunction.getBlockNameIndex(name);
	}

	public void push_front(statement stmt) {
		assert !(stmt instanceof terminalStmt);
		stmts.addFirst(stmt);
		stmt.belongTo = this;
		if (stmt instanceof phi) {
			phi stmt_ = (phi) stmt;
			phiCollections.put((register) stmt.dest, stmt_);
			phiMapping.put(stmt_, (register) stmt.dest);
		}
	}

	public void push_back(statement stmt) {
		if (tailStmt != null) return;
		stmts.add(stmt);
		stmt.belongTo = this;
		if (stmt instanceof terminalStmt) tailStmt = (terminalStmt) stmt;
		if (stmt instanceof phi) {
			phi stmt_ = (phi) stmt;
			phiCollections.put((register) stmt.dest, stmt_);
			phiMapping.put(stmt_, (register) stmt.dest);
		}
	}

	public ArrayList<basicBlock> successors() {
		ArrayList<basicBlock> ret = new ArrayList<>();
		if (tailStmt instanceof br) {
			ret.add(((br) tailStmt).trueBranch);
			if (((br) tailStmt).falseBranch != null && ((br) tailStmt).trueBranch != ((br) tailStmt).falseBranch) ret.add(((br) tailStmt).falseBranch);
		}
		return ret;
	}

	public void mergeBlock(basicBlock blk, function func) {
		ArrayList<_move> mv1 = new ArrayList<>(), mv2 = new ArrayList<>();
		for (ListIterator<statement> stmtItr = blk.stmts.listIterator();stmtItr.hasNext();) {
			statement inst = stmtItr.next();
			if (!(inst instanceof phi)) break;
			phi inst_ = (phi) inst;
			assert inst_.values.size() == 1;
			register tmp = new register(inst_.dest.type, "mb.tmp", func);
			_move mvInst = new _move(inst_.values.iterator().next(), tmp);
			tmp.def = mvInst;
			mv1.add(mvInst);
			mvInst = new _move(tmp, inst_.dest);
			((register) inst_.dest).def = mvInst;
			mv2.add(mvInst);
			stmtItr.remove();
		}
		mv2.forEach(mv -> blk.stmts.addFirst(mv));
		mv1.forEach(mv -> blk.stmts.addFirst(mv));
		blk.stmts.forEach(stmt -> stmt.belongTo = this);
		stmts.remove(tailStmt);
		tailStmt = blk.tailStmt;
		stmts.addAll(blk.stmts);
		blk.stmts = null;
	}

	public void splitCallInstruction(basicBlock after, call inst) {
		ListIterator<statement> instItr = stmts.listIterator(stmts.indexOf(inst));
		removeInstruction(instItr.next(), false);
		for (instItr.remove();instItr.hasNext();) {
			statement stmt = instItr.next();
			removeInstruction(stmt, false);
			instItr.remove();
			after.push_back(stmt);
		}
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

	public void removeInstruction(statement stmt, boolean actuallyRemoving) {
		if (stmt instanceof terminalStmt) tailStmt = null;
		if (actuallyRemoving) stmts.remove(stmt);
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
		//noinspection ConstantConditions
		assert !(inst instanceof phi) || !(inst instanceof terminalStmt);
		inst.belongTo = this;
		stmts.add(stmts.size() - 1, inst);
	}

	public boolean hasNoTerminalStmt() {return tailStmt == null;}

	public void variablesAnalysis(Set<register> variables, Set<register> uses, Set<register> defs, Map<register, Set<statement>> usePoses, Map<register, Set<basicBlock>> defPoses) {
		stmts.forEach(stmt -> {
			if (variables != null) variables.addAll(stmt.variables());
			if (uses != null) uses.addAll(stmt.uses());
			if (defs != null) defs.addAll(stmt.defs());
			if (usePoses != null) stmt.uses().forEach(r -> {
				if (usePoses.containsKey(r)) usePoses.get(r).add(stmt);
				else usePoses.put(r, new LinkedHashSet<>(Collections.singleton(stmt)));
			});
			if (defPoses != null) stmt.defs().forEach(r -> {
				if (defPoses.containsKey(r)) defPoses.get(r).add(this);
				else defPoses.put(r, new LinkedHashSet<>(Collections.singleton(this)));
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
