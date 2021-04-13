package LLVMIR.Instruction;

import LLVMIR.Operand.booleanConstant;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.integerConstant;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class _move extends statement {
	public entity src;

	public _move(entity src, entity dest) {
		super(dest);
		this.src = src;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new HashSet<>();
		if (src instanceof register) ret.add((register) src);
		ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return src instanceof register ? new HashSet<>(Collections.singleton((register) src)) : new HashSet<>();
	}

	@Override public void replaceUse(entity oldReg, entity newReg) {if (src == oldReg) src = newReg;}

	@Override
	public String toString() {
		if (src.type instanceof LLVMPointerType) return dest + " = getelementptr " + ((LLVMPointerType) src.type).pointeeType + ", " + src.type + " " + src + ", i32 0";
		if (((LLVMIntegerType) src.type).is_boolean) return (new binary(binary.instCode.xor, new booleanConstant(0), src, dest)).toString();
		return (new binary(binary.instCode.add, new integerConstant(src.type.size(), 0), src, dest)).toString();
	}
}
