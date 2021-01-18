package AST;

import Util.position;

import java.util.ArrayList;

public class funcCallExprNode extends exprStmtNode {
	public String funcName;
	public ArrayList<exprStmtNode> argList = null;

	public funcCallExprNode(position pos) {super(pos);}
}
