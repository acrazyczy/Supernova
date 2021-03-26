package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;

public class nullPointerConstant extends constant {
	public nullPointerConstant() {super(new LLVMPointerType(new LLVMIntegerType(8)));}

	@Override public String toString() {return "null";}
}
