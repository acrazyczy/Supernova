package AST;

import LLVMIR.Operand.entity;
import LLVMIR.function;
import Util.position;

import java.util.ArrayList;

public class funcCallExprNode extends exprStmtNode {
	public String funcName;
	public ArrayList<exprStmtNode> argList = null;
	public entity thisEntity = null;
	public function func;

	public funcCallExprNode(position pos, String funcName) {
		super(pos);
		this.funcName = funcName;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
