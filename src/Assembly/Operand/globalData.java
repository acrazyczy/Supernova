package Assembly.Operand;

public class globalData extends operand {
	public final String name;
	private final int int_val;
	private final String str_val;

	public globalData(String name, int value) {
		this.name = name;
		this.int_val = value;
		this.str_val = null;
	}

	public globalData(String name, String value) {
		this.name = name;
		this.int_val = 0;
		this.str_val = value.replace("\\", "\\\\").
			replace("\n", "\\n").
			replace("\"", "\\\"");
	}

	@Override public String toString() {
		return str_val != null ?
			name + ":\n"
			+ "\t.asciz\t" + "\"" + str_val + "\"\n" :
			"\t.globl\t" + name + "\n"
			+ "\t.p2align\t2\n"
			+ name + ":\n"
			+ "\t.word\t" + int_val + "\n";
	}
}
