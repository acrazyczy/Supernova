package LLVMIR.TypeSystem;

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
}
