package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

abstract public class terminalStmt extends statement {
	public terminalStmt() {super();}

	@Override abstract public void replaceUse(entity oldReg, entity newReg);
}
