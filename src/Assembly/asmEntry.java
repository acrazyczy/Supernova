package Assembly;

import Assembly.Operand.globalData;
import LLVMIR.Operand.globalVariable;
import LLVMIR.function;

import java.util.LinkedHashMap;

public class asmEntry {
	public LinkedHashMap<globalVariable, globalData> gblMapping = new LinkedHashMap<>();
	public LinkedHashMap<function, asmFunction> asmFunctions = new LinkedHashMap<>();

	public asmEntry() {}
}
