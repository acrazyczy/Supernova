package Backend;

import Assembly.Instruction.*;
import Assembly.Operand.*;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;
import LLVMIR.function;

import java.util.ArrayList;
import java.util.HashMap;

public class regAllocator implements asmVisitor {
	private class LRUNode {
		virtualReg pre = null, suf = null;
		int location, offset; // location == -1 -> offset, otherwise physical register
	}

	private final asmEntry programAsmEntry;
	private HashMap<virtualReg, LRUNode> LRU;
	private virtualReg LRUHead, LRUTail;
	static physicalReg
		sp = physicalReg.pRegs.get("sp"),
		ra = physicalReg.pRegs.get("ra");
	private int pRegCounter;

	public regAllocator(asmEntry programAsmEntry) {
		this.programAsmEntry = programAsmEntry;
	}

	private physicalReg getPhysicalRegister(virtualReg vReg, ArrayList<inst> insts) {
		LRUNode node = LRU.get(vReg);
		physicalReg pReg;
		if (node.location < 0) {
			if (pRegCounter == 28) {
				LRUNode node_ = LRU.get(LRUTail);
				physicalReg pReg_ = physicalReg.allocatablePRegs.get(physicalReg.allocatablePRegNames[node_.location]);
				insts.add(new storeInst(storeInst.storeType.sw, sp, new intImm(node_.offset), pReg_));
				LRU.get(LRUTail = node.pre).suf = null;
				node_.location = -1;
				node_.pre = node_.suf = null;
			} else node.location = pRegCounter ++;
			if (LRUHead == null) LRUHead = LRUTail = vReg;
			else {
				LRU.get(node.suf = LRUHead).pre = vReg;
				node.pre = null;
				LRUHead = vReg;
			}
			pReg = physicalReg.allocatablePRegs.get(physicalReg.allocatablePRegNames[node.location]);
			insts.add(new loadInst(loadInst.loadType.lw, pReg, sp, new intImm(node.offset)));
		} else pReg = physicalReg.allocatablePRegs.get(physicalReg.allocatablePRegNames[node.location]);
		return pReg;
	}

	private void allocateAsmBlock(asmBlock blk) {
		LRUHead = LRUTail = null;
		pRegCounter = 0;
		LRU = new HashMap<>();
		inst currentInst = blk.headInst;
		while (currentInst != null) {
			ArrayList<inst> newInst = new ArrayList<>();
			if (currentInst instanceof callInst) {

			} else currentInst.replaceVirtualRegister(newInst, this::getPhysicalRegister);
			inst top = currentInst.linkBefore(newInst);
			if (top != null) blk.headInst = top;
			currentInst = currentInst.suf;
		}
	}

	private void allocateAsmFunction(function IRfunc, asmFunction asmFunc) {
		// fill init block
		asmFunc.initBlock.addInst(new storeInst(storeInst.storeType.sw, sp, new intImm(asmFunc.stkFrame.allocate(4)), ra));
		for (String name: physicalReg.calleeSavePRegNames)
			asmFunc.initBlock.addInst(new storeInst(storeInst.storeType.sw, sp, ));
		asmFunc.asmBlocks.forEach(this::allocateAsmBlock);
		// fill return block
		asmFunc.asmBlocks.
	}

	@Override public void run() {programAsmEntry.asmFunctions.forEach(this::allocateAsmFunction);}
}
