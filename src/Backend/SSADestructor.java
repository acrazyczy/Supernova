package Backend;

import LLVMIR.IREntry;
import LLVMIR.Instruction._move;
import LLVMIR.Instruction.br;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class SSADestructor implements pass {
	private final IREntry programIREntry;

	private Map<basicBlock, List<basicBlock>> predecessors;

	public SSADestructor(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private Map<basicBlock, Set<_move>> parallelCopy;
	private int registerCounter;

	@Override
	public boolean run() {
		programIREntry.functions.stream().filter(func -> func.blocks != null).forEach(func -> {
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
		parallelCopy = new HashMap<>();
		ArrayList<basicBlock> blocks = new ArrayList<>(func.blocks);
		blocks.forEach(blk -> parallelCopy.put(blk, new HashSet<>()));
		blocks.stream().filter(blk -> !blk.phiCollections.isEmpty()).forEach(blk -> {
			predecessors.get(blk).stream()
				.filter(preBlk -> preBlk.successors().size() > 1)
				.forEach(preBlk -> {
					basicBlock splitBlock = new basicBlock(preBlk.name + "_" + blk.name + ".split", func);
					parallelCopy.put(splitBlock, new HashSet<>());
					splitBlock.push_back(new br(blk));
					blk.replacePredecessor(preBlk, splitBlock);
					if (preBlk.replaceSuccessor(blk, splitBlock))
						func.blocks.add(func.blocks.indexOf(blk), splitBlock);
					else
						func.blocks.add(splitBlock);
				});
			blk.phiCollections.values().forEach(phiInst -> {
				Iterator<basicBlock> blkItr = phiInst.blocks.iterator();
				ListIterator<entity> valItr = phiInst.values.listIterator();
				for (int i = 0;i < phiInst.blocks.size();++ i) {
					entity v = valItr.next();
					_move mvInst = new _move(v, phiInst.dest);
					parallelCopy.get(blkItr.next()).add(mvInst);
				}
			});
		});
	}

	private void parallelCopyReplacing(function func) {
		registerCounter = 0;
		func.blocks.forEach(blk -> {
			Set<_move> PCs = parallelCopy.get(blk);
			blk.removeAllPhi();
			Map<register, Integer> degree = new HashMap<>();
			PCs.forEach(mvInst -> mvInst.variables().forEach(v -> degree.put(v, 0)));
			PCs.forEach(mvInst -> {
				if (mvInst.src instanceof register)
					degree.put((register) mvInst.src, degree.get((register) mvInst.src) + 1);
			});
			while (!PCs.isEmpty()) {
				boolean found = false;
				for (Iterator<_move> PCItr = PCs.iterator();PCItr.hasNext();) {
					_move mvInst = PCItr.next();
					if (degree.get(mvInst.dest) == 0) {
						blk.insertInstructionBeforeTail(mvInst);
						PCItr.remove();
						found = true;
						if (mvInst.src instanceof register) degree.put((register) mvInst.src, degree.get(mvInst.src) - 1);
					}
				}
				if (!found) {
					_move mvInst = PCs.iterator().next();
					register v = new register(mvInst.dest.type, "_PCR." + (registerCounter ++));
					degree.put(v, 0);
					blk.insertInstructionBeforeTail(new _move(mvInst.src, v));
					PCs.remove(mvInst);
					PCs.add(new _move(v, mvInst.dest));
					degree.put((register) mvInst.src, degree.get(mvInst.src) - 1);
				}
			}
		});
	}
}
