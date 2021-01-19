package AST;

import Util.position;

import java.util.ArrayList;

public class suiteStmtNode extends stmtNode {
	public ArrayList<stmtNode> stmts = null;
	
	public suiteStmtNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
