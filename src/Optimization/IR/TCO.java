package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.br;
import LLVMIR.Instruction.call;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.ret;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class TCO implements pass {
	private final IREntry programIREntry;

	public TCO(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private boolean run(function func) {
		boolean changed = false;
		ArrayList<basicBlock> blocks = new ArrayList<>(func.blocks);
		LinkedList<register> argValues = new LinkedList<>();
		LinkedList<basicBlock> phiBlocks = new LinkedList<>();
		Map<register, LinkedList<entity>> phiValues = new HashMap<>();
		basicBlock newEntry, oldEntry = func.blocks.iterator().next();
		for (basicBlock blk: blocks)
			if (blk.tailStmt instanceof ret) {
				ret retInst = (ret) blk.tailStmt;
				if (blk.stmts.size() >= 2 && blk.stmts.get(blk.stmts.size() - 2) instanceof call) {
					call callInst = (call) blk.stmts.get(blk.stmts.size() - 2);
					if (callInst.callee == func && callInst.dest == retInst.value) {
						if (!changed) {
							newEntry = new basicBlock("TCO.entry", func);
							newEntry.push_back(new br(oldEntry));
							func.blocks.add(0, newEntry);
							phiBlocks.add(newEntry);
							for (ListIterator<register> argItr = func.argValues.listIterator();argItr.hasNext();) {
								register argv = argItr.next();
								argValues.add(argv);
								phiValues.put(argv, new LinkedList<>());
								register nArgv = new register(argv.type, argv.name + ".TCO", func);
								argItr.set(nArgv);
								phiValues.get(argv).add(nArgv);
							}
						}
						phiBlocks.add(blk);
						Iterator<register> argItr = argValues.iterator();
						Iterator<entity> paraItr = callInst.parameters.iterator();
						while (argItr.hasNext()) phiValues.get(argItr.next()).add(paraItr.next());
						blk.removeInstruction(blk.stmts.getLast(), true);
						blk.removeInstruction(blk.stmts.getLast(), true);
						blk.push_back(new br(oldEntry));
						changed = true;
					}
				}
			}
		if (changed) {
			phiValues.forEach((v, values) -> {
				phi phiInst = new phi(phiBlocks, values, v);
				v.def = phiInst;
				oldEntry.insertInstructionAfterPhi(phiInst);
			});
		}
		return changed;
	}

	@Override
	public boolean run() {
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
