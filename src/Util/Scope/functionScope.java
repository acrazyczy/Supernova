package Util.Scope;

import Util.Type.Type;

public class functionScope extends Scope {
	public functionScope(Scope parentScope) {super(parentScope);}

	@Override
	public boolean containVariable(String name) {return members.containsKey(name);}

	@Override
	public Type getType(String name) {
		if (members.containsKey(name)) return members.get(name);
		return null;
	}
}
