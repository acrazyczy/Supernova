package Util.Scope;

import Util.Type.Type;

public class functionScope extends Scope {
	public functionScope(Scope parentScope) {super(parentScope);}

	@Override public boolean containVariable(String name) {return vars.containsKey(name);}

	@Override public boolean containMethod(String name) {return meths.containsKey(name);}

	@Override public Type getVariableType(String name) {return vars.getOrDefault(name, null);}

	@Override public Type getMethodType(String name) {return meths.getOrDefault(name, null);}
}
