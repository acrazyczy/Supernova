package Backend;

import Assembly.Instruction.inst;
import Assembly.Instruction.mvInst;
import Assembly.Operand.physicalReg;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

public class registerAllocator implements asmVisitor {
	private final asmEntry programAsmEntry;

	private Set<virtualReg> precolored;
	private Set<virtualReg> initial;
	private ArrayList<virtualReg> simplifyWorkList;
	private ArrayList<virtualReg> freezeWorkList;
	private ArrayList<virtualReg> spillWorkList;
	private ArrayList<virtualReg> spilledNodes;
	private Set<virtualReg> coalescedNodes;
	private Set<virtualReg> coloredNodes;
	private Stack<virtualReg> selectStack;

	private Set<mvInst> coalescedMoves;
	private Set<mvInst> constrainedMoves;
	private Set<mvInst> frozenMoves;
	private Set<mvInst> workListMoves;
	private Set<mvInst> activeMoves;

	private static class edge extends Pair<virtualReg, virtualReg> {
		public edge(virtualReg x, virtualReg y) {super(x, y);}
	}

	private Set<edge> adjSet;
	private Map<virtualReg, virtualReg> alias;

	static int registerNumber = physicalReg.allocatablePRegNames.length;

	public registerAllocator(asmEntry programAsmEntry) {
		this.programAsmEntry = programAsmEntry;
	}

	private void runGraphColoring(asmFunction asmFunc) {
		while (true) {
			new livenessAnalyser(programAsmEntry).run();
			build(asmFunc);
			makeWorkList();
			do {
				if (!simplifyWorkList.isEmpty()) simplify();
				else if (!workListMoves.isEmpty()) coalesce();
				else if (!freezeWorkList.isEmpty()) freeze();
				else if (!spillWorkList.isEmpty()) selectSpill();
			} while (!simplifyWorkList.isEmpty() || !workListMoves.isEmpty() ||
				!freezeWorkList.isEmpty() || !spillWorkList.isEmpty());
			assignColors();
			if (spilledNodes.isEmpty()) break;
			rewriteProgram(spilledNodes);
		}
	}
	
	private void build(asmFunction asmFunc) {
		for (asmBlock block: asmFunc.asmBlocks) {
			Set<virtualReg> live = block.liveOut;
			for (inst i = block.tailInst;i != null;i = i.pre) {
				if (i instanceof mvInst) {
					live.removeAll(i.use);
					for (virtualReg n: i.def) n.moveList.add((mvInst) i);
					for (virtualReg n: i.use) n.moveList.add((mvInst) i);
					workListMoves.add((mvInst) i);
				}
				live.addAll(i.def);
				i.def.forEach(d -> live.forEach(l -> addEdge(l, d)));
				live.addAll(i.use);
				for (virtualReg l: live) if (!i.def.contains(l)) live.add(l);
			}
		}
	}
	
	private void makeWorkList() {
		for (virtualReg n: initial) {
			initial.add(n);
			if (n.deg >= registerNumber) spillWorkList.add(n);
			else if (moveRelated(n)) freezeWorkList.add(n);
			else simplifyWorkList.add(n);
		}
	}

	private Set<virtualReg> adjacent(virtualReg n) {
		Set<virtualReg> ret = n.adjList;
		ret.retainAll(selectStack);
		ret.retainAll(coalescedNodes);
		return ret;
	}

	private Set<mvInst> nodeMoves(virtualReg n) {
		Set<mvInst> ret = activeMoves;
		ret.addAll(workListMoves);
		ret.retainAll(n.moveList);
		return ret;
	}

	private boolean moveRelated(virtualReg n) {
		return !nodeMoves(n).isEmpty();
	}

	private void simplify() {
		virtualReg n = simplifyWorkList.get(0);
		simplifyWorkList.remove(n);
		selectStack.add(n);
		adjacent(n).forEach(this::decrementDegree);
	}

	private void decrementDegree(virtualReg m) {
		int d = m.deg --;
		if (d == registerNumber) {
			Set<virtualReg> nodes = new HashSet<>(adjacent(m));
			nodes.add(m);
			enableMoves(nodes);
			spillWorkList.remove(m);
			if (moveRelated(m)) freezeWorkList.add(m);
			else simplifyWorkList.add(m);
		}
	}

	private void enableMoves(Set<virtualReg> nodes) {
		nodes.forEach(n -> nodeMoves(n).forEach(m -> {
			if (activeMoves.contains(m)) {
				activeMoves.remove(m);
				workListMoves.add(m);
			}
		}));
	}
	
