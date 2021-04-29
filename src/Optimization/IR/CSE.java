package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

public class CSE implements pass {
	private final IREntry programIREntry;

	public CSE(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private boolean isCandidateInstruction(statement inst) {
		return inst instanceof binary || inst instanceof icmp || inst instanceof getelementptr;
	}

	private String getHashCode(statement inst) {
		if (inst instanceof binary) {
			binary inst_ = (binary) inst;
			return "&" + inst_.inst.hashCode() + inst_.op1.hashCode() + inst_.op2.hashCode();
		} else if (inst instanceof icmp) {
			icmp inst_ = (icmp) inst;
			return "<" + inst_.cond.hashCode() + inst_.op1.hashCode() + inst_.op2.hashCode();
		} else {
			assert inst instanceof getelementptr;
			getelementptr inst_ = (getelementptr) inst;
			StringBuilder ret = new StringBuilder("@" + inst_.pointer.hashCode());
			for (entity idx: inst_.idxes) ret.append("|").append(idx.hashCode());
			return ret.toString();
		}
	}

	Map<String, register> commonSubExpression;

	private boolean run(function func) {
		boolean changed = false;
		for (basicBlock blk: func.blocks) {
			commonSubExpression = new LinkedHashMap<>();
			for (ListIterator<statement> instItr = blk.stmts.listIterator(); instItr.hasNext(); ) {
				statement inst = instItr.next();
				if (isCandidateInstruction(inst)) {
					String hashCode = getHashCode(inst);
					if (commonSubExpression.containsKey(hashCode)) {
						_move mvInst = new _move(commonSubExpression.get(hashCode), inst.dest);
						((register) inst.dest).def = mvInst;
						mvInst.belongTo = blk;
						instItr.set(mvInst);
						changed = true;
					} else commonSubExpression.put(hashCode, (register) inst.dest);
				}
			}
		}
		return changed;
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
