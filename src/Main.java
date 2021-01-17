import Frontend.ASTBuilder;
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

		try {
			RootNode ASTRoot;
			globalScope gScope = new globalScope(null);

			MxStarLexer lexer = new MxStarLexer(CharStreams.fromStream(input));
			lexer.removeErrorListeners();
			lexer.addErrorListener(new MxStarErrorListener());
			MxStarParser parser = new MxStarParser(new CommonTokenStream(lexer));
			parser.removeErrorListeners();
			parser.addErrorListener(new MxStarErrorListener());
			ParseTree parseTreeRoot = parser.program();
			ASTBuilder astBuilder = new ASTBuilder(gScope);
			ASTRoot = (RootNode)astBuilder.visit(parseTreeRoot);
			new SymbolCollector(gScope).visit(ASTRoot);
			new SemanticChecker(gScope).visit(ASTRoot);
		} catch (error er) {
			System.err.println(er.toString());
			throw new RuntimeException();
		}
	}
}
