package AST;

public interface ASTVisitor {
	void visit(RootNode it);
	void visit(classDefNode it);
	void visit(funcDefNode it);

	void visit(stmtNode it);
	void visit(typeNode it);
}
