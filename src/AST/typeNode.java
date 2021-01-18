package AST;

import Util.position;

public class typeNode extends ASTNode {
	public String typeName;
	public int dim;

	public typeNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
