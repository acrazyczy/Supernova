package AST;

import Util.position;

public class typeNode extends ASTNode {
	public String typeName;
	public int dim;

	public typeNode(position pos, String typeName, int dim) {
		super(pos);
		this.typeName = typeName;
		this.dim = dim;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
