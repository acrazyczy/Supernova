package LLVMIR.TypeSystem;

public class LLVMPointerType extends LLVMFirstClassType {
	private LLVMFirstClassType pointeeType;

	@Override public int size() {return 32;}

	@Override public String toString() {return pointeeType + "*";}
}
