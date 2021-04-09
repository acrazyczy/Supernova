package LLVMIR.Instruction;

import LLVMIR.Operand.register;

abstract public class terminalStmt extends statement {
	public terminalStmt() {super();}

	@Override abstract public void replaceUse(register oldReg, register newReg);
}
