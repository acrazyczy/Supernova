//complete classes with member variables and member methods, check semantic error in class definition
package Frontend;

import AST.*;
import Util.Scope.globalScope;
import Util.Type.Type;
import Util.Type.arrayType;
import Util.Type.classType;
import Util.Type.functionType;
import Util.error.semanticError;

import java.util.ArrayList;

public class classGenerator implements ASTVisitor {
	private globalScope gScope;
	private classType currentClass = null;
	private String currentClassName = null;
	private boolean containConstructor = false;

	public classGenerator(globalScope gScope) {this.gScope = gScope;}

	@Override
	public void visit(rootNode it) {it.classDefs.forEach(cd -> cd.accept(this));}

	@Override public void visit(typeNode it) {}
	@Override public void visit(ifStmtNode it) {}
	@Override public void visit(cmpExprNode it) {}
	@Override public void visit(forStmtNode it) {}

	@Override
	public void visit(funcDefNode it) {
		if (it.returnType == null) {
			if (it.name != currentClassName)
				throw new semanticError("return type of " + it.name + " undeclared.", it.pos);
			if (!it.paraName.isEmpty())
				throw new semanticError("MxStar does not support constructor with parameters.", it.pos);
			functionType defaultConstructor = new functionType(currentClass, new ArrayList<>());
			currentClass.memberMethods.put(currentClassName, defaultConstructor);
		} else {
			if (it.name == currentClassName)
				throw new semanticError("cannot declare return type of constructor.", it.pos);
			Type returnType = calcType(it.returnType);
			ArrayList<Type> paraType = new ArrayList<>();
			it.paraType.forEach(t -> paraType.add(calcType(t)));
			currentClass.memberMethods.put(it.name, new functionType(returnType, paraType));
		}
	}

	@Override
	public void visit(classDefNode it) {
		currentClass = (classType) gScope.getTypeFromName(it.name, it.pos);
		currentClassName = it.name;
		it.varDefs = new ArrayList<>();
		it.varDefs.forEach(vd -> vd.accept(this));
		it.methodDefs = new ArrayList<>();
		it.methodDefs.forEach(md -> md.accept(this));
		if (!containConstructor) {
			functionType defaultConstructor = new functionType(currentClass, new ArrayList<>());
			currentClass.memberMethods.put(it.name, defaultConstructor);
		} else containConstructor = false;
		currentClass = null;
		currentClassName = null;
	}

	@Override
	public void visit(varDefStmtNode it) {
		Type varType = calcType(it.varType);
		for (String varName: it.names) {
			if (currentClass.memberVariables.containsKey(varName))
				throw new semanticError("redefinition of member " + varName + ".", it.pos);
			if (varName == currentClassName)
				throw new semanticError("name of member variables cannot be the same as its class.", it.pos);
			currentClass.memberVariables.put(varName, varType);
		}
		if (it.init != null)
			throw new semanticError("MxStar does not support default init of member variables.", it.init.pos);
	}

	@Override public void visit(arrayLiteralNode it) {}
	@Override public void visit(continueStmtNode it) {}
	@Override public void visit(funcCallExprNode it) {}
	@Override public void visit(memberAccessExprNode it) {}
	@Override public void visit(subscriptionExprNode it) {}
	@Override public void visit(newExprNode it) {}
	@Override public void visit(varExprNode it) {}
	@Override public void visit(thisExprNode it) {}
	@Override public void visit(breakStmtNode it) {}
	@Override public void visit(constExprNode it) {}
	@Override public void visit(logicExprNode it) {}
	@Override public void visit(suiteStmtNode it) {}
	@Override public void visit(unaryExprNode it) {}
	@Override public void visit(whileStmtNode it) {}
	@Override public void visit(assignExprNode it) {}
	@Override public void visit(binaryExprNode it) {}
	@Override public void visit(returnStmtNode it) {}

	private Type calcType(typeNode it) {
		Type baseType = gScope.getTypeFromName(it.typeName, it.pos);
		return it.dim > 1 ? new arrayType(baseType, it.dim) : baseType;
	}
}
