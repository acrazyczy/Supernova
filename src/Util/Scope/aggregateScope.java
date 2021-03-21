package Util.Scope;

import LLVMIR.Operand.entity;
import LLVMIR.function;
import Util.Type.Type;

public class aggregateScope extends Scope {
	private String className;

	public aggregateScope(Scope parentScope, String className) {
		super(parentScope);
		this.className = className;
	}

	@Override
	public boolean containVariable(String name, boolean lookUpon) {
		return super.containVariable(className + "." + name, lookUpon) || super.containVariable(name, lookUpon);
	}

	@Override
	public boolean containMethod(String name, boolean lookUpon) {
		return super.containMethod(className + "." + name, lookUpon) || super.containMethod(name, lookUpon);
	}

	@Override
	public Type getVariableType(String name, boolean lookUpon) {
		Type ret = super.getVariableType(className + "." + name, lookUpon);
		if (ret == null) ret = super.getVariableType(name, lookUpon);
		return ret;
	}

	@Override
	public Type getMethodType(String name, boolean lookUpon) {
		Type ret = super.getMethodType(className + "." + name, lookUpon);
		if (ret == null) ret = super.getVariableType(name, lookUpon);
		return ret;
	}

	@Override
	public entity getVariableEntity(String name, boolean lookUpon) {
		entity ret = super.getVariableEntity(className + "." + name, lookUpon);
		if (ret == null) ret = super.getVariableEntity(name, lookUpon);
		return ret;
	}

	@Override
	public function getMethodFunction(String name, boolean lookUpon) {
		function ret = super.getMethodFunction(className + "." + name, lookUpon);
		if (ret == null) ret = super.getMethodFunction(name, lookUpon);
		return ret;
	}
}
