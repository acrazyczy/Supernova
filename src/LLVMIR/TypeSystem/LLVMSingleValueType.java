package LLVMIR.TypeSystem;

abstract public class LLVMSingleValueType extends LLVMFirstClassType {
	public LLVMSingleValueType() {super();}

	@Override abstract public String toString();
	@Override abstract public boolean equals(Object obj);
	@Override abstract public int hashCode();
}
