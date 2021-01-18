package AST;

import Util.position;

public class assignExprNode extends exprStmtNode {
	public varExprNode varName;
	public exprStmtNode value;

	public assignExprNode(position pos) {super(pos);}
}
