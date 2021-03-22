package LLVMIR;

import LLVMIR.Operand.globalVariable;

import java.util.ArrayList;

public class entry {
	public ArrayList<function> functions = new ArrayList<>();
	public ArrayList<globalVariable> globals = new ArrayList<>();

	public entry() {}
}
