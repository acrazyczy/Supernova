package AST;

import Util.position;

public class varExprNode extends exprStmtNode {
	public String varName;

	public varExprNode(position pos, String varName) {
		super(pos);
		this.varName = varName;
	}

	@Override
	public boolean isAssignable() {return true;}
}
