package Util;

import AST.funcDefNode;
import AST.typeNode;
import Util.Scope.globalScope;
import Util.Type.Type;
import Util.Type.arrayType;
import Util.Type.functionType;

import java.util.ArrayList;

public class typeCalculator {
	static public Type calcType(globalScope gScope, typeNode it) {
		Type baseType = gScope.getTypeFromName(it.typeName, it.pos);
		return it.dim > 1 ? new arrayType(baseType, it.dim) : baseType;
	}

	static public boolean isEqualType(Type lhs, Type rhs) {
		return true;
	}

	static public functionType functionTypeGenerator(globalScope gScope, funcDefNode it) {
		ArrayList<Type> paraType = new ArrayList<>();
		it.paraType.forEach(t -> paraType.add(calcType(gScope, t)));
		return new functionType(calcType(gScope, it.returnType), paraType);
	}
}
