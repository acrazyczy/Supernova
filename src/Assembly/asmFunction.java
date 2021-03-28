package Assembly;

import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.LinkedList;

public class asmFunction {
	private final String name;
	public final stackFrame stkFrame;
	public LinkedList<asmBlock> asmBlocks;
	public asmBlock initBlock, retBlock;
	public ArrayList<virtualReg> virtualRegs;

	public asmFunction(String name, stackFrame stkFrame, ArrayList<virtualReg> virtualRegs) {
		this.name = name;
		this.stkFrame = stkFrame;
		this.virtualRegs = virtualRegs;
	}

	@Override public String toString() {return this.name + ":\t";}
}
