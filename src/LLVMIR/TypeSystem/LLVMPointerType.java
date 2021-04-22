package LLVMIR.TypeSystem;

import java.util.Objects;

public class LLVMPointerType extends LLVMSingleValueType {
	public final LLVMFirstClassType pointeeType;

	public LLVMPointerType(LLVMFirstClassType pointeeType) {
		super();
		this.pointeeType = pointeeType;
	}

	@Override public int size() {return 32;}

	@Override public String toString() {return pointeeType + "*";}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LLVMPointerType that = (LLVMPointerType) o;
		return pointeeType.equals(that.pointeeType);
	}

	@Override public int hashCode() {return Objects.hash(pointeeType);}
}
