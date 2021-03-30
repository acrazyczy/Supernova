package Assembly.Instruction;

import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

import java.util.HashSet;
import java.util.Set;

abstract public class inst {
	public inst pre = null, suf = null;
	public virtualReg rd, rs1, rs2;
	private String comment = null;

	/* graph coloring information */
	public Set<virtualReg> def = new HashSet<>(), use = new HashSet<>();
	public asmBlock belongTo;

	public inst(asmBlock belongTo) {this.belongTo = belongTo;}
	public inst(String comment, asmBlock belongTo) {
		this.comment = comment;
		this.belongTo = belongTo;
	}

	public void linkBefore(inst newInst) {
		if (pre != null) pre.suf = newInst;
		else belongTo.headInst = newInst;
		newInst.pre = pre;
		(newInst.suf = this).pre = newInst;
	}

	public void linkAfter(inst newInst) {
		if (suf != null) suf.pre = newInst;
		else belongTo.tailInst = newInst;
		newInst.suf = suf;
		(newInst.pre = this).suf = newInst;
	}

	public void replaceUse(virtualReg oldReg, virtualReg newReg) {
	}

	public void replaceDef(virtualReg oldReg, virtualReg newReg) {
		if (rd == oldReg) {
			rd = newReg;
			def.remove(oldReg);
			def.add(newReg);
		}
	}

	@Override abstract public String toString();
}
