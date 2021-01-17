package Util.error;

import Util.position;

public class internalError extends error {
	public internalError(String message, position pos) {super("Internal Error: " + message, pos);}
}
