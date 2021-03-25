package LLVMIR;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMSingleValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class function {
	public LLVMSingleValueType returnType;
	public String functionName;
	public ArrayList<entity> argValues;
	public ArrayList<basicBlock> blocks;
	private final HashMap<String, Integer> blockNameCounter;

	public function(LLVMSingleValueType returnType, String functionName, ArrayList<entity> argValues, boolean is_builtin) {
		this.returnType = returnType;
		this.functionName = functionName;
		this.argValues = argValues;
		this.blocks = is_builtin ? null : new ArrayList<>(Collections.singletonList(new basicBlock("entry", null)));
		this.blockNameCounter = new HashMap<>();
	}

	public int getBlockNameIndex(String blockName) {
		if (blockNameCounter.containsKey(blockName)) {
			int idx = blockNameCounter.get(blockName);
			blockNameCounter.put(blockName, idx + 1);
			return idx;
		} else {
			blockNameCounter.put(blockName, 1);
			return 0;
		}
	}

	private String functionArgListToString(ArrayList<entity> argList) {
		StringBuilder ret = new StringBuilder();
		if (argList.isEmpty()) return ret.toString();
		entity argv = argList.get(0);
		ret.append(argv.type).append(" ").append(argv);
		for (int i = 1;i < argList.size();++ i) {
			argv = argList.get(i);
			ret.append(", ").append(argv.type).append(" ").append(argv);
		}
		return ret.toString();
	}

	public String functionToString(ArrayList<entity> argList) {
		return returnType + " " + "@" + functionName + "(" + functionArgListToString(argList == null ? argValues : argList) + ")";
	}

	@Override public String toString() {return functionToString(null);}
}