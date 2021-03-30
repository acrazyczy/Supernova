package Assembly.Operand;

public class relocationImm extends Imm {
	public enum type {
		hi, lo
	}

	private final type ty;
	private final globalData data;

	public relocationImm(type ty, globalData data) {
		super();
		this.ty = ty;
		this.data = data;
	}

	@Override public String toString() {return "%" + ty + "(" + data.name + ")";}
}
