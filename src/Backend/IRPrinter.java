package Backend;

import LLVMIR.Operand.globalVariable;
import LLVMIR.basicBlock;
import LLVMIR.entry;
import LLVMIR.function;

import java.io.PrintStream;

public class IRPrinter implements pass {
	private final PrintStream out;
	private final entry programEntry;


	public IRPrinter(entry programEntry, PrintStream out) {
		this.programEntry = programEntry;
		this.out = out;
	}

	private void printGlobalVariable(globalVariable gVar) {out.println(gVar + " = global " + gVar.type + ", align 4");}

	private void printFunction(function func) {
		out.println();
		out.print("define " + func);
		out.println(" {");
		func.blocks.forEach(this::printBasicBlock);
		out.println("}");
	}

	private void printBasicBlock(basicBlock block) {
		out.println(block.name + ": ");
		block.stmts.forEach(stmt -> {out.println("\t" + stmt);});
	}

	@Override
	public void run() {
		programEntry.globals.forEach(this::printGlobalVariable);
		programEntry.functions.forEach(this::printFunction);
	}
}
