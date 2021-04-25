package Assembly;

import Assembly.Instruction.brInst;
import Assembly.Instruction.inst;
import Assembly.Instruction.jumpInst;
import Assembly.Operand.virtualReg;

import java.util.HashSet;
import java.util.Set;

public class asmBlock {
	private final int index;
	public String comment;
	public inst headInst, tailInst;
	public Set<asmBlock> predecessors, successors;
	public Set<virtualReg> liveOut, liveIn;

	public asmBlock(int index) {
		this.index = index;
		this.headInst = this.tailInst = null;
		this.predecessors = new HashSet<>();
		this.successors = new HashSet<>();
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

	public void mergeBlock(asmBlock blk) {
		for (inst i = headInst;i != null;i = i.suf)
			if (i instanceof jumpInst) {
				assert ((jumpInst) i).label == blk;
				if (i.pre != null) i.pre.suf = i.suf;
				else headInst = i.suf;
				if (i.suf != null) i.suf.pre = i.pre;
				else tailInst = i.pre;
				break;
			}
		for (inst i = blk.headInst;i != null;i = i.suf) i.belongTo = this;
		if (headInst == null) headInst = blk.headInst;
		else (tailInst.suf = blk.headInst).pre = tailInst;
		tailInst = blk.tailInst;
		blk.headInst = null;
		successors = new HashSet<>(blk.successors);
		successors.forEach(sucBlk -> {
			sucBlk.predecessors.remove(blk); sucBlk.predecessors.add(this);
		});
		for (inst i = headInst;i != null && !blk.successors.isEmpty();i = i.suf)
			if (i instanceof jumpInst) blk.successors.remove(((jumpInst) i).label);
			else if (i instanceof brInst) blk.successors.remove(((brInst) i).label);
		assert blk.successors.size() <= 1;
		if (!blk.successors.isEmpty()) tailInst.linkAfter(new jumpInst(tailInst.belongTo, blk.successors.iterator().next()));
	}

	public Set<virtualReg> defs() {
		Set<virtualReg> ret = new HashSet<>();
		for (inst i = headInst;i != null;i = i.suf) ret.addAll(i.defs);
		return ret;
	}

	public Set<virtualReg> uses() {
		Set<virtualReg> ret = new HashSet<>();
		for (inst i = tailInst;i != null;i = i.pre) {
			ret.removeAll(i.defs);
			ret.addAll(i.uses);
		}
		return ret;
	}

	public String name() {return ".Block" + index;}
	@Override public String toString() {return name() + ":\t\t\t# " + comment;}
}
