package Assembly.Operand;

public class physicalReg extends reg {
	private final String name;

	public physicalReg(String name) {
		super();
		this.name = name;
	}

	@Override public String toString() {return name;}
}