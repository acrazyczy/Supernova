package Frontend;

import AST.*;
import Util.Scope.globalScope;

public class semanticChecker implements ASTVisitor {
	private globalScope gScope;

	public semanticChecker(globalScope gScope) {this.gScope = gScope;}

	@Override
	public void visit(rootNode it) {

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

	}

	@Override
	public void visit(newExprNode it) {

	}

	@Override
	public void visit(varExprNode it) {

	}

	@Override
	public void visit(classDefNode it) {

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

	}

	@Override
	public void visit(varDefStmtNode it) {

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
}
