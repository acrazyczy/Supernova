package AST;

import Util.position;

public class cmpExprNode extends exprStmtNode {
	public exprStmtNode lhs, rhs;

	public cmpExprNode(position pos) {super(pos);}
}
