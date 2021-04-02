package Util.error;

import Util.position;

abstract public class error extends RuntimeException {
	private final String message;
	private final position pos;

	public error(String message, position pos) {
		this.message = message;
		this.pos = pos;
	}

	@Override
	public String toString() {return message + " @ " + pos.toString();}
}
