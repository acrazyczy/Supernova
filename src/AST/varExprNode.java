package AST;

import Util.position;

public class varExprNode extends exprStmtNode {
	public String varName;

	public varExprNode(position pos) {super(pos);}

	@Override
	public boolean isAssignable() {return true;}
}
