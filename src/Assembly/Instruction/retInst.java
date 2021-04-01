package Assembly.Instruction;

import Assembly.Operand.physicalReg;
import Assembly.asmBlock;

public class retInst extends inst {
	public retInst(asmBlock belongTo) {
		super(belongTo);
		this.uses.add(physicalReg.pRegToVReg.get(physicalReg.pRegs.get("ra")));
		physicalReg.pRegToVReg.get(physicalReg.pRegs.get("ra")).uses.add(this);
	}

	@Override public String toString() {return "ret";}
}
