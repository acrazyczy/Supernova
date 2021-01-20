// Generated from E:/Repository/Supernova/src/Parser\MxStar.g4 by ANTLR 4.9
package Parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxStarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Class=1, New=2, Return=3, Break=4, Continue=5, If=6, Else=7, While=8, 
		For=9, This=10, Bool=11, Int=12, Void=13, String=14, IntegerConstant=15, 
		StringConstant=16, True=17, False=18, Null=19, WhiteSpace=20, BlockComment=21, 
		LineComment=22, LeftParen=23, RightParen=24, LeftBracket=25, RightBracket=26, 
		LeftBrace=27, RightBrace=28, Lt=29, Leq=30, Gt=31, Geq=32, Lsh=33, Rsh=34, 
		Plus=35, PlusPlus=36, Minus=37, MinusMinus=38, Star=39, Div=40, Mod=41, 
		And=42, Or=43, AndAnd=44, OrOr=45, Caret=46, Not=47, Tilde=48, Question=49, 
		Colon=50, Semi=51, Comma=52, Assign=53, Equal=54, NotEqual=55, Dot=56, 
		Identifier=57;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Class", "New", "Return", "Break", "Continue", "If", "Else", "While", 
			"For", "This", "Bool", "Int", "Void", "String", "IntegerConstant", "DecimalInteger", 
			"StringConstant", "Char", "True", "False", "Null", "WhiteSpace", "BlockComment", 
			"LineComment", "LeftParen", "RightParen", "LeftBracket", "RightBracket", 
			"LeftBrace", "RightBrace", "Lt", "Leq", "Gt", "Geq", "Lsh", "Rsh", "Plus", 
			"PlusPlus", "Minus", "MinusMinus", "Star", "Div", "Mod", "And", "Or", 
			"AndAnd", "OrOr", "Caret", "Not", "Tilde", "Question", "Colon", "Semi", 
			"Comma", "Assign", "Equal", "NotEqual", "Dot", "Identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'class'", "'new'", "'return'", "'break'", "'continue'", "'if'", 
			"'else'", "'while'", "'for'", "'this'", "'bool'", "'int'", "'void'", 
			"'string'", null, null, "'true'", "'false'", "'null'", null, null, null, 
			"'('", "')'", "'['", "']'", "'{'", "'}'", "'<'", "'<='", "'>'", "'>='", 
			"'<<'", "'>>'", "'+'", "'++'", "'-'", "'--'", "'*'", "'/'", "'%'", "'&'", 
			"'|'", "'&&'", "'||'", "'^'", "'!'", "'~'", "'?'", "':'", "';'", "','", 
			"'='", "'=='", "'!='", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Class", "New", "Return", "Break", "Continue", "If", "Else", "While", 
			"For", "This", "Bool", "Int", "Void", "String", "IntegerConstant", "StringConstant", 
			"True", "False", "Null", "WhiteSpace", "BlockComment", "LineComment", 
			"LeftParen", "RightParen", "LeftBracket", "RightBracket", "LeftBrace", 
			"RightBrace", "Lt", "Leq", "Gt", "Geq", "Lsh", "Rsh", "Plus", "PlusPlus", 
			"Minus", "MinusMinus", "Star", "Div", "Mod", "And", "Or", "AndAnd", "OrOr", 
			"Caret", "Not", "Tilde", "Question", "Colon", "Semi", "Comma", "Assign", 
			"Equal", "NotEqual", "Dot", "Identifier"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MxStarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MxStar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2;\u0168\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\21\3\21\7\21\u00ca\n\21"+
		"\f\21\16\21\u00cd\13\21\3\21\5\21\u00d0\n\21\3\22\3\22\7\22\u00d4\n\22"+
		"\f\22\16\22\u00d7\13\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5"+
		"\23\u00e2\n\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\26\3\26\3\26\3\26\3\26\3\27\6\27\u00f5\n\27\r\27\16\27\u00f6\3\27\3"+
		"\27\3\30\3\30\3\30\3\30\7\30\u00ff\n\30\f\30\16\30\u0102\13\30\3\30\3"+
		"\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\7\31\u010d\n\31\f\31\16\31\u0110"+
		"\13\31\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37"+
		"\3\37\3 \3 \3!\3!\3!\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3&\3&\3\'\3\'"+
		"\3\'\3(\3(\3)\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3/\3\60\3\60\3"+
		"\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3"+
		"\67\38\38\39\39\39\3:\3:\3:\3;\3;\3<\3<\7<\u0164\n<\f<\16<\u0167\13<\3"+
		"\u0100\2=\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\2#\22%\2\'\23)\24+\25-\26/\27\61\30\63\31\65\32\67\33"+
		"9\34;\35=\36?\37A C!E\"G#I$K%M&O\'Q(S)U*W+Y,[-]._/a\60c\61e\62g\63i\64"+
		"k\65m\66o\67q8s9u:w;\3\2\t\3\2\63;\3\2\62;\6\2\f\f\17\17$$^^\5\2\13\f"+
		"\17\17\"\"\4\2\f\f\17\17\4\2C\\c|\6\2\62;C\\aac|\2\u016f\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2#\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2"+
		"\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2"+
		"\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2"+
		"O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3"+
		"\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2"+
		"\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2"+
		"u\3\2\2\2\2w\3\2\2\2\3y\3\2\2\2\5\177\3\2\2\2\7\u0083\3\2\2\2\t\u008a"+
		"\3\2\2\2\13\u0090\3\2\2\2\r\u0099\3\2\2\2\17\u009c\3\2\2\2\21\u00a1\3"+
		"\2\2\2\23\u00a7\3\2\2\2\25\u00ab\3\2\2\2\27\u00b0\3\2\2\2\31\u00b5\3\2"+
		"\2\2\33\u00b9\3\2\2\2\35\u00be\3\2\2\2\37\u00c5\3\2\2\2!\u00cf\3\2\2\2"+
		"#\u00d1\3\2\2\2%\u00e1\3\2\2\2\'\u00e3\3\2\2\2)\u00e8\3\2\2\2+\u00ee\3"+
		"\2\2\2-\u00f4\3\2\2\2/\u00fa\3\2\2\2\61\u0108\3\2\2\2\63\u0113\3\2\2\2"+
		"\65\u0115\3\2\2\2\67\u0117\3\2\2\29\u0119\3\2\2\2;\u011b\3\2\2\2=\u011d"+
		"\3\2\2\2?\u011f\3\2\2\2A\u0121\3\2\2\2C\u0124\3\2\2\2E\u0126\3\2\2\2G"+
		"\u0129\3\2\2\2I\u012c\3\2\2\2K\u012f\3\2\2\2M\u0131\3\2\2\2O\u0134\3\2"+
		"\2\2Q\u0136\3\2\2\2S\u0139\3\2\2\2U\u013b\3\2\2\2W\u013d\3\2\2\2Y\u013f"+
		"\3\2\2\2[\u0141\3\2\2\2]\u0143\3\2\2\2_\u0146\3\2\2\2a\u0149\3\2\2\2c"+
		"\u014b\3\2\2\2e\u014d\3\2\2\2g\u014f\3\2\2\2i\u0151\3\2\2\2k\u0153\3\2"+
		"\2\2m\u0155\3\2\2\2o\u0157\3\2\2\2q\u0159\3\2\2\2s\u015c\3\2\2\2u\u015f"+
		"\3\2\2\2w\u0161\3\2\2\2yz\7e\2\2z{\7n\2\2{|\7c\2\2|}\7u\2\2}~\7u\2\2~"+
		"\4\3\2\2\2\177\u0080\7p\2\2\u0080\u0081\7g\2\2\u0081\u0082\7y\2\2\u0082"+
		"\6\3\2\2\2\u0083\u0084\7t\2\2\u0084\u0085\7g\2\2\u0085\u0086\7v\2\2\u0086"+
		"\u0087\7w\2\2\u0087\u0088\7t\2\2\u0088\u0089\7p\2\2\u0089\b\3\2\2\2\u008a"+
		"\u008b\7d\2\2\u008b\u008c\7t\2\2\u008c\u008d\7g\2\2\u008d\u008e\7c\2\2"+
		"\u008e\u008f\7m\2\2\u008f\n\3\2\2\2\u0090\u0091\7e\2\2\u0091\u0092\7q"+
		"\2\2\u0092\u0093\7p\2\2\u0093\u0094\7v\2\2\u0094\u0095\7k\2\2\u0095\u0096"+
		"\7p\2\2\u0096\u0097\7w\2\2\u0097\u0098\7g\2\2\u0098\f\3\2\2\2\u0099\u009a"+
		"\7k\2\2\u009a\u009b\7h\2\2\u009b\16\3\2\2\2\u009c\u009d\7g\2\2\u009d\u009e"+
		"\7n\2\2\u009e\u009f\7u\2\2\u009f\u00a0\7g\2\2\u00a0\20\3\2\2\2\u00a1\u00a2"+
		"\7y\2\2\u00a2\u00a3\7j\2\2\u00a3\u00a4\7k\2\2\u00a4\u00a5\7n\2\2\u00a5"+
		"\u00a6\7g\2\2\u00a6\22\3\2\2\2\u00a7\u00a8\7h\2\2\u00a8\u00a9\7q\2\2\u00a9"+
		"\u00aa\7t\2\2\u00aa\24\3\2\2\2\u00ab\u00ac\7v\2\2\u00ac\u00ad\7j\2\2\u00ad"+
		"\u00ae\7k\2\2\u00ae\u00af\7u\2\2\u00af\26\3\2\2\2\u00b0\u00b1\7d\2\2\u00b1"+
		"\u00b2\7q\2\2\u00b2\u00b3\7q\2\2\u00b3\u00b4\7n\2\2\u00b4\30\3\2\2\2\u00b5"+
		"\u00b6\7k\2\2\u00b6\u00b7\7p\2\2\u00b7\u00b8\7v\2\2\u00b8\32\3\2\2\2\u00b9"+
		"\u00ba\7x\2\2\u00ba\u00bb\7q\2\2\u00bb\u00bc\7k\2\2\u00bc\u00bd\7f\2\2"+
		"\u00bd\34\3\2\2\2\u00be\u00bf\7u\2\2\u00bf\u00c0\7v\2\2\u00c0\u00c1\7"+
		"t\2\2\u00c1\u00c2\7k\2\2\u00c2\u00c3\7p\2\2\u00c3\u00c4\7i\2\2\u00c4\36"+
		"\3\2\2\2\u00c5\u00c6\5!\21\2\u00c6 \3\2\2\2\u00c7\u00cb\t\2\2\2\u00c8"+
		"\u00ca\t\3\2\2\u00c9\u00c8\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9\3\2"+
		"\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00d0\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce"+
		"\u00d0\7\62\2\2\u00cf\u00c7\3\2\2\2\u00cf\u00ce\3\2\2\2\u00d0\"\3\2\2"+
		"\2\u00d1\u00d5\7$\2\2\u00d2\u00d4\5%\23\2\u00d3\u00d2\3\2\2\2\u00d4\u00d7"+
		"\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d8\3\2\2\2\u00d7"+
		"\u00d5\3\2\2\2\u00d8\u00d9\7$\2\2\u00d9$\3\2\2\2\u00da\u00e2\n\4\2\2\u00db"+
		"\u00dc\7^\2\2\u00dc\u00e2\7p\2\2\u00dd\u00de\7^\2\2\u00de\u00e2\7^\2\2"+
		"\u00df\u00e0\7^\2\2\u00e0\u00e2\7$\2\2\u00e1\u00da\3\2\2\2\u00e1\u00db"+
		"\3\2\2\2\u00e1\u00dd\3\2\2\2\u00e1\u00df\3\2\2\2\u00e2&\3\2\2\2\u00e3"+
		"\u00e4\7v\2\2\u00e4\u00e5\7t\2\2\u00e5\u00e6\7w\2\2\u00e6\u00e7\7g\2\2"+
		"\u00e7(\3\2\2\2\u00e8\u00e9\7h\2\2\u00e9\u00ea\7c\2\2\u00ea\u00eb\7n\2"+
		"\2\u00eb\u00ec\7u\2\2\u00ec\u00ed\7g\2\2\u00ed*\3\2\2\2\u00ee\u00ef\7"+
		"p\2\2\u00ef\u00f0\7w\2\2\u00f0\u00f1\7n\2\2\u00f1\u00f2\7n\2\2\u00f2,"+
		"\3\2\2\2\u00f3\u00f5\t\5\2\2\u00f4\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6"+
		"\u00f4\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\b\27"+
		"\2\2\u00f9.\3\2\2\2\u00fa\u00fb\7\61\2\2\u00fb\u00fc\7,\2\2\u00fc\u0100"+
		"\3\2\2\2\u00fd\u00ff\13\2\2\2\u00fe\u00fd\3\2\2\2\u00ff\u0102\3\2\2\2"+
		"\u0100\u0101\3\2\2\2\u0100\u00fe\3\2\2\2\u0101\u0103\3\2\2\2\u0102\u0100"+
		"\3\2\2\2\u0103\u0104\7,\2\2\u0104\u0105\7\61\2\2\u0105\u0106\3\2\2\2\u0106"+
		"\u0107\b\30\2\2\u0107\60\3\2\2\2\u0108\u0109\7\61\2\2\u0109\u010a\7\61"+
		"\2\2\u010a\u010e\3\2\2\2\u010b\u010d\n\6\2\2\u010c\u010b\3\2\2\2\u010d"+
		"\u0110\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0111\3\2"+
		"\2\2\u0110\u010e\3\2\2\2\u0111\u0112\b\31\2\2\u0112\62\3\2\2\2\u0113\u0114"+
		"\7*\2\2\u0114\64\3\2\2\2\u0115\u0116\7+\2\2\u0116\66\3\2\2\2\u0117\u0118"+
		"\7]\2\2\u01188\3\2\2\2\u0119\u011a\7_\2\2\u011a:\3\2\2\2\u011b\u011c\7"+
		"}\2\2\u011c<\3\2\2\2\u011d\u011e\7\177\2\2\u011e>\3\2\2\2\u011f\u0120"+
		"\7>\2\2\u0120@\3\2\2\2\u0121\u0122\7>\2\2\u0122\u0123\7?\2\2\u0123B\3"+
		"\2\2\2\u0124\u0125\7@\2\2\u0125D\3\2\2\2\u0126\u0127\7@\2\2\u0127\u0128"+
		"\7?\2\2\u0128F\3\2\2\2\u0129\u012a\7>\2\2\u012a\u012b\7>\2\2\u012bH\3"+
		"\2\2\2\u012c\u012d\7@\2\2\u012d\u012e\7@\2\2\u012eJ\3\2\2\2\u012f\u0130"+
		"\7-\2\2\u0130L\3\2\2\2\u0131\u0132\7-\2\2\u0132\u0133\7-\2\2\u0133N\3"+
		"\2\2\2\u0134\u0135\7/\2\2\u0135P\3\2\2\2\u0136\u0137\7/\2\2\u0137\u0138"+
		"\7/\2\2\u0138R\3\2\2\2\u0139\u013a\7,\2\2\u013aT\3\2\2\2\u013b\u013c\7"+
		"\61\2\2\u013cV\3\2\2\2\u013d\u013e\7\'\2\2\u013eX\3\2\2\2\u013f\u0140"+
		"\7(\2\2\u0140Z\3\2\2\2\u0141\u0142\7~\2\2\u0142\\\3\2\2\2\u0143\u0144"+
		"\7(\2\2\u0144\u0145\7(\2\2\u0145^\3\2\2\2\u0146\u0147\7~\2\2\u0147\u0148"+
		"\7~\2\2\u0148`\3\2\2\2\u0149\u014a\7`\2\2\u014ab\3\2\2\2\u014b\u014c\7"+
		"#\2\2\u014cd\3\2\2\2\u014d\u014e\7\u0080\2\2\u014ef\3\2\2\2\u014f\u0150"+
		"\7A\2\2\u0150h\3\2\2\2\u0151\u0152\7<\2\2\u0152j\3\2\2\2\u0153\u0154\7"+
		"=\2\2\u0154l\3\2\2\2\u0155\u0156\7.\2\2\u0156n\3\2\2\2\u0157\u0158\7?"+
		"\2\2\u0158p\3\2\2\2\u0159\u015a\7?\2\2\u015a\u015b\7?\2\2\u015br\3\2\2"+
		"\2\u015c\u015d\7#\2\2\u015d\u015e\7?\2\2\u015et\3\2\2\2\u015f\u0160\7"+
		"\60\2\2\u0160v\3\2\2\2\u0161\u0165\t\7\2\2\u0162\u0164\t\b\2\2\u0163\u0162"+
		"\3\2\2\2\u0164\u0167\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166"+
		"x\3\2\2\2\u0167\u0165\3\2\2\2\13\2\u00cb\u00cf\u00d5\u00e1\u00f6\u0100"+
		"\u010e\u0165\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}