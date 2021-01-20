package AST;

import Util.position;

public class memberAccessExprNode extends exprStmtNode {
	public exprStmtNode lhs, rhs;

	public memberAccessExprNode(position pos, exprStmtNode lhs, exprStmtNode rhs) {
		super(pos);
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public boolean isAssignable() {return rhs.isAssignable();}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
