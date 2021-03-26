package Assembly.Operand;

public class virtualReg extends register {
	private int index;

	public virtualReg(int index) {
		super();
		this.index = index;
	}

	@Override public String toString() {return "%" + index;}
}
