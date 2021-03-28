package Assembly.Operand;

public class globalData extends operand {
	private final String name;
	private final int int_val;
	private final String str_val;
	private final boolean is_constant;

	public globalData(String name, int value, boolean is_constant) {
		this.name = name;
		this.int_val = value;
		this.str_val = null;
		this.is_constant = is_constant;
	}

	public globalData(String name, String value, boolean is_constant) {
		this.name = name;
		this.int_val = 0;
		this.str_val = value;
		this.is_constant = is_constant;
	}

	@Override public String toString() {return name;}
}
