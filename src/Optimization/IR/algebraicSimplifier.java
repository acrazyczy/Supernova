package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.binary;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.integerConstant;
import LLVMIR.Operand.register;
import LLVMIR.function;

import java.util.LinkedHashSet;
import java.util.Set;

public class algebraicSimplifier implements pass {
	private final IREntry programIREntry;

	public algebraicSimplifier(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private boolean simplification(register v, Set<register> isVisited) {
		if (isVisited.contains(v)) return false;
		isVisited.add(v);
		if (v.def instanceof binary) {
			binary def = (binary) v.def;
			if (def.inst == binary.instCode.add || def.inst == binary.instCode.mul) {
				if (def.op2 instanceof integerConstant) {
					entity tmp = def.op2;
					def.op2 = def.op1;
					def.op1 = tmp;
				}
				if (def.op1 instanceof integerConstant)
					if (def.op2 instanceof register) {
						simplification((register) def.op2, isVisited);
						if (((register) def.op2).def instanceof binary) {
							binary def_ = (binary) ((register) def.op2).def;
							if (def.inst == def_.inst && (def_.op1 instanceof integerConstant)) {
								switch (def.inst) {
									case add -> {
										def.op1 = new integerConstant(def.op1.type.size(), ((integerConstant) def.op1).val + ((integerConstant) def_.op1).val);
										def.op2 = def_.op2;
										return true;
									}
									case mul -> {
										def.op1 = new integerConstant(def.op1.type.size(), ((integerConstant) def.op1).val * ((integerConstant) def_.op1).val);
										def.op2 = def_.op2;
										return true;
									}
								}
							}
						}
					}
			}
		}
		return false;
	}

	private boolean run(function func) {
		Set<register> vars = new LinkedHashSet<>(), isVisited = new LinkedHashSet<>();
		func.variablesAnalysis(vars, null, null, null, null);
		return vars.stream().map(v -> simplification(v, isVisited)).reduce(false, (a, b) -> a || b);
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