	private void coalesce() {
		mvInst m = workListMoves.iterator().next();
		virtualReg x = getAlias(m.rd), y = getAlias(m.rs);
		virtualReg u, v;
		if (precolored.contains(y)) {
			u = y;
			v = x;
		} else {
			u = x;
			v = y;
		}
		workListMoves.remove(m);
		if (u == v) {
			coalescedMoves.add(m);
			addWorkList(u);
		} else if (precolored.contains(v) && adjSet.contains(new edge(u, v))) {
			constrainedMoves.add(m);
			addWorkList(u);
			addWorkList(v);
		} else {
			Set<virtualReg> nodes = adjacent(u);
			nodes.addAll(adjacent(v));
			if (precolored.contains(u) ? adjacent(v).stream().allMatch(t -> ok(t, u)) : conservative(nodes)) {
				coalescedMoves.add(m);
				combine(u, v);
				addWorkList(u);
			} else activeMoves.add(m);
		}
	}

	private void addWorkList(virtualReg u) {
		if (!precolored.contains(u) && !moveRelated(u) && u.deg < registerNumber) {
			freezeWorkList.remove(u);
			simplifyWorkList.add(u);
		}
	}

	private boolean ok(virtualReg t, virtualReg r) {
		return t.deg < registerNumber || precolored.contains(t) || adjSet.contains(new edge(t, r));
	}

	private boolean conservative(Set<virtualReg> nodes) {
		int k = 0;
		for (virtualReg n: nodes) if (n.deg >= registerNumber) ++ k;
		return k < registerNumber;
	}

	private virtualReg getAlias(virtualReg n) {
		return coalescedNodes.contains(n) ? getAlias(alias.get(n)) : n;
	}

	private void combine(virtualReg u, virtualReg v) {
		if (freezeWorkList.contains(v)) freezeWorkList.remove(v);
		else spillWorkList.remove(v);
		coalescedNodes.add(v);
		alias.put(v, u);
		u.moveList.addAll(v.moveList);
		enableMoves(new HashSet<>(Collections.singletonList(v)));
		adjacent(v).forEach(t -> {addEdge(t, u);decrementDegree(t);});
		if (u.deg >= registerNumber && freezeWorkList.contains(u)) {
			freezeWorkList.remove(u);
			spillWorkList.add(u);
		}
	}
	
	private void freeze() {
		virtualReg u = freezeWorkList.get(0);
		freezeWorkList.remove(u);
		simplifyWorkList.add(u);
		freezeMoves(u);
	}

	private void freezeMoves(virtualReg u) {
		for (mvInst m: nodeMoves(u)) {
			virtualReg x = getAlias(m.rd), y = getAlias(m.rs),
			v = getAlias(y) == getAlias(u) ? getAlias(x) : getAlias(y);
			activeMoves.remove(m);
			frozenMoves.add(m);
			if (nodeMoves(v).isEmpty() && v.deg < registerNumber) {
				freezeWorkList.remove(v);
				simplifyWorkList.add(v);
			}
		}
	}
	
	private void selectSpill() {
		virtualReg m = selectVirtualRegisterToSpill();
		spillWorkList.remove(m);
		simplifyWorkList.add(m);
		freezeMoves(m);
	}

	private virtualReg selectVirtualRegisterToSpill() {
		// TODO: 2021/3/30
		return null;
	}
	
	private void assignColors() {
		while (!selectStack.isEmpty()) {
			virtualReg n = selectStack.pop();
			Set<physicalReg> okColors = new HashSet<>(physicalReg.allocatablePRegs.values());
			n.adjList.forEach(w -> {
				virtualReg w_ = getAlias(w);
				if (coloredNodes.contains(w_) || precolored.contains(w_)) okColors.remove(w_.color);
			});
			if (okColors.isEmpty()) spilledNodes.add(n);
			else {
				coloredNodes.add(n);
				n.color = okColors.iterator().next();
			}
		}
		coalescedNodes.forEach(n -> n.color = getAlias(n).color);
	}
	
	private void rewriteProgram(ArrayList<virtualReg> spilledNodes) {
		// allocate a memory unit for each spilled node v
		// create a new temporary variable v_i for each def and use
		// insert a store instruction after each def of v_i and a load instruction before each use of v_i
		// add all v_i into newTemps
		Set<virtualReg> newTemps = null;
		spilledNodes.clear();
		initial = coloredNodes;
		initial.addAll(coalescedNodes);
		initial.addAll(newTemps);
		coloredNodes.clear();
		coalescedNodes.clear();
	}
	
	private void addEdge(virtualReg u, virtualReg v) {
		if (!adjSet.contains(new edge(u, v)) && u != v) {
			adjSet.add(new edge(u, v));
			adjSet.add(new edge(v, u));
			if (!precolored.contains(u)) {
				u.adjList.add(v);
				++ u.deg;
			}
			if (!precolored.contains(v)) {
				v.adjList.add(u);
				++ v.deg;
			}
		}
	}

	@Override
	public void run() {
		programAsmEntry.asmFunctions.forEach((IRFunc, asmFunc) -> runGraphColoring(asmFunc));
	}
}
