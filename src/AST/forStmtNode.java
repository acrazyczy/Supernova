package AST;

import LLVMIR.basicBlock;
import Util.position;

public class forStmtNode extends stmtNode {
	public exprStmtNode init = null, cond = null, incr = null;
	public stmtNode stmt;

	public basicBlock destBlock, incrBlock;

	public forStmtNode(position pos, stmtNode stmt) {
		super(pos);
		this.stmt = stmt;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
