package AST;

import Util.position;

public class assignExprNode extends exprStmtNode {
	public exprStmtNode var, value;

	public assignExprNode(position pos, exprStmtNode var, exprStmtNode value) {
		super(pos);
		this.var = var;
		this.value = value;
	}

	@Override
	public boolean isAssignable() {return true;}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
