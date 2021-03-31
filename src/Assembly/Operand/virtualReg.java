package Assembly.Operand;

import Assembly.Instruction.inst;
import Assembly.Instruction.mvInst;

import java.util.HashSet;
import java.util.Set;

public class virtualReg extends reg {
	private int index;

	/*graph coloring information*/
	public Set<virtualReg> adjList;
	public Set<mvInst> moveList;
	public physicalReg color;
	public Set<inst> uses, defs;
	public double spillCost;
	public int deg;

	public virtualReg() {
		super();
		this.index = -1024;
	}

	public virtualReg(int index) {
		super();
		this.index = index;
	}

	public void initializationForGraphColoring() {
		adjList = new HashSet<>();
		moveList = new HashSet<>();
		color = null;
		deg = 0;
		spillCost = 0;
	}

	@Override public String toString() {
		return color == null ? "%" + index : color.toString();
	}
}
