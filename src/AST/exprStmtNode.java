package AST;

import Util.Type.Type;
import Util.position;

abstract public class exprStmtNode extends stmtNode {
	public Type resultType;

	public exprStmtNode(position pos) {super(pos);}

	public boolean isAssignable() {return false;}
}
