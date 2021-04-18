package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction._move;
import LLVMIR.Instruction.binary;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.constant;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.integerConstant;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

import static java.lang.Integer.min;

public class OSR implements pass {
	private static int reductionCounter = 0;

	private final IREntry programIREntry;
	private dominanceAnalyser dominanceProperty;

	public OSR(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private Map<statement, Integer> DFN, low;
	private Map<basicBlock, Integer> RPONumber;
	private Set<statement> stackSet;
	private Stack<statement> stack;
	private Map<statement, basicBlock> header;

	// TODO: 2021/4/18 null def block
	
	public class exprType {
		entity iv, rc;
		binary.instCode op;

		public exprType(binary n) {
			if (n.op2 instanceof register && isRegionConst(n.op1, header.get(((register) n.op2).def))) {
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

		@Override
		public int hashCode() {
			return Objects.hash(iv, rc, op);
		}
	}

	private Map<exprType, register> exprMap;
	private int DFSIndex;

	private boolean changed;

	private void classifyIV(Set<statement> SCC) {
		basicBlock hdr = null;
		for (statement n: SCC)
			if (hdr == null || RPONumber.get(hdr) > RPONumber.get(n.belongTo))
				hdr = n.belongTo;
		if (isInductionVariable(SCC, hdr)) for (statement n: SCC) header.put(n, hdr);
		else for (statement n: SCC)
			if (isCandidateOperation(n, hdr)) replace((binary) n);
			else header.put(n, null);
	}

	private void replace(binary n) {
		exprType expr = new exprType(n);
		register result = reduce(expr);
		_move mvInst = new _move(n.dest, result);
		n.belongTo.replaceInstruction(n, mvInst);
		((register) n.dest).def = mvInst;
		header.put(n, header.get(((register) expr.iv).def));
		changed = true;
	}

	private entity replacer(exprType expr, statement newDef, entity o) {
		if (o instanceof register && header.get(((register) o).def) == header.get(((register) expr.iv).def))
			return reduce(new exprType(o, expr.rc, expr.op));
		else if (expr.op == binary.instCode.mul || newDef instanceof phi)
			return apply(new exprType(o, expr.rc, expr.op));
	}

	private register reduce(exprType expr){
		register result;
		if (exprMap.containsKey(expr)) result = exprMap.get(expr);
		else {
			changed = true;
			result = new register(expr.iv.type, "_rd." + (reductionCounter ++));
			exprMap.put(expr, result);
			statement newDef = null;
			try {
				newDef = (statement) ((register) expr.iv).def.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			assert newDef != null;
			(result.def = newDef).dest = result;
			statement ivDef = ((register) expr.iv).def;
			ivDef.belongTo.insertInstructionAfter(newDef, ivDef);
			header.put(newDef, header.get(ivDef);
			newDef.testAndReplaceOperand(this::replacer, expr, newDef);
		}
		return result;
	}

	private register apply(exprType expr) {
		register result;
		if (exprMap.containsKey(expr)) result = exprMap.get(expr);
		else {
			changed = true;
			if (expr.iv instanceof register && isRegionConst(expr.rc, header.get(((register) expr.iv).def)))
				result = reduce(new exprType(expr.iv, expr.rc, expr.op));
			else if (expr.rc instanceof register && isRegionConst(expr.iv, header.get(((register) expr.rc).def)))
				result = reduce(new exprType(expr.rc, expr.iv, expr.op));
			else {
				result = new register(expr.iv.type, "_rd." + (reductionCounter ++));
				exprMap.put(expr, result);
				statement newOper;
				if (expr.iv instanceof integerConstant && expr.rc instanceof integerConstant) {

				} else if (expr.iv instanceof integerConstant) {

				} else if (expr.rc instanceof integerConstant) {

				} else {
					assert expr.iv instanceof register && expr.rc instanceof register;
					basicBlock blk1 = ((register) expr.iv).def.belongTo, blk2 = ((register) expr.rc).def.belongTo)
					if (dominanceProperty.isDominatedBy(blk1, blk2)) {

					} else {
						assert dominanceProperty.isDominatedBy(blk2, blk1);
					}
				}
				header.put(newOper, null);
			}
		}
		return result;
	}

	private boolean tester(Set<statement> SCC, basicBlock hdr, entity o) {
		return !(o instanceof register && SCC.contains(((register) o).def)) && !isRegionConst(o, hdr);
	}

	private boolean isInductionVariable(Set<statement> SCC, basicBlock hdr) {
		return SCC.stream().noneMatch(n ->
			!(n instanceof phi
				|| n instanceof binary
					&& (((binary) n).inst == binary.instCode.add || ((binary) n).inst == binary.instCode.sub)
				|| n instanceof _move)
			|| !n.testOperand(this::tester, SCC, hdr)
		);
	}

	private boolean isCandidateOperation(statement n, basicBlock header) {
		if (n instanceof binary) {
			binary n_ = (binary) n;
			if (n_.inst == binary.instCode.add || n_.inst == binary.instCode.sub || n_.inst == binary.instCode.mul)
				return n_.op1 instanceof register && isRegionConst(n_.op2, header) ||
					n_.op2 instanceof register && isRegionConst(n_.op1, header) && n_.inst != binary.instCode.sub;
		}
		return false;
	}

	private boolean isRegionConst(entity v, basicBlock header) {
		if (v instanceof integerConstant) return true;
		if (header == null) return false;
		assert v instanceof register;
		return dominanceProperty.isDominatedBy(header, ((register) v).def.belongTo);
	}

	private void processSCC(Set<statement> SCC) {
		if (SCC.size() > 1) classifyIV(SCC);
		else {
			statement n = SCC.iterator().next();
			if (n != null) {
				if (isCandidateOperation(n, header.containsKey(n) ? header.get(n): n.belongTo)) replace((binary) n);
				else header.put(n, null);
			}
		}
	}

	private void Tarjan(statement stmt) {
		DFN.put(stmt, DFSIndex);
		low.put(stmt, DFSIndex ++);
		stack.push(stmt);
		stackSet.add(stmt);
		if (stmt != null) stmt.uses().forEach(u -> {
			if (!DFN.containsKey(u.def)) {
				Tarjan(u.def);
				low.put(stmt, min(low.get(stmt), low.get(u.def)));
			} else if (stackSet.contains(u.def)) low.put(stmt, min(low.get(stmt), DFN.get(u.def)));
		});
		if (DFN.get(stmt).equals(low.get(stmt))) {
			Set<statement> SCC = new HashSet<>();
			statement top;
			do {
				top = stack.pop();
				SCC.add(top);
			} while (top != stmt);
			processSCC(SCC);
		}
	}

	private boolean run(function func) {
		dominanceProperty = new dominanceAnalyser(func.blocks.get(0), new HashSet<>(func.blocks));
		func.blocks.forEach(blk -> blk.successors().forEach(sucBlk -> dominanceProperty.addEdge(blk, sucBlk)));
		dominanceProperty.dominanceAnalysis(false);
		ArrayList<basicBlock> RPO = dominanceProperty.getReversePostOrderOfGraph();
		RPONumber = new HashMap<>();
		for (int i = 0;i < RPO.size();++ i) RPONumber.put(RPO.get(i), i);

		DFN = new HashMap<>();
		low = new HashMap<>();
		stackSet = new HashSet<>();
		stack = new Stack<>();
		DFSIndex = 0;
		header = new HashMap<>();
		exprMap = new HashMap<>();

		changed = false;
		func.blocks.forEach(blk -> blk.stmts.forEach(stmt -> {if (!DFN.containsKey(stmt)) Tarjan(stmt);}));
		return changed;
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
