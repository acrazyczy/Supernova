//collect all type names to global scope
package Frontend;

import AST.*;
import Util.Scope.globalScope;
import Util.Type.Type;
import Util.Type.classType;

public class symbolCollector implements ASTVisitor {
	private globalScope gScope;

	public symbolCollector(globalScope gScope) {this.gScope = gScope;}

	@Override
	public void visit(rootNode it) {
		gScope.addType("bool", new Type("bool"), it.pos);
		gScope.addType("int", new Type("int"), it.pos);
		gScope.addType("string", new Type("string"), it.pos);
		gScope.addType("void", new Type("void"), it.pos);
		it.units.forEach(unit -> {if (unit.classDef != null) unit.classDef.accept(this);});
	}

	@Override
	public void visit(classDefNode it) {gScope.addType(it.name, new classType(it.name), it.pos);}

	@Override public void visit(typeNode it) {}
	@Override public void visit(programUnitNode it) {}
	@Override public void visit(ifStmtNode it) {}
	@Override public void visit(cmpExprNode it) {}
	@Override public void visit(forStmtNode it) {}
	@Override public void visit(funcDefNode it) {}
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
	@Override public void visit(varDefStmtNode it) {}
	@Override public void visit(arrayLiteralNode it) {}
	@Override public void visit(continueStmtNode it) {}
	@Override public void visit(funcCallExprNode it) {}
	@Override public void visit(memberAccessExprNode it) {}
	@Override public void visit(subscriptionExprNode it) {}
	@Override public void visit(classLiteralNode it) {}
}