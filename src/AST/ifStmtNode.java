package AST;

import Util.position;

public class ifStmtNode extends stmtNode {
	public exprStmtNode cond;
	public stmtNode trueNode, falseNode = null;

	public ifStmtNode(position pos, exprStmtNode cond, stmtNode trueNode) {
		super(pos);
		this.cond = cond;
		this.trueNode = trueNode;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
