package LLVMIR.TypeSystem;

public class LLVMPointerType extends LLVMSingleValueType {
	public LLVMSingleValueType pointeeType;

	public LLVMPointerType(LLVMSingleValueType pointeeType) {
		super();
		this.pointeeType = pointeeType;
	}

	@Override public int size() {return 32;}

	@Override public String toString() {return pointeeType + "*";}
}
