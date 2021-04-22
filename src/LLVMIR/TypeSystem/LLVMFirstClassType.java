package LLVMIR.TypeSystem;

abstract public class LLVMFirstClassType extends LLVMType {
	public LLVMFirstClassType() {super();}

	abstract public int size();

	@Override abstract public String toString();
	@Override abstract public boolean equals(Object obj);
	@Override abstract public int hashCode();
}
