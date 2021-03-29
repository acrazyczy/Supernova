package Assembly.Operand;

import Assembly.Instruction.mvInst;

import java.util.Set;

public class virtualReg extends reg {
	private int index;

	/*graph coloring information*/
	public Set<virtualReg> adjList;
	public Set<mvInst> moveList;
	public physicalReg color;
	public int deg;

	public virtualReg(int index) {
		super();
		this.index = index;
	}

	@Override public String toString() {return "%" + index;}
}
