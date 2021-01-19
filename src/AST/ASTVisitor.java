package AST;

public interface ASTVisitor {
	void visit(arrayLiteralNode it);
	void visit(assignExprNode it);
	void visit(binaryExprNode it);
	void visit(breakStmtNode it);
	void visit(classDefNode it);
	void visit(classLiteralNode it);
	void visit(cmpExprNode it);
	void visit(constExprNode it);
	void visit(continueStmtNode it);
	void visit(forStmtNode it);
	void visit(funcCallExprNode it);
	void visit(funcDefNode it);
	void visit(ifStmtNode it);
	void visit(logicExprNode it);
	void visit(memberAccessExprNode it);
	void visit(newExprNode it);
	void visit(returnStmtNode it);
	void visit(rootNode it);
	void visit(subscriptionExprNode it);
	void visit(suiteStmtNode it);
	void visit(thisExprNode it);
	void visit(typeNode it);
	void visit(unaryExprNode it);
	void visit(varDefStmtNode it);
	void visit(varExprNode it);
	void visit(whileStmtNode it);
}
