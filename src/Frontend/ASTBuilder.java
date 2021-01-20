package Frontend;

import AST.*;
import Parser.MxStarBaseVisitor;
import Parser.MxStarParser;
import Util.Scope.globalScope;
import Util.error.semanticError;
import Util.position;

import java.util.ArrayList;

public class ASTBuilder extends MxStarBaseVisitor<ASTNode> {
	private globalScope gScope;

	public ASTBuilder(globalScope gScope) {this.gScope = gScope;}

/*	@Override
	public ASTNode visitArgList(MxStarParser.ArgListContext ctx) {
		return super.visitArgList(ctx);
	}*/

	@Override
	public ASTNode visitArithmeticExpr(MxStarParser.ArithmeticExprContext ctx) {
		binaryExprNode binaryExpr = new binaryExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression(0)),
			(exprStmtNode) visit(ctx.expression(1))
		);
		switch (ctx.op.getText()) {
			case "+" -> binaryExpr.op = binaryExprNode.opType.Plus;
			case "-" -> binaryExpr.op = binaryExprNode.opType.Minus;
			case "*" -> binaryExpr.op = binaryExprNode.opType.Mul;
			case "/" -> binaryExpr.op = binaryExprNode.opType.Div;
			case "%" -> binaryExpr.op = binaryExprNode.opType.Mod;
		}
		return binaryExpr;
	}

	@Override
	public ASTNode visitArrayGenerator(MxStarParser.ArrayGeneratorContext ctx) {
		arrayLiteralNode arrayLiteral = new arrayLiteralNode(new position(ctx), (typeNode) visit(ctx.typeName()));
		ctx.expression().forEach(exp -> arrayLiteral.dims.add((exprStmtNode) visit(exp)));
		arrayLiteral.totalDim = ctx.LeftBracket().size();
		return arrayLiteral;
	}

	@Override
	public ASTNode visitAssigmentExpr(MxStarParser.AssigmentExprContext ctx) {
		return new assignExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression(0)),
			(exprStmtNode) visit(ctx.expression(1))
		);
	}

	@Override
	public ASTNode visitAtom(MxStarParser.AtomContext ctx) {
		if (ctx.trailer() == null) return new varExprNode(new position(ctx), ctx.Identifier().getText());
		else {
			funcCallExprNode funcCallExpr = new funcCallExprNode(new position(ctx), ctx.Identifier().getText());
			if (ctx.trailer() != null) {
				funcCallExpr.argList = new ArrayList<>();
				if (ctx.trailer().argList() != null)
					ctx.trailer().argList().expression().forEach(exp -> funcCallExpr.argList.add((exprStmtNode) visit(exp)));
			}
			return funcCallExpr;
		}
	}

	@Override
	public ASTNode visitAtomicExpr(MxStarParser.AtomicExprContext ctx) {
		return visitAtomicExpression(ctx.atomicExpression());
	}

	@Override
	public ASTNode visitAtomicExpression(MxStarParser.AtomicExpressionContext ctx) {
		if (ctx.constant() != null) return visitConstant(ctx.constant());
		if (ctx.This() != null) return new thisExprNode(new position(ctx));
		if (ctx.atom() != null) return visitAtom(ctx.atom());
		return null;
	}

	@Override
	public ASTNode visitBitwiseExpr(MxStarParser.BitwiseExprContext ctx) {
		binaryExprNode binaryExpr = new binaryExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression(0)),
			(exprStmtNode) visit(ctx.expression(1))
		);
		switch (ctx.op.getText()) {
			case "&" -> binaryExpr.op = binaryExprNode.opType.And;
			case "^" -> binaryExpr.op = binaryExprNode.opType.Xor;
			case "|" -> binaryExpr.op = binaryExprNode.opType.Or;
		}
		return binaryExpr;
	}

	@Override
	public ASTNode visitBitwiseShiftExpr(MxStarParser.BitwiseShiftExprContext ctx) {
		binaryExprNode binaryExpr = new binaryExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression(0)),
			(exprStmtNode) visit(ctx.expression(1))
		);
		binaryExpr.op = ctx.Lsh() == null ? binaryExprNode.opType.Rsh : binaryExprNode.opType.Lsh;
		return binaryExpr;
	}

	@Override
	public ASTNode visitBreakStmt(MxStarParser.BreakStmtContext ctx) {
		return new breakStmtNode(new position(ctx));
	}

	@Override
	public ASTNode visitClassDef(MxStarParser.ClassDefContext ctx) {
		classDefNode classDef = new classDefNode(new position(ctx), ctx.Identifier().getText());
		ctx.funcDef().forEach(fd -> classDef.methodDefs.add((funcDefNode) visit(fd)));
		ctx.varDef().forEach(vd -> classDef.varDefs.add((varDefStmtNode) visit(vd)));
		return classDef;
	}

	@Override
	public ASTNode visitClassGenerator(MxStarParser.ClassGeneratorContext ctx) {
		return new classLiteralNode(new position(ctx), (typeNode) visit(ctx.typeName()));
	}

	@Override
	public ASTNode visitCompoundStmt(MxStarParser.CompoundStmtContext ctx) {
		return super.visitCompoundStmt(ctx);
	}

	@Override
	public ASTNode visitConstant(MxStarParser.ConstantContext ctx) {
		constExprNode constExpr =  new constExprNode(new position(ctx), ctx.getText());
		if (ctx.logicConstant() != null) constExpr.type = "bool";
		else if (ctx.IntegerConstant() != null) constExpr.type = "int";
		else if (ctx.StringConstant() != null) constExpr.type = "string";
		else constExpr.type = null;
		return constExpr;
	}

	@Override
	public ASTNode visitContinueStmt(MxStarParser.ContinueStmtContext ctx) {
		return new continueStmtNode(new position(ctx));
	}

	@Override
	public ASTNode visitControlStmt(MxStarParser.ControlStmtContext ctx) {
		if (ctx.breakStmt() != null) return visitBreakStmt(ctx.breakStmt());
		if (ctx.continueStmt() != null) return visitContinueStmt(ctx.continueStmt());
		if (ctx.returnStmt() != null) return visitReturnStmt(ctx.returnStmt());
		return null;
	}

	@Override
	public ASTNode visitDynamicMemoryAllocation(MxStarParser.DynamicMemoryAllocationContext ctx) {
		return new newExprNode(new position(ctx), (exprStmtNode) visit(ctx.generator()));
	}

	@Override
	public ASTNode visitForStmt(MxStarParser.ForStmtContext ctx) {
		forStmtNode forStmt = new forStmtNode(
			new position(ctx),
			(stmtNode) visit(ctx.statement())
		);
		if (ctx.initExpr != null) forStmt.init = (exprStmtNode) visit(ctx.initExpr);
		if (ctx.condExpr != null) forStmt.cond = (exprStmtNode) visit(ctx.condExpr);
		if (ctx.incrExpr != null) forStmt.incr = (exprStmtNode) visit(ctx.incrExpr);
		return forStmt;
	}

	@Override
	public ASTNode visitFuncDef(MxStarParser.FuncDefContext ctx) {
		funcDefNode funcDef = new funcDefNode(new position(ctx), ctx.Identifier().getText(), (suiteStmtNode) visit(ctx.suite()));
		if (ctx.typeArgList() != null) {
			funcDef.paraType = new ArrayList<>();
			ctx.typeArgList().varType().forEach(t -> funcDef.paraType.add((typeNode) visit(t)));
			funcDef.paraName = new ArrayList<>();
			ctx.typeArgList().Identifier().forEach(var -> funcDef.paraName.add(var.getText()));
		}
		funcDef.returnType = ctx.varType() == null ? null : (typeNode) visit(ctx.varType());
		return funcDef;
	}

	@Override
	public ASTNode visitIfStmt(MxStarParser.IfStmtContext ctx) {
		ifStmtNode ifStmt = new ifStmtNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression()),
			(stmtNode) visit(ctx.trueStmt)
		);
		if (ctx.falseStmt != null) ifStmt.falseNode = (stmtNode) visit(ctx.falseStmt);
		return ifStmt;
	}

	@Override
	public ASTNode visitInnerExpr(MxStarParser.InnerExprContext ctx) {
		return visit(ctx.expression());
	}

