package LLVMIR;

import LLVMIR.Instruction.br;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMSingleValueType;

import java.util.*;
import java.util.stream.Collectors;

public class function {
	public LLVMSingleValueType returnType;
	public String functionName;
	public ArrayList<register> argValues;
	public LinkedList<basicBlock> blocks;
	private final HashMap<String, Integer> blockNameCounter = new HashMap<>();
	private final HashMap<String, Integer> registerNameCounter = new HashMap<>();

	public function(LLVMSingleValueType returnType, String functionName, ArrayList<register> argValues, boolean is_builtin) {
		this.returnType = returnType;
		this.functionName = functionName;
		this.argValues = argValues;
		this.blocks = is_builtin ? null : new LinkedList<>(Collections.singletonList(new basicBlock("entry", null)));
	}

	public function(LLVMSingleValueType returnType, String functionName, ArrayList<register> argValues, LinkedList<basicBlock> blocks) {
		this.returnType = returnType;
		this.functionName = functionName;
		this.argValues = argValues;
		this.blocks = blocks;
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
		entity argv = argList.iterator().next();
		ret.append(argv.type);
		if (!argv.toString().equals("")) ret.append(" ").append(argv);
		for (int i = 1;i < argList.size();++ i) {
			argv = argList.get(i);
			ret.append(", ").append(argv.type);
			if (!argv.toString().equals("")) ret.append(" ").append(argv);
		}
		return ret.toString();
	}

	public void variablesAnalysis(Set<register> variables, Set<register> uses, Set<register> defs, Map<register, Set<statement>> usePoses, Map<register, Set<basicBlock>> defPoses) {
		if (variables != null) variables.addAll(argValues);
		if (defs != null) defs.addAll(argValues);
		if (defPoses != null)
			argValues.forEach(argv -> {
				if (defPoses.containsKey(argv)) defPoses.get(argv).add(blocks.iterator().next());
				else defPoses.put(argv, new HashSet<>(Collections.singleton(blocks.iterator().next())));
			});
		blocks.forEach(blk -> blk.variablesAnalysis(variables, uses, defs, usePoses, defPoses));
	}

	public String functionToString(ArrayList<entity> argList) {
		return (returnType == null ? "void" : returnType) + " " + "@" + functionName + "(" + functionArgListToString(argList == null ? new ArrayList<>(argValues.stream().map(argv -> (entity) argv).collect(Collectors.toList())) : argList) + ")";
	}

	private register getReg(register reg, Map<register, register> regMapping) {
		if (!regMapping.containsKey(reg)) {
			register regMirror = new register(reg.type, reg.name);
			regMapping.put(reg, regMirror);
		}
		return regMapping.get(reg);
	}

	private basicBlock getBlk(basicBlock blk, Map<basicBlock, basicBlock> blkMapping) {
		if (!blkMapping.containsKey(blk)) {
			basicBlock blkMirror = new basicBlock(blk.name, null);
			blkMapping.put(blk, blkMirror);
		}
		return blkMapping.get(blk);
	}

	public function clone() {
		assert this.blocks != null;
		Map<basicBlock, basicBlock> blkMapping = new HashMap<>();
		Map<register, register> regMapping = new HashMap<>();
		ArrayList<register> argValues = new ArrayList<>(this.argValues);
		for (ListIterator<register> argItr = argValues.listIterator();argItr.hasNext();) argItr.set(getReg(argItr.next(), regMapping));
		LinkedList<basicBlock> blocks = new LinkedList<>();
		this.blocks.forEach(blk -> {
			basicBlock blkMirror = getBlk(blk, blkMapping);
			blocks.add(blkMirror);
			blk.stmts.forEach(stmt -> {
				statement stmtMirror = stmt.clone();
				stmtMirror.replaceAllRegister(reg -> getReg(reg, regMapping));
				if (stmtMirror instanceof br) {
					br brInst = (br) stmtMirror;
					brInst.trueBranch = getBlk(brInst.trueBranch, blkMapping);
					if (brInst.cond != null) brInst.falseBranch = getBlk(brInst.falseBranch, blkMapping);
				} else if (stmtMirror instanceof phi)
					for (ListIterator<basicBlock> phiBlkItr = ((phi) stmtMirror).blocks.listIterator();phiBlkItr.hasNext();)
						phiBlkItr.set(getBlk(phiBlkItr.next(), blkMapping));
				blkMirror.push_back(stmtMirror);
			});
		});
		return new function(returnType, functionName, argValues, blocks);
	}

	@Override public String toString() {return functionToString(null);}
}