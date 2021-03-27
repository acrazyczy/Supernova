package Assembly.Instruction;

abstract public class inst {
	public inst pre = null, suf = null;
	private String comment = null;

	public inst() {}
	public inst(String comment) {this.comment = comment;}

	@Override abstract public String toString();
}
