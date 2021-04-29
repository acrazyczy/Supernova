package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction._move;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.register;
import LLVMIR.function;
import Util.disjointSet;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class copyPropagation implements pass {
	private final IREntry programIREntry;

	public copyPropagation(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private boolean run(function func) {
		Set<register> vars = new LinkedHashSet<>();
		Map<register, Set<statement>> uses = new LinkedHashMap<>();
		func.variablesAnalysis(vars, null, null, uses, null);
		disjointSet<register> dsu = new disjointSet<>();
		vars.stream().filter(v -> v.def instanceof _move && ((_move) v.def).src instanceof register)
			.forEach(v -> dsu.put(v, (register) ((_move) v.def).src));
		boolean changed = false;
		for (register v: vars) {
			register r = dsu.query(v);
			if (r != v) {
				v.def.belongTo.removeInstruction(v.def, true);
				if (uses.get(v) != null) uses.get(v).forEach(u -> u.replaceUse(v, r));
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
