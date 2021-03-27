package Assembly.Operand;

public class virtualReg extends reg {
	private int index;

	public virtualReg(int index) {
		super();
		this.index = index;
	}

	@Override public String toString() {return "%" + index;}
}
