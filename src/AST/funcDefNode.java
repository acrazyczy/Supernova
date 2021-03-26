package AST;

import LLVMIR.function;
import Util.position;

import java.util.ArrayList;

public class funcDefNode extends ASTNode {
	public typeNode returnType = null;
	public ArrayList<typeNode> paraType = new ArrayList<>();
	public ArrayList<String> paraName = new ArrayList<>();
	public suiteStmtNode funcBody;
	public String name;
	public function func;

	public funcDefNode(position pos, String name, suiteStmtNode funcBody) {
		super(pos);
		this.name = name;
		this.funcBody = funcBody;
	}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
