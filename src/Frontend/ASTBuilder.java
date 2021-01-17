package Frontend;

import AST.ASTNode;
import Parser.MxStarBaseVisitor;
import Util.Scope.globalScope;

public class ASTBuilder extends MxStarBaseVisitor<ASTNode> {
	private globalScope gScope;

	public ASTBuilder(globalScope gScope) {this.gScope = gScope;}
}
