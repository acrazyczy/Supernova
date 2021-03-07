package AST;

import Util.position;

public class continueStmtNode extends stmtNode {
	public stmtNode loopNode;

	public continueStmtNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
