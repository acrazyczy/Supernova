// Generated from MxStar.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxStarParser}.
 */
public interface MxStarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxStarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxStarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxStarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void enterFuncDef(MxStarParser.FuncDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void exitFuncDef(MxStarParser.FuncDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassDef(MxStarParser.ClassDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassDef(MxStarParser.ClassDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef(MxStarParser.VarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef(MxStarParser.VarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#typeArgList}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgList(MxStarParser.TypeArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#typeArgList}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgList(MxStarParser.TypeArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(MxStarParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(MxStarParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#suite}.
	 * @param ctx the parse tree
	 */
	void enterSuite(MxStarParser.SuiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#suite}.
	 * @param ctx the parse tree
	 */
	void exitSuite(MxStarParser.SuiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MxStarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MxStarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(MxStarParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(MxStarParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#atomicExpression}.
	 * @param ctx the parse tree
	 */
	void enterAtomicExpression(MxStarParser.AtomicExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#atomicExpression}.
	 * @param ctx the parse tree
	 */
	void exitAtomicExpression(MxStarParser.AtomicExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#trailer}.
	 * @param ctx the parse tree
	 */
	void enterTrailer(MxStarParser.TrailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#trailer}.
	 * @param ctx the parse tree
	 */
	void exitTrailer(MxStarParser.TrailerContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(MxStarParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(MxStarParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomicExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAtomicExpr(MxStarParser.AtomicExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomicExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAtomicExpr(MxStarParser.AtomicExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberAccess(MxStarParser.MemberAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberAccess(MxStarParser.MemberAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(MxStarParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(MxStarParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSuffixExpr(MxStarParser.SuffixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSuffixExpr(MxStarParser.SuffixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code innerExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInnerExpr(MxStarParser.InnerExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code innerExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInnerExpr(MxStarParser.InnerExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dynamicMemoryAllocation}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDynamicMemoryAllocation(MxStarParser.DynamicMemoryAllocationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dynamicMemoryAllocation}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDynamicMemoryAllocation(MxStarParser.DynamicMemoryAllocationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relationalExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpr(MxStarParser.RelationalExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relationalExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpr(MxStarParser.RelationalExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitwiseExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseExpr(MxStarParser.BitwiseExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitwiseExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseExpr(MxStarParser.BitwiseExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrefixExpr(MxStarParser.PrefixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrefixExpr(MxStarParser.PrefixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicExpr(MxStarParser.LogicExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicExpr(MxStarParser.LogicExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assigmentExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssigmentExpr(MxStarParser.AssigmentExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assigmentExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssigmentExpr(MxStarParser.AssigmentExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpr(MxStarParser.ArithmeticExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpr(MxStarParser.ArithmeticExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitwiseShiftExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseShiftExpr(MxStarParser.BitwiseShiftExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitwiseShiftExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseShiftExpr(MxStarParser.BitwiseShiftExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void enterControlStmt(MxStarParser.ControlStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void exitControlStmt(MxStarParser.ControlStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(MxStarParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(MxStarParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(MxStarParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(MxStarParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(MxStarParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(MxStarParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#compoundStmt}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStmt(MxStarParser.CompoundStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#compoundStmt}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStmt(MxStarParser.CompoundStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(MxStarParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(MxStarParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(MxStarParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(MxStarParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(MxStarParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(MxStarParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(MxStarParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(MxStarParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#logicConstant}.
	 * @param ctx the parse tree
	 */
	void enterLogicConstant(MxStarParser.LogicConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#logicConstant}.
	 * @param ctx the parse tree
	 */
	void exitLogicConstant(MxStarParser.LogicConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayGenerator}
	 * labeled alternative in {@link MxStarParser#generator}.
	 * @param ctx the parse tree
	 */
	void enterArrayGenerator(MxStarParser.ArrayGeneratorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayGenerator}
	 * labeled alternative in {@link MxStarParser#generator}.
	 * @param ctx the parse tree
	 */
	void exitArrayGenerator(MxStarParser.ArrayGeneratorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classGenerator}
	 * labeled alternative in {@link MxStarParser#generator}.
	 * @param ctx the parse tree
	 */
	void enterClassGenerator(MxStarParser.ClassGeneratorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classGenerator}
	 * labeled alternative in {@link MxStarParser#generator}.
	 * @param ctx the parse tree
	 */
	void exitClassGenerator(MxStarParser.ClassGeneratorContext ctx);
}