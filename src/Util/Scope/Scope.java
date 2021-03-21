package Util.Scope;

import java.util.HashMap;

import AST.funcDefNode;
import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMSingleValueType;
import LLVMIR.function;
import Util.Type.Type;
import Util.Type.functionType;
import Util.error.semanticError;
import Util.position;
import Util.typeCalculator;

public class Scope {
	protected HashMap<String, Type> vars = new HashMap<>(), meths = new HashMap<>();
	protected HashMap<String, entity> varEntities;
	protected HashMap<String, function> methFunctions;
	private Scope parentScope;

	public Scope(Scope parentScope) {this.parentScope = parentScope;}

	public Scope parentScope() {return parentScope;}

	public void defineVariable(String name, Type t, position pos) {
		if (vars.containsKey(name)) throw new semanticError("variable redefine.", pos);
		vars.put(name, t);
	}

	public void bindVariableToEntity(String name, entity enty) {
		varEntities.put(name, enty);
	}

	public void bindMethodToFunction(String name, function func) {methFunctions.put(name, func);}

	public void defineMethod(String name, Type t, position pos) {
		if (meths.containsKey(name)) throw new semanticError("method redefine.", pos);
		meths.put(name, t);
	}

	public boolean containVariable(String name, boolean lookUpon) {
		if (vars.containsKey(name)) return true;
		else if (parentScope != null && lookUpon) return parentScope.containVariable(name, true);
		else return false;
	}

	public boolean containMethod(String name, boolean lookUpon) {
		if (meths.containsKey(name)) return true;
		else if (parentScope != null && lookUpon) return parentScope.containMethod(name, true);
		else return false;
	}

	public Type getVariableType(String name, boolean lookUpon) {
		if (vars.containsKey(name)) return vars.get(name);
		else if (parentScope != null && lookUpon) return parentScope.getVariableType(name, true);
		else return null;
	}

	public entity getVariableEntity(String name, boolean lookUpon) {
		if (varEntities.containsKey(name)) return varEntities.get(name);
		else if (parentScope != null && lookUpon) return parentScope.getVariableEntity(name, true);
		else return null; // member variable will go here
	}

	public function getMethodFunction(String name, boolean lookUpon) {
		if (methFunctions.containsKey(name)) return methFunctions.get(name);
		else if (parentScope != null && lookUpon) return parentScope.getMethodFunction(name, true);
		else return null;
	}

	public Type getMethodType(String name, boolean lookUpon) {
		if (meths.containsKey(name)) return meths.get(name);
		else if (parentScope != null && lookUpon) return parentScope.getMethodType(name, true);
		else return null;
	}

	public void registerMethod(globalScope gScope, funcDefNode funcDef) {
		if (funcDef.returnType == null && (this instanceof globalScope))
			throw new semanticError("return type not declared.", funcDef.pos);
		functionType funcType = typeCalculator.functionTypeGenerator(gScope, funcDef);
		if (funcDef.name.equals("main") && (this instanceof globalScope)) {
			if (!funcType.returnType.is_int)
				throw new semanticError("return type of main function must be int.", funcDef.pos);
			if (!funcType.paraType.isEmpty())
				throw new semanticError("main function should not have parameters.", funcDef.pos);
		}
		meths.put(funcDef.name, funcType);
	}
}
