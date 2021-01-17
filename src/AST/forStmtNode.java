package AST;

import Util.position;

public class forStmtNode extends stmtNode {
	public forStmtNode(position pos) {super(pos);}

	public exprStmtNode init, cond, incr;
}
