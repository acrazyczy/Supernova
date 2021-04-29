package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.Operand.undefinedValue;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class inlineExpansion implements pass {
	private final IREntry programIREntry;
	private final boolean forceInlining;

	private static final int bound = 64;

	public inlineExpansion(IREntry programIREntry, boolean forceInlining) {
		this.programIREntry = programIREntry;
		this.forceInlining = forceInlining;
	}

	Set<function> isVisited, unInlinable;
	callingAnalyser callingProperty;
	LinkedList<function> dfsStack;

	private boolean dfs(function func) {
		isVisited.add(func);
		dfsStack.add(func);
		boolean inCycle = false;
		for (function f: dfsStack) {
			if (callingProperty.callee.get(func).contains(f)) inCycle = true;
			if (inCycle) unInlinable.add(f);
		}
		boolean ret = callingProperty.callee.get(func).stream().filter(callee -> !isVisited.contains(callee)).map(this::dfs).reduce(false, (a, b) -> a || b);
		for (function callee: callingProperty.callee.get(func))
			if (!unInlinable.contains(callee) && callee.blocks.stream().mapToInt(blk -> blk.stmts.size()).sum() < bound) {
				callingProperty.callInst.get(func).get(callee).forEach(stmt -> inlining(stmt, func));
				ret = true;
			}
		dfsStack.removeLast();
		return ret;
	}

	private register getReg(register reg, Map<register, register> regMapping, function func) {
		if (!regMapping.containsKey(reg)) {
			String regName = reg.name;
			register regMirror = new register(reg.type, regName + ".il", func);
			regMapping.put(reg, regMirror);
		}
		return regMapping.get(reg);
	}

	private basicBlock getBlk(basicBlock blk, Map<basicBlock, basicBlock> blkMapping, function func) {
		if (!blkMapping.containsKey(blk)) {
			String blkName = blk.name;
			if (blkName.lastIndexOf('.') != -1) blkName = blkName.substring(0, blkName.lastIndexOf('.'));
			basicBlock blkMirror = new basicBlock(blkName, func);
			blkMapping.put(blk, blkMirror);
		}
		return blkMapping.get(blk);
	}

	Map<function, function> cloneMap;

	private function getClone(function func) {
		if (!cloneMap.containsKey(func)) cloneMap.put(func, func.clone());
		return cloneMap.get(func);
	}

	private void inlining(call callInst, function func) {
		function callee = forceInlining ? callInst.callee.clone() : getClone(callInst.callee);

		// split current block into three parts: before calling, callee function body and after calling
		String blockName = callInst.belongTo.name;
		basicBlock before = callInst.belongTo;
		before.name = blockName + ".beforeCall" + func.getBlockNameIndex(blockName + ".beforeCall");
		basicBlock after = new basicBlock(blockName + ".afterCall", func);
		before.successors().forEach(sucBlk -> sucBlk.replacePredecessor(before, after));
		before.splitCallInstruction(after, callInst);
		func.blocks.add(func.blocks.indexOf(before) + 1, after);

		// initialize block and register mapping
		Map<basicBlock, basicBlock> blkMapping = new LinkedHashMap<>();
		Map<register, register> regMapping = new LinkedHashMap<>();
		LinkedList<basicBlock> phiBlocks = new LinkedList<>();
		LinkedList<entity> phiValues = new LinkedList<>();

		// assign value to each parameter
		Iterator<entity> srcItr = callInst.parameters.iterator();
		Iterator<register> destItr = callee.argValues.iterator();
		while (srcItr.hasNext()) {
			_move mvInst = new _move(srcItr.next(), getReg(destItr.next(), regMapping, func));
			((register) mvInst.dest).def = mvInst;
			before.push_back(mvInst);
		}

		// add each block of callee to caller, replace register name and block name one by one and replace ret statement
		ListIterator<basicBlock> blkItr = func.blocks.listIterator(func.blocks.indexOf(after));
		callee.blocks.forEach(blk -> {
			basicBlock blkMirror = getBlk(blk, blkMapping, func);
			blkItr.add(blkMirror);
			blk.stmts.forEach(stmt -> {
				statement stmtMirror = stmt.clone();
				stmtMirror.replaceAllRegister(reg -> getReg(reg, regMapping, func));
				if (stmtMirror.dest != null) ((register) stmtMirror.dest).def = stmtMirror;
				if (stmtMirror instanceof ret) {
					ret retInst = (ret) stmtMirror;
					if (retInst.value != null) {
						phiBlocks.add(blkMirror); phiValues.add(retInst.value);
					}
					blkMirror.push_back(new br(after));
				} else {
					if (stmtMirror instanceof br) {
						br brInst = (br) stmtMirror;
						brInst.trueBranch = getBlk(brInst.trueBranch, blkMapping, func);
						if (brInst.cond != null) brInst.falseBranch = getBlk(brInst.falseBranch, blkMapping, func);
					} else if (stmtMirror instanceof phi)
						for (ListIterator<basicBlock> phiBlkItr = ((phi) stmtMirror).blocks.listIterator();phiBlkItr.hasNext();)
							phiBlkItr.set(getBlk(phiBlkItr.next(), blkMapping, func));
					blkMirror.push_back(stmtMirror);
				}
			});
		});

		// add an unconditional jump to before block
		before.push_back(new br(getBlk(callee.blocks.iterator().next(), blkMapping, func)));

		// use phi function to collect all possible return values
		if (callee.returnType != null)
			if (!phiBlocks.isEmpty()) {
				phi phiInst = new phi(phiBlocks, phiValues, callInst.dest);
				after.push_front(phiInst);
				((register) callInst.dest).def = phiInst;
			} else {
				_move mvInst = new _move(new undefinedValue(callInst.dest.type), callInst.dest);
				after.push_front(mvInst);
				((register) callInst.dest).def = mvInst;
			}
	}

	@Override
	public boolean run() {
		isVisited = new LinkedHashSet<>();
		unInlinable = new LinkedHashSet<>();
		programIREntry.functions.forEach(func -> {if (func.blocks == null) {unInlinable.add(func); isVisited.add(func);}});
		callingProperty = new callingAnalyser(programIREntry);
		callingProperty.run();
		if (forceInlining) {
			LinkedHashMap<function, Integer> lineNumber = new LinkedHashMap<>();
			unInlinable.add(programIREntry.functions.iterator().next());
			programIREntry.functions.forEach(func -> lineNumber.put(func, func.blocks == null ? 0 : func.blocks.stream().mapToInt(blk -> blk.stmts.size()).sum()));
			Map<call, function> inliningCalls = new LinkedHashMap<>();
			programIREntry.functions.stream().filter(func -> func.blocks != null)
				.forEach(func -> func.blocks.forEach(blk -> blk.stmts.stream().filter(stmt -> stmt instanceof call).forEach(stmt -> {
					call callInst = (call) stmt;
					function callee = callInst.callee;
					if (!unInlinable.contains(callee) && lineNumber.get(callee) < bound) {
						inliningCalls.put(callInst, func);
						lineNumber.put(func, lineNumber.get(func) + lineNumber.get(callee));
					}
			})));
			inliningCalls.forEach(this::inlining);
			return true;
		} else {
			dfsStack = new LinkedList<>();
			cloneMap = new LinkedHashMap<>();
			return programIREntry.functions.stream().filter(func -> !isVisited.contains(func)).map(this::dfs).reduce(false, (a, b) -> a || b);
		}
	}
}
