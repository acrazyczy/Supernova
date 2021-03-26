package LLVMIR.TypeSystem;

public class LLVMArrayType extends LLVMAggregateType {
	private final int cnt;
	private final LLVMFirstClassType elementType;

	public LLVMArrayType(int cnt, LLVMSingleValueType elementType) {
		super();
		this.cnt = cnt;
		this.elementType = elementType;
	}

	@Override public int size() {return cnt * elementType.size();}

	@Override public String toString() {return "[ " + cnt + " x " + elementType + " ]";}
}
