package AST;

import Util.position;

public class stmtNode extends ASTNode {
	public stmtNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
