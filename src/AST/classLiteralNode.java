package AST;

import Util.position;

public class classLiteralNode extends exprStmtNode {
	public typeNode type;

	public classLiteralNode(position pos, typeNode type) {
		super(pos);
		this.type = type;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
