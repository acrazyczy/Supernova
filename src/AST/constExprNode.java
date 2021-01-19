package AST;

import Util.position;

public class constExprNode extends exprStmtNode {
	public String value, type;

	public constExprNode(position pos, String value) {
		super(pos);
		this.value = value;
	}
}
