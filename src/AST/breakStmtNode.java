package AST;

import Util.position;

public class breakStmtNode extends stmtNode {
	public stmtNode loopNode;

	public breakStmtNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
