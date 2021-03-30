package Assembly;

import Assembly.Operand.intImm;
import Assembly.Operand.virtualReg;
import LLVMIR.function;

import java.util.ArrayList;
import java.util.Map;

public class stackFrame {
	private asmFunction asmFunc;
	private int size;

	public Map<virtualReg, intImm> spilledRegisterOffsets;
	public Map<function, ArrayList<intImm>> calleeParameterOffsets;
	public ArrayList<intImm> callerParameterOffsets;

	public stackFrame(asmFunction asmFunc) {this.asmFunc = asmFunc;}

	public void offsetsComputation() {
		int maxParameterNumber = calleeParameterOffsets.keySet().stream().mapToInt(func -> func.argValues.size()).summaryStatistics().getMax();
		size = spilledRegisterOffsets.size() + maxParameterNumber;
		for (int i = 0;i < callerParameterOffsets.size();++ i)
			callerParameterOffsets.get(i).val = (i + size) * 4;
		calleeParameterOffsets.values().forEach(offsets -> {
			for (int i = 0;i < offsets.size();++ i)
				offsets.get(i).val = i * 4;
		});
		int cnt = 0;
		for (intImm offset: spilledRegisterOffsets.values())
			offset.val = ((cnt ++) + maxParameterNumber) * 4;
	}
}