/*	@Override
	public ASTNode visitLogicConstant(MxStarParser.LogicConstantContext ctx) {
		return super.visitLogicConstant(ctx);
	}*/

	@Override
	public ASTNode visitLogicExpr(MxStarParser.LogicExprContext ctx) {
		logicExprNode logicExpr = new logicExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression(0)),
			(exprStmtNode) visit(ctx.expression(1))
		);
		logicExpr.op = ctx.op.getText().equals("&&") ? logicExprNode.opType.And : logicExprNode.opType.Or;
		return logicExpr;
	}

	@Override
	public ASTNode visitMemberAccess(MxStarParser.MemberAccessContext ctx) {
		return new memberAccessExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression()),
			(exprStmtNode) visit(ctx.atom())
		);
	}

	@Override
	public ASTNode visitPrefixExpr(MxStarParser.PrefixExprContext ctx) {
		unaryExprNode unaryExpr = new unaryExprNode(new position(ctx), (exprStmtNode) visit(ctx.expression()));
		switch (ctx.op.getText()) {
			case "++" -> unaryExpr.op = unaryExprNode.opType.PreIncr;
			case "--" -> unaryExpr.op = unaryExprNode.opType.PreDecr;
			case "+" -> unaryExpr.op = unaryExprNode.opType.Plus;
			case "-" -> unaryExpr.op = unaryExprNode.opType.Minus;
			case "!" -> unaryExpr.op = unaryExprNode.opType.LogicNot;
			case "~" -> unaryExpr.op = unaryExprNode.opType.BitwiseNot;
		}
		return unaryExpr;
	}

	@Override
	public ASTNode visitProgram(MxStarParser.ProgramContext ctx) {
		rootNode root = new rootNode(new position(ctx));
		ctx.classDef().forEach(cd -> root.classDefs.add((classDefNode) visit(cd)));
		ctx.funcDef().forEach(fd -> root.funcDefs.add((funcDefNode) visit(fd)));
		ctx.varDef().forEach(vd -> root.varDefs.add((varDefStmtNode) visit(vd)));
		return root;
	}

	@Override
	public ASTNode visitRelationalExpr(MxStarParser.RelationalExprContext ctx) {
		cmpExprNode cmpExpr = new cmpExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression(0)),
			(exprStmtNode) visit(ctx.expression(1))
		);
		switch (ctx.op.getText()) {
			case "<" -> cmpExpr.op = cmpExprNode.opType.Lt;
			case ">" -> cmpExpr.op = cmpExprNode.opType.Gt;
			case "<=" -> cmpExpr.op = cmpExprNode.opType.Leq;
			case ">=" -> cmpExpr.op = cmpExprNode.opType.Geq;
			case "!=" -> cmpExpr.op = cmpExprNode.opType.Neq;
			case "==" -> cmpExpr.op = cmpExprNode.opType.Equ;
		}
		return cmpExpr;
	}

	@Override
	public ASTNode visitReturnStmt(MxStarParser.ReturnStmtContext ctx) {
		returnStmtNode returnStmt = new returnStmtNode(new position(ctx));
		if (ctx.expression() != null) returnStmt.returnExpr = (exprStmtNode) visit(ctx.expression());
		return returnStmt;
	}

	@Override
	public ASTNode visitStatement(MxStarParser.StatementContext ctx) {
		if (ctx.suite() != null) return visitSuite(ctx.suite());
		if (ctx.varDef() != null) return visitVarDef(ctx.varDef());
		if (ctx.controlStmt() != null) return visitControlStmt(ctx.controlStmt());
		if (ctx.compoundStmt() != null) return visitCompoundStmt(ctx.compoundStmt());
		if (ctx.expression() != null) return visit(ctx.expression());
		return null;
	}

	@Override
	public ASTNode visitSubscript(MxStarParser.SubscriptContext ctx) {
		return new subscriptionExprNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression(0)),
			(exprStmtNode) visit(ctx.expression(1))
		);
	}

	@Override
	public ASTNode visitSuffixExpr(MxStarParser.SuffixExprContext ctx) {
		unaryExprNode unaryExpr = new unaryExprNode(new position(ctx), (exprStmtNode) visit(ctx.expression()));
		unaryExpr.op = ctx.op.getText().equals("++") ? unaryExprNode.opType.SufIncr : unaryExprNode.opType.SufDecr;
		return unaryExpr;
	}

	@Override
	public ASTNode visitSuite(MxStarParser.SuiteContext ctx) {
		suiteStmtNode suiteStmt = new suiteStmtNode(new position(ctx));
		ctx.statement().forEach(stm -> suiteStmt.stmts.add((stmtNode) visit(stm)));
		return suiteStmt;
	}

