package Assembly;

import java.util.ArrayList;

public class asmFunction {
	private final String name;
	public ArrayList<asmBlock> asmBlocks;

	public asmFunction(String name) {
		this.name = name;
	}

	@Override public String toString() {return this.name + ":\t";}
}
