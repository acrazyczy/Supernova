package AST;

import Util.position;

import java.util.ArrayList;

public class funcDefNode extends ASTNode {
	public typeNode returnType = null;
	public ArrayList<typeNode> paraType = null;
	public ArrayList<String> paraName = null;
	public suiteStmtNode funcBody = null;

	public funcDefNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
