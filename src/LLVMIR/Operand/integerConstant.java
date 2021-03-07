package LLVMIR.Operand;

public class integerConstant extends constant {
	private int width, val;

	public integerConstant(int width, int val) {
		super();
		this.width = width;
		this.val = val;
	}
}
