package AST;

import Util.position;

public class assignExprNode extends exprStmtNode {
	public varExprNode varName;
	public exprStmtNode value;

	public assignExprNode(position pos, varExprNode varName, exprStmtNode value) {
		super(pos);
		this.varName = varName;
		this.value = value;
	}
}
