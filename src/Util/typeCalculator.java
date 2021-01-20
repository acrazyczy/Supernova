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
		return it.dim >= 1 ? new arrayType(baseType, it.dim) : baseType;
	}

	static public boolean isEqualType(Type lhs, Type rhs) {
		if (lhs instanceof arrayType) {
			if (!(rhs instanceof arrayType)) return false;
			return ((arrayType) lhs).dim == ((arrayType) rhs).dim && isEqualType(((arrayType) lhs).elementType, ((arrayType) rhs).elementType);
		} else if (rhs instanceof arrayType) return false;
		return lhs == rhs;
	}

	static public functionType functionTypeGenerator(globalScope gScope, funcDefNode it) {
		ArrayList<Type> paraType = new ArrayList<>();
		it.paraType.forEach(t -> paraType.add(calcType(gScope, t)));
		return new functionType(calcType(gScope, it.returnType), paraType);
	}
}
