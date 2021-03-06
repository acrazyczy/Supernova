package LLVMIR;

import LLVMIR.Operand.globalVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class entry {
	public HashMap<String, function> functions;
	public ArrayList<globalVariable> globals;
	public ArrayList<basicBlock> basicBlocks;

}
