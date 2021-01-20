package Frontend;

import AST.*;
import Util.Scope.Scope;
import Util.Scope.functionScope;
import Util.Scope.globalScope;
import Util.Scope.loopScope;
import Util.Type.arrayType;
import Util.error.semanticError;
import Util.typeCalculator;
import Util.Type.Type;
import Util.Type.classType;
import Util.Type.functionType;

import java.util.ArrayList;

public class semanticChecker implements ASTVisitor {
	private Scope currentScope = null;
	private classType currentClass = null;
	private String currentClassName = null;
	private Type currentReturnType = null;
	private globalScope gScope;

	public semanticChecker(globalScope gScope) {this.currentScope = this.gScope = gScope;}

	@Override
	public void visit(rootNode it) {
		gScope.defineMethod("print",
			new functionType(
				gScope.getTypeFromName("void", it.pos),
				new ArrayList<>(){{add(gScope.getTypeFromName("string", it.pos));}}
			), it.pos
		);
		gScope.defineMethod("println",
			new functionType(
				gScope.getTypeFromName("void", it.pos),
				new ArrayList(){{add(gScope.getTypeFromName("string", it.pos));}}
			), it.pos
		);
		gScope.defineMethod("printInt",
			new functionType(
				gScope.getTypeFromName("void", it.pos),
				new ArrayList(){{add(gScope.getTypeFromName("int", it.pos));}}
			), it.pos
		);
		gScope.defineMethod("printlnInt",
			new functionType(
				gScope.getTypeFromName("void", it.pos),
				new ArrayList(){{add(gScope.getTypeFromName("int", it.pos));}}
			), it.pos
		);
		gScope.defineMethod("getString",
			new functionType(
				gScope.getTypeFromName("string", it.pos),
				new ArrayList<>()
			), it.pos
		);
		gScope.defineMethod("getInt",
			new functionType(
				gScope.getTypeFromName("int", it.pos),
				new ArrayList<>()
			), it.pos
		);
		gScope.defineMethod("toString",
			new functionType(
				gScope.getTypeFromName("string", it.pos),
				new ArrayList<>(){{add(gScope.getTypeFromName("int", it.pos));}}
			), it.pos
		);
		it.varDefs.forEach(vd -> vd.accept(this));
		it.classDefs.forEach(cd -> cd.accept(this));
		it.funcDefs.forEach(fd -> fd.accept(this));
	}

	@Override public void visit(typeNode it) {}

