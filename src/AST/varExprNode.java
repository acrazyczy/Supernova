package AST;

import LLVMIR.Operand.entity;
import Util.position;

public class varExprNode extends exprStmtNode {
	public String varName;

	public varExprNode(position pos, String varName) {
		super(pos);
		this.varName = varName;
	}

	@Override
	public boolean isAssignable() {return true;}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
