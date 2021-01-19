package AST;

import Util.position;

public class subscriptionExprNode extends exprStmtNode {
	public exprStmtNode lhs, rhs;

	public subscriptionExprNode(position pos, exprStmtNode lhs, exprStmtNode rhs) {
		super(pos);
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public boolean isAssignable() {return lhs.isAssignable();}
}
