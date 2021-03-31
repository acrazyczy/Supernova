package Assembly;

import Assembly.Instruction.inst;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class asmBlock {
	private final int index;
	public String comment;
	public inst headInst, tailInst;
	public ArrayList<asmBlock> predecessors, successors;
	public Set<virtualReg> liveOut, liveIn;
	public int loopDepth;

	public asmBlock(int index, int loopDepth) {
		this.index = index;
		this.headInst = this.tailInst = null;
		this.loopDepth = loopDepth;
		this.predecessors = new ArrayList<>();
		this.successors = new ArrayList<>();
	}

	public void addSuccessor(asmBlock asmBlk) {
		successors.add(asmBlk);
		asmBlk.predecessors.add(this);
	}

	public void addInst(inst i) {
		if (headInst == null) headInst = i;
		else (i.pre = tailInst).suf = i;
		tailInst = i;
	}

	public Set<virtualReg> defs() {
		Set<virtualReg> ret = new HashSet<>();
		for (inst i = headInst;i != null;i = i.suf) ret.addAll(i.defs);
		return ret;
	}

	public Set<virtualReg> uses() {
		Set<virtualReg> ret = new HashSet<>();
		for (inst i = headInst;i != null;i = i.suf) ret.addAll(i.uses);
		return ret;
	}

	public String name() {return ".Block" + index;}
	@Override public String toString() {return name() + ":\t\t\t# " + comment;}
}
