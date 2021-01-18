package AST;

import Util.position;

public class forStmtNode extends stmtNode {
	public exprStmtNode init = null, cond = null, incr = null;

	public forStmtNode(position pos) {super(pos);}
}
