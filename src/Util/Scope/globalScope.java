package Util.Scope;

import LLVMIR.TypeSystem.LLVMFirstClassType;
import LLVMIR.TypeSystem.LLVMStructureType;
import Util.Type.Type;
import Util.error.semanticError;
import Util.position;

import java.util.HashMap;

public class globalScope extends Scope {
	private HashMap<String, Type> types = new HashMap<>();
	private HashMap<Type, LLVMFirstClassType> typesMap = new HashMap<>();

	public globalScope(Scope parentScope) {super(parentScope);}

	public void addType(String name, Type t, position pos) {
		if (types.containsKey(name)) throw new semanticError("multiple definition of " + name + ".", pos);
		types.put(name, t);
		typesMap.put(t, new LLVMStructureType(name));
	}

	public Type getTypeFromName(String name, position pos) {
		if (name == null) return null;
		if (types.containsKey(name)) return types.get(name);
		throw new semanticError("no such type: " + name + ".", pos);
	}

	public LLVMFirstClassType getLLVMTypeFromType(Type t) {
		return typesMap.get(t);
	}
}
