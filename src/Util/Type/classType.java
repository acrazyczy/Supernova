package Util.Type;

import java.util.HashMap;

public class classType extends Type {
	public HashMap<String, Type> memberVariables = null;
	public HashMap<String, Integer> memberVariablesIndex = null;
	public HashMap<String, functionType> memberMethods = null;

	public int memberVariablesCounter = 0;
}
