package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.*;
import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.basicBlock;
import LLVMIR.function;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

import static java.lang.Integer.compareUnsigned;

public class SCCP implements pass {
	private final IREntry programIREntry;

	private Set<basicBlock> executableBlocks;
	private Map<basicBlock, List<basicBlock>> predecessors;
	private enum valType {
		undetermined, determined, nonConstant
	}
	private Map<register, valType> latticeType;
	private Map<register, Integer> latticeValue;

	public SCCP(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private Pair<valType, Integer> constantExtracting(entity var) {
		if (var instanceof booleanConstant)
			return new Pair<>(valType.determined, ((booleanConstant) var).val);
		if (var instanceof integerConstant)
			return new Pair<>(valType.determined, ((integerConstant) var).val);
		if (var instanceof undefinedValue)
			return new Pair<>(valType.nonConstant, 0);
		if (var instanceof nullPointerConstant)
			return new Pair<>(valType.nonConstant, 0);
		assert var instanceof register;
		if (!(var.type instanceof LLVMIntegerType))
			return new Pair<>(valType.nonConstant, 0);
		return new Pair<>(latticeType.get(var), latticeValue.get(var));
	}

	private boolean run(function func) {
		executableBlocks = new LinkedHashSet<>();
		latticeType = new LinkedHashMap<>();
		latticeValue = new LinkedHashMap<>();
		predecessors = new LinkedHashMap<>();

		func.blocks.forEach(blk -> predecessors.put(blk, new ArrayList<>()));
		func.blocks.forEach(blk -> blk.successors().forEach(sucBlk -> predecessors.get(sucBlk).add(blk)));

		boolean changed = false;
		Set<basicBlock> blockWorkList = new LinkedHashSet<>();
		Set<register> vars = new LinkedHashSet<>(), variableWorkList = new LinkedHashSet<>();
		func.variablesAnalysis(vars, null, null, null, null);

		Map<register, Set<statement>> uses = new LinkedHashMap<>();
		vars.forEach(v -> uses.put(v, new LinkedHashSet<>()));
		func.blocks.forEach(blk -> blk.stmts.forEach(stmt -> stmt.uses().forEach(v -> uses.get(v).add(stmt))));

		vars.forEach(v -> latticeType.put(v, valType.undetermined));
		func.argValues.forEach(argv -> latticeType.put(argv, valType.nonConstant));
		executableBlocks.add(func.blocks.iterator().next());
		blockWorkList.add(func.blocks.iterator().next());
		while (!blockWorkList.isEmpty() || !variableWorkList.isEmpty()) {
			if (!blockWorkList.isEmpty()) {
				basicBlock blk = blockWorkList.iterator().next();
				blockWorkList.remove(blk);
				blk.stmts.forEach(stmt -> visitInstruction(stmt, blockWorkList, variableWorkList));
				blk.successors().stream().filter(sucBlk -> executableBlocks.contains(sucBlk))
					.forEach(sucBlk -> sucBlk.phiCollections.values().forEach(phiInst -> visitInstruction(phiInst, blockWorkList, variableWorkList)));
			}
			if (!variableWorkList.isEmpty()) {
				register var = variableWorkList.iterator().next();
				variableWorkList.remove(var);
				uses.get(var).stream().filter(stmt -> executableBlocks.contains(stmt.belongTo))
					.forEach(stmt -> visitInstruction(stmt, blockWorkList, variableWorkList));
			}
		}

		// remove constant register
		for (basicBlock blk: func.blocks) {
			List<statement> stmts = new LinkedList<>(blk.stmts);
			for (statement stmt : stmts) {
				if (!changed)
					changed = stmt.variables().stream().anyMatch(v -> latticeType.get(v) == valType.determined);
				if (stmt.dest != null && latticeType.get(stmt.dest) == valType.determined) blk.removeInstruction(stmt, true);
				else stmt.uses().stream().filter(v -> latticeType.get(v) == valType.determined)
					.forEach(v -> stmt.replaceUse(v, ((LLVMIntegerType) v.type).is_boolean ? new booleanConstant(latticeValue.get(v)) : new integerConstant(v.type.size(), latticeValue.get(v))));
			}
		}

		// remove unreachable blocks
		List<basicBlock> blocks = new LinkedList<>(func.blocks);
		for (basicBlock blk: blocks)
			if (!executableBlocks.contains(blk)) {
				changed = true;
				func.blocks.remove(blk);
				predecessors.get(blk).stream().filter(preBlk -> executableBlocks.contains(preBlk)).forEach(preBlk -> preBlk.removeSuccessor(blk));
				blk.successors().stream()
					.filter(sucBlk -> executableBlocks.contains(sucBlk))
					.forEach(sucBlk -> sucBlk.removeBlockFromPhi(blk));
			}

		return changed;
	}

	void visitInstruction(statement stmt, Set<basicBlock> blockWorkList, Set<register> variableWorkList) {
		register dest = (register) stmt.dest;
		valType old = latticeType.get(dest);
		if (stmt instanceof call || stmt instanceof load) {
			if (stmt.dest != null) latticeType.put(dest, valType.nonConstant);
		} else if (stmt instanceof binary) {
			binary stmt_ = (binary) stmt;
			Pair<valType, Integer> lhs = constantExtracting(stmt_.op1), rhs = constantExtracting(stmt_.op2);
			switch (stmt_.inst) {
				case add -> {
					if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
					else if (lhs.a == valType.determined && rhs.a == valType.determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, lhs.b + rhs.b);
					}
				}
				case sub -> {
					if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
					else if (lhs.a == valType.determined && rhs.a == valType.determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, lhs.b - rhs.b);
					}
				}
				case mul -> {
					boolean determined = true;
					int value = -1;
					if (lhs.a == valType.determined && lhs.b == 0) value = 0;
					else if (rhs.a == valType.determined && rhs.b == 0) value = 0;
					else if (lhs.a == valType.determined && rhs.a == valType.determined) value = lhs.b * rhs.b;
					else determined = false;
					if (determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, value);
					} else if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
				}
				case sdiv -> {
					boolean determined = true;
					int value = -1;
					if (lhs.a == valType.determined && lhs.b == 0) value = 0;
					else if (lhs.a == valType.determined && rhs.a == valType.determined) value = lhs.b / rhs.b;
					else determined = false;
					if (determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, value);
					} else if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
				}
				case srem -> {
					boolean determined = true;
					int value = -1;
					if (lhs.a == valType.determined && lhs.b == 0) value = 0;
					else if (rhs.a == valType.determined && rhs.b == 1) value = 0;
					else if (lhs.a == valType.determined && rhs.a == valType.determined) value = lhs.b % rhs.b;
					else determined = false;
					if (determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, value);
					} else if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
				}
				case shl -> {
					boolean determined = true;
					int value = -1;
					if (lhs.a == valType.determined && lhs.b == 0) value = 0;
					else if (rhs.a == valType.determined && rhs.b >= stmt_.op1.type.size()) value = 0;
					else if (lhs.a == valType.determined && rhs.a == valType.determined) value = lhs.b << rhs.b;
					else determined = false;
					if (determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, value);
					} else if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
				}
				case ashr -> {
					boolean determined = true;
					int value = -1;
					if (lhs.a == valType.determined && lhs.b == 0) value = 0;
					else if (rhs.a == valType.determined && rhs.b >= stmt_.op1.type.size()) value = 0;
					else if (lhs.a == valType.determined && rhs.a == valType.determined) value = lhs.b >> rhs.b;
					else determined = false;
					if (determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, value);
					} else if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
				}
				case and -> {
					boolean determined = true;
					int value = -1;
					if (lhs.a == valType.determined && lhs.b == 0) value = 0;
					else if (rhs.a == valType.determined && rhs.b == 0) value = 0;
					else if (lhs.a == valType.determined && rhs.a == valType.determined) value = lhs.b & rhs.b;
					else determined = false;
					if (determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, value);
					} else if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
				}
				case or -> {
					boolean determined = true;
					int value = -1, mask = ~0 & ((1 << stmt_.op1.type.size()) - 1);
					if (lhs.a == valType.determined && lhs.b == mask) value = mask;
					else if (rhs.a == valType.determined && rhs.b == mask) value = mask;
					else if (lhs.a == valType.determined && rhs.a == valType.determined) value = lhs.b | rhs.b;
					else determined = false;
					if (determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, value);
					} else if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
				}
				case xor -> {
					if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
					else if (lhs.a == valType.determined && rhs.a == valType.determined) {
						latticeType.put(dest, valType.determined);
						latticeValue.put(dest, lhs.b ^ rhs.b);
					}
				}
			}
		} else if (stmt instanceof icmp) {
			icmp stmt_ = (icmp) stmt;
			Pair<valType, Integer> lhs = constantExtracting(stmt_.op1), rhs = constantExtracting(stmt_.op2);
			if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
			else if (lhs.a == valType.determined && rhs.a == valType.determined) {
				latticeType.put(dest, valType.determined);
				switch (stmt_.cond) {
					case eq -> latticeValue.put(dest, lhs.b.equals(rhs.b) ? 1 : 0);
					case ne -> latticeValue.put(dest, !lhs.b.equals(rhs.b) ? 1 : 0);
					case ugt -> latticeValue.put(dest, compareUnsigned(lhs.b, rhs.b) > 0 ? 1 : 0);
					case uge -> latticeValue.put(dest, compareUnsigned(lhs.b, rhs.b) >= 0 ? 1 : 0);
					case ult -> latticeValue.put(dest, compareUnsigned(lhs.b, rhs.b) < 0 ? 1 : 0);
					case ule -> latticeValue.put(dest, compareUnsigned(lhs.b, rhs.b) <= 0 ? 1 : 0);
					case sgt -> latticeValue.put(dest, lhs.b > rhs.b ? 1 : 0);
					case sge -> latticeValue.put(dest, lhs.b >= rhs.b ? 1 : 0);
					case slt -> latticeValue.put(dest, lhs.b < rhs.b ? 1 : 0);
					case sle -> latticeValue.put(dest, lhs.b <= rhs.b ? 1 : 0);
				}
			}
		} else if (stmt instanceof _move) {
			_move stmt_ = (_move) stmt;
			Pair<valType, Integer> src = constantExtracting(stmt_.src);
			if (src.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
			else if (src.a == valType.determined) {
				latticeType.put(dest, valType.determined);
				latticeValue.put(dest, src.b);
			}
		} else if (stmt instanceof phi) {
			phi stmt_ = (phi) stmt;
			valType type = valType.undetermined;
			int value = -1;
			for (int i = 0;i < stmt_.blocks.size() && type != valType.nonConstant;++ i)
				if (executableBlocks.contains(stmt_.blocks.get(i))) {
					Pair<valType, Integer> src = constantExtracting(stmt_.values.get(i));
					if (src.a == valType.nonConstant) type = valType.nonConstant;
					else if (src.a == valType.determined)
						if (type == valType.undetermined) {
							type = valType.determined;
							value = src.b;
						} else if (value != src.b) type = valType.nonConstant;
				}
			if (type == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
			else if (type == valType.determined) {
				latticeType.put(dest, valType.determined);
				latticeValue.put(dest, value);
			}
		} else if (stmt instanceof br) {
			br stmt_ = (br) stmt;
			if (stmt_.cond == null) {
				if (!executableBlocks.contains(stmt_.trueBranch)) {
					executableBlocks.add(stmt_.trueBranch);
					blockWorkList.add(stmt_.trueBranch);
				}
			} else {
				Pair<valType, Integer> cond = constantExtracting(stmt_.cond);
				if (cond.a == valType.nonConstant) {
					if (!executableBlocks.contains(stmt_.trueBranch)) {
						executableBlocks.add(stmt_.trueBranch);
						blockWorkList.add(stmt_.trueBranch);
					}
					if (!executableBlocks.contains(stmt_.falseBranch)) {
						executableBlocks.add(stmt_.falseBranch);
						blockWorkList.add(stmt_.falseBranch);
					}
				} else if (cond.a == valType.determined)
					if (cond.b == 1) {
						if (!executableBlocks.contains(stmt_.trueBranch)) {
							executableBlocks.add(stmt_.trueBranch);
							blockWorkList.add(stmt_.trueBranch);
						}
					} else {
						if (!executableBlocks.contains(stmt_.falseBranch)) {
							executableBlocks.add(stmt_.falseBranch);
							blockWorkList.add(stmt_.falseBranch);
						}
					}
			}
		}
		if (old != latticeType.get(dest)) variableWorkList.add(dest);
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
