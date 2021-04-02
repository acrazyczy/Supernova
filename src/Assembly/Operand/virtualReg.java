package Assembly.Operand;

import Assembly.Instruction.inst;
import Assembly.Instruction.mvInst;

import java.util.HashSet;
import java.util.Set;

public class virtualReg extends reg {
	private final int index;

	/*graph coloring information*/
	public Set<virtualReg> adjList;
	public Set<mvInst> moveList;
	public physicalReg color;
	public Set<inst> uses = new HashSet<>(), defs = new HashSet<>();
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
		if (-32 > index || index >= 0) {
			color = null;
			deg = 0;
		} else deg = Integer.MAX_VALUE >> 1;
		spillCost = 0;
	}

	@Override public String toString() {return color == null ? "%" + index : color.toString();}
}
