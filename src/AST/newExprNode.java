package AST;

import Util.position;

public class newExprNode extends exprStmtNode {
	public exprStmtNode expr;

	public newExprNode(position pos, exprStmtNode expr) {
		super(pos);
		this.expr = expr;
	}
}
