package LLVMIR.TypeSystem;

public class LLVMArrayType extends LLVMAggregateType {
	private int length;
	private LLVMFirstClassType elementType;

	@Override public int size() {return length * elementType.size();}

	@Override public String toString() {return "[" + length + "x" + elementType + "]";}
}
