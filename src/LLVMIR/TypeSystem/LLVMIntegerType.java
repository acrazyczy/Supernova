package LLVMIR.TypeSystem;

import java.util.Objects;

public class LLVMIntegerType extends LLVMSingleValueType {
	private final int width;
	public final boolean is_boolean;

	public LLVMIntegerType() {this(32);}

	public LLVMIntegerType(int width) {this(width, false);}

	public LLVMIntegerType(int width, boolean is_boolean) {
		super();
		this.width = width;
		this.is_boolean = is_boolean;
	}

	@Override public int size() {return width;}

	@Override public String toString() {return "i" + (is_boolean ? 1 : width);}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LLVMIntegerType that = (LLVMIntegerType) o;
		return width == that.width && is_boolean == that.is_boolean;
	}

	@Override public int hashCode() {return Objects.hash(width, is_boolean);}
}
