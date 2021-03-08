package LLVMIR.TypeSystem;

public abstract class LLVMFirstClassType extends LLVMType {
	public LLVMFirstClassType() {super();}

	public abstract int size();
}
