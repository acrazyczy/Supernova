package Assembly;

import Assembly.Instruction.ITypeInst;
import Assembly.Instruction.retInst;
import Assembly.Operand.intImm;
import Assembly.Operand.physicalReg;
import Assembly.Operand.virtualReg;
import LLVMIR.function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Integer.max;

public class stackFrame {
	private final asmFunction asmFunc;

	public Map<virtualReg, intImm> spilledRegisterOffsets;
	public Map<function, ArrayList<intImm>> calleeParameterOffsets;
	public ArrayList<intImm> callerParameterOffsets;

	public stackFrame(asmFunction asmFunc) {
		this.asmFunc = asmFunc;
		spilledRegisterOffsets = new LinkedHashMap<>();
		calleeParameterOffsets = new LinkedHashMap<>();
	}

	public void offsetsComputation() {
		int maxParameterNumber = max(calleeParameterOffsets.isEmpty() ? 0 : calleeParameterOffsets.keySet().stream().mapToInt(func -> func.argValues.size()).summaryStatistics().getMax() - 8, 0);
		int size = spilledRegisterOffsets.size() + maxParameterNumber;
		for (int i = 0;i < callerParameterOffsets.size();++ i)
			callerParameterOffsets.get(i).val = (i + size) * 4;
		calleeParameterOffsets.values().forEach(offsets -> {
			for (int i = 0;i < offsets.size();++ i)
				offsets.get(i).val = i * 4;
		});
		int cnt = 0;
		for (intImm offset: spilledRegisterOffsets.values())
			offset.val = ((cnt ++) + maxParameterNumber) * 4;
		virtualReg sp = physicalReg.pRegToVReg.get(physicalReg.pRegs.get("sp"));
		ITypeInst spShiftInst = new ITypeInst(asmFunc.initBlock, ITypeInst.opType.addi, sp, sp, new intImm(-size * 4));
		if (asmFunc.initBlock.headInst != null) asmFunc.initBlock.headInst.linkBefore(spShiftInst);
		else asmFunc.initBlock.headInst = asmFunc.initBlock.tailInst = spShiftInst;
		spShiftInst = new ITypeInst(asmFunc.initBlock, ITypeInst.opType.addi, sp, sp, new intImm(size * 4));
		assert asmFunc.retBlock.tailInst instanceof retInst;
		asmFunc.retBlock.tailInst.linkBefore(spShiftInst);
	}
}