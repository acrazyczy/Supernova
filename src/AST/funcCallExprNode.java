package AST;

import Util.position;

import java.util.ArrayList;

public class funcCallExprNode extends exprStmtNode {
	public String funcName;
	public ArrayList<exprStmtNode> argList = null;

	public funcCallExprNode(position pos, String funcName) {
		super(pos);
		this.funcName = funcName;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
