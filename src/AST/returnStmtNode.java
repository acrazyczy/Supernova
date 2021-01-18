package AST;

import Util.position;

public class returnStmtNode extends stmtNode {
	public exprStmtNode returnExpr = null;

	public returnStmtNode(position pos) {super(pos);}
}
