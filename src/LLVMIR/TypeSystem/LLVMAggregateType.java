package LLVMIR.TypeSystem;

abstract public class LLVMAggregateType extends LLVMFirstClassType {
	public LLVMAggregateType() {super();}

	@Override abstract public String toString();
	@Override abstract public boolean equals(Object obj);
	@Override abstract public int hashCode();
}
