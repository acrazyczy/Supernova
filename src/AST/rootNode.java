package AST;

import Util.position;
import java.util.ArrayList;

public class rootNode extends ASTNode {
	public ArrayList<funcDefNode> funcDefs = new ArrayList<>();
	public ArrayList<classDefNode> classDefs = new ArrayList<>();
	public ArrayList<varDefStmtNode> varDefs = new ArrayList<>();

	public rootNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
