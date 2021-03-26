import AST.rootNode;
import Backend.IRBuilder;
import Backend.IRPrinter;
import Frontend.ASTBuilder;
import Frontend.classGenerator;
import Frontend.semanticChecker;
import Frontend.symbolCollector;
import LLVMIR.entry;
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
//	private static final String OPTIMIZATION = "-O2";
	private static final String LLVM = "-emit-llvm";
//	private static final String SSA_DESTRUCT = "-fno-ssa";
	private static final String INPUT_FILE = "-i";
	private static final String OUTPUT_FILE = "-o";
//	private static final String HELP = "--help";
//	private static final String HELP_ABBR = "-h";

	private InputStream is;
	private OutputStream os;
	private boolean LLVMGeneratingFlag;
	private boolean assemblyGeneratingFlag;
//	private boolean optimizationFlag;
//	private boolean ssaDestructFlag;

	public void error(String errorMessage) {
		System.out.println(errorMessage);
		System.exit(-1);
	}

	public Main(String[] args) {
		is = System.in;
		os = System.out;
		LLVMGeneratingFlag = false;
		assemblyGeneratingFlag = true;
//		optimizationFlag = false;
//		ssaDestructFlag = false;
		boolean hasBeenSpecified = false;
		for (int i = 0; i < args.length; ++i) {
			if (args[i].charAt(0) == '-') {
				// options
				switch (args[i]) {
					case SYNTAX:
					case LLVM:
						if (!hasBeenSpecified) hasBeenSpecified = true;
						else error("duplicated specification");
				}
				switch (args[i]) {
					case SYNTAX -> {
						LLVMGeneratingFlag = false;
						assemblyGeneratingFlag = false;
					}
					case LLVM -> {
						LLVMGeneratingFlag = true;
						assemblyGeneratingFlag = false;
					}
//					case OPTIMIZATION -> optimizationFlag = true;
					case INPUT_FILE -> {
						if (i + 1 >= args.length || args[i + 1].charAt(0) == '-') error("no input file specified");
						try {
							is = new FileInputStream(args[i + 1]);
							i++;
						} catch (FileNotFoundException e) {
							error("file not found: " + args[i + 1]);
						}
					}
					case OUTPUT_FILE -> {
						if (i + 1 >= args.length || args[i + 1].charAt(0) == '-') error("no output file specified");
						try {
							os = new FileOutputStream(args[i + 1]);
							i++;
						} catch (FileNotFoundException e) {
							error("cannot write to file: " + args[i + 1]);
						}
					}
					/*case HELP, HELP_ABBR -> {
						System.out.println("See https://github.com/XOR-op/TrickRoom for more information.");
						System.exit(0);
					}*/
					//case SSA_DESTRUCT -> ssaDestructFlag = true;
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
			entry programEntry = new entry();
			new semanticChecker(gScope, programEntry).visit(ASTRoot);
			new IRBuilder(gScope, programEntry).visit(ASTRoot);

			if (LLVMGeneratingFlag) new IRPrinter(programEntry, os).run();
			if (assemblyGeneratingFlag) {

			}
		} catch (error | IOException er) {
			System.err.println(er.toString());
			throw new RuntimeException();
		}
	}
}
