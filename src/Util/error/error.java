package Util.error;

import Util.position;

abstract public class error extends RuntimeException {
	private String message;
	private position pos;

	public error(String message, position pos) {
		this.message = message;
		this.pos = pos;
	}

	@Override
	public String toString() {return message + " @ " + pos.toString();}
}
