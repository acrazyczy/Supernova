package LLVMIR;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMFirstClassType;
import LLVMIR.TypeSystem.LLVMType;

import java.util.ArrayList;

public class function {
	public LLVMType returnType;
	public String functionName;
	public ArrayList<LLVMFirstClassType> argTypes;
	public ArrayList<entity> argValues;
	public ArrayList<basicBlock> blocks;
}