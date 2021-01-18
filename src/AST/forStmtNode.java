package AST;

import Util.position;

public class forStmtNode extends stmtNode {
	public exprStmtNode init = null, cond = null, incr = null;
	public stmtNode stmt;

	public forStmtNode(position pos, stmtNode stmt) {
		super(pos);
		this.stmt = stmt;
	}
}
