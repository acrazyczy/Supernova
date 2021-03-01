package LLVMIR.Instruction;

import LLVMIR.basicBlock;

public class jump extends terminalStmt {
	public basicBlock destination;

	public jump(basicBlock destination) {
		super();
		this.destination = destination;
	}
}
