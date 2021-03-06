package AST;

import LLVMIR.Operand.entity;
import Util.Type.Type;
import Util.position;

abstract public class exprStmtNode extends stmtNode {
	public Type resultType;
	public entity val;

	public exprStmtNode(position pos) {super(pos);}

	public boolean isAssignable() {return false;}
}
