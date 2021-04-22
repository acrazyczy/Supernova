package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;

public class nullPointerConstant extends constant {
	public nullPointerConstant() {super(new LLVMPointerType(new LLVMIntegerType(8)));}

	@Override public String toString() {return "null";}

	@Override public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return super.equals(obj);
	}

	@Override public int hashCode() {return super.hashCode();}
}
