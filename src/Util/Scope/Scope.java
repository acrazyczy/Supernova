package Util.Scope;

import java.util.HashMap;

import Util.Type.Type;
import Util.error.semanticError;
import Util.position;

public class Scope {
	public HashMap<String, Type> members = new HashMap<>();
	public Scope parentScope;

	public Scope(Scope parentScope) {this.parentScope = parentScope;}

	public Scope parentScope() {return parentScope;}

	public void defineVariable(String name, Type t, position pos) {
		if (members.containsKey(name)) throw new semanticError("variable redefine.", pos);
		members.put(name, t);
	}

	public boolean containVariable(String name) {
		if (members.containsKey(name)) return true;
		else if (parentScope != null) return parentScope.containVariable(name);
		else return false;
	}

	public Type getType(String name) {
		if (members.containsKey(name)) return members.get(name);
		else if (parentScope != null) return parentScope.getType(name);
		else return null;
	}
}
