package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.Set;

abstract public class terminalStmt extends statement {
	public terminalStmt() {super();}

	@Override abstract public void replaceUse(entity oldReg, entity newReg);

	@Override abstract public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef);

	@Override abstract public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr);

	@Override abstract public statement clone();
}
