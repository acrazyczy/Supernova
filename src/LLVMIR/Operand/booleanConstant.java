package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;

import java.util.Objects;

public class booleanConstant extends constant {
	public final int val;

	public booleanConstant(int val) {
		super(new LLVMIntegerType(8, true));
		this.val = val;
	}

	@Override public String toString() {return String.valueOf(val);}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		booleanConstant that = (booleanConstant) o;
		return val == that.val;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), val);
	}
}