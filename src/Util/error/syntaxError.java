package Util.error;

import Util.position;

public class syntaxError extends error {
	public syntaxError(String message, position pos) {super("Syntax Error: " + message, pos);}
}
