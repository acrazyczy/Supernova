package Backend;

import AST.*;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.integerConstant;
import LLVMIR.Operand.register;
import LLVMIR.Operand.voidConstant;
import LLVMIR.TypeSystem.LLVMAggregateType;
import LLVMIR.TypeSystem.LLVMFirstClassType;
import LLVMIR.TypeSystem.LLVMIntegerType;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.basicBlock;
import LLVMIR.entry;
import LLVMIR.function;
import Util.Scope.Scope;
import Util.Scope.globalScope;
import Util.Type.Type;
import Util.Type.arrayType;
import Util.typeCalculator;

import java.util.HashMap;

public class IRBuilder implements ASTVisitor {
	private globalScope gScope;
	private Scope currentScope = null;
	private HashMap<String, Integer> nameTable;
	private basicBlock currentBlock = null;
	private function currentFunction = null;
	private entry programEntry;
	private int counter = 0;

	public IRBuilder() {
		//add initial block
	}

	@Override
	public void visit(classLiteralNode it) {

	}

	@Override
	public void visit(memberAccessExprNode it) {

	}

	@Override
	public void visit(funcCallExprNode it) {

	}

	@Override
	public void visit(continueStmtNode it) {
		if (it.loopNode instanceof whileStmtNode) currentBlock.push_back(new br(((whileStmtNode) it.loopNode).bodyBlock));
		else currentBlock.push_back(new br(((forStmtNode) it.loopNode).incrBlock));
	}

	@Override
	public void visit(arrayLiteralNode it) {

	}

	@Override
	public void visit(varDefStmtNode it) {
		Type semanticType = typeCalculator.calcType(gScope, it.varType);
		register value = new register();
		if (semanticType.is_int) currentBlock.push_back(new alloca(new LLVMIntegerType(32), value));
		else if (semanticType.is_bool) currentBlock.push_back(new alloca(new LLVMIntegerType(1), value));
		else if (semanticType.is_string) currentBlock.push_back(new alloca(new LLVMPointerType(new LLVMIntegerType(8)), value));
		else if (semanticType instanceof arrayType) {
			// to do: get mapped type
			LLVMAggregateType mappedType = null;
			currentBlock.push_back(new alloca(new LLVMPointerType(mappedType), value));

		} else {
			// to do: get mapped type
			LLVMAggregateType mappedType = null;
			currentBlock.push_back(new alloca(new LLVMPointerType(mappedType), value));
		}
	}

	@Override
	public void visit(returnStmtNode it) {
		if (it.returnExpr == null) currentBlock.push_back(new ret(new voidConstant()));
		else {
			it.returnExpr.accept(this);
			currentBlock.push_back(new ret(it.returnExpr.val));
		}
	}

	@Override
	public void visit(binaryExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		register value;
		if (it.val != null) value = (register) it.val;
		else {
			value = new register();
			it.val = value;
		}
		binary.instCode instCode = null;
		switch (it.op) {
			case Plus -> instCode = binary.instCode.add;
			case Minus -> instCode = binary.instCode.sub;
			case Mul -> instCode = binary.instCode.mul;
			case Div -> instCode = binary.instCode.sdiv;
			case Mod -> instCode = binary.instCode.srem;
			case Lsh -> instCode = binary.instCode.shl;
			case Rsh -> instCode = binary.instCode.ashr;
			case And -> instCode = binary.instCode.and;
			case Or -> instCode = binary.instCode.or;
			case Xor -> instCode = binary.instCode.xor;
		}
		currentBlock.push_back(new binary(instCode, it.lhs.val, it.rhs.val, value));
	}

	@Override
	public void visit(assignExprNode it) {
		it.var.accept(this);
		it.value.val = it.var.val;
		it.value.accept(this);
	}

	@Override
	public void visit(whileStmtNode it) {
		basicBlock cond = new basicBlock(), body = new basicBlock(), dest = new basicBlock();
		currentBlock.push_back(new br(cond));
		it.cond.trueBranch = body;
		it.cond.falseBranch = dest;
		currentBlock = cond;
		it.cond.accept(this);
		currentBlock = body;
		it.stmt.accept(this);
		if (!currentBlock.hasTerminalStmt()) currentBlock.push_back(new br(cond));
		currentBlock = dest;
	}

