package LLVMIR;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMSingleValueType;

import java.util.ArrayList;
import java.util.Collections;

public class function {
	public LLVMSingleValueType returnType;
	public String functionName;
	public ArrayList<entity> argValues;
	public ArrayList<basicBlock> blocks;

	public function(LLVMSingleValueType returnType, String functionName, ArrayList<entity> argValues, boolean is_builtin) {
		this.returnType = returnType;
		this.functionName = functionName;
		this.argValues = argValues;
		this.blocks = is_builtin ? null : new ArrayList<>(Collections.singletonList(new basicBlock("entry")));
	}
}