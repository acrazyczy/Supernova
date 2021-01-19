package AST;

import Util.position;

public class unaryExprNode extends exprStmtNode {
	public enum opType{
		PreIncr, PreDecr, SufIncr, SufDecr, Plus, Minus, LogicNot, BitwiseNot
	};

	public exprStmtNode expr;
	public opType op;

	public unaryExprNode(position pos, exprStmtNode expr) {
		super(pos);
		this.expr = expr;
	}
}
