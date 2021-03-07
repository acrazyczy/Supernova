package LLVMIR.TypeSystem;

public class LLVMIntegerType extends LLVMFirstClassType {
	private int width;

	public LLVMIntegerType(int width) {
		super();
		this.width = width;
	}

	@Override public int size() {return width;}

	@Override public String toString() {return "i" + width;}
}
