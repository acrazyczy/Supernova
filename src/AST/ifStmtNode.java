package AST;

import Util.position;

public class ifStmtNode extends stmtNode {
	public exprStmtNode cond, trueNode, falseNode = null;

	public ifStmtNode(position pos) {super(pos);}
}
