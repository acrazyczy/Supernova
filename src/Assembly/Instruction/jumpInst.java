package Assembly.Instruction;

import Assembly.asmBlock;

public class jumpInst extends inst {
	public final asmBlock label;

	public jumpInst(asmBlock belongTo, asmBlock label) {
		super(belongTo);
		this.label = label;
	}

	@Override public String toString() {return "j " + label.name();}
}