/*	@Override
	public ASTNode visitTrailer(MxStarParser.TrailerContext ctx) {
		return super.visitTrailer(ctx);
	}*/

/*	@Override
	public ASTNode visitTypeArgList(MxStarParser.TypeArgListContext ctx) {
		return super.visitTypeArgList(ctx);
	}*/

	@Override
	public ASTNode visitInvalidArrayGenerator(MxStarParser.InvalidArrayGeneratorContext ctx) {
		throw new semanticError("invalid new expression.", new position(ctx));
	}

	@Override
	public ASTNode visitTypeName(MxStarParser.TypeNameContext ctx) {
		return new typeNode(new position(ctx), ctx.getText(), 0);
	}

	@Override
	public ASTNode visitVarDef(MxStarParser.VarDefContext ctx) {
		varDefStmtNode varDefStmt = new varDefStmtNode(new position(ctx), (typeNode) visit(ctx.varType()));
		ctx.Identifier().forEach(var -> varDefStmt.names.add(var.getText()));
		if (ctx.expression() != null) varDefStmt.init = (exprStmtNode) visit(ctx.expression());
		return varDefStmt;
	}

	@Override
	public ASTNode visitVarType(MxStarParser.VarTypeContext ctx) {
		typeNode type = (typeNode) visit(ctx.typeName());
		type.dim = ctx.LeftBracket().size();
		return type;
	}

	@Override
	public ASTNode visitWhileStmt(MxStarParser.WhileStmtContext ctx) {
		return new whileStmtNode(
			new position(ctx),
			(exprStmtNode) visit(ctx.expression()),
			(stmtNode) visit(ctx.statement())
		);
	}
}
