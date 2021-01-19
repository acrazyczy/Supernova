package Frontend;

import AST.*;
import Util.Scope.Scope;
import Util.Scope.functionScope;
import Util.Scope.globalScope;
import Util.Scope.loopScope;
import Util.error.semanticError;
import Util.typeCalculator;
import Util.Type.Type;
import Util.Type.classType;

public class semanticChecker implements ASTVisitor {
	private Scope currentScope = null;
	private classType currentClass = null;
	private String currentClassName = null;
	private Type currentReturnType = null;
	private globalScope gScope;

	public semanticChecker(globalScope gScope) {this.gScope = gScope;}

	@Override
	public void visit(rootNode it) {
		it.classDefs.forEach(cd -> cd.accept(this));
		it.funcDefs.forEach(fd -> fd.accept(this));
	}

	@Override
	public void visit(typeNode it) {
	}

	@Override
	public void visit(ifStmtNode it) {

	}

	@Override
	public void visit(cmpExprNode it) {

	}

	@Override
	public void visit(forStmtNode it) {

	}

	@Override
	public void visit(funcDefNode it) {
		if (currentScope.containMethod(it.name))
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

	}

	@Override
	public void visit(varExprNode it) {

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

	}

	@Override
	public void visit(breakStmtNode it) {

	}

	@Override
	public void visit(constExprNode it) {

	}

	@Override
	public void visit(logicExprNode it) {

	}

	@Override
	public void visit(suiteStmtNode it) {
		currentScope = currentScope instanceof loopScope ? new loopScope(currentScope) : new Scope(currentScope);
		it.stmts.forEach(stmt -> stmt.accept(this));
		currentScope = currentScope.parentScope();
	}

	@Override
	public void visit(unaryExprNode it) {

	}

	@Override
	public void visit(whileStmtNode it) {

	}

	@Override
	public void visit(assignExprNode it) {

	}

	@Override
	public void visit(binaryExprNode it) {

	}

	@Override
	public void visit(returnStmtNode it) {
		if (currentReturnType == null)
			throw new semanticError("return statement should be inside a function body.", it.pos);
		if (typeCalculator.isEqualType(currentReturnType, new Type("void")) && it.returnExpr != null)
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
			if (currentScope.containVariable(name))
				throw new semanticError("redefinition of variable " + name + ".", it.pos);
			currentScope.defineVariable(name, type, it.pos);
		}
	}

	@Override
	public void visit(arrayLiteralNode it) {

	}

	@Override
	public void visit(continueStmtNode it) {

	}

	@Override
	public void visit(funcCallExprNode it) {

	}

	@Override
	public void visit(memberAccessExprNode it) {

	}

	@Override
	public void visit(subscriptionExprNode it) {

	}

	@Override
	public void visit(classLiteralNode it) {

	}
}
