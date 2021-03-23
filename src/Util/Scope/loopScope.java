//All scopes under loop scope are loop scopes (before reaching a function call)

package Util.Scope;

import AST.stmtNode;

public class loopScope extends Scope {
	public stmtNode loopNode;

	public loopScope(Scope parentScope) {
		super(parentScope);
		if (parentScope instanceof loopScope) this.loopNode = ((loopScope) parentScope).loopNode;
	}

	public loopScope(Scope parentScope, stmtNode loopNode) {
		super(parentScope);
		this.loopNode = loopNode;
	}
}
