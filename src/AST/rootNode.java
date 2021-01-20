package AST;

import Util.position;
import java.util.ArrayList;

public class rootNode extends ASTNode {
	public ArrayList<programUnitNode> units = new ArrayList<>();

	public rootNode(position pos) {super(pos);}

	@Override
	public void accept(ASTVisitor visitor) {visitor.visit(this);}
}
