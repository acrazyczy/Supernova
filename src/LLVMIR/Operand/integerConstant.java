package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;

import java.util.Objects;

public class integerConstant extends constant {
	public int val;

	public integerConstant(int width, int val) {
		super(new LLVMIntegerType(width));
		this.val = val;
	}

	@Override public String toString() {return String.valueOf(val);}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		integerConstant that = (integerConstant) o;
		return val == that.val;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), val);
	}
}
