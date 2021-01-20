package AST;

import Util.position;

public class newExprNode extends exprStmtNode {
	public exprStmtNode expr;

	public newExprNode(position pos, exprStmtNode expr) {
		super(pos);
		this.expr = expr;
	}

	@Override
	public boolean isAssignable() {return true;}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
