package AST;

import LLVMIR.basicBlock;
import Util.position;

public class whileStmtNode extends stmtNode {
	public exprStmtNode cond;
	public stmtNode stmt;

	public basicBlock destBlock, bodyBlock;

	public whileStmtNode(position pos, exprStmtNode cond, stmtNode stmt) {
		super(pos);
		this.cond = cond;
		this.stmt = stmt;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
