import AST.rootNode;
import Backend.IRBuilder;
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

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
	public static void main(String[] args) throws Exception{

		String name = "test.mx";
		InputStream input = new FileInputStream(name);
//		InputStream input = System.in;

		try {
			rootNode ASTRoot;
			globalScope gScope = new globalScope(null);

			MxStarLexer lexer = new MxStarLexer(CharStreams.fromStream(input));
			lexer.removeErrorListeners();
			lexer.addErrorListener(new MxStarErrorListener());
			MxStarParser parser = new MxStarParser(new CommonTokenStream(lexer));
			parser.removeErrorListeners();
			parser.addErrorListener(new MxStarErrorListener());
			ParseTree parseTreeRoot = parser.program();
			ASTBuilder astBuilder = new ASTBuilder(gScope);
			ASTRoot = (rootNode)astBuilder.visit(parseTreeRoot);
			new symbolCollector(gScope).visit(ASTRoot);
			new classGenerator(gScope).visit(ASTRoot);
			new semanticChecker(gScope).visit(ASTRoot);
			entry programEntry = new entry();
			new IRBuilder(gScope, programEntry).visit(ASTRoot);
		} catch (error er) {
			System.err.println(er.toString());
			throw new RuntimeException();
		}
	}
}
