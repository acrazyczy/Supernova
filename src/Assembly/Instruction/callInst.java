package Assembly.Instruction;

import Assembly.Operand.physicalReg;
import Assembly.asmBlock;
import Assembly.asmFunction;

public class callInst extends inst {
	private final asmFunction func;

	public callInst(asmBlock belongTo, asmFunction func) {
		super(belongTo);
		this.func = func;
		physicalReg.callerSavePRegs.values().stream().
			map(pReg -> physicalReg.pRegToVReg.get(pReg)).forEach(vReg -> def.add(vReg));
	}

	@Override public String toString() {return "call " + func;}
}
