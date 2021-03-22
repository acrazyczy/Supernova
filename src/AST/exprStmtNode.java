package AST;

import LLVMIR.Operand.entity;
import LLVMIR.basicBlock;
import Util.Type.Type;
import Util.position;

abstract public class exprStmtNode extends stmtNode {
	public Type resultType;
	public entity val, ptr = null;

	public basicBlock trueBranch = null, falseBranch = null;

	public exprStmtNode(position pos) {super(pos);}

	public boolean isAssignable() {return false;}
}
