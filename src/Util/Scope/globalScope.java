package Util.Scope;

import AST.funcDefNode;
import LLVMIR.TypeSystem.LLVMFirstClassType;
import LLVMIR.TypeSystem.LLVMStructureType;
import Util.Type.Type;
import Util.Type.functionType;
import Util.error.semanticError;
import Util.position;
import Util.typeCalculator;

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

	public void registerMethod(funcDefNode funcDef, String funcName, boolean isClassMethod) {
		if (funcDef.returnType == null && !isClassMethod)
			throw new semanticError("return type not declared.", funcDef.pos);
		functionType funcType = typeCalculator.functionTypeGenerator(this, funcDef);
		if (funcDef.name.equals("main") && !isClassMethod) {
			if (!funcType.returnType.is_int)
				throw new semanticError("return type of main function must be int.", funcDef.pos);
			if (!funcType.paraType.isEmpty())
				throw new semanticError("main function should not have parameters.", funcDef.pos);
		}
		meths.put(funcName, funcType);
	}

	public LLVMFirstClassType getLLVMTypeFromType(Type t) {
		return typesMap.get(t);
	}
}
