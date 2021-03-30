package Assembly.Operand;

public class intImm extends Imm {
	public int val;

	public intImm() {super();}

	public intImm(int val) {
		super();
		this.val = val;
	}

	@Override public String toString() {return String.valueOf(val);}
}
