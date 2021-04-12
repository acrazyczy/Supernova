package Backend;

import LLVMIR.IREntry;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class SSADestructor implements pass {
	private final IREntry programIREntry;

	private Map<basicBlock, List<basicBlock>> predecessors;

	public SSADestructor(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private class parallelCopy {
	}

	@Override
	public boolean run() {
		programIREntry.functions.forEach(func -> {
			initialization(func);
			criticalEdgeSplitting(func);
			parallelCopyReplacing(func);
		});
		return true;
	}

	private void initialization(function func) {
		predecessors = new HashMap<>();
		func.blocks.forEach(blk -> predecessors.put(blk, new ArrayList<>()));
		func.blocks.forEach(blk -> blk.successors().forEach(sucBlk -> predecessors.get(sucBlk).add(blk)));
	}

	private void criticalEdgeSplitting(function func) {
	}

	private void parallelCopyReplacing(function func) {
	}
}
