package Util.Type;

import java.util.LinkedHashMap;

public class classType extends Type {
	public LinkedHashMap<String, Type> memberVariables = null;
	public LinkedHashMap<String, Integer> memberVariablesIndex = null;
	public LinkedHashMap<String, functionType> memberMethods = null;
	public String className;
	public int memberVariablesCounter = 0;

	public classType(String className) {
		super();
		this.className = className;
	}
}
