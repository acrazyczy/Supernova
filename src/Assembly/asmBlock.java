package Assembly;

import Assembly.Instruction.inst;

public class asmBlock {
	private final int index;
	private final String comment;
	public inst head, tail;

	public asmBlock(int index, String comment) {
		this.index = index;
		this.comment = comment;
		this.head = this.tail = null;
	}

	public void addInst(inst i) {
		if (head == null) head = i;
		else (i.pre = tail).suf = i;
		tail = i;
	}

	@Override public String toString() {return ".Block"  + index + ":\t\t\t" + comment;}
}
