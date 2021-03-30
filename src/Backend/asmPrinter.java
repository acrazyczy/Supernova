package Backend;

import Assembly.asmEntry;
import Assembly.asmFunction;

import java.io.OutputStream;
import java.io.PrintWriter;

public class asmPrinter implements asmVisitor {
	private final asmEntry programAsmEntry;
	private final OutputStream out;
	private final PrintWriter pWriter;

	public asmPrinter(asmEntry programAsmEntry, OutputStream out) {
		this.programAsmEntry = programAsmEntry;
		this.out = out;
		this.pWriter = new PrintWriter(out);
	}

	private void printAsmFunction(asmFunction asmFunc) {
		pWriter.println(asmFunc);
		asmFunc.asmBlocks.forEach(pWriter::println);
	}

	@Override
	public void run() {
		pWriter.println("\t.text");
		programAsmEntry.asmFunctions.forEach((IRFunc, asmFunc) -> {if (asmFunc.asmBlocks != null) printAsmFunction(asmFunc);});
		pWriter.println("\t.section\t.sdata,\"aw\",@progbits");
		programAsmEntry.gblMapping.values().forEach(pWriter::println);
		pWriter.println();
	}
}
