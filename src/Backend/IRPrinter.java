package Backend;

import LLVMIR.Operand.globalVariable;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMStructureType;
import LLVMIR.basicBlock;
import LLVMIR.entry;
import LLVMIR.function;

import java.io.OutputStream;
import java.io.PrintWriter;

public class IRPrinter implements pass {
	private final OutputStream out;
	private final entry programEntry;
	private final PrintWriter pWriter;

	public IRPrinter(entry programEntry, OutputStream out) {
		this.programEntry = programEntry;
		this.out = out;
		this.pWriter = new PrintWriter(out);
	}

	private void printClass(LLVMStructureType struct) {pWriter.println(struct.classDefString());}

	private void printGlobalVariable(globalVariable gVar) {
		pWriter.print(gVar + " = " + (gVar.isConstant ? "constant" : "global") + " " + ((LLVMPointerType) gVar.type).pointeeType + " ");
		if (gVar.isString) pWriter.println("c\"" + gVar.convert() + "\\00\"");
		else if (gVar.val != null) pWriter.println(gVar.val);
		else if (((LLVMPointerType) gVar.type).pointeeType instanceof LLVMPointerType) pWriter.println("null");
		else pWriter.println("0");
	}

	private void printFunction(function func) {
		if (func.blocks != null) {
			pWriter.print("define " + func);
			pWriter.println(" {");
			func.blocks.forEach(this::printBasicBlock);
			pWriter.println("}");
		} else pWriter.println("declare " + func);
		pWriter.println();
	}

	private void printBasicBlock(basicBlock block) {
		pWriter.println(block.name + ":");
		block.stmts.forEach(stmt -> pWriter.println("\t" + stmt));
	}

	@Override
	public void run() {
		programEntry.classes.forEach(this::printClass);
		pWriter.println();
		programEntry.globals.forEach(this::printGlobalVariable);
		pWriter.println();
		programEntry.functions.forEach(this::printFunction);
		pWriter.flush();
	}
}
