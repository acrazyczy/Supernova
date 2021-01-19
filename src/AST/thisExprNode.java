package AST;

import Util.position;

public class thisExprNode extends exprStmtNode {
	public thisExprNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
