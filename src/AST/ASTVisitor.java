package AST;

public interface ASTVisitor {
	void visit(RootNode it);
	void visit(classDefNode it);
	void visit(funcDefNode it);

	void visit(varDefStmtNode it);
	void visit(returnStmtNode it);
	void visit(exprStmtNode it);
	void visit(ifStmtNode it);

	void visit(assignExprNode it);
	void visit(binaryExprNode it);
	void visit(constExprNode it);
	void visit(cmpExprNode it);
	void visit(varExprNode it);
}
