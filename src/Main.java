import AST.rootNode;
import Assembly.asmEntry;
import Backend.*;
import Frontend.ASTBuilder;
import Frontend.classGenerator;
import Frontend.semanticChecker;
import Frontend.symbolCollector;
import LLVMIR.IREntry;
import Optimization.IR.IROptimizer;
import Optimization.asm.asmOptimizer;
import Util.error.error;
import Util.Scope.globalScope;
import Util.MxStarErrorListener;
import Parser.MxStarLexer;
import Parser.MxStarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;

public class Main {
	private static final String SYNTAX = "-fsyntax-only";
	private static final String OPTIMIZATION = "-O2";
	private static final String LLVM = "-emit-llvm";
	private static final String ASM = "-S";
	private static final String SSA_DESTRUCT = "-fno-ssa"; // dependency: LLVM
	private static final String INPUT_FILE = "-i";

	private InputStream is;
	private OutputStream LLVMOs, asmOs;
	private boolean LLVMGeneratingFlag;
	private boolean assemblyGeneratingFlag;
	private boolean optimizationFlag;
	private boolean ssaDestructFlag;

	public void error(String errorMessage) {
		System.out.println(errorMessage);
		System.exit(-1);
	}

	public Main(String[] args) throws FileNotFoundException {
		is = System.in;
		LLVMOs = new FileOutputStream("output.ll");
		asmOs = new FileOutputStream("output.s");
		LLVMGeneratingFlag = false;
		assemblyGeneratingFlag = true;
		optimizationFlag = false;
		ssaDestructFlag = false;
		int doCompilation = 0;
		for (int i = 0; i < args.length; ++i) {
			if (args[i].charAt(0) == '-') {
				// options
				switch (args[i]) {
					case SYNTAX:
						if (doCompilation == 2) error("duplicated specification");
						else doCompilation = 1;
						break;
					case LLVM:
					case ASM:
						if (doCompilation == 1) error("duplicated specification");
						else doCompilation = 2;
				}
				switch (args[i]) {
					case SYNTAX -> {
						LLVMGeneratingFlag = false;
						assemblyGeneratingFlag = false;
					}
					case LLVM -> LLVMGeneratingFlag = true;
					case ASM -> assemblyGeneratingFlag = true;
					case OPTIMIZATION -> optimizationFlag = true;
					case SSA_DESTRUCT -> ssaDestructFlag = true;
					case INPUT_FILE -> {
						if (i + 1 >= args.length || args[i + 1].charAt(0) == '-') error("no input file specified");
						try {
							is = new FileInputStream(args[i + 1]);
							LLVMOs = LLVMGeneratingFlag ? new FileOutputStream(args[i + 1].substring(0, args[i + 1].indexOf(".")) + ".ll") : null;
							asmOs = assemblyGeneratingFlag ? new FileOutputStream(args[i + 1].substring(0, args[i + 1].indexOf(".")) + ".s") : null;
						} catch (FileNotFoundException e) {
							error("file not found: " + args[i + 1]);
						}
						++ i;
					}
				}
			} else
				error("wrong argument: " + args[i]);
		}
	}

	public static void main(String[] args) throws Exception{new Main(args).run();}

	void run() {
		try {
			rootNode ASTRoot;
			globalScope gScope = new globalScope(null);

			MxStarLexer lexer = new MxStarLexer(CharStreams.fromStream(is));
			lexer.removeErrorListeners();
			lexer.addErrorListener(new MxStarErrorListener());
			MxStarParser parser = new MxStarParser(new CommonTokenStream(lexer));
			parser.removeErrorListeners();
			parser.addErrorListener(new MxStarErrorListener());
			ParseTree parseTreeRoot = parser.program();
			ASTBuilder astBuilder = new ASTBuilder();
			ASTRoot = (rootNode)astBuilder.visit(parseTreeRoot);
			new symbolCollector(gScope).visit(ASTRoot);
			new classGenerator(gScope).visit(ASTRoot);
			IREntry programIREntry = new IREntry();
			new semanticChecker(gScope, programIREntry).visit(ASTRoot);

			if (LLVMGeneratingFlag || assemblyGeneratingFlag) {
				new IRBuilder(gScope, programIREntry).visit(ASTRoot);
				new SSAConstructor(programIREntry).run();

				if (optimizationFlag) new IROptimizer(programIREntry).run(true);

				if (ssaDestructFlag) new SSADestructor(programIREntry).run();
				if (LLVMGeneratingFlag) new IRPrinter(programIREntry, LLVMOs).run();
				if (!ssaDestructFlag) new SSADestructor(programIREntry).run();

				if (optimizationFlag) new IROptimizer(programIREntry).run(false);
				if (assemblyGeneratingFlag) {
					asmEntry programAsmEntry = new asmEntry();
					new instructionSelector(programIREntry, programAsmEntry).run();
					new registerAllocator(programAsmEntry).run();
					if (optimizationFlag) new asmOptimizer(programAsmEntry).run();
					new asmPrinter(programAsmEntry, asmOs).run();
				}
			}
		} catch (error | IOException er) {
			er.printStackTrace();
			throw new RuntimeException(er);
		}
	}
}
