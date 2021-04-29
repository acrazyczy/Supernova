package Assembly.Instruction;

import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

import java.util.LinkedHashSet;
import java.util.Set;

abstract public class inst {
	public inst pre = null, suf = null;
	public virtualReg rd, rs1, rs2;

	/* graph coloring information */
	public Set<virtualReg> defs = new LinkedHashSet<>(), uses = new LinkedHashSet<>();
	public asmBlock belongTo;

	public inst(asmBlock belongTo) {this.belongTo = belongTo;}

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

	public void removeFromBlock() {
		defs.forEach(def -> def.defs.remove(this));
		uses.forEach(use -> use.uses.remove(this));
		if (pre != null) pre.suf = suf;
		else belongTo.headInst = suf;
		if (suf != null) suf.pre = pre;
		else belongTo.tailInst = pre;
	}

	public void removeFromBlockWithoutRelinking() {
		defs.forEach(def -> def.defs.remove(this));
		uses.forEach(use -> use.uses.remove(this));
	}

	public void replaceUse(virtualReg oldReg, virtualReg newReg) {
		uses.remove(oldReg);
		oldReg.uses.remove(this);
		if (rs1 == oldReg || rs2 == oldReg) {
			uses.add(newReg);
			newReg.uses.add(this);
			if (rs1 == oldReg) rs1 = newReg;
			if (rs2 == oldReg) rs2 = newReg;
		}
	}

	public void replaceDef(virtualReg oldReg, virtualReg newReg) {
		defs.remove(oldReg);
		oldReg.defs.remove(this);
		if (rd == oldReg) {
			defs.add(newReg);
			newReg.defs.add(this);
			rd = newReg;
		}
	}

	@Override abstract public String toString();
}
