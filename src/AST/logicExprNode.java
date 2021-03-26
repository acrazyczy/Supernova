package AST;

import Util.position;

public class logicExprNode extends exprStmtNode {
	public enum opType{
		And, Or
	}

	public exprStmtNode lhs, rhs;
	public opType op;

	public logicExprNode(position pos, exprStmtNode lhs, exprStmtNode rhs) {
		super(pos);
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
