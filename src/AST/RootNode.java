package AST;

import Util.position;
import java.util.ArrayList;

public class RootNode extends ASTNode {
	public ArrayList<funcDefNode> funcDefs = null;
	public ArrayList<classDefNode> classDefs = null;
	public ArrayList<varDefStmtNode> varDefs = null;

	public RootNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
