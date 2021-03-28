package Backend;

import Assembly.asmEntry;

public class asmPrinter implements asmVisitor {
	private final asmEntry programAsmEntry;

	public asmPrinter(asmEntry programAsmEntry) {
		this.programAsmEntry = programAsmEntry;
	}

	@Override
	public void run() {
	}
}
