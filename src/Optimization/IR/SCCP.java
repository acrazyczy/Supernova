package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.*;
import LLVMIR.basicBlock;
import LLVMIR.function;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

public class SCCP implements pass {
	private final IREntry programIREntry;

	private Map<basicBlock, Boolean> isExecutable;

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
		assert var instanceof register;
		return new Pair<>(latticeType.get(var), latticeValue.get(var));
	}

	private boolean run(function func) {
		boolean ret = false;
		Set<basicBlock> blockWorkList = new HashSet<>();
		Set<register> vars = new HashSet<>(), variableWorkList = new HashSet<>();
		func.variablesAnalysis(vars, null, null, null, null);

		Map<register, Set<statement>> uses = new HashMap<>();
		vars.forEach(v -> uses.put(v, new HashSet<>()));
		func.blocks.forEach(blk -> blk.stmts.forEach(stmt -> stmt.uses().forEach(v -> uses.get(v).add(stmt))));

		vars.forEach(v -> latticeType.put(v, valType.undetermined));
		func.blocks.forEach(blk -> isExecutable.put(blk, false));
		func.argValues.forEach(argv -> latticeType.put(argv, valType.nonConstant));
		isExecutable.put(func.blocks.get(0), true);
		blockWorkList.add(func.blocks.get(0));
		while (!blockWorkList.isEmpty() || !variableWorkList.isEmpty()) {
			if (!blockWorkList.isEmpty()) {
				basicBlock blk = blockWorkList.iterator().next();
				blockWorkList.remove(blk);
				blk.stmts.forEach(stmt -> {
					register dest = (register) stmt.dest;
					if (stmt instanceof call || stmt instanceof load) {
						if (stmt.dest != null) {
							latticeType.put((register) stmt.dest, valType.nonConstant);
							variableWorkList.add((register) stmt.dest);
						}
					} else if (stmt instanceof binary) {
						binary stmt_ = (binary) stmt;
						Pair<valType, Integer> lhs = constantExtracting(stmt_.op1), rhs = constantExtracting(stmt_.op2);
						if (lhs.a == valType.nonConstant || rhs.a == valType.nonConstant) latticeType.put(dest, valType.nonConstant);
						else switch (stmt_.inst) {
							case add -> {
								if (lhs.a == valType.determined && rhs.a == valType.determined) {
									latticeType.put(dest, valType.determined);
									latticeValue.put(dest, lhs.b + rhs.b);
								}
							}
							case sub -> {
								if (lhs.a == valType.determined && rhs.a == valType.determined) {
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
								}
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
								}
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
								}
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
								}
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
								}
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
								}
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
								}
							}
							case xor -> {
								if (lhs.a == valType.determined && rhs.a == valType.determined) {
									latticeType.put(dest, valType.determined);
									latticeValue.put(dest, lhs.b ^ rhs.b);
								}
							}
						}
					} else if (stmt instanceof icmp) {

					} else if (stmt instanceof _move) {
					} else if (stmt instanceof phi) {
					} else if (stmt instanceof br) {
					}
				});
				if (blk.successors().size() == 1) {
					basicBlock suc = blk.successors().get(0);
					if (!isExecutable.get(suc)) {
						isExecutable.put(suc, true);
						blockWorkList.add(suc);
					}
				}
				// TODO: 2021/4/12 check phi functions of successors
			}
			if (!variableWorkList.isEmpty()) {
				register var = variableWorkList.iterator().next();
				variableWorkList.remove(var);
				uses.get(var).stream().filter(stmt -> isExecutable.get(stmt.belongTo)).forEach(stmt -> {
				});
			}
		}
		return ret;
	}

	@Override
	public boolean run() {
		isExecutable = new HashMap<>();
		latticeType = new HashMap<>();
		latticeValue = new HashMap<>();
		boolean ret = programIREntry.functions.stream().map(this::run).reduce(false, (a, b) -> a || b);
		return false;
	}
}
