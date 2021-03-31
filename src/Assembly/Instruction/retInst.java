package Assembly.Instruction;

import Assembly.asmBlock;

public class retInst extends inst {
	public retInst(asmBlock belongTo) {super(belongTo);}

	@Override public String toString() {return "ret";}
}
