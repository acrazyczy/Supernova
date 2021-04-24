package Backend;

import Assembly.Instruction.inst;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;

import java.io.OutputStream;
import java.io.PrintWriter;

public class asmPrinter implements asmVisitor {
	private final asmEntry programAsmEntry;
	private final PrintWriter pWriter;

	public asmPrinter(asmEntry programAsmEntry, OutputStream out) {
		this.programAsmEntry = programAsmEntry;
		this.pWriter = new PrintWriter(out);
	}

	private void printAsmBlock(asmBlock asmBlk) {
		pWriter.println(asmBlk);
		for (inst i = asmBlk.headInst;i != null;i = i.suf) pWriter.println("\t" + i);
	}

	private void printAsmFunction(asmFunction asmFunc) {
		pWriter.println();
		pWriter.println(asmFunc);
		printAsmBlock(asmFunc.initBlock);
		asmFunc.asmBlocks.forEach(this::printAsmBlock);
		if (asmFunc.initBlock != asmFunc.retBlock) printAsmBlock(asmFunc.retBlock);
	}

	@Override
	public boolean run() {
		pWriter.println("\t.text");
		programAsmEntry.asmFunctions.values().stream().filter(asmFunc  -> asmFunc.asmBlocks != null).forEach(this::printAsmFunction);
		pWriter.println();
		pWriter.println("\t.section\t.sdata,\"aw\",@progbits");
		programAsmEntry.gblMapping.values().forEach(pWriter::println);
		pWriter.println();
		pWriter.flush();
		return true;
	}
}
