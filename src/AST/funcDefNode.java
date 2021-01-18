package AST;

import Util.position;

import java.util.ArrayList;

public class funcDefNode extends ASTNode {
	public typeNode returnType = null;
	public ArrayList<typeNode> paraType = null;
	public ArrayList<String> paraName = null;
	public suiteStmtNode funcBody = null;
	public String name;

	public funcDefNode(position pos, String name, suiteStmtNode funcBody) {
		super(pos);
		this.name = name;
		this.funcBody = funcBody;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
