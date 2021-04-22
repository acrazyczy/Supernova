package LLVMIR.TypeSystem;

abstract public class LLVMType {
	public LLVMType() {}

	@Override abstract public String toString();
	@Override abstract public boolean equals(Object obj);
	@Override abstract public int hashCode();
}
