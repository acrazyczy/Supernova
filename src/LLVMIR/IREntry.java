package LLVMIR;

import LLVMIR.Operand.globalVariable;
import LLVMIR.TypeSystem.LLVMStructureType;

import java.util.ArrayList;

public class IREntry {
	public ArrayList<function> functions = new ArrayList<>();
	public ArrayList<globalVariable> globals = new ArrayList<>();
	public ArrayList<LLVMStructureType> classes = new ArrayList<>();

	public IREntry() {}
}
