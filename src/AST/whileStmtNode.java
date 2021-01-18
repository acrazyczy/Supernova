package AST;

import Util.position;

public class whileStmtNode extends stmtNode {
	public exprStmtNode cond;
	public suiteStmtNode suite;

	public whileStmtNode(position pos) {super(pos);}
}
