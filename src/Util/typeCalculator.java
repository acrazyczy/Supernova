package Util;

import AST.funcDefNode;
import AST.typeNode;
import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMSingleValueType;
import Util.Scope.globalScope;
import Util.Type.Type;
import Util.Type.arrayType;
import Util.Type.classType;
import Util.Type.functionType;

import java.util.ArrayList;

public class typeCalculator {
	static public Type calcType(globalScope gScope, typeNode it) {
		if (it == null) return gScope.getTypeFromName("void", null);
		Type baseType = gScope.getTypeFromName(it.typeName, it.pos);
		return it.dim >= 1 ? new arrayType(baseType, it.dim) : baseType;
	}

	static public boolean isEqualType(Type lhs, Type rhs) {
		if (lhs == null) return rhs == null || !rhs.is_bool && !rhs.is_int && !rhs.is_void;
		if (rhs == null) return !lhs.is_bool && !lhs.is_int && !lhs.is_void;
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

	static public LLVMSingleValueType calcLLVMSingleValueType(globalScope gScope, Type type) {
		if (type instanceof classType) return new LLVMPointerType(gScope.getLLVMTypeFromType(type));
		else if (type instanceof arrayType) return new LLVMPointerType(calcLLVMSingleValueType(gScope, ((arrayType) type).subType()));
		else if (type.is_bool) return new LLVMIntegerType(8, true);
		else if (type.is_string) return new LLVMPointerType(new LLVMIntegerType(8));
		else if (type.is_void) return null;
		else {
			assert type.is_int;
			return new LLVMIntegerType();
		}
	}
}
