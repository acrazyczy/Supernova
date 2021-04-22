package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;

import java.util.Objects;

abstract public class constant extends entity {
	public constant(LLVMSingleValueType type) {super(type);}

	@Override abstract public String toString();

	@Override public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		constant that = (constant) obj;
		return type.equals(that.type);
	}

	@Override public int hashCode() {return Objects.hashCode(type);}
}
