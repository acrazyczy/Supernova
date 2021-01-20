package AST;

import Util.position;

public class programUnitNode extends ASTNode{
	public funcDefNode funcDef = null;
	public classDefNode classDef = null;
	public varDefStmtNode varDef = null;

	public programUnitNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
