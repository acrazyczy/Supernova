// Generated from E:/Repository/Supernova/src/Parser\MxStar.g4 by ANTLR 4.9.1
package Parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxStarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxStarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxStarParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxStarParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#programUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramUnit(MxStarParser.ProgramUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#funcDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(MxStarParser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#classDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDef(MxStarParser.ClassDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarType(MxStarParser.VarTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDef(MxStarParser.VarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#singleVarDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleVarDef(MxStarParser.SingleVarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#typeArgList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgList(MxStarParser.TypeArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(MxStarParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#suite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuite(MxStarParser.SuiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MxStarParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(MxStarParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#atomicExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicExpression(MxStarParser.AtomicExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#trailer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailer(MxStarParser.TrailerContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(MxStarParser.ArgListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atomicExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicExpr(MxStarParser.AtomicExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAccess(MxStarParser.MemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscript(MxStarParser.SubscriptContext ctx);
	/**
	 * Visit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixExpr(MxStarParser.SuffixExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code innerExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerExpr(MxStarParser.InnerExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dynamicMemoryAllocation}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDynamicMemoryAllocation(MxStarParser.DynamicMemoryAllocationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relationalExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpr(MxStarParser.RelationalExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitwiseExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseExpr(MxStarParser.BitwiseExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixExpr(MxStarParser.PrefixExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicExpr(MxStarParser.LogicExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assigmentExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssigmentExpr(MxStarParser.AssigmentExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithmeticExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExpr(MxStarParser.ArithmeticExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitwiseShiftExpr}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseShiftExpr(MxStarParser.BitwiseShiftExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#controlStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlStmt(MxStarParser.ControlStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(MxStarParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#breakStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(MxStarParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#continueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(MxStarParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#compoundStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStmt(MxStarParser.CompoundStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(MxStarParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(MxStarParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#forStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(MxStarParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MxStarParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#logicConstant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicConstant(MxStarParser.LogicConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code invalidArrayGenerator}
	 * labeled alternative in {@link MxStarParser#generator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvalidArrayGenerator(MxStarParser.InvalidArrayGeneratorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayGenerator}
	 * labeled alternative in {@link MxStarParser#generator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayGenerator(MxStarParser.ArrayGeneratorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classGenerator}
	 * labeled alternative in {@link MxStarParser#generator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassGenerator(MxStarParser.ClassGeneratorContext ctx);
}