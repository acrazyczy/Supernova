package Assembly;

import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.LinkedList;

public class asmFunction {
	private final String name;
	public LinkedList<asmBlock> asmBlocks;
	public asmBlock initBlock, retBlock;
	public ArrayList<virtualReg> virtualRegs;
	public stackFrame stkFrame;

	public asmFunction(String name, ArrayList<virtualReg> virtualRegs) {
		this.name = name;
		this.virtualRegs = virtualRegs;
		if (virtualRegs == null) asmBlocks = null;
		else asmBlocks = new LinkedList<>();
	}

	@Override public String toString() {
		return "\t.globl\t" + this.name + "\n\t.p2align\t2\n" + this.name + ":\n";
	}
}
