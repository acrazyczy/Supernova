package AST;

import Util.position;

public class cmpExprNode extends exprStmtNode {
	public enum opType {
		Gt, Lt, Geq, Leq, Neq, Equ
	}

	public exprStmtNode lhs, rhs;
	public opType op;

	public cmpExprNode(position pos, exprStmtNode lhs, exprStmtNode rhs) {
		super(pos);
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
