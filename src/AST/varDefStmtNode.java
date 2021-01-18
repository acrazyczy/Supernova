package AST;

import Util.position;

import java.util.ArrayList;

public class varDefStmtNode extends stmtNode {
	public typeNode varType;
	public ArrayList<String> names = new ArrayList<>();
	public exprStmtNode init = null;

	public varDefStmtNode(position pos) {super(pos);}
}