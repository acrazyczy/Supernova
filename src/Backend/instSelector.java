package Backend;

import Assembly.Instruction.LOAD;
import Assembly.Instruction.STORE;
import Assembly.Operand.Imm;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;
import LLVMIR.IREntry;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.globalVariable;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.basicBlock;
import LLVMIR.Instruction.*;
import LLVMIR.function;

import java.util.HashMap;

public class instSelector implements pass {
	private final IREntry programIREntry;
	private final asmEntry programAsmEntry;
	private int blockCounter = 0;
	private int virtualRegCounter = 0;

	private asmFunction currentFunction;
	private asmBlock currentBlock;

	private HashMap<register, virtualReg> regMap;
	private HashMap<basicBlock, asmBlock> blkMap;

	private virtualReg registerMapping(register reg) {
		if (!regMap.containsKey(reg)) regMap.put(reg, new virtualReg(++ virtualRegCounter));
		return regMap.get(reg);
	}

	private asmBlock blockMapping(basicBlock blk) {
		if (!blkMap.containsKey(blk)) blkMap.put(blk, new asmBlock(++ blockCounter, blk.name));
		return blkMap.get(blk);
	}

	public instSelector(IREntry programIREntry, asmEntry programAsmEntry) {
		this.programIREntry = programIREntry;
		this.programAsmEntry = programAsmEntry;
	}

	private void buildAsmInst(statement stmt) {
		if (stmt instanceof _move) {

		} else if (stmt instanceof binary) {

		} else if (stmt instanceof bitcast) {

		} else if (stmt instanceof br) {

		} else if (stmt instanceof call) {

		} else if (stmt instanceof getelementptr) {

		} else if (stmt instanceof icmp) {

		} else if (stmt instanceof load) {
			load stmt_ = (load) stmt;
			if (stmt_.pointer instanceof globalVariable) {
				// TODO: 2021/3/27 deal with data segmentation 
			} else {
				currentBlock.insts.add(new LOAD(
					((LLVMPointerType) stmt_.pointer.type).pointeeType.size() == 8 ? LOAD.loadType.LBU : LOAD.loadType.LW,
					registerMapping((register) stmt_.dest),
					registerMapping((register) stmt_.pointer),
					new Imm(0)
				));
			}
		} else if (stmt instanceof ret) {

		} else {
			assert stmt instanceof store;
			store stmt_ = (store) stmt;
			if (stmt_.dest instanceof globalVariable) {
				// TODO: 2021/3/27 deal with data segmentation
			} else {
				currentBlock.insts.add(new STORE(
					stmt_.value.type.size() == 8 ? STORE.storeType.SB : STORE.storeType.SW,
					registerMapping((register) stmt_.pointer),
					new Imm(0),
					registerMapping((register) stmt_.value)
				));
			}
		}
	}

	private void buildAsmBlock(basicBlock block) {
		asmBlock asmBlk = new asmBlock(++ blockCounter, block.name);
		currentFunction.asmBlocks.add(asmBlk);
		currentBlock = asmBlk;
		block.stmts.forEach(this::buildAsmInst);
		currentBlock = null;
	}

	private void buildAsmFunction(function func) {
		asmFunction asmFunc = new asmFunction(func.functionName);
		programAsmEntry.asmFunctions.add(asmFunc);
		currentFunction = asmFunc;
		func.blocks.forEach(this::buildAsmBlock);
		currentFunction = null;
	}

	@Override
	public void run() {
		// TODO: 2021/3/27 deal with global data
		programIREntry.functions.forEach(this::buildAsmFunction);
	}
}