	@Override
	public void visit(unaryExprNode it) {
		it.expr.accept(this);
		it.val = it.expr.val;
		if (it.op == unaryExprNode.opType.PreIncr)
			currentBlock.push_back(new binary(binary.instCode.add, it.expr.val, new integerConstant(32, 1), it.expr.val));
		else if (it.op == unaryExprNode.opType.PreDecr)
			currentBlock.push_back(new binary(binary.instCode.sub, it.expr.val, new integerConstant(32, 1), it.expr.val));
		else {
			register value;
			if (it.val != null) value = (register) it.val;
			else {
				value = new register();
				it.val = value;
			}
			if (it.op == unaryExprNode.opType.Plus)
				currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 0), it.expr.val, value));
			else if (it.op == unaryExprNode.opType.Minus)
				currentBlock.push_back(new binary(binary.instCode.sub, new integerConstant(32, 0), it.expr.val, value));
			else if (it.op == unaryExprNode.opType.LogicNot) {
				currentBlock.push_back(new binary(binary.instCode.xor, new integerConstant(1, 1), it.expr.val, value));
				if (it.trueBranch != null) {
					assert it.falseBranch != null;
					currentBlock.push_back(new br(value, it.trueBranch, it.falseBranch));
				}
			}
			else if (it.op == unaryExprNode.opType.BitwiseNot)
				currentBlock.push_back(new binary(binary.instCode.xor, new integerConstant(32, -1), it.expr.val, value));
			else if (it.op == unaryExprNode.opType.SufIncr) {
				currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 0), it.expr.val, value));
				currentBlock.push_back(new binary(binary.instCode.add, it.expr.val, new integerConstant(32, 1), it.expr.val));
			} else {
				currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 0), it.expr.val, value));
				currentBlock.push_back(new binary(binary.instCode.sub, it.expr.val, new integerConstant(32, 1), it.expr.val));
			}
			it.val = value;
		}
	}

	@Override public void visit(suiteStmtNode it) {it.stmts.forEach(stmt -> stmt.accept(this));}

	@Override
	public void visit(logicExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		register value;
		if (it.val != null) value = (register) it.val;
		else {
			value = new register();
			it.val = value;
		}
		binary.instCode instCode = it.op == logicExprNode.opType.And ? binary.instCode.and : binary.instCode.or;
		currentBlock.push_back(new binary(instCode, it.lhs.val, it.rhs.val, value));
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(value, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(constExprNode it) {

	}

	@Override
	public void visit(breakStmtNode it) {
		if (it.loopNode instanceof whileStmtNode) currentBlock.push_back(new br(((whileStmtNode) it.loopNode).destBlock));
		else currentBlock.push_back(new br(((forStmtNode) it.loopNode).destBlock));
	}

	@Override
	public void visit(thisExprNode it) {

	}

	@Override
	public void visit(classDefNode it) {

	}

	@Override
	public void visit(varExprNode it) {

	}

	@Override
	public void visit(newExprNode it) {

	}

	@Override
	public void visit(funcDefNode it) {

	}

	@Override
	public void visit(forStmtNode it) {
		if (it.init != null) it.init.accept(this);
		basicBlock cond = new basicBlock(), body = new basicBlock(), incr = new basicBlock(), dest = new basicBlock();
		if (it.cond != null) {
			currentBlock.push_back(new br(cond));
			it.cond.trueBranch = body;
			it.cond.falseBranch = dest;
			currentBlock = cond;
			it.cond.accept(this);
		} else {
			cond = body;
			currentBlock.push_back(new br(cond));
		}
		if (it.incr != null) {
			currentBlock = incr;
			it.incr.accept(this);
		} else incr = cond;
		currentBlock = body;
		it.stmt.accept(this);
		if (!currentBlock.hasTerminalStmt()) currentBlock.push_back(new br(incr));
		currentBlock = dest;
	}

	@Override
	public void visit(cmpExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		register value;
		if (it.val != null) value = (register) it.val;
		else {
			value = new register();
			it.val = value;
		}
		icmp.condCode condCode = null;
		switch (it.op) {
			case Gt -> condCode = icmp.condCode.sgt;
			case Lt -> condCode = icmp.condCode.slt;
			case Geq -> condCode = icmp.condCode.sge;
			case Leq -> condCode = icmp.condCode.sle;
			case Neq -> condCode = icmp.condCode.ne;
			case Equ -> condCode = icmp.condCode.eq;
		}
		currentBlock.push_back(new icmp(condCode, it.lhs.val, it.rhs.val, value));
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(value, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(ifStmtNode it) {
		it.cond.accept(this);
		basicBlock trueBranch = new basicBlock(), falseBranch = new basicBlock();
		currentBlock.push_back(new br(it.cond.val, trueBranch, falseBranch));

		basicBlock destination = new basicBlock();
		currentBlock = trueBranch;
		it.trueNode.accept(this);
		currentBlock.push_back(new br(destination));
		currentBlock = falseBranch;
		it.falseNode.accept(this);
		currentBlock.push_back(new br(destination));
		currentBlock = destination;

		programEntry.basicBlocks.add(trueBranch);
		programEntry.basicBlocks.add(falseBranch);
		programEntry.basicBlocks.add(destination);
	}

	@Override
	public void visit(typeNode it) {

	}

	@Override
	public void visit(rootNode it) {

	}
}
