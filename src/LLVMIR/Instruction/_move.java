package LLVMIR.Instruction;

import LLVMIR.Operand.booleanConstant;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.integerConstant;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

public class _move extends statement {
	public entity src;

	public _move(entity src, entity dest) {
		super(dest);
		this.src = src;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new LinkedHashSet<>();
		if (src instanceof register) ret.add((register) src);
		ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return src instanceof register ? new LinkedHashSet<>(Collections.singleton((register) src)) : new LinkedHashSet<>();
	}

	@Override public void replaceUse(entity oldReg, entity newReg) {if (src == oldReg) src = newReg;}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		src = replacer.apply(expr, newDef, src);
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		return tester.test(SCC, hdr, src);
	}

	@Override public statement clone() {return new _move(src, dest);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		dest = replacer.apply((register) dest);
		if (src instanceof register) src = replacer.apply((register) src);
	}

	@Override
	public String toString() {
		if (src.type instanceof LLVMPointerType) return dest + " = getelementptr " + ((LLVMPointerType) src.type).pointeeType + ", " + src.type + " " + src + ", i32 0";
		if (((LLVMIntegerType) src.type).is_boolean) return (new binary(binary.instCode.xor, new booleanConstant(0), src, dest)).toString();
		return (new binary(binary.instCode.add, new integerConstant(src.type.size(), 0), src, dest)).toString();
	}
}
