package Backend;

import LLVMIR.Operand.globalVariable;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMStructureType;
import LLVMIR.basicBlock;
import LLVMIR.IREntry;
import LLVMIR.function;

import java.io.OutputStream;
import java.io.PrintWriter;

public class IRPrinter implements pass {
	private final IREntry programIREntry;
	private final PrintWriter pWriter;

	public IRPrinter(IREntry programIREntry, OutputStream out) {
		this.programIREntry = programIREntry;
		this.pWriter = new PrintWriter(out);
	}

	private void printClass(LLVMStructureType struct) {pWriter.println(struct.classDefString());}

	private void printGlobalVariable(globalVariable gVar) {
		pWriter.print(gVar + " = " + (gVar.isConstant ? "constant" : "global") + " " + ((LLVMPointerType) gVar.type).pointeeType + " ");
		if (gVar.isString) pWriter.println("c\"" + gVar.convert() + "\\00\"");
		else pWriter.println(gVar.val);
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
	public boolean run() {
		programIREntry.classes.forEach(this::printClass);
		pWriter.println();
		programIREntry.globals.forEach(this::printGlobalVariable);
		pWriter.println();
		programIREntry.functions.forEach(this::printFunction);
		pWriter.flush();
		return true;
	}
}