	@Override
	public void visit(ifStmtNode it) {
		it.cond.accept(this);
		if (!typeCalculator.isEqualType(it.cond.resultType, gScope.getTypeFromName("bool", it.pos)))
			throw new semanticError("type not match. It should be bool.", it.cond.pos);
		currentScope = currentScope instanceof loopScope ? new loopScope(currentScope) : new Scope(currentScope);
		it.trueNode.accept(this);
		currentScope.parentScope();
		if (it.falseNode != null) {
			currentScope = currentScope instanceof loopScope ? new loopScope(currentScope) : new Scope(currentScope);
			it.falseNode.accept(this);
			currentScope.parentScope();
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
		currentScope = new loopScope(currentScope);
		it.stmt.accept(this);
		currentScope = currentScope.parentScope();
	}

	@Override
	public void visit(funcDefNode it) {
		if (currentScope.containMethod(it.name, false))
			throw new semanticError("redefinition of method " + it.name + ".", it.pos);
		currentScope.defineMethod(it.name, typeCalculator.functionTypeGenerator(gScope, it), it.pos);
		currentScope = new functionScope(currentScope);
		for (int i = it.paraName.size() - 1;i >= 0;-- i)
			currentScope.defineVariable(it.paraName.get(i), typeCalculator.calcType(gScope, it.paraType.get(i)), it.pos);
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
	}

	@Override
	public void visit(classDefNode it) {
		currentClass = (classType) gScope.getTypeFromName(it.name, it.pos);
		currentClassName = it.name;
		currentScope = new Scope(currentScope);
		currentClass.memberVariables.forEach((varName, varType) -> currentScope.defineVariable(varName, varType, it.pos));
		currentClass.memberMethods.forEach((methName, methType) -> currentScope.defineMethod(methName, methType, it.pos));
		it.methodDefs.forEach(md -> md.accept(this));
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
		currentScope = currentScope instanceof loopScope ? new loopScope(currentScope) : new Scope(currentScope);
		it.stmts.forEach(stmt -> stmt.accept(this));
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
		currentScope = new loopScope(currentScope);
		it.stmt.accept(this);
		currentScope = currentScope.parentScope();
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
		if (typeCalculator.isEqualType(it.lhs.resultType, gScope.getTypeFromName("bool", it.pos)))
			throw new semanticError("expected int or string type for calculation.", it.pos);
		if (typeCalculator.isEqualType(it.lhs.resultType, gScope.getTypeFromName("string", it.pos)) && it.op != binaryExprNode.opType.Plus)
			throw new semanticError("MxStar does not support arithmetic for string except addition.", it.pos);
		it.resultType = it.lhs.resultType;
	}

	@Override
	public void visit(returnStmtNode it) {
		if (currentReturnType == null)
			throw new semanticError("return statement should be inside of a function body.", it.pos);
		if (typeCalculator.isEqualType(currentReturnType, gScope.getTypeFromName("void", it.pos)) && it.returnExpr != null)
			throw new semanticError("void function should not return any value.", it.pos);
		it.returnExpr.accept(this);
		if (!typeCalculator.isEqualType(currentReturnType, it.returnExpr.resultType))
			throw new semanticError("return type not match.", it.pos);
	}

	@Override
	public void visit(varDefStmtNode it) {
		Type type = typeCalculator.calcType(gScope, it.varType);
		if (it.init != null) {
			it.init.accept(this);
			if (!typeCalculator.isEqualType(type, it.init.resultType))
				throw new semanticError("type not match.", it.init.pos);
		}
		for (String name: it.names) {
			if (currentScope.containVariable(name, false))
				throw new semanticError("redefinition of variable " + name + ".", it.pos);
			currentScope.defineVariable(name, type, it.pos);
		}
	}

	@Override
	public void visit(arrayLiteralNode it) {it.resultType = new arrayType(typeCalculator.calcType(gScope, it.type), it.totalDim);}

	@Override
	public void visit(continueStmtNode it) {
		if (!(currentScope instanceof loopScope))
			throw new semanticError("continue statement should be inside of a loop body.", it.pos);
	}

	@Override
	public void visit(funcCallExprNode it) {
		if (!currentScope.containMethod(it.funcName, true))
			throw new semanticError("method " + it.funcName + " not defined.", it.pos);
		functionType type = (functionType) currentScope.getMethodType(it.funcName, true);
		if (it.argList.size() != type.paraType.size())
			throw new semanticError("number of parameters not match.", it.pos);
		for (int i = it.argList.size() - 1;i >= 0;-- i) {
			it.argList.get(i).accept(this);
			if (!typeCalculator.isEqualType(it.argList.get(i).resultType, type.paraType.get(i)))
				throw new semanticError("parameter type not match.", it.pos);
		}
		it.resultType = type.returnType;
	}

	@Override
	public void visit(memberAccessExprNode it) {
		it.lhs.accept(this);
		currentScope = new Scope(currentScope);
		if (it.lhs.resultType instanceof classType) {
			((classType) it.lhs.resultType).memberVariables.forEach((varName, varType) -> currentScope.defineVariable(varName, varType, it.pos));
			((classType) it.lhs.resultType).memberMethods.forEach((methName, methType) -> currentScope.defineMethod(methName, methType, it.pos));
		} else if (it.lhs.resultType instanceof arrayType) {
			currentScope.defineMethod("size", new functionType(gScope.getTypeFromName("int", it.pos), new ArrayList<>()),it.pos);
		} else if (typeCalculator.isEqualType(it.lhs.resultType, gScope.getTypeFromName("string", it.pos))) {
			currentScope.defineMethod("length",
				new functionType(
					gScope.getTypeFromName("int", it.pos),
					new ArrayList<>()
				), it.pos
			);
			currentScope.defineMethod("substring",
				new functionType(
					gScope.getTypeFromName("string", it.pos),
					new ArrayList(){
						{
							add(gScope.getTypeFromName("int", it.pos));
							add(gScope.getTypeFromName("int", it.pos));
						}
					}
				), it.pos
			);
			currentScope.defineMethod("parseInt",
				new functionType(
					gScope.getTypeFromName("int", it.pos),
					new ArrayList()
				), it.pos
			);
			currentScope.defineMethod("ord",
				new functionType(
					gScope.getTypeFromName("int", it.pos),
					new ArrayList(){{add(gScope.getTypeFromName("int", it.pos));}}
				), it.pos
			);
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
	public void visit(classLiteralNode it) {it.resultType = typeCalculator.calcType(gScope, it.type);}
}
