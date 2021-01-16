package Util.error;

import Util.position;

public class semanticError extends error {
	public semanticError(String message, position pos) {super("Semantic Error: " + message, pos);}
}
