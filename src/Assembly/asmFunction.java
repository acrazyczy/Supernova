package Assembly;

import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class asmFunction {
	public final String name;
	public LinkedList<asmBlock> asmBlocks;
	public asmBlock initBlock, retBlock;
	public ArrayList<virtualReg> parameters;
	public Set<virtualReg> virtualRegs;
	public stackFrame stkFrame;

	private Set<asmBlock> visited;

	public asmFunction(String name, ArrayList<virtualReg> parameters) {
		this.name = name;
		this.parameters = parameters;
		if (parameters == null) {
			this.asmBlocks = null;
			this.virtualRegs = new LinkedHashSet<>();
		} else {
			this.asmBlocks = new LinkedList<>();
			this.virtualRegs = new LinkedHashSet<>(parameters);
		}
	}

	private void dfsBlock(asmBlock asmBlk, ArrayList<asmBlock> dfsOrder) {
		dfsOrder.add(asmBlk);
		visited.add(asmBlk);
		asmBlk.successors.stream().filter(blk -> !visited.contains(blk)).forEach(blk -> dfsBlock(blk, dfsOrder));
	}

	public ArrayList<asmBlock> dfsOrderComputation() {
		ArrayList<asmBlock> ret = new ArrayList<>();
		visited = new LinkedHashSet<>();
		dfsBlock(initBlock, ret);
		return ret;
	}

	@Override public String toString() {return "\t.globl\t" + this.name + "\n\t.p2align\t2\n" + this.name + ":";}
}
