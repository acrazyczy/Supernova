package AST;

import Util.position;

public class constExprNode extends exprStmtNode {
	public String value;

	public constExprNode(position pos, String value) {
		super(pos);
		this.value = value;
	}
}
