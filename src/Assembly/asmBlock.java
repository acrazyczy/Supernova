package Assembly;

import Assembly.Instruction.inst;

public class asmBlock {
	private final int index;
	public String comment;
	public inst headInst, tailInst;

	public asmBlock(int index) {
		this.index = index;
		this.headInst = this.tailInst = null;
	}

	public void addInst(inst i) {
		if (headInst == null) headInst = i;
		else (i.pre = tailInst).suf = i;
		tailInst = i;
	}

	@Override public String toString() {return ".Block"  + index + ":\t\t\t" + comment;}
}
