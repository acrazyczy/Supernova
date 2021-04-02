package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMFirstClassType;
import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;

public class globalVariable extends entity {
	public final String name;
	public boolean isConstant, isString;
	public String val;

	public globalVariable(LLVMFirstClassType type, String name, boolean isConstant, boolean isString) {
		super(new LLVMPointerType(type));
		this.isConstant = isConstant;
		this.isString = isString;
		this.name = name;
		if (type instanceof LLVMIntegerType) val = "0";
		else val = "null";
	}

	public String convert() {
		return val.replace("\\", "\\5C")
			.replace("\n", "\\0A")
			.replace("\"", "\\22")
			.replace("\t", "\\09");
	}

	@Override public String toString() {return "@" + name;}
}
