package Frontend;

import AST.*;
import LLVMIR.Operand.entity;
import LLVMIR.Operand.globalVariable;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMSingleValueType;
import LLVMIR.TypeSystem.LLVMStructureType;
import LLVMIR.entry;
import LLVMIR.function;
import Util.Scope.*;
import Util.Type.arrayType;
import Util.error.semanticError;
import Util.typeCalculator;
import Util.Type.Type;
import Util.Type.classType;
import Util.Type.functionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class semanticChecker implements ASTVisitor {
	private Scope currentScope;
	private classType currentClass = null;
	private String currentClassName = null;
	private Type currentReturnType = null;
	private final entry programEntry;
	private final globalScope gScope;

	public semanticChecker(globalScope gScope, entry programEntry) {
		this.currentScope = this.gScope = gScope;
		this.programEntry = programEntry;
	}

	private void bindBuiltinFunction(LLVMSingleValueType returnType, String functionName, ArrayList<entity> argValues) {
		function func = new function(returnType, functionName, argValues, true);
		gScope.bindMethodToFunction(functionName, func);
		programEntry.functions.add(func);
	}

	@Override
	public void visit(rootNode it) {
		functionType func;
		String funcName;

		// a main function for initialization
		programEntry.functions.add(new function(new LLVMIntegerType(32), "main", new ArrayList<>(), false));

		funcName = "print";
		func = new functionType(
			gScope.getTypeFromName("void", it.pos),
			new ArrayList<>(Collections.singletonList(gScope.getTypeFromName("string", it.pos)))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(null, funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "println";
		func = new functionType(
			gScope.getTypeFromName("void", it.pos),
			new ArrayList<>(Collections.singletonList(gScope.getTypeFromName("string", it.pos)))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(null, funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "printInt";
		func = new functionType(
			gScope.getTypeFromName("void", it.pos),
			new ArrayList<>(Collections.singletonList(gScope.getTypeFromName("int", it.pos)))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(null, funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMIntegerType(32))
			))
		);

		funcName = "printlnInt";
		func = new functionType(
			gScope.getTypeFromName("void", it.pos),
			new ArrayList<>(Collections.singletonList(gScope.getTypeFromName("int", it.pos)))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(null, funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMIntegerType(32))
			))
		);

		funcName = "getString";
		func = new functionType(
			gScope.getTypeFromName("string", it.pos),
			new ArrayList<>()
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMPointerType(new LLVMIntegerType(8)),
			funcName, new ArrayList<>()
		);

		funcName = "getInt";
		func = new functionType(
			gScope.getTypeFromName("int", it.pos),
			new ArrayList<>()
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(32),
			funcName, new ArrayList<>()
		);

		funcName = "toString";
		func = new functionType(
			gScope.getTypeFromName("string", it.pos),
			new ArrayList<>(Collections.singletonList(gScope.getTypeFromName("int", it.pos)))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMPointerType(new LLVMIntegerType(8)), funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMIntegerType(32))
			))
		);

		funcName = "malloc";
		//not a keyword function in MxStar,
		bindBuiltinFunction(new LLVMPointerType(new LLVMIntegerType(8)), funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMIntegerType(64))
			))
		);

		funcName = "builtin.string.add";
		func = new functionType(
			gScope.getTypeFromName("string", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("string", it.pos),
				gScope.getTypeFromName("string", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMPointerType(new LLVMIntegerType(8)),
			funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.isEqual";
		func = new functionType(
			gScope.getTypeFromName("bool", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("string", it.pos),
				gScope.getTypeFromName("string", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(8), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.isNotEqual";
		func = new functionType(
			gScope.getTypeFromName("bool", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("string", it.pos),
				gScope.getTypeFromName("string", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(8), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.isLessThan";
		func = new functionType(
			gScope.getTypeFromName("bool", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("string", it.pos),
				gScope.getTypeFromName("string", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(8), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.isGreaterThan";
		func = new functionType(
			gScope.getTypeFromName("bool", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("string", it.pos),
				gScope.getTypeFromName("string", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(8), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.isLessThanOrEqual";
		func = new functionType(
			gScope.getTypeFromName("bool", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("string", it.pos),
				gScope.getTypeFromName("string", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(8), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.isGreaterThanOrEqual";
		func = new functionType(
			gScope.getTypeFromName("bool", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("string", it.pos),
				gScope.getTypeFromName("string", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(8), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.length";
		func = new functionType(
			gScope.getTypeFromName("int", it.pos),
			new ArrayList<>()
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(32), funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.substring";
		func = new functionType(
			gScope.getTypeFromName("string", it.pos),
			new ArrayList<>(Arrays.asList(
				gScope.getTypeFromName("int", it.pos),
				gScope.getTypeFromName("int", it.pos)
			))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMPointerType(new LLVMIntegerType(8)), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMIntegerType(32)),
				new register(new LLVMIntegerType(32))
			))
		);

		funcName = "builtin.string.parseInt";
		func = new functionType(
			gScope.getTypeFromName("int", it.pos),
			new ArrayList<>()
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(32), funcName,
			new ArrayList<>(Collections.singletonList(
				new register(new LLVMPointerType(new LLVMIntegerType(8)))
			))
		);

		funcName = "builtin.string.ord";
		func = new functionType(
			gScope.getTypeFromName("int", it.pos),
			new ArrayList<>(Collections.singletonList(gScope.getTypeFromName("int", it.pos)))
		);
		gScope.defineMethod(funcName, func, it.pos);
		bindBuiltinFunction(new LLVMIntegerType(32), funcName,
			new ArrayList<>(Arrays.asList(
				new register(new LLVMPointerType(new LLVMIntegerType(8))),
				new register(new LLVMIntegerType(32))
			))
		);

		funcName = "builtin.array.size";
		func = new functionType(
			gScope.getTypeFromName("int", it.pos),
			new ArrayList<>()
		);
		gScope.defineMethod(funcName, func, it.pos);
		// size method of array will be implemented in IR builder

		registerAllMethods(it);

		if (!gScope.containMethod("_g.main", true))
			throw new semanticError("no main function.", it.pos);
		it.units.forEach(unit -> unit.accept(this));
	}

	@Override public void visit(typeNode it) {}

	@Override
	public void visit(programUnitNode it) {
		if (it.funcDef != null) it.funcDef.accept(this);
		if (it.classDef != null) it.classDef.accept(this);
		if (it.varDef != null) it.varDef.accept(this);
	}

	@Override
	public void visit(ifStmtNode it) {
		it.cond.accept(this);
		if (!typeCalculator.isEqualType(it.cond.resultType, gScope.getTypeFromName("bool", it.pos)))
			throw new semanticError("type not match. It should be bool.", it.cond.pos);
		if (it.trueNode != null) {
			currentScope = (currentScope instanceof loopScope) ? new loopScope(currentScope) : new Scope(currentScope);
			it.trueNode.accept(this);
			currentScope = currentScope.parentScope();
		}
		if (it.falseNode != null) {
			currentScope = (currentScope instanceof loopScope) ? new loopScope(currentScope) : new Scope(currentScope);
			it.falseNode.accept(this);
			currentScope = currentScope.parentScope();
		}
	}

	@Override
	public void visit(cmpExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		if (!typeCalculator.isEqualType(it.lhs.resultType, it.rhs.resultType))
			throw new semanticError("type not match.", it.pos);
		if (typeCalculator.isEqualType(it.lhs.resultType, gScope.getTypeFromName("bool", it.pos))
			&& it.op != cmpExprNode.opType.Equ && it.op != cmpExprNode.opType.Neq)
			throw new semanticError("expected int or string type for comparison.", it.pos);
		it.resultType = gScope.getTypeFromName("bool", it.pos);
	}

	@Override
	public void visit(forStmtNode it) {
		if (it.init != null) it.init.accept(this);
		if (it.cond != null) {
			it.cond.accept(this);
			if (!typeCalculator.isEqualType(it.cond.resultType, gScope.getTypeFromName("bool", it.pos)))
				throw new semanticError("type not match. It should be bool.", it.cond.pos);
		}
		if (it.incr != null) it.incr.accept(this);
		if (it.stmt != null) {
			currentScope = new loopScope(currentScope, it);
			it.stmt.accept(this);
			currentScope = currentScope.parentScope();
		}
	}

	@Override
	public void visit(funcDefNode it) {
		currentScope = new functionScope(currentScope);
		for (int i = 0;i < it.paraName.size();++ i) {
			Type type = typeCalculator.calcType(gScope, it.paraType.get(i));
			currentScope.defineVariable(it.paraName.get(i), type, it.pos);
			currentScope.bindVariableToEntity(it.paraName.get(i), it.func.argValues.get(i + (currentClass != null ? 1 : 0)));
		}
		currentReturnType = typeCalculator.calcType(gScope, it.returnType);
		it.funcBody.accept(this);
		currentReturnType = null;
		currentScope = currentScope.parentScope();
	}

	@Override
	public void visit(newExprNode it) {
		it.expr.accept(this);
		it.resultType = it.expr.resultType;
	}

	@Override
	public void visit(varExprNode it) {
		if (!currentScope.containVariable(it.varName, true))
			throw new semanticError("variable " + it.varName + " not defined.", it.pos);
		it.resultType = currentScope.getVariableType(it.varName, true);
		it.varEntity = currentScope.getVariableEntity(it.varName, true);
	}

	@Override
	public void visit(classDefNode it) {
		currentClass = (classType) gScope.getTypeFromName(it.name, it.pos);
		programEntry.classes.add((LLVMStructureType) gScope.getLLVMTypeFromType(currentClass));
		currentClassName = it.name;
		currentScope = new aggregateScope(currentScope, it.name);
		it.units.forEach(unit -> {if (unit.funcDef != null) unit.funcDef.accept(this);});
		currentScope = currentScope.parentScope();
		currentClassName = null;
		currentClass = null;
	}

	@Override
	public void visit(thisExprNode it) {
		if (currentClass == null)
			throw new semanticError("this pointer should be used inside of a class body.", it.pos);
		it.resultType = currentClass;
	}

	@Override
	public void visit(breakStmtNode it) {
		if (!(currentScope instanceof loopScope))
			throw new semanticError("break statement should be inside of a loop body.", it.pos);
		it.loopNode = ((loopScope) currentScope).loopNode;
	}

	@Override
	public void visit(constExprNode it) {it.resultType = gScope.getTypeFromName(it.type, it.pos);}

	@Override
	public void visit(logicExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		if (!typeCalculator.isEqualType(it.lhs.resultType, it.rhs.resultType))
			throw new semanticError("type not match.", it.pos);
		if (!typeCalculator.isEqualType(it.lhs.resultType, gScope.getTypeFromName("bool", it.pos)))
			throw new semanticError("expected bool type for binary logic operator.", it.pos);
		it.resultType = gScope.getTypeFromName("bool", it.pos);
	}

	@Override
	public void visit(suiteStmtNode it) {
		currentScope = (currentScope instanceof loopScope) ? new loopScope(currentScope) : new Scope(currentScope);
		it.stmts.forEach(stmt -> {if (stmt != null) stmt.accept(this);});
		currentScope = currentScope.parentScope();
	}

	@Override
	public void visit(unaryExprNode it) {
		it.expr.accept(this);
		if (it.op == unaryExprNode.opType.LogicNot)
		{
			if (!typeCalculator.isEqualType(it.expr.resultType, gScope.getTypeFromName("bool", it.pos)))
				throw new semanticError("expected bool type for unary logic not operator.", it.pos);
			it.resultType = gScope.getTypeFromName("bool", it.pos);
		} else {
			if (!typeCalculator.isEqualType(it.expr.resultType, gScope.getTypeFromName("int", it.pos)))
				throw new semanticError("expected int type for unary operator " + it.op.name() + ".", it.pos);
			if (it.op != unaryExprNode.opType.Plus &&
				it.op != unaryExprNode.opType.Minus &&
				it.op != unaryExprNode.opType.BitwiseNot &&
				!it.expr.isAssignable()
			) throw new semanticError("expected lvalue for unary operator " + it.op.name() + ".", it.pos);
			it.resultType = gScope.getTypeFromName("int", it.pos);
		}
	}

	@Override
	public void visit(whileStmtNode it) {
		it.cond.accept(this);
		if (!typeCalculator.isEqualType(it.cond.resultType, gScope.getTypeFromName("bool", it.pos)))
			throw new semanticError("type not match. It should be bool.", it.cond.pos);
		if (it.stmt != null) {
			currentScope = new loopScope(currentScope, it);
			it.stmt.accept(this);
			currentScope = currentScope.parentScope();
		}
	}

	@Override
	public void visit(assignExprNode it) {
		it.var.accept(this);
		it.value.accept(this);
		if (!it.var.isAssignable())
			throw new semanticError("assignment to rvalue is invalid.", it.pos);
		if (!typeCalculator.isEqualType(it.var.resultType, it.value.resultType))
			throw new semanticError("type not match.", it.pos);
		it.resultType = it.var.resultType;
	}

	@Override
	public void visit(binaryExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		if (!typeCalculator.isEqualType(it.lhs.resultType, it.rhs.resultType))
			throw new semanticError("type not match.", it.pos);
		if (!it.lhs.resultType.is_int && !it.lhs.resultType.is_string)
			throw new semanticError("expected int or string type for calculation.", it.pos);
		if (typeCalculator.isEqualType(it.lhs.resultType, gScope.getTypeFromName("string", it.pos)) && it.op != binaryExprNode.opType.Plus)
			throw new semanticError("MxStar does not support arithmetic for string except addition.", it.pos);
		it.resultType = it.lhs.resultType;
	}

	@Override
	public void visit(returnStmtNode it) {
		if (currentReturnType == null)
			throw new semanticError("return statement should be inside of a function body.", it.pos);
		if (typeCalculator.isEqualType(currentReturnType, gScope.getTypeFromName("void", it.pos))) {
			if (it.returnExpr != null) throw new semanticError("void function should not return any value.", it.pos);
		} else if (it.returnExpr == null) throw new semanticError("return without return value.", it.pos);
		else {
			it.returnExpr.accept(this);
			if (!typeCalculator.isEqualType(currentReturnType, it.returnExpr.resultType))
				throw new semanticError("return type not match.", it.pos);
		}
	}

	@Override
	public void visit(varDefStmtNode it) {
		Type type = typeCalculator.calcType(gScope, it.varType);
		if (type.is_void || (type instanceof arrayType) && ((arrayType) type).elementType.is_void)
			throw new semanticError("variable type cannot be void.", it.varType.pos);
		if (it.init != null) {
			it.init.accept(this);
			if (!typeCalculator.isEqualType(type, it.init.resultType))
				throw new semanticError("type not match.", it.init.pos);
		}
		for (String name: it.names) {
			if (currentScope.containVariable(name, false))
				throw new semanticError("redefinition of variable " + name + ".", it.pos);
			currentScope.defineVariable(name, type, it.pos);
			assert currentClass == null;
		}
		if (currentScope instanceof globalScope) for (String name: it.names) {
			globalVariable gVar = new globalVariable(typeCalculator.calcLLVMSingleValueType(gScope, type), "_g." + name, false, false);
			currentScope.bindVariableToEntity(name, gVar);
			it.varEntities.add(gVar);
			programEntry.globals.add(gVar);
		}
		else for (String name: it.names) {
			entity varEntity = new register(typeCalculator.calcLLVMSingleValueType(gScope, type));
			currentScope.bindVariableToEntity(name, varEntity);
			it.varEntities.add(varEntity);
		}
	}

	@Override
	public void visit(arrayLiteralNode it) {
		for (exprStmtNode stmt: it.dims) {
			stmt.accept(this);
			if (!typeCalculator.isEqualType(stmt.resultType, gScope.getTypeFromName("int", it.pos)))
				throw new semanticError("expected int type for array size.", it.pos);
		}
		Type baseType = typeCalculator.calcType(gScope, it.type);
		if (typeCalculator.isEqualType(baseType, gScope.getTypeFromName("void", it.pos)))
			throw new semanticError("void type cannot be used as base type of an array.", it.pos);
		it.resultType = new arrayType(baseType, it.totalDim);
	}

	@Override
	public void visit(continueStmtNode it) {
		if (!(currentScope instanceof loopScope))
			throw new semanticError("continue statement should be inside of a loop body.", it.pos);
		it.loopNode = ((loopScope) currentScope).loopNode;
	}

	@Override
	public void visit(funcCallExprNode it) {
		boolean isGlobalFunc = false;
		if (!currentScope.containMethod(it.funcName, true)) {
			isGlobalFunc = true;
			if (!currentScope.containMethod("_g." + it.funcName, true))
				throw new semanticError("method " + it.funcName + " not defined.", it.pos);
		}
		functionType type;
		if (isGlobalFunc) {
			type = (functionType) currentScope.getMethodType("_g." + it.funcName, true);
			it.func = currentScope.getMethodFunction("_g." + it.funcName, true);
		} else {
			type = (functionType) currentScope.getMethodType(it.funcName, true);
			it.func = currentScope.getMethodFunction(it.funcName, true);
		}
		if (it.argList.size() != type.paraType.size())
			throw new semanticError("number of parameters not match.", it.pos);
		Scope tmpScope = currentScope;
		if (currentScope instanceof aggregateScope) currentScope = currentScope.parentScope();
		for (int i = it.argList.size() - 1;i >= 0;-- i) {
			it.argList.get(i).accept(this);
			if (!typeCalculator.isEqualType(it.argList.get(i).resultType, type.paraType.get(i)))
				throw new semanticError("parameter type not match.", it.pos);
		}
		it.resultType = type.returnType;
		currentScope = tmpScope;
	}

	@Override
	public void visit(memberAccessExprNode it) {
		it.lhs.accept(this);
		if (it.lhs.resultType instanceof classType) currentScope = new aggregateScope(currentScope, ((classType) it.lhs.resultType).className);
		else if (it.lhs.resultType instanceof arrayType)
			currentScope = new aggregateScope(currentScope, "builtin.array");
		else {
			assert typeCalculator.isEqualType(it.lhs.resultType, gScope.getTypeFromName("string", it.pos));
			currentScope = new aggregateScope(currentScope, "builtin.string");
		}
		it.rhs.accept(this);
		currentScope = currentScope.parentScope();
		it.resultType = it.rhs.resultType;
	}

	@Override
	public void visit(subscriptionExprNode it) {
		it.lhs.accept(this);
		if (!(it.lhs.resultType instanceof arrayType))
			throw new semanticError("expected array type for subscription.", it.lhs.pos);
		it.rhs.accept(this);
		if (!typeCalculator.isEqualType(it.rhs.resultType, gScope.getTypeFromName("int", it.pos)))
			throw new semanticError("expected int type for subscription.", it.rhs.pos);
		it.resultType = ((arrayType) it.lhs.resultType).subType();
	}

	@Override
	public void visit(classLiteralNode it) {
		it.resultType = typeCalculator.calcType(gScope, it.type);
		if (it.resultType.is_bool || it.resultType.is_int || it.resultType.is_void)
			throw new semanticError("type cannot be used in new expression.", it.pos);
	}

	private void registerAllMethods(rootNode it) {
		it.units.forEach(unit -> {if (unit.funcDef != null) bindFunction(unit.funcDef);});
		it.units.forEach(unit -> {
			if (unit.classDef != null) {
				currentClass = (classType) gScope.getTypeFromName(unit.classDef.name, unit.classDef.pos);
				currentClassName = unit.classDef.name;
				unit.classDef.units.forEach(classUnit -> {
					if (classUnit.funcDef != null) bindFunction(classUnit.funcDef);
				});
				currentClassName = null;
				currentClass = null;
			}
		});
	}

	private void bindFunction(funcDefNode it) {
		ArrayList<entity> argValues = new ArrayList<>();
		String funcName = it.name;
		if (currentClass != null) {
			funcName = currentClassName + "." + funcName;
			argValues.add(new register(typeCalculator.calcLLVMSingleValueType(gScope, currentClass)));
		} else funcName = "_g." + funcName;
		gScope.registerMethod(it, funcName, currentClass != null);
		for (int i = 0;i < it.paraName.size();++ i) {
			Type type = typeCalculator.calcType(gScope, it.paraType.get(i));
			register argEntity = new register(typeCalculator.calcLLVMSingleValueType(gScope, type), it.paraName.get(i));
			argValues.add(argEntity);
		}
		function func = new function(
			typeCalculator.calcLLVMSingleValueType(gScope, typeCalculator.calcType(gScope, it.returnType)),
			funcName, argValues, false);
		gScope.bindMethodToFunction(it.name, func);
		programEntry.functions.add(func);
		it.func = func;
	}
}
