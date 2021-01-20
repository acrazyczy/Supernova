package AST;

import Util.position;

import java.util.ArrayList;

public class arrayLiteralNode extends exprStmtNode {
	public typeNode type;
	public ArrayList<exprStmtNode> dims = new ArrayList<>();
	public int totalDim;

	public arrayLiteralNode(position pos, typeNode type) {
		super(pos);
		this.type = type;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
