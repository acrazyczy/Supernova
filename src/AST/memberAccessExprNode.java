package AST;

import Util.position;

public class memberAccessExprNode extends exprStmtNode {
	public exprStmtNode lhs, rhs;

	public memberAccessExprNode(position pos) {super(pos);}

	@Override
	public boolean isAssignable() {return lhs.isAssignable();}
}
