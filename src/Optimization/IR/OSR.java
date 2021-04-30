package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction._move;
import LLVMIR.Instruction.binary;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.booleanConstant;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.integerConstant;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;
import Optimization.IR.Analyser.dominanceAnalyser;

import java.util.*;

import static java.lang.Integer.min;

public class OSR implements pass {
	private static int reductionCounter = 0;

	private final IREntry programIREntry;
	private dominanceAnalyser dominanceProperty;

	public OSR(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private Map<register, Integer> DFN, low;
	private Map<basicBlock, Integer> RPONumber;
	private Set<register> stackSet;
	private Stack<register> stack;
	private Map<register, basicBlock> header;
	private basicBlock entry;

	private basicBlock getBlock(statement stmt) {return stmt == null ? entry : stmt.belongTo;}
	
	public class exprType {
		entity iv, rc;
		binary.instCode op;

		public exprType(binary n) {
			if (n.op2 instanceof register && isRegionConst(n.op1, header.get((register) n.op2))) {
				iv = n.op2;
				rc = n.op1;
			} else {
				iv = n.op1;
				rc = n.op2;
			}
			op = n.inst;
		}
		
		public exprType(entity iv, entity rc, binary.instCode op) {
			this.iv = iv;
			this.rc = rc;
			this.op = op;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			exprType exprType = (exprType) o;
			return iv.equals(exprType.iv) && rc.equals(exprType.rc) && op == exprType.op;
		}

		@Override public int hashCode() {return Objects.hash(iv, rc, op);}
	}

	private Map<exprType, entity> exprMap;
	private int DFSIndex;

	private boolean changed;

	private void classifyIV(Set<register> SCC) {
		basicBlock hdr = null;
		for (register n: SCC)
			if (hdr == null || RPONumber.get(hdr) > RPONumber.get(n.def.belongTo))
				hdr = n.def.belongTo;
		if (isInductionVariable(SCC, hdr)) for (register n: SCC) header.put(n, hdr);
		else for (register n: SCC)
			if (isCandidateOperation(n.def, hdr)) replace((binary) n.def);
			else header.put(n, null);
	}

	private void replace(binary n) {
		exprType expr = new exprType(n);
		entity result = reduce(expr);
		_move mvInst = new _move(result, n.dest);
		n.belongTo.replaceInstruction(n, mvInst);
		((register) n.dest).def = mvInst;
		header.put((register) n.dest, header.get((register) expr.iv));
		changed = true;
	}

	private entity replacer(exprType expr, statement newDef, entity o) {
		if (o instanceof register && header.get((register) o) != null && header.get((register) o) == header.get((register) expr.iv))
			return reduce(new exprType(o, expr.rc, expr.op));
		else if (expr.op == binary.instCode.mul || newDef instanceof phi)
			return apply(new exprType(o, expr.rc, expr.op));
		return o;
	}

	private entity reduce(exprType expr){
		entity result;
		if (exprMap.containsKey(expr)) result = exprMap.get(expr);
		else {
			changed = true;
			result = new register(expr.iv.type, "_rd." + (reductionCounter ++));
			exprMap.put(expr, result);
			statement ivDef = ((register) expr.iv).def;
			if (ivDef != null) {
				statement newDef = ivDef.clone();
				(((register) result).def = newDef).dest = result;
				getBlock(ivDef).insertInstructionAfter(newDef, ivDef);
				header.put((register) result, header.get((register) expr.iv));
				newDef.replaceOperand(this::replacer, expr, newDef);
			} else {
				statement newDef = new binary(expr.op, expr.iv, expr.rc, result);
				(((register) result).def = newDef).dest = result;
				entry.insertInstructionAfterPhi(newDef);
				header.put((register) result, header.get((register) expr.iv));
			}
		}
		return result;
	}

	private entity apply(exprType expr) {
		entity result;
		if (exprMap.containsKey(expr)) result = exprMap.get(expr);
		else {
			changed = true;
			if (expr.iv instanceof register && isRegionConst(expr.rc, header.get((register) expr.iv)))
				result = reduce(new exprType(expr.iv, expr.rc, expr.op));
			else if (expr.rc instanceof register && isRegionConst(expr.iv, header.get((register) expr.rc)))
				result = reduce(new exprType(expr.rc, expr.iv, expr.op));
			else {
				if (expr.iv instanceof integerConstant && expr.rc instanceof integerConstant) {
					int val;
					if (expr.op == binary.instCode.add) val = ((integerConstant) expr.iv).val + ((integerConstant) expr.rc).val;
					else if (expr.op == binary.instCode.sub) val = ((integerConstant) expr.iv).val - ((integerConstant) expr.rc).val;
					else val = ((integerConstant) expr.iv).val * ((integerConstant) expr.rc).val;
					result = new integerConstant(expr.iv.type.size(), val);
				} else {
					result = new register(expr.iv.type, "_rd." + (reductionCounter ++));
					statement newOper = new binary(expr.op, expr.iv, expr.rc, result);
					((register) result).def = newOper;
					if (expr.iv instanceof integerConstant) {
						assert expr.rc instanceof register;
						statement rcDef = ((register) expr.rc).def;
						getBlock(rcDef).insertInstructionAfter(newOper, rcDef);
					} else if (expr.rc instanceof integerConstant) {
						assert expr.iv instanceof register;
						statement ivDef = ((register) expr.iv).def;
						getBlock(ivDef).insertInstructionAfter(newOper, ivDef);
					} else {
						assert expr.iv instanceof register && expr.rc instanceof register;
						statement rcDef = ((register) expr.rc).def, ivDef = ((register) expr.iv).def;
						basicBlock rcBlk = getBlock(rcDef), ivBlk = getBlock(ivDef);
						if (dominanceProperty.isDominatedBy(rcBlk, ivBlk))
							rcBlk.insertInstructionAfter(newOper, rcDef);
						else {
							assert dominanceProperty.isDominatedBy(ivBlk, rcBlk);
							ivBlk.insertInstructionAfter(newOper, ivDef);
						}
					}
					header.put((register) result, null);
				}
				exprMap.put(expr, result);
			}
		}
		return result;
	}

	private boolean tester(Set<register> SCC, basicBlock hdr, entity o) {
		return (o instanceof register && SCC.contains((register) o)) || isRegionConst(o, hdr);
	}

	private boolean isInductionVariable(Set<register> SCC, basicBlock hdr) {
		return SCC.stream().map(n -> n.def).allMatch(n ->
			(n instanceof phi
				|| n instanceof binary
					&& (((binary) n).inst == binary.instCode.add || ((binary) n).inst == binary.instCode.sub)
				|| n instanceof _move)
			&& n.testOperand(this::tester, SCC, hdr)
		);
	}

	private boolean isCandidateOperation(statement n, basicBlock hdr) {
		if (n instanceof binary) {
			binary n_ = (binary) n;
			if (n_.inst == binary.instCode.add || n_.inst == binary.instCode.sub || n_.inst == binary.instCode.mul) {
				basicBlock hdr1 = n_.op1 instanceof register ? header.get(n_.op1) : null,
					hdr2 = n_.op2 instanceof register ? header.get(n_.op2) : null;
				return hdr1 != null && dominanceProperty.isDominatedBy(hdr, hdr1) && isRegionConst(n_.op2, hdr1) ||
					hdr2 != null && dominanceProperty.isDominatedBy(hdr, hdr2) && isRegionConst(n_.op1, hdr2) && n_.inst != binary.instCode.sub;
			}
		}
		return false;
	}

	private boolean isRegionConst(entity v, basicBlock header) {
		if (header == null) return false;
		if (v instanceof integerConstant) return true;
		if (v instanceof booleanConstant) return false;
		assert v instanceof register;
		return dominanceProperty.isStrictlyDominatedBy(header, getBlock(((register) v).def));
	}

	private void processSCC(Set<register> SCC) {
		if (SCC.size() > 1) classifyIV(SCC);
		else {
			register n = SCC.iterator().next();
			if (n.def != null && isCandidateOperation(n.def, getBlock(n.def))) replace((binary) n.def);
			header.put(n, null);
		}
	}

	private void Tarjan(register v) {
		DFN.put(v, DFSIndex);
		low.put(v, DFSIndex ++);
		stack.push(v);
		stackSet.add(v);
		if (v.def != null) v.def.uses().forEach(u -> {
			if (!DFN.containsKey(u)) {
				Tarjan(u);
				low.put(v, min(low.get(v), low.get(u)));
			} else if (stackSet.contains(u)) low.put(v, min(low.get(v), DFN.get(u)));
		});
		if (DFN.get(v).equals(low.get(v))) {
			Set<register> SCC = new LinkedHashSet<>();
			register top;
			do {
				stackSet.remove(top = stack.pop());
				SCC.add(top);
			} while (top != v);
			processSCC(SCC);
		}
	}

	private boolean run(function func) {
		dominanceProperty = new dominanceAnalyser(func.blocks.iterator().next(), new LinkedHashSet<>(func.blocks));
		func.blocks.forEach(blk -> blk.successors().forEach(sucBlk -> dominanceProperty.addEdge(blk, sucBlk)));
		changed = false;
		if (dominanceProperty.dominanceAnalysis(false)) {
			ArrayList<basicBlock> RPO = dominanceProperty.getReversePostOrderOfGraph();
			RPONumber = new LinkedHashMap<>();
			for (int i = 0; i < RPO.size(); ++ i) RPONumber.put(RPO.get(i), i);

			DFN = new LinkedHashMap<>();
			low = new LinkedHashMap<>();
			stackSet = new LinkedHashSet<>();
			stack = new Stack<>();
			DFSIndex = 0;
			header = new LinkedHashMap<>();
			exprMap = new LinkedHashMap<>();
			entry = func.blocks.iterator().next();

			Set<register> vars = new LinkedHashSet<>();
			func.variablesAnalysis(vars, null, null, null, null);

			vars.forEach(v -> {
				if (!DFN.containsKey(v)) Tarjan(v);
			});
		}
		return changed;
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
