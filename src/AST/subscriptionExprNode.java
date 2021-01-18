package AST;

import Util.position;

public class subscriptionExprNode extends exprStmtNode {
	public exprStmtNode lhs, rhs;

	public subscriptionExprNode(position pos) {super(pos);}

	@Override
	public boolean isAssignable() {return lhs.isAssignable();}
}
