package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.integerConstant;
import LLVMIR.Operand.undefinedValue;
import LLVMIR.TypeSystem.LLVMPointerType;

public class _move extends statement {
	public entity src;

	public _move(entity src, entity dest) {
		super(dest);
		this.src = src;
	}

	@Override
	public String toString() {
		if (src instanceof undefinedValue) return dest + " = " + src;
		if (src.type instanceof LLVMPointerType) return dest + " = getelementptr " + ((LLVMPointerType) src.type).pointeeType + ", " + src.type + " " + src + ", i32 0";
		return (new binary(binary.instCode.add, new integerConstant(src.type.size(), 0), src, dest)).toString();
	}
}
