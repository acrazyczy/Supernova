package LLVMIR;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMSingleValueType;

import java.util.ArrayList;

public class function {
	public LLVMSingleValueType returnType;
	public String functionName;
	public ArrayList<LLVMSingleValueType> argTypes;
	public ArrayList<entity> argValues;
	public ArrayList<basicBlock> blocks;
}