package Optimization.IR.Analyser;

import LLVMIR.IRLoop;
import LLVMIR.Instruction.br;
import LLVMIR.Instruction.phi;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class loopAnalyzer {
	private final function func;
	private final boolean addPreHeaderFlag;
	private dominanceAnalyser dominanceProperty;

	public Set<IRLoop> rootLoops = new LinkedHashSet<>();
	private final Map<basicBlock, IRLoop> loopMapping = new LinkedHashMap<>();

	public loopAnalyzer(function func, boolean addPreHeaderFlag) {
		this.func = func;
		this.addPreHeaderFlag = addPreHeaderFlag;
	}

	private void collectLoop(basicBlock tail, basicBlock head) {
		if (!loopMapping.containsKey(head)) {
			IRLoop loop = new IRLoop();
			loopMapping.put(head, loop);
		}
		loopMapping.get(head).tails.add(tail);
	}

	private void addPreHeader(basicBlock head, IRLoop loop) {
		List<basicBlock> predecessors = new ArrayList<>(dominanceProperty.radj.get(head));
		predecessors.removeAll(loop.tails);
		if (predecessors.size() == 1) loop.preHead = predecessors.iterator().next();
		else {
			basicBlock preHead = new basicBlock("preHead", func);
			func.blocks.add(func.blocks.indexOf(head), preHead);

			head.phiCollections.values().forEach(phiInst -> {
				phi prePhi = null;
				ListIterator<entity> valItr = phiInst.values.listIterator();
				ListIterator<basicBlock> blkItr = phiInst.blocks.listIterator();
				while (valItr.hasNext()) {
					entity value = valItr.next();
					basicBlock blk = blkItr.next();
					if (!loop.tails.contains(blk)) {
						if (prePhi == null) {
							prePhi = new phi(
								new LinkedList<>(), new LinkedList<>(),
								new register(phiInst.dest.type, "preHead." + ((register) phiInst.dest).name)
							);
							preHead.push_back(prePhi);
						}
						((register) prePhi.dest).def = prePhi;
						prePhi.values.add(value);
						prePhi.blocks.add(blk);
						valItr.remove();
						blkItr.remove();
					}
				}

				if (prePhi != null) {
					phiInst.values.add(prePhi.dest);
					phiInst.blocks.add(preHead);
				}
			});

			predecessors.forEach(preBlk -> {
				dominanceProperty.radj.put(preBlk, new LinkedHashSet<>(dominanceProperty.radj.get(head)));
				predecessors.forEach(dominanceProperty.radj.get(head)::remove);
				dominanceProperty.radj.get(head).add(preHead);
				preBlk.replaceSuccessor(head, preHead);
			});
			preHead.push_back(new br(head));
			loop.preHead = preHead;
		}
	}

	private void getWholeLoop(basicBlock tail, basicBlock head) {
		Set<basicBlock> inLoopBlocks = new LinkedHashSet<>();
		Queue<basicBlock> workList = new ArrayDeque<>();
		inLoopBlocks.add(head);
		inLoopBlocks.add(tail);
		workList.offer(tail);

		while (!workList.isEmpty()) {
			basicBlock blk = workList.poll();
			dominanceProperty.radj.get(blk).forEach(preBlk -> {
				if (!inLoopBlocks.contains(preBlk)) {
					workList.offer(preBlk);
					inLoopBlocks.add(preBlk);
				}
			});
		}

		loopMapping.get(head).blocks.addAll(inLoopBlocks);
	}

	Set<basicBlock> isVisited;
	Stack<IRLoop> loopStack;

	private void jumpOutOfLoop(basicBlock blk) {
		while (!loopStack.isEmpty() && !loopStack.peek().blocks.contains(blk)) loopStack.pop();
	}

	private void loopScanning(basicBlock blk) {
		isVisited.add(blk);
		jumpOutOfLoop(blk);
		if (loopMapping.containsKey(blk)) {
			IRLoop loop = loopMapping.get(blk);
			if (loopStack.isEmpty()) rootLoops.add(loop);
			else loopStack.peek().children.add(loop);
			loopStack.push(loop);
		}
		blk.loopDepth = loopStack.size();
		blk.successors().forEach(sucBlk -> {if (!isVisited.contains(sucBlk)) loopScanning(sucBlk);});
	}

	public void run() {
		dominanceProperty = new dominanceAnalyser(func.blocks.iterator().next(), new LinkedHashSet<>(func.blocks));
		func.blocks.forEach(u -> u.successors().forEach(v -> dominanceProperty.addEdge(u, v)));
		dominanceProperty.dominanceAnalysis(false);

		func.blocks.forEach(blk -> {
			for (basicBlock sucBlk: blk.successors())
				if (dominanceProperty.isDominatedBy(blk, sucBlk)) {
					collectLoop(blk, sucBlk);
					break;
				}
		});
		if (addPreHeaderFlag) loopMapping.forEach(this::addPreHeader);
		loopMapping.forEach((head, loop) -> loop.tails.forEach(tail -> getWholeLoop(tail, head)));
		isVisited = new LinkedHashSet<>();
		loopStack = new Stack<>();
		loopScanning(func.blocks.iterator().next());
	}
}
