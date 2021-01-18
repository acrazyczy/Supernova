package AST;

import Util.position;

public class logicExprNode extends exprStmtNode {
	public exprStmtNode lhs, rhs;

	public logicExprNode(position pos) {super(pos);}
}
