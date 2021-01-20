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
		T__0=1, Class=2, New=3, Return=4, Break=5, Continue=6, If=7, Else=8, While=9, 
		For=10, This=11, Bool=12, Int=13, Void=14, String=15, IntegerConstant=16, 
		StringConstant=17, True=18, False=19, Null=20, WhiteSpace=21, NewLine=22, 
		BlockComment=23, LineComment=24, LeftParen=25, RightParen=26, LeftBracket=27, 
		RightBracket=28, LeftBrace=29, RightBrace=30, Lt=31, Leq=32, Gt=33, Geq=34, 
		Lsh=35, Rsh=36, Plus=37, PlusPlus=38, Minus=39, MinusMinus=40, Star=41, 
		Div=42, Mod=43, And=44, Or=45, AndAnd=46, OrOr=47, Caret=48, Not=49, Tilde=50, 
		Question=51, Colon=52, Semi=53, Comma=54, Assign=55, Equal=56, NotEqual=57, 
		Dot=58, Identifier=59;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "Class", "New", "Return", "Break", "Continue", "If", "Else", 
			"While", "For", "This", "Bool", "Int", "Void", "String", "IntegerConstant", 
			"DecimalInteger", "StringConstant", "Char", "True", "False", "Null", 
			"WhiteSpace", "NewLine", "BlockComment", "LineComment", "LeftParen", 
			"RightParen", "LeftBracket", "RightBracket", "LeftBrace", "RightBrace", 
			"Lt", "Leq", "Gt", "Geq", "Lsh", "Rsh", "Plus", "PlusPlus", "Minus", 
			"MinusMinus", "Star", "Div", "Mod", "And", "Or", "AndAnd", "OrOr", "Caret", 
			"Not", "Tilde", "Question", "Colon", "Semi", "Comma", "Assign", "Equal", 
			"NotEqual", "Dot", "Identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'};'", "'class'", "'new'", "'return'", "'break'", "'continue'", 
			"'if'", "'else'", "'while'", "'for'", "'this'", "'bool'", "'int'", "'void'", 
			"'string'", null, null, "'true'", "'false'", "'null'", null, null, null, 
			null, "'('", "')'", "'['", "']'", "'{'", "'}'", "'<'", "'<='", "'>'", 
			"'>='", "'<<'", "'>>'", "'+'", "'++'", "'-'", "'--'", "'*'", "'/'", "'%'", 
			"'&'", "'|'", "'&&'", "'||'", "'^'", "'!'", "'~'", "'?'", "':'", "';'", 
			"','", "'='", "'=='", "'!='", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "Class", "New", "Return", "Break", "Continue", "If", "Else", 
			"While", "For", "This", "Bool", "Int", "Void", "String", "IntegerConstant", 
			"StringConstant", "True", "False", "Null", "WhiteSpace", "NewLine", "BlockComment", 
			"LineComment", "LeftParen", "RightParen", "LeftBracket", "RightBracket", 
			"LeftBrace", "RightBrace", "Lt", "Leq", "Gt", "Geq", "Lsh", "Rsh", "Plus", 
			"PlusPlus", "Minus", "MinusMinus", "Star", "Div", "Mod", "And", "Or", 
			"AndAnd", "OrOr", "Caret", "Not", "Tilde", "Question", "Colon", "Semi", 
			"Comma", "Assign", "Equal", "NotEqual", "Dot", "Identifier"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2=\u0178\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3"+
		"\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3"+
		"\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3"+
		"\21\3\22\3\22\7\22\u00d1\n\22\f\22\16\22\u00d4\13\22\3\22\5\22\u00d7\n"+
		"\22\3\23\3\23\7\23\u00db\n\23\f\23\16\23\u00de\13\23\3\23\3\23\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\24\5\24\u00e9\n\24\3\25\3\25\3\25\3\25\3\25"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\6\30\u00fc"+
		"\n\30\r\30\16\30\u00fd\3\30\3\30\3\31\3\31\5\31\u0104\n\31\3\31\5\31\u0107"+
		"\n\31\3\31\3\31\3\32\3\32\3\32\3\32\7\32\u010f\n\32\f\32\16\32\u0112\13"+
		"\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\7\33\u011d\n\33\f\33"+
		"\16\33\u0120\13\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3"+
		" \3 \3!\3!\3\"\3\"\3#\3#\3#\3$\3$\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'\3(\3("+
		"\3)\3)\3)\3*\3*\3+\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3"+
		"\61\3\62\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\3"+
		"8\38\39\39\3:\3:\3;\3;\3;\3<\3<\3<\3=\3=\3>\3>\7>\u0174\n>\f>\16>\u0177"+
		"\13>\3\u0110\2?\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\2%\23\'\2)\24+\25-\26/\27\61\30\63\31\65\32"+
		"\67\339\34;\35=\36?\37A C!E\"G#I$K%M&O\'Q(S)U*W+Y,[-]._/a\60c\61e\62g"+
		"\63i\64k\65m\66o\67q8s9u:w;y<{=\3\2\t\3\2\63;\3\2\62;\6\2\f\f\17\17$$"+
		"^^\4\2\13\13\"\"\4\2\f\f\17\17\4\2C\\c|\6\2\62;C\\aac|\2\u0181\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2%\3\2\2\2\2)\3"+
		"\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65"+
		"\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3"+
		"\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2"+
		"\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2"+
		"[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3"+
		"\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2"+
		"\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\3}\3\2\2\2\5\u0080\3\2"+
		"\2\2\7\u0086\3\2\2\2\t\u008a\3\2\2\2\13\u0091\3\2\2\2\r\u0097\3\2\2\2"+
		"\17\u00a0\3\2\2\2\21\u00a3\3\2\2\2\23\u00a8\3\2\2\2\25\u00ae\3\2\2\2\27"+
		"\u00b2\3\2\2\2\31\u00b7\3\2\2\2\33\u00bc\3\2\2\2\35\u00c0\3\2\2\2\37\u00c5"+
		"\3\2\2\2!\u00cc\3\2\2\2#\u00d6\3\2\2\2%\u00d8\3\2\2\2\'\u00e8\3\2\2\2"+
		")\u00ea\3\2\2\2+\u00ef\3\2\2\2-\u00f5\3\2\2\2/\u00fb\3\2\2\2\61\u0106"+
		"\3\2\2\2\63\u010a\3\2\2\2\65\u0118\3\2\2\2\67\u0123\3\2\2\29\u0125\3\2"+
		"\2\2;\u0127\3\2\2\2=\u0129\3\2\2\2?\u012b\3\2\2\2A\u012d\3\2\2\2C\u012f"+
		"\3\2\2\2E\u0131\3\2\2\2G\u0134\3\2\2\2I\u0136\3\2\2\2K\u0139\3\2\2\2M"+
		"\u013c\3\2\2\2O\u013f\3\2\2\2Q\u0141\3\2\2\2S\u0144\3\2\2\2U\u0146\3\2"+
		"\2\2W\u0149\3\2\2\2Y\u014b\3\2\2\2[\u014d\3\2\2\2]\u014f\3\2\2\2_\u0151"+
		"\3\2\2\2a\u0153\3\2\2\2c\u0156\3\2\2\2e\u0159\3\2\2\2g\u015b\3\2\2\2i"+
		"\u015d\3\2\2\2k\u015f\3\2\2\2m\u0161\3\2\2\2o\u0163\3\2\2\2q\u0165\3\2"+
		"\2\2s\u0167\3\2\2\2u\u0169\3\2\2\2w\u016c\3\2\2\2y\u016f\3\2\2\2{\u0171"+
		"\3\2\2\2}~\7\177\2\2~\177\7=\2\2\177\4\3\2\2\2\u0080\u0081\7e\2\2\u0081"+
		"\u0082\7n\2\2\u0082\u0083\7c\2\2\u0083\u0084\7u\2\2\u0084\u0085\7u\2\2"+
		"\u0085\6\3\2\2\2\u0086\u0087\7p\2\2\u0087\u0088\7g\2\2\u0088\u0089\7y"+
		"\2\2\u0089\b\3\2\2\2\u008a\u008b\7t\2\2\u008b\u008c\7g\2\2\u008c\u008d"+
		"\7v\2\2\u008d\u008e\7w\2\2\u008e\u008f\7t\2\2\u008f\u0090\7p\2\2\u0090"+
		"\n\3\2\2\2\u0091\u0092\7d\2\2\u0092\u0093\7t\2\2\u0093\u0094\7g\2\2\u0094"+
		"\u0095\7c\2\2\u0095\u0096\7m\2\2\u0096\f\3\2\2\2\u0097\u0098\7e\2\2\u0098"+
		"\u0099\7q\2\2\u0099\u009a\7p\2\2\u009a\u009b\7v\2\2\u009b\u009c\7k\2\2"+
		"\u009c\u009d\7p\2\2\u009d\u009e\7w\2\2\u009e\u009f\7g\2\2\u009f\16\3\2"+
		"\2\2\u00a0\u00a1\7k\2\2\u00a1\u00a2\7h\2\2\u00a2\20\3\2\2\2\u00a3\u00a4"+
		"\7g\2\2\u00a4\u00a5\7n\2\2\u00a5\u00a6\7u\2\2\u00a6\u00a7\7g\2\2\u00a7"+
		"\22\3\2\2\2\u00a8\u00a9\7y\2\2\u00a9\u00aa\7j\2\2\u00aa\u00ab\7k\2\2\u00ab"+
		"\u00ac\7n\2\2\u00ac\u00ad\7g\2\2\u00ad\24\3\2\2\2\u00ae\u00af\7h\2\2\u00af"+
		"\u00b0\7q\2\2\u00b0\u00b1\7t\2\2\u00b1\26\3\2\2\2\u00b2\u00b3\7v\2\2\u00b3"+
		"\u00b4\7j\2\2\u00b4\u00b5\7k\2\2\u00b5\u00b6\7u\2\2\u00b6\30\3\2\2\2\u00b7"+
		"\u00b8\7d\2\2\u00b8\u00b9\7q\2\2\u00b9\u00ba\7q\2\2\u00ba\u00bb\7n\2\2"+
		"\u00bb\32\3\2\2\2\u00bc\u00bd\7k\2\2\u00bd\u00be\7p\2\2\u00be\u00bf\7"+
		"v\2\2\u00bf\34\3\2\2\2\u00c0\u00c1\7x\2\2\u00c1\u00c2\7q\2\2\u00c2\u00c3"+
		"\7k\2\2\u00c3\u00c4\7f\2\2\u00c4\36\3\2\2\2\u00c5\u00c6\7u\2\2\u00c6\u00c7"+
		"\7v\2\2\u00c7\u00c8\7t\2\2\u00c8\u00c9\7k\2\2\u00c9\u00ca\7p\2\2\u00ca"+
		"\u00cb\7i\2\2\u00cb \3\2\2\2\u00cc\u00cd\5#\22\2\u00cd\"\3\2\2\2\u00ce"+
		"\u00d2\t\2\2\2\u00cf\u00d1\t\3\2\2\u00d0\u00cf\3\2\2\2\u00d1\u00d4\3\2"+
		"\2\2\u00d2\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d7\3\2\2\2\u00d4"+
		"\u00d2\3\2\2\2\u00d5\u00d7\7\62\2\2\u00d6\u00ce\3\2\2\2\u00d6\u00d5\3"+
		"\2\2\2\u00d7$\3\2\2\2\u00d8\u00dc\7$\2\2\u00d9\u00db\5\'\24\2\u00da\u00d9"+
		"\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd"+
		"\u00df\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00e0\7$\2\2\u00e0&\3\2\2\2\u00e1"+
		"\u00e9\n\4\2\2\u00e2\u00e3\7^\2\2\u00e3\u00e9\7p\2\2\u00e4\u00e5\7^\2"+
		"\2\u00e5\u00e9\7^\2\2\u00e6\u00e7\7^\2\2\u00e7\u00e9\7$\2\2\u00e8\u00e1"+
		"\3\2\2\2\u00e8\u00e2\3\2\2\2\u00e8\u00e4\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9"+
		"(\3\2\2\2\u00ea\u00eb\7v\2\2\u00eb\u00ec\7t\2\2\u00ec\u00ed\7w\2\2\u00ed"+
		"\u00ee\7g\2\2\u00ee*\3\2\2\2\u00ef\u00f0\7h\2\2\u00f0\u00f1\7c\2\2\u00f1"+
		"\u00f2\7n\2\2\u00f2\u00f3\7u\2\2\u00f3\u00f4\7g\2\2\u00f4,\3\2\2\2\u00f5"+
		"\u00f6\7p\2\2\u00f6\u00f7\7w\2\2\u00f7\u00f8\7n\2\2\u00f8\u00f9\7n\2\2"+
		"\u00f9.\3\2\2\2\u00fa\u00fc\t\5\2\2\u00fb\u00fa\3\2\2\2\u00fc\u00fd\3"+
		"\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff"+
		"\u0100\b\30\2\2\u0100\60\3\2\2\2\u0101\u0103\7\17\2\2\u0102\u0104\7\f"+
		"\2\2\u0103\u0102\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0107\3\2\2\2\u0105"+
		"\u0107\7\f\2\2\u0106\u0101\3\2\2\2\u0106\u0105\3\2\2\2\u0107\u0108\3\2"+
		"\2\2\u0108\u0109\b\31\2\2\u0109\62\3\2\2\2\u010a\u010b\7\61\2\2\u010b"+
		"\u010c\7,\2\2\u010c\u0110\3\2\2\2\u010d\u010f\13\2\2\2\u010e\u010d\3\2"+
		"\2\2\u010f\u0112\3\2\2\2\u0110\u0111\3\2\2\2\u0110\u010e\3\2\2\2\u0111"+
		"\u0113\3\2\2\2\u0112\u0110\3\2\2\2\u0113\u0114\7,\2\2\u0114\u0115\7\61"+
		"\2\2\u0115\u0116\3\2\2\2\u0116\u0117\b\32\2\2\u0117\64\3\2\2\2\u0118\u0119"+
		"\7\61\2\2\u0119\u011a\7\61\2\2\u011a\u011e\3\2\2\2\u011b\u011d\n\6\2\2"+
		"\u011c\u011b\3\2\2\2\u011d\u0120\3\2\2\2\u011e\u011c\3\2\2\2\u011e\u011f"+
		"\3\2\2\2\u011f\u0121\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0122\b\33\2\2"+
		"\u0122\66\3\2\2\2\u0123\u0124\7*\2\2\u01248\3\2\2\2\u0125\u0126\7+\2\2"+
		"\u0126:\3\2\2\2\u0127\u0128\7]\2\2\u0128<\3\2\2\2\u0129\u012a\7_\2\2\u012a"+
		">\3\2\2\2\u012b\u012c\7}\2\2\u012c@\3\2\2\2\u012d\u012e\7\177\2\2\u012e"+
		"B\3\2\2\2\u012f\u0130\7>\2\2\u0130D\3\2\2\2\u0131\u0132\7>\2\2\u0132\u0133"+
		"\7?\2\2\u0133F\3\2\2\2\u0134\u0135\7@\2\2\u0135H\3\2\2\2\u0136\u0137\7"+
		"@\2\2\u0137\u0138\7?\2\2\u0138J\3\2\2\2\u0139\u013a\7>\2\2\u013a\u013b"+
		"\7>\2\2\u013bL\3\2\2\2\u013c\u013d\7@\2\2\u013d\u013e\7@\2\2\u013eN\3"+
		"\2\2\2\u013f\u0140\7-\2\2\u0140P\3\2\2\2\u0141\u0142\7-\2\2\u0142\u0143"+
		"\7-\2\2\u0143R\3\2\2\2\u0144\u0145\7/\2\2\u0145T\3\2\2\2\u0146\u0147\7"+
		"/\2\2\u0147\u0148\7/\2\2\u0148V\3\2\2\2\u0149\u014a\7,\2\2\u014aX\3\2"+
		"\2\2\u014b\u014c\7\61\2\2\u014cZ\3\2\2\2\u014d\u014e\7\'\2\2\u014e\\\3"+
		"\2\2\2\u014f\u0150\7(\2\2\u0150^\3\2\2\2\u0151\u0152\7~\2\2\u0152`\3\2"+
		"\2\2\u0153\u0154\7(\2\2\u0154\u0155\7(\2\2\u0155b\3\2\2\2\u0156\u0157"+
		"\7~\2\2\u0157\u0158\7~\2\2\u0158d\3\2\2\2\u0159\u015a\7`\2\2\u015af\3"+
		"\2\2\2\u015b\u015c\7#\2\2\u015ch\3\2\2\2\u015d\u015e\7\u0080\2\2\u015e"+
		"j\3\2\2\2\u015f\u0160\7A\2\2\u0160l\3\2\2\2\u0161\u0162\7<\2\2\u0162n"+
		"\3\2\2\2\u0163\u0164\7=\2\2\u0164p\3\2\2\2\u0165\u0166\7.\2\2\u0166r\3"+
		"\2\2\2\u0167\u0168\7?\2\2\u0168t\3\2\2\2\u0169\u016a\7?\2\2\u016a\u016b"+
		"\7?\2\2\u016bv\3\2\2\2\u016c\u016d\7#\2\2\u016d\u016e\7?\2\2\u016ex\3"+
		"\2\2\2\u016f\u0170\7\60\2\2\u0170z\3\2\2\2\u0171\u0175\t\7\2\2\u0172\u0174"+
		"\t\b\2\2\u0173\u0172\3\2\2\2\u0174\u0177\3\2\2\2\u0175\u0173\3\2\2\2\u0175"+
		"\u0176\3\2\2\2\u0176|\3\2\2\2\u0177\u0175\3\2\2\2\r\2\u00d2\u00d6\u00dc"+
		"\u00e8\u00fd\u0103\u0106\u0110\u011e\u0175\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}