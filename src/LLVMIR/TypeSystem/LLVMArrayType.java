package LLVMIR.TypeSystem;

import java.util.Objects;

public class LLVMArrayType extends LLVMAggregateType {
	private final int cnt;
	private final LLVMFirstClassType elementType;

	public LLVMArrayType(int cnt, LLVMSingleValueType elementType) {
		super();
		this.cnt = cnt;
		this.elementType = elementType;
	}

	@Override public int size() {return cnt * elementType.size();}

	@Override public String toString() {return "[ " + cnt + " x " + elementType + " ]";}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LLVMArrayType that = (LLVMArrayType) o;
		return cnt == that.cnt && elementType.equals(that.elementType);
	}

	@Override public int hashCode() {return Objects.hash(cnt, elementType);}
}
