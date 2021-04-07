package LLVMIR;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMSingleValueType;

import java.util.*;

public class function {
	public LLVMSingleValueType returnType;
	public String functionName;
	public ArrayList<entity> argValues;
	public ArrayList<basicBlock> blocks;
	private final HashMap<String, Integer> blockNameCounter = new HashMap<>();
	private final HashMap<String, Integer> registerNameCounter = new HashMap<>();

	private Set<basicBlock> visited;

	public function(LLVMSingleValueType returnType, String functionName, ArrayList<entity> argValues, boolean is_builtin) {
		this.returnType = returnType;
		this.functionName = functionName;
		this.argValues = argValues;
		this.blocks = is_builtin ? null : new ArrayList<>(Collections.singletonList(new basicBlock("entry", null, 0)));
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

	public int getRegisterNameIndex(String registerName) {
		if (registerNameCounter.containsKey(registerName)) {
			int idx = registerNameCounter.get(registerName);
			registerNameCounter.put(registerName, idx + 1);
			return idx;
		} else {
			registerNameCounter.put(registerName, 1);
			return 0;
		}
	}

	private String functionArgListToString(ArrayList<entity> argList) {
		StringBuilder ret = new StringBuilder();
		if (argList.isEmpty()) return ret.toString();
		entity argv = argList.get(0);
		ret.append(argv.type);
		if (!argv.toString().equals("")) ret.append(" ").append(argv);
		for (int i = 1;i < argList.size();++ i) {
			argv = argList.get(i);
			ret.append(", ").append(argv.type);
			if (!argv.toString().equals("")) ret.append(" ").append(argv);
		}
		return ret.toString();
	}

	private void dfsBlock(basicBlock blk, ArrayList<basicBlock> dfsOrder) {
		dfsOrder.add(blk);
		visited.add(blk);
		blk.successors().stream().filter(sucBlk -> !visited.contains(sucBlk)).forEach(sucBlk -> dfsBlock(sucBlk, dfsOrder));
	}

	public ArrayList<basicBlock> dfsOrderComputation() {
		assert !blocks.isEmpty();
		ArrayList<basicBlock> ret = new ArrayList<>();
		visited = new HashSet<>();
		dfsBlock(blocks.get(0), ret);
		return ret;
	}

	public Set<register> variables() {
		Set<register> ret = new HashSet<>();
		blocks.forEach(blk -> ret.addAll(blk.variables()));
		return ret;
	}

	public String functionToString(ArrayList<entity> argList) {
		return (returnType == null ? "void" : returnType) + " " + "@" + functionName + "(" + functionArgListToString(argList == null ? argValues : argList) + ")";
	}

	@Override public String toString() {return functionToString(null);}
}