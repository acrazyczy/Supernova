package Assembly.Instruction;

import Assembly.Operand.globalData;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class laInst extends inst {
	private final globalData symbol;

	public laInst(asmBlock belongTo, virtualReg rd, globalData symbol) {
		super(belongTo);
		this.defs.add(this.rd = rd);
		rd.defs.add(this);
		this.symbol = symbol;
	}

	@Override public String toString() {return "la " + rd + ", " + symbol.name;}
}
