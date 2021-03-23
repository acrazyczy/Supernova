package Backend;

import AST.*;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.*;
import LLVMIR.TypeSystem.*;
import LLVMIR.basicBlock;
import LLVMIR.function;
import Util.Scope.globalScope;
import Util.Type.Type;
import Util.Type.arrayType;
import Util.Type.classType;
import Util.position;
import Util.typeCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IRBuilder implements ASTVisitor {
	private final globalScope gScope;
	private basicBlock currentBlock = null;
	private function currentFunction = null;
	private classType currentClass = null;

	public IRBuilder(globalScope gScope) {this.gScope = gScope;}

	@Override
	public void visit(classLiteralNode it) {
		LLVMAggregateType baseType = (LLVMAggregateType) gScope.getLLVMTypeFromType(it.resultType);
		register classSize = new register(new LLVMIntegerType(32));
		currentBlock.push_back(new binary(binary.instCode.ashr, new integerConstant(32, baseType.size()), new integerConstant(32, 3), classSize));
		register value = it.val != null ? (register) it.val : new register(new LLVMPointerType(baseType));
		function mallocFunc = gScope.getMethodFunction("malloc", true);
		currentBlock.push_back(new call(mallocFunc, new ArrayList<>(Collections.singletonList(classSize)) , value));
		it.val = value;
	}

	@Override
	public void visit(memberAccessExprNode it) {
		it.lhs.accept(this);
		register value = null;
		if (it.val != null) value = (register) it.val;
		if (it.rhs instanceof funcCallExprNode) {
			it.rhs.val = it.val;
			((funcCallExprNode) it.rhs).thisEntity = it.lhs.val;
			it.rhs.accept(this);
			value = (register) it.rhs.val;
		} else {
			assert it.rhs instanceof varExprNode;
			classType structType = (classType) it.lhs.resultType;
			ArrayList<entity> idxes = new ArrayList<>();
			idxes.add(new integerConstant(32, 0));
			idxes.add(new integerConstant(32, structType.memberVariablesIndex.get(((varExprNode) it.rhs).varName)));
			Type memberType = structType.memberVariables.get(((varExprNode) it.rhs).varName);
			LLVMSingleValueType memberLLVMType = typeCalculator.calcLLVMSingleValueType(gScope, memberType);
			entity ptr = new register(new LLVMPointerType(memberLLVMType));
			currentBlock.push_back(new getelementptr(it.lhs.val, idxes, ptr));
			if (value == null) value = new register(memberLLVMType);
			currentBlock.push_back(new load(ptr, value));
			it.ptr = ptr;
		}
		it.val = value;
	}

	@Override
	public void visit(funcCallExprNode it) {
		function func = it.func;
		if (it.func == null) {
			assert it.thisEntity != null && it.funcName.equals("size");
			register arraySizePtr = new register(new LLVMPointerType(new LLVMIntegerType(32)));
			currentBlock.push_back(new bitcast(it.thisEntity, arraySizePtr));
			currentBlock.push_back(new getelementptr(arraySizePtr, new ArrayList<>(Collections.singletonList(new integerConstant(32, -1))), arraySizePtr));
			register value;
			if (it.val != null) value = (register) it.val;
			else value = new register(new LLVMIntegerType(32));
			currentBlock.push_back(new load(arraySizePtr, value));
			it.val = value;
		} else {
			ArrayList<entity> parameters = new ArrayList<>();
			if (it.thisEntity != null) parameters.add(it.thisEntity);
			it.argList.forEach(argv -> {
				argv.accept(this);
				parameters.add(argv.val);
			});
			if (func.returnType == null) currentBlock.push_back(new call(func, parameters));
			else {
				register value;
				if (it.val != null) value = (register) it.val;
				else value = new register(func.returnType);
				currentBlock.push_back(new call(func, parameters, value));
				it.val = value;
			}
		}
	}

	@Override
	public void visit(continueStmtNode it) {
		if (it.loopNode instanceof whileStmtNode) currentBlock.push_back(new br(((whileStmtNode) it.loopNode).bodyBlock));
		else currentBlock.push_back(new br(((forStmtNode) it.loopNode).incrBlock));
	}

	@Override
	public void visit(arrayLiteralNode it) {
		LLVMSingleValueType baseType = typeCalculator.calcLLVMSingleValueType(gScope, ((arrayType) it.resultType).elementType);
		for (int i = it.totalDim - it.dims.size();i >= 1;-- i) baseType = new LLVMPointerType(baseType);
		function mallocFunc = gScope.getMethodFunction("malloc", true);
		LLVMSingleValueType ptr = baseType;
		register value = null;
		assert it.dims.size() >= 1;
		for (int i = it.dims.size() - 1;i >= 0;-- i) {
			exprStmtNode idxNode = it.dims.get(i);
			idxNode.accept(this);
			if (i != 0 || it.val == null) value = new register(new LLVMPointerType(ptr));
			else value = (register) it.val;
			register arraySize = new register(new LLVMIntegerType(32)),
				mallocSize = new register(new LLVMIntegerType(32)),
				mallocPtr = new register(new LLVMPointerType(new LLVMIntegerType(8))),
				arraySizePtr = new register(new LLVMPointerType(new LLVMIntegerType(32)));
			currentBlock.push_back(new binary(binary.instCode.ashr, new integerConstant(32, ptr.size()) , new integerConstant(32, 3), arraySize));
			currentBlock.push_back(new binary(binary.instCode.mul, idxNode.val, arraySize, arraySize));
			currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 4), arraySize, mallocSize));
			currentBlock.push_back(new call(mallocFunc, new ArrayList<>(Collections.singletonList(mallocSize)), mallocPtr));
			currentBlock.push_back(new bitcast(mallocPtr, arraySizePtr));
			currentBlock.push_back(new store(arraySize, arraySizePtr));
			currentBlock.push_back(new getelementptr(arraySizePtr, new ArrayList<>(Collections.singletonList(new integerConstant(32, 1))), arraySizePtr));
			currentBlock.push_back(new bitcast(arraySizePtr, value));
			ptr = new LLVMPointerType(ptr);
		}
		it.val = value;
	}

	@Override
	public void visit(varDefStmtNode it) {
		Type semanticType = typeCalculator.calcType(gScope, it.varType);
		register value = new register(typeCalculator.calcLLVMSingleValueType(gScope, semanticType));
		currentBlock.push_back(new _move(new undefinedValue(), value));
	}

	@Override
	public void visit(returnStmtNode it) {
		if (it.returnExpr == null) currentBlock.push_back(new ret(null));
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
		else value = new register(new LLVMIntegerType(32));
		if (it.lhs.resultType.is_string) {
			assert it.rhs.resultType.is_string;
			assert it.op == binaryExprNode.opType.Plus;
			currentBlock.push_back(new call(gScope.getMethodFunction("builtin.string.add", true),
				new ArrayList<>(Arrays.asList(it.lhs.val, it.rhs.val)), value));
		} else {
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
		it.val = value;
	}

	@Override
	public void visit(assignExprNode it) {
		it.var.accept(this);
		it.value.val = it.var.val;
		it.value.accept(this);
		if (it.var.ptr != null) currentBlock.push_back(new store(it.var.val, it.var.ptr));
	}

	@Override
	public void visit(whileStmtNode it) {
		basicBlock cond = new basicBlock("while.cond", currentFunction),
			body = new basicBlock("while.body", currentFunction),
			dest = new basicBlock("while.dest", currentFunction);
		currentBlock.push_back(new br(cond));
		it.cond.trueBranch = body;
		it.cond.falseBranch = dest;
		currentBlock = cond;
		it.cond.accept(this);
		currentBlock = body;
		it.stmt.accept(this);
		if (!currentBlock.hasTerminalStmt()) currentBlock.push_back(new br(cond));
		currentBlock = dest;

		currentFunction.blocks.add(cond);
		currentFunction.blocks.add(body);
		currentFunction.blocks.add(dest);
	}

	@Override
	public void visit(unaryExprNode it) {
		it.expr.accept(this);
		it.val = it.expr.val;
		if (it.op == unaryExprNode.opType.PreIncr) {
			currentBlock.push_back(new binary(binary.instCode.add, it.expr.val, new integerConstant(32, 1), it.expr.val));
			if (it.expr.ptr != null) currentBlock.push_back(new store(it.expr.val, it.expr.ptr));
		}
		else if (it.op == unaryExprNode.opType.PreDecr) {
			currentBlock.push_back(new binary(binary.instCode.sub, it.expr.val, new integerConstant(32, 1), it.expr.val));
			if (it.expr.ptr != null) currentBlock.push_back(new store(it.expr.val, it.expr.ptr));
		}
		else {
			register value;
			if (it.val != null) value = (register) it.val;
			else {
				value = new register(it.op == unaryExprNode.opType.LogicNot ? new LLVMIntegerType(8) : new LLVMIntegerType(32));
				it.val = value;
			}
			if (it.op == unaryExprNode.opType.Plus)
				currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 0), it.expr.val, value));
			else if (it.op == unaryExprNode.opType.Minus)
				currentBlock.push_back(new binary(binary.instCode.sub, new integerConstant(32, 0), it.expr.val, value));
			else if (it.op == unaryExprNode.opType.LogicNot) {
				currentBlock.push_back(new binary(binary.instCode.xor, new booleanConstant(1), it.expr.val, value));
				if (it.trueBranch != null) {
					assert it.falseBranch != null;
					currentBlock.push_back(new br(value, it.trueBranch, it.falseBranch));
				}
			} else if (it.op == unaryExprNode.opType.BitwiseNot)
				currentBlock.push_back(new binary(binary.instCode.xor, new integerConstant(32, -1), it.expr.val, value));
			else if (it.op == unaryExprNode.opType.SufIncr) {
				currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 0), it.expr.val, value));
				currentBlock.push_back(new binary(binary.instCode.add, it.expr.val, new integerConstant(32, 1), it.expr.val));
				if (it.expr.ptr != null) currentBlock.push_back(new store(it.expr.val, it.expr.ptr));
			} else {
				assert it.op == unaryExprNode.opType.SufDecr;
				currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 0), it.expr.val, value));
				currentBlock.push_back(new binary(binary.instCode.sub, it.expr.val, new integerConstant(32, 1), it.expr.val));
				if (it.expr.ptr != null) currentBlock.push_back(new store(it.expr.val, it.expr.ptr));
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
			value = new register(new LLVMIntegerType(8));
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
		entity value = null;
		if (it.val != null) value = it.val;
		if (it.type == null)
			if (value == null) value = new nullPointerConstant();
			else currentBlock.push_back(new _move(new nullPointerConstant(), value));
		else if (it.type.equals("int"))
			if (value == null) value = new integerConstant(32, Integer.parseInt(it.value));
			else currentBlock.push_back(new _move(new integerConstant(32, Integer.parseInt(it.value)), value));
		else if (it.type.equals("bool"))
			if (value == null) value = new booleanConstant(it.value.equals("true") ? 1 : 0);
			else currentBlock.push_back(new _move(new booleanConstant(it.value.equals("true") ? 1 : 0), value));
		else {
			assert it.type.equals("string");
			if (value == null) value = new register(new LLVMPointerType(new LLVMIntegerType(8)));
			function mallocFunc = gScope.getMethodFunction("malloc", true);
			register charPtr = new register(new LLVMPointerType(new LLVMIntegerType(8)));
			currentBlock.push_back(new call(mallocFunc, new ArrayList<>(Collections.singletonList(new integerConstant(32, it.value.length() + 1))), value));
			for (int i = 0;i < it.value.length();++ i)
			{
				currentBlock.push_back(new getelementptr(value, new ArrayList<>(Collections.singletonList(new integerConstant(32, i))), charPtr));
				currentBlock.push_back(new store(new integerConstant(8, it.value.charAt(i)), charPtr));
			}
			currentBlock.push_back(new getelementptr(value, new ArrayList<>(Collections.singletonList(new integerConstant(32, it.value.length()))), charPtr));
			currentBlock.push_back(new store(new integerConstant(8, 0), charPtr));
		}
		it.val = value;
	}

	@Override
	public void visit(breakStmtNode it) {
		if (it.loopNode instanceof whileStmtNode) currentBlock.push_back(new br(((whileStmtNode) it.loopNode).destBlock));
		else currentBlock.push_back(new br(((forStmtNode) it.loopNode).destBlock));
	}

	@Override public void visit(thisExprNode it) {it.val = currentFunction.argValues.get(0);}

	@Override public void visit(classDefNode it) {
		currentClass = (classType) gScope.getTypeFromName(it.name, new position(0, 0));
		it.units.forEach(unit -> {if (unit.funcDef != null) unit.funcDef.accept(this);});
		currentClass = null;
	}

	@Override
	public void visit(varExprNode it) {
		if (it.varEntity == null) {
			LLVMSingleValueType varLLVMType = typeCalculator.calcLLVMSingleValueType(gScope, it.resultType);
			register varPtr = new register(new LLVMPointerType(varLLVMType));
			currentBlock.push_back(new getelementptr(currentFunction.argValues.get(0),
				new ArrayList<>(Collections.singletonList(
					new integerConstant(32, currentClass.memberVariablesIndex.get(it.varName))
				)), varPtr));
			register value;
			if (it.val != null) value = (register) it.val;
			else it.val = value = new register(varLLVMType);
			currentBlock.push_back(new load(varPtr, value));
		}
		if (it.val == null) it.val = it.varEntity;
		else currentBlock.push_back(new _move(it.varEntity, it.val));
	}

	@Override
	public void visit(newExprNode it) {
		register value = null;
		if (it.val != null) value = (register) it.val;
		it.expr.val = value;
		it.expr.accept(this);
		assert it.expr instanceof arrayLiteralNode || it.expr instanceof classLiteralNode;
		if (value == null) value = (register) it.expr.val;
		it.val = value;
	}

	@Override
	public void visit(funcDefNode it) {
		currentFunction = it.func;
		currentBlock = it.func.blocks.get(0);
		it.funcBody.accept(this);
	}

	@Override
	public void visit(forStmtNode it) {
		if (it.init != null) it.init.accept(this);
		basicBlock cond = new basicBlock("for.cond", currentFunction),
			body = new basicBlock("for.body", currentFunction),
			incr = new basicBlock("for.incr", currentFunction),
			dest = new basicBlock("for.dest", currentFunction);
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

		currentFunction.blocks.add(cond);
		currentFunction.blocks.add(body);
		currentFunction.blocks.add(incr);
		currentFunction.blocks.add(dest);
	}

	@Override
	public void visit(cmpExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		register value;
		if (it.val != null) value = (register) it.val;
		else {
			value = new register(new LLVMIntegerType(8));
			it.val = value;
		}
		if (it.lhs.resultType.is_string) {
			assert it.rhs.resultType.is_string;
			function cmpFunc = null;
			switch (it.op) {
				case Gt -> cmpFunc = gScope.getMethodFunction("builtin.string.isGreaterThan", true);
				case Lt -> cmpFunc = gScope.getMethodFunction("builtin.string.isLessThan", true);
				case Geq -> cmpFunc = gScope.getMethodFunction("builtin.string.isGreaterThanOrEqual", true);
				case Leq -> cmpFunc = gScope.getMethodFunction("builtin.string.isLessThanOrEqual", true);
				case Neq -> cmpFunc = gScope.getMethodFunction("builtin.string.isNotEqual", true);
				case Equ -> cmpFunc = gScope.getMethodFunction("builtin.string.isEqual", true);
			}
			currentBlock.push_back(new call(cmpFunc, new ArrayList<>(Arrays.asList(it.lhs.val, it.rhs.val)) , value));
		} else {
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
		}
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(value, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(ifStmtNode it) {
		it.cond.accept(this);
		basicBlock trueBranch = new basicBlock("if.trueBranch", currentFunction),
			falseBranch = new basicBlock("if.falseBranch", currentFunction);
		currentBlock.push_back(new br(it.cond.val, trueBranch, falseBranch));

		basicBlock destination = new basicBlock("if.dest", currentFunction);
		currentBlock = trueBranch;
		it.trueNode.accept(this);
		if (!currentBlock.hasTerminalStmt()) currentBlock.push_back(new br(destination));
		currentBlock = falseBranch;
		if (it.falseNode != null) it.falseNode.accept(this);
		if (!currentBlock.hasTerminalStmt()) currentBlock.push_back(new br(destination));
		currentBlock = destination;

		currentFunction.blocks.add(trueBranch);
		currentFunction.blocks.add(falseBranch);
		currentFunction.blocks.add(destination);
	}

	@Override public void visit(typeNode it) {}

	@Override public void visit(rootNode it) {it.units.forEach(unit -> unit.accept(this));}

	@Override
	public void visit(subscriptionExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		register value;
		if (it.val != null) value = (register) it.val;
		else {
			value = new register((LLVMSingleValueType) ((LLVMPointerType) it.lhs.val.type).pointeeType);
			it.val = value;
		}
		register ptr = new register(it.lhs.val.type);
		currentBlock.push_back(new getelementptr(it.lhs.val, new ArrayList<>(Collections.singletonList(it.rhs.val)) , ptr));
		it.ptr = ptr;
		currentBlock.push_back(new load(ptr, value));
	}

	@Override
	public void visit(programUnitNode it) {
		if (it.classDef != null) it.classDef.accept(this);
		if (it.varDef != null) it.varDef.accept(this);
		if (it.funcDef != null) it.funcDef.accept(this);
	}
}
