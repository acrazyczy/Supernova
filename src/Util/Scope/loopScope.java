//All scopes under loop scope are loop scopes (before reaching a function call)

package Util.Scope;

public class loopScope extends Scope {
	public loopScope(Scope parentScope) {super(parentScope);}
}
