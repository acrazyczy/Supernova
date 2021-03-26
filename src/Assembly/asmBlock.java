package Assembly;

import Assembly.Instruction.inst;

import java.util.ArrayList;

public class asmBlock {
	private final int index;
	private final String comment;
	public ArrayList<inst> insts;

	public asmBlock(int index, String comment) {
		this.index = index;
		this.comment = comment;
	}

	@Override public String toString() {return ".Block"  + index + ":\t\t\t" + comment;}
}
