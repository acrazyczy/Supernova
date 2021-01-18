package AST;

import Util.position;

import java.util.ArrayList;

public class classDefNode extends ASTNode {
	public ArrayList<varDefStmtNode> varDefs = null;
	public ArrayList<funcDefNode> methodDefs = null;
	public String name;

	public classDefNode(position pos, String name) {
		super(pos);
		this.name = name;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
