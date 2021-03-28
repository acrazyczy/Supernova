package Assembly;

import Assembly.Operand.globalData;
import LLVMIR.Operand.globalVariable;
import LLVMIR.function;

import java.util.HashMap;

public class asmEntry {
	public HashMap<globalVariable, globalData> gblMapping = new HashMap<>();
	public HashMap<function, asmFunction> asmFunctions = new HashMap<>();

	public asmEntry() {}
}
