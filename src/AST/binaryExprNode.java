package AST;

import Util.position;

public class binaryExprNode extends exprStmtNode {
	public enum opType {
		Plus, Minus, Mul, Div, Mod, Lsh, Rsh, And, Or, Xor;
	}

	public exprStmtNode lhs, rhs;
	public opType op;

	public binaryExprNode(position pos, exprStmtNode lhs, exprStmtNode rhs) {
		super(pos);
		this.lhs = lhs;
		this.rhs = rhs;
	}
}
