package AST;

import LLVMIR.Operand.entity;
import Util.position;

import java.util.ArrayList;

public class varDefStmtNode extends stmtNode {
	public typeNode varType;
	public ArrayList<String> names = new ArrayList<>();
	public ArrayList<entity> varEntities = new ArrayList();
	public exprStmtNode init = null;

	public varDefStmtNode(position pos, typeNode varType) {
		super(pos);
		this.varType = varType;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}