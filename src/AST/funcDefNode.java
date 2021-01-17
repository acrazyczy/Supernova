package AST;

import Util.position;

import java.util.ArrayList;

public class funcDefNode extends ASTNode {
	public typeNode returnType;
	public ArrayList<typeNode> paraType = new ArrayList<>();
	public ArrayList<String> paraName = new ArrayList<>();
	public suiteStmtNode funcBody;

	public funcDefNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
