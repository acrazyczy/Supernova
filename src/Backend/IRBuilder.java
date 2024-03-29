package Backend;

import AST.*;
import LLVMIR.Instruction.*;
import LLVMIR.Operand.*;
import LLVMIR.TypeSystem.*;
import LLVMIR.basicBlock;
import LLVMIR.IREntry;
import LLVMIR.function;
import Util.Scope.globalScope;
import Util.Type.Type;
import Util.Type.arrayType;
import Util.Type.classType;
import Util.position;
import Util.typeCalculator;

import java.util.*;

public class IRBuilder implements ASTVisitor {
	private final globalScope gScope;
	private basicBlock currentBlock = null;
	private function currentFunction = null;
	private classType currentClass = null;
	private final IREntry programIREntry;
	private int strConstCounter = 0;

	private final LinkedHashMap<String, globalVariable> globalStrMap = new LinkedHashMap<>();

	public IRBuilder(globalScope gScope, IREntry programIREntry) {
		this.gScope = gScope;
		this.programIREntry = programIREntry;
	}

	@Override
	public void visit(classLiteralNode it) {
		assert it.resultType instanceof classType;
		classType type = (classType) it.resultType;
		LLVMAggregateType baseType = (LLVMAggregateType) gScope.getLLVMTypeFromType(type);
		entity classSize = new integerConstant(32, baseType.size() >> 3);
		register value = new register(new LLVMPointerType(new LLVMIntegerType(8)), "_classMallocPtr", currentFunction, true);
		it.val = new register(new LLVMPointerType(baseType), "_classPtr", currentFunction, true);
		function mallocFunc = gScope.getMethodFunction("_malloc", true);
		currentBlock.push_back(new call(mallocFunc, new ArrayList<>(Collections.singletonList(classSize)) , value));
		currentBlock.push_back(new bitcast(value, it.val));
		String constructorName = type.className + "." + type.className;
		if (gScope.containMethod(constructorName , true))
			currentBlock.push_back(new call(gScope.getMethodFunction(constructorName, true), new ArrayList<>(Collections.singletonList(it.val))));
	}

	@Override
	public void visit(memberAccessExprNode it) {
		it.lhs.accept(this);
		register value;
		if (it.rhs instanceof funcCallExprNode) {
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
			entity ptr = new register(new LLVMPointerType(memberLLVMType), "_memberAccessPtr", currentFunction, true);
			currentBlock.push_back(new getelementptr(it.lhs.val, idxes, ptr));
			value = new register(memberLLVMType, "_memberValue", currentFunction, true);
			currentBlock.push_back(new load(ptr, value));
			it.ptr = ptr;
		}
		it.val = value;
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(it.val, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(funcCallExprNode it) {
		function func = it.func;
		if (it.func.functionName.equals("builtin.array.size")) {
			assert it.thisEntity != null && it.funcName.equals("size");
			register arraySizePtr = new register(new LLVMPointerType(new LLVMIntegerType()), "_arraySizePtr", currentFunction, false);
			currentBlock.push_back(new bitcast(it.thisEntity, arraySizePtr));
			currentBlock.push_back(new getelementptr(arraySizePtr, new ArrayList<>(Collections.singletonList(new integerConstant(32, -1))), arraySizePtr));
			register value = new register(new LLVMIntegerType(), "_arraySize", currentFunction, true);
			currentBlock.push_back(new load(arraySizePtr, value));
			it.val = value;
		} else {
			ArrayList<entity> parameters = new ArrayList<>();
			if (it.thisEntity != null) parameters.add(it.thisEntity);
			else if (currentClass != null && func.functionName.indexOf(currentClass.className + ".") == 0)
				parameters.add(currentFunction.argValues.iterator().next());
			it.argList.forEach(argv -> {
				argv.accept(this);
				parameters.add(argv.val);
			});
			if (func.returnType == null) currentBlock.push_back(new call(func, parameters));
			else {
				register value = new register(func.returnType, "_funcReturnValue", currentFunction, true);
				currentBlock.push_back(new call(func, parameters, value));
				it.val = value;
			}
		}
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(it.val, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(continueStmtNode it) {
		if (it.loopNode instanceof whileStmtNode) currentBlock.push_back(new br(((whileStmtNode) it.loopNode).condBlock));
		else currentBlock.push_back(new br(((forStmtNode) it.loopNode).incrBlock));
	}

	@Override
	public void visit(arrayLiteralNode it) {
		LLVMSingleValueType baseType = typeCalculator.calcLLVMSingleValueType(gScope, ((arrayType) it.resultType).elementType);
		for (int i = it.totalDim - it.dims.size();i >= 1;-- i) baseType = new LLVMPointerType(baseType);
		function mallocFunc = gScope.getMethodFunction("_malloc", true);
		LLVMSingleValueType ptr = baseType;
		assert it.dims.size() >= 1;
		for (int i = it.dims.size() - 1;i >= 0;-- i) ptr = new LLVMPointerType(ptr);
		it.val = arrayCreator(mallocFunc, it.dims, 0, (LLVMPointerType) ptr);
	}

	private register arrayCreator(function mallocFunc, ArrayList<exprStmtNode> idxNodes, int currentDim, LLVMPointerType currentType) {
		basicBlock initBlock = new basicBlock("_new.loop.init", currentFunction);
		currentBlock.push_back(new br(initBlock));
		currentFunction.blocks.add(initBlock);
		currentBlock = initBlock;
		exprStmtNode idxNode = idxNodes.get(currentDim);
		idxNode.accept(this);
		register ptr = new register(currentType, "_subArrayPtr", currentFunction, true);
		register arraySize = new register(new LLVMIntegerType(), "_arraySize", currentFunction, false),
			mallocSize = new register(new LLVMIntegerType(), "_arrayMallocSize", currentFunction, true),
			mallocPtr = new register(new LLVMPointerType(new LLVMIntegerType(8)), "_arrayMallocPtr", currentFunction, true),
			arraySizePtr = new register(new LLVMPointerType(new LLVMIntegerType()), "_arraySizePtr", currentFunction, false);
		currentBlock.push_back(new _move(new integerConstant(32, currentType.pointeeType.size() >> 3), arraySize));
		currentBlock.push_back(new binary(binary.instCode.mul, idxNode.val, arraySize, arraySize));
		currentBlock.push_back(new binary(binary.instCode.add, new integerConstant(32, 4), arraySize, mallocSize));
		currentBlock.push_back(new call(mallocFunc, new ArrayList<>(Collections.singletonList(mallocSize)), mallocPtr));
		currentBlock.push_back(new bitcast(mallocPtr, arraySizePtr));
		currentBlock.push_back(new binary(binary.instCode.ashr, arraySize , new integerConstant(32, 2), arraySize));
		currentBlock.push_back(new store(arraySize, arraySizePtr));
		currentBlock.push_back(new getelementptr(arraySizePtr, new ArrayList<>(Collections.singletonList(new integerConstant(32, 1))), arraySizePtr));
		currentBlock.push_back(new bitcast(arraySizePtr, ptr));
		if (currentDim != idxNodes.size() - 1) {
			register arrayShiftPtr = new register(currentType, "_arrayShiftPtr", currentFunction, false), itr = new register(new LLVMIntegerType(), "_i", currentFunction, false);
			currentBlock.push_back(new _move(ptr, arrayShiftPtr));
			currentBlock.push_back(new _move(new integerConstant(32, 0), itr));
			basicBlock condBlock = new basicBlock("_new.loop.cond", currentFunction);
			currentBlock.push_back(new br(condBlock));
			currentFunction.blocks.add(condBlock);
			currentBlock = condBlock;
			basicBlock bodyBlock = new basicBlock("_new.loop.body", currentFunction),
				destBlock = new basicBlock("_new.loop.dest", currentFunction),
				incrBlock = new basicBlock("_new.loop.incr", currentFunction);
			register cond = new register(new LLVMIntegerType(8, true), "_cond", currentFunction, true);
			currentBlock.push_back(new icmp(icmp.condCode.slt, itr, idxNode.val, cond));
			currentBlock.push_back(new br(cond, bodyBlock, destBlock));
			currentFunction.blocks.add(bodyBlock);
			currentBlock = bodyBlock;
			register subArrayPtr = arrayCreator(mallocFunc, idxNodes, currentDim + 1, (LLVMPointerType) currentType.pointeeType);
			currentBlock.push_back(new br(incrBlock));
			currentFunction.blocks.add(incrBlock);
			currentBlock = incrBlock;
			currentBlock.push_back(new store(subArrayPtr, arrayShiftPtr));
			currentBlock.push_back(new getelementptr(arrayShiftPtr, new ArrayList<>(Collections.singletonList(new integerConstant(32, 1))), arrayShiftPtr));
			currentBlock.push_back(new binary(binary.instCode.add, itr, new integerConstant(32, 1), itr));
			currentBlock.push_back(new br(condBlock));
			currentFunction.blocks.add(destBlock);
			currentBlock = destBlock;
		}
		return ptr;
	}

	@Override
	public void visit(varDefStmtNode it) {
		Iterator<String> nameItr = it.names.iterator();
		Iterator<exprStmtNode> initItr = it.init.iterator();
		Iterator<entity> varItr = it.varEntities.iterator();
		while (nameItr.hasNext()) {
			String name = nameItr.next();
			exprStmtNode init = initItr.next();
			entity var = varItr.next();
			if (init == null) {
				if (!currentFunction.functionName.equals("main"))
					currentBlock.push_back(new _move(new undefinedValue(var.type), var));
			} else if (currentFunction.functionName.equals("main")) {
				globalVariable gVar = (globalVariable) var;
				if (init.resultType != null && init.resultType.is_string) {
					String str = ((constExprNode) init).value;
					globalVariable gStr;
					if (globalStrMap.containsKey(str)) gStr = globalStrMap.get(str);
					else {
						gStr = new globalVariable(new LLVMArrayType(str.length() + 1, new LLVMIntegerType(8)), "._strConst." + strConstCounter, true, true);
						++ strConstCounter;
						gStr.val = str;
						globalStrMap.put(str, gStr);
						programIREntry.globals.add(gStr);
					}
					register strPtr = new register(new LLVMPointerType(new LLVMIntegerType(8)), "_strPtr", currentFunction, true);
					currentBlock.push_back(new getelementptr(gStr,
						new ArrayList<>(Arrays.asList(new integerConstant(32, 0), new integerConstant(32, 0))),
						strPtr
					));
					currentBlock.push_back(new store(strPtr, gVar));
					gVar.val = "null";
				} else if (init instanceof constExprNode) gVar.val = ((constExprNode) init).value;
				else {
					init.accept(this);
					currentBlock.push_back(new store(init.val, gVar));
				}
			} else {
				init.accept(this);
				currentBlock.push_back(new _move(init.val, var));
			}
			if (!currentFunction.functionName.equals("main"))
				((register) var).name = name + currentFunction.getRegisterNameIndex(name);
		}
	}

	@Override
	public void visit(returnStmtNode it) {
		if (it.returnExpr == null) currentBlock.push_back(new ret(null));
		else {
			it.returnExpr.accept(this);
			if (it.returnExpr.val instanceof nullPointerConstant) it.returnExpr.val.type = currentFunction.returnType;
			currentBlock.push_back(new ret(it.returnExpr.val));
		}
	}

	@Override
	public void visit(binaryExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		register value = new register(it.lhs.val.type, "", currentFunction, true);
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
		it.value.accept(this);
		if (it.value.val instanceof nullPointerConstant) it.value.val.type = it.var.val.type;
		if (it.var.ptr != null) {
			currentBlock.push_back(new store(it.value.val, it.var.ptr));
			it.ptr = it.var.ptr;
		}
		currentBlock.push_back(new _move(it.value.val, it.var.val));
		it.val = it.var.val;
	}

	@Override
	public void visit(whileStmtNode it) {
		basicBlock cond = new basicBlock("while.cond", currentFunction),
			body = new basicBlock("while.body", currentFunction),
			dest = new basicBlock("while.dest", currentFunction);

		it.condBlock = cond;
		it.destBlock = dest;
		currentBlock.push_back(new br(cond));
		it.cond.trueBranch = body;
		it.cond.falseBranch = dest;

		currentFunction.blocks.add(cond);
		currentBlock = cond;
		it.cond.accept(this);

		currentFunction.blocks.add(body);
		currentBlock = body;
		if (it.stmt != null) it.stmt.accept(this);
		if (currentBlock.hasNoTerminalStmt()) currentBlock.push_back(new br(cond));

		currentFunction.blocks.add(dest);
		currentBlock = dest;
	}

	@Override
	public void visit(unaryExprNode it) {
		it.expr.accept(this);
		if (it.op == unaryExprNode.opType.PreIncr) {
			currentBlock.push_back(new binary(binary.instCode.add, it.expr.val, new integerConstant(32, 1), it.expr.val));
			if (it.expr.ptr != null) {
				currentBlock.push_back(new store(it.expr.val, it.expr.ptr));
				it.ptr = it.expr.ptr;
			}
			it.val = it.expr.val;
		}
		else if (it.op == unaryExprNode.opType.PreDecr) {
			currentBlock.push_back(new binary(binary.instCode.sub, it.expr.val, new integerConstant(32, 1), it.expr.val));
			if (it.expr.ptr != null) {
				currentBlock.push_back(new store(it.expr.val, it.expr.ptr));
				it.ptr = it.expr.ptr;
			}
			it.val = it.expr.val;
		}
		else {
			register value = new register(it.op == unaryExprNode.opType.LogicNot ? new LLVMIntegerType(8, true) : new LLVMIntegerType(), "", currentFunction, true);
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
			else {
				currentBlock.push_back(new _move(it.expr.val, value));
				if (it.op == unaryExprNode.opType.SufIncr) currentBlock.push_back(new binary(binary.instCode.add, it.expr.val, new integerConstant(32, 1), it.expr.val));
				else {
					assert it.op == unaryExprNode.opType.SufDecr;
					currentBlock.push_back(new binary(binary.instCode.sub, it.expr.val, new integerConstant(32, 1), it.expr.val));
				}
				if (it.expr.ptr != null) currentBlock.push_back(new store(it.expr.val, it.expr.ptr));
			}
			it.val = value;
		}
	}

	@Override public void visit(suiteStmtNode it) {it.stmts.forEach(stmt -> {if (stmt != null) stmt.accept(this);});}

	private basicBlock shortCircuitBlock;

	@Override
	public void visit(logicExprNode it) {
		basicBlock rhs = new basicBlock(it.op == logicExprNode.opType.And ? "and.lhs.true" : "or.lhs.false", currentFunction);
		currentFunction.blocks.add(rhs);
		it.lhs.accept(this);
		basicBlock lhs = currentBlock;
		currentBlock = rhs;
		it.rhs.trueBranch = it.trueBranch;
		it.rhs.falseBranch = it.falseBranch;
		it.rhs.accept(this);
		if (it.rhs instanceof logicExprNode && ((logicExprNode) it.rhs).op == it.op) {
			lhs.push_back(it.op == logicExprNode.opType.And ? new br(it.lhs.val, rhs, shortCircuitBlock) : new br(it.lhs.val, shortCircuitBlock, rhs));
			it.val = it.rhs.val;
		} else if (it.trueBranch == null) {
			shortCircuitBlock = new basicBlock(it.op == logicExprNode.opType.And ? "and.false" : "or.true", currentFunction);
			currentFunction.blocks.add(shortCircuitBlock);
			lhs.push_back(it.op == logicExprNode.opType.And ? new br(it.lhs.val, rhs, shortCircuitBlock) : new br(it.lhs.val, shortCircuitBlock, rhs));
			basicBlock end = new basicBlock(it.op == logicExprNode.opType.And ? "and.true" : "or.false", currentFunction),
				dest = new basicBlock(it.op == logicExprNode.opType.And ? "and.dest" : "or.dest", currentFunction);
			register value = new register(new LLVMIntegerType(8, true), it.op == logicExprNode.opType.And ? "and.res" : "or.res", currentFunction, dest);
			currentFunction.blocks.add(end);
			currentFunction.blocks.add(dest);
			currentBlock.push_back(it.op == logicExprNode.opType.And ? new br(it.rhs.val, end, shortCircuitBlock) : new br(it.rhs.val, shortCircuitBlock, end));
			shortCircuitBlock.push_back(new _move(new booleanConstant(it.op == logicExprNode.opType.And ? 0 : 1), value));
			shortCircuitBlock.push_back(new br(dest));
			end.push_back(new _move(new booleanConstant(it.op == logicExprNode.opType.And ? 1 : 0), value));
			end.push_back(new br(dest));
			currentBlock = dest;
			it.val = value;
		} else {
			shortCircuitBlock = it.op == logicExprNode.opType.And ? it.falseBranch : it.trueBranch;
			lhs.push_back(it.op == logicExprNode.opType.And ? new br(it.lhs.val, rhs, shortCircuitBlock) : new br(it.lhs.val, shortCircuitBlock, rhs));
			basicBlock end = it.op == logicExprNode.opType.And ? it.trueBranch : it.falseBranch;
			currentBlock.push_back(it.op == logicExprNode.opType.And ? new br(it.rhs.val, end, shortCircuitBlock) : new br(it.rhs.val, shortCircuitBlock, end));
		}
	}

	@Override
	public void visit(constExprNode it) {
		entity value;
		if (it.type == null) {
//			value = new register(new LLVMPointerType(new LLVMIntegerType(8)), "_null", currentFunction);
//			currentBlock.push_back(new _move(new nullPointerConstant(), value));
			value = new nullPointerConstant();
		} else if (it.type.equals("int")) {
//			value = new register(new LLVMIntegerType(), "_int", currentFunction);
//			currentBlock.push_back(new _move(new integerConstant(32, Integer.parseInt(it.value)), value));
			value = new integerConstant(32, Integer.parseInt(it.value));
		} else if (it.type.equals("bool")) {
//			value = new register(new LLVMIntegerType(8, true), "_bool", currentFunction);
//			currentBlock.push_back(new _move(new booleanConstant(it.value.equals("true") ? 1 : 0), value));
			value = new booleanConstant(it.value.equals("true") ? 1 : 0);
		} else {
			assert it.type.equals("string");
			value = new register(new LLVMPointerType(new LLVMIntegerType(8)), "_string", currentFunction, true);
			globalVariable gStr;
			if (globalStrMap.containsKey(it.value)) gStr = globalStrMap.get(it.value);
			else {
				gStr = new globalVariable(new LLVMArrayType(it.value.length() + 1, new LLVMIntegerType(8)), "._strConst." + strConstCounter, true, true);
				++ strConstCounter;
				gStr.val = it.value;
				globalStrMap.put(it.value, gStr);
				programIREntry.globals.add(gStr);
			}
			currentBlock.push_back(new getelementptr(gStr,
				new ArrayList<>(Arrays.asList(new integerConstant(32, 0), new integerConstant(32, 0))),
				value
			));
		}
		it.val = value;
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(it.val, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(breakStmtNode it) {
		if (it.loopNode instanceof whileStmtNode) currentBlock.push_back(new br(((whileStmtNode) it.loopNode).destBlock));
		else currentBlock.push_back(new br(((forStmtNode) it.loopNode).destBlock));
	}

	@Override public void visit(thisExprNode it) {it.val = currentFunction.argValues.iterator().next();}

	@Override public void visit(classDefNode it) {
		currentClass = (classType) gScope.getTypeFromName(it.name, new position(0, 0));
		it.units.forEach(unit -> {if (unit.funcDef != null) unit.funcDef.accept(this);});
		currentClass = null;
	}

	@Override
	public void visit(varExprNode it) {
		if (it.varEntity == null) {
			LLVMSingleValueType varLLVMType = typeCalculator.calcLLVMSingleValueType(gScope, it.resultType);
			register varPtr = new register(new LLVMPointerType(varLLVMType), "_varPtr", currentFunction, true);
			currentBlock.push_back(new getelementptr(currentFunction.argValues.iterator().next(),
				new ArrayList<>(Arrays.asList(
					new integerConstant(32, 0),
					new integerConstant(32, currentClass.memberVariablesIndex.get(it.varName))
				)), varPtr));
			register value = new register(varLLVMType, "", currentFunction, true);
			currentBlock.push_back(new load(varPtr, value));
			it.val = value;
			it.ptr = varPtr;
		} else if (it.varEntity instanceof globalVariable) {
			assert it.varEntity.type instanceof LLVMPointerType;
			LLVMSingleValueType varLLVMType = (LLVMSingleValueType) ((LLVMPointerType) it.varEntity.type).pointeeType;
			register value = new register(varLLVMType, "", currentFunction, true);
			currentBlock.push_back(new load(it.varEntity, value));
			it.val = value;
			it.ptr = it.varEntity;
		} else it.val = it.varEntity;
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(it.val, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(newExprNode it) {
		it.expr.accept(this);
		assert it.expr instanceof arrayLiteralNode || it.expr instanceof classLiteralNode;
		it.val = it.expr.val;
	}

	@Override
	public void visit(funcDefNode it) {
		currentFunction = it.func;
		currentBlock = it.func.blocks.iterator().next();
		it.funcBody.accept(this);
		if (currentBlock.hasNoTerminalStmt())
			currentBlock.push_back(new ret(currentFunction.returnType == null ? null :
				currentFunction.returnType instanceof LLVMPointerType ? new nullPointerConstant() :
					new integerConstant(currentFunction.returnType.size(), 0)));
		currentBlock = null;
		currentFunction = null;
	}

	@Override
	public void visit(forStmtNode it) {
		if (it.init != null) it.init.accept(this);

		basicBlock cond = new basicBlock("for.cond", currentFunction),
			body = new basicBlock("for.body", currentFunction),
			incr = new basicBlock("for.incr", currentFunction),
			dest = new basicBlock("for.dest", currentFunction);
		it.destBlock = dest;
		it.incrBlock = incr;
		currentFunction.blocks.add(cond);
		currentBlock.push_back(new br(cond));
		currentBlock = cond;
		if (it.cond != null) {
			it.cond.trueBranch = body;
			it.cond.falseBranch = dest;
			currentBlock = cond;
			it.cond.accept(this);
		} else currentBlock.push_back(new br(body));

		currentFunction.blocks.add(body);
		currentBlock = body;
		if (it.stmt != null) it.stmt.accept(this);
		if (currentBlock.hasNoTerminalStmt()) currentBlock.push_back(new br(incr));

		currentFunction.blocks.add(incr);
		currentBlock = incr;
		if (it.incr != null) it.incr.accept(this);
		if (currentBlock.hasNoTerminalStmt()) currentBlock.push_back(new br(cond));

		currentFunction.blocks.add(dest);
		currentBlock = dest;
	}

	@Override
	public void visit(cmpExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		if (it.lhs.val instanceof nullPointerConstant) it.lhs.val.type = it.rhs.val.type;
		if (it.rhs.val instanceof nullPointerConstant) it.rhs.val.type = it.lhs.val.type;
		register value = new register(new LLVMIntegerType(8, true), "", currentFunction, true);
		if (it.lhs.resultType == null && it.rhs.resultType == null)
			currentBlock.push_back(new _move(new booleanConstant(it.op == cmpExprNode.opType.Equ ? 1 : 0), value));
		else if (it.lhs.resultType instanceof arrayType || it.rhs.resultType instanceof arrayType) {
			entity lhsEntity, rhsEntity;
			if (((LLVMPointerType) it.lhs.val.type).pointeeType.size() != 1) {
				lhsEntity = new register(new LLVMPointerType(new LLVMIntegerType(8)), "_arrayPtr", currentFunction, true);
				currentBlock.push_back(new bitcast(it.lhs.val, lhsEntity));
			}
			if (((LLVMPointerType) it.rhs.val.type).pointeeType.size() != 1) {
				rhsEntity = new register(new LLVMPointerType(new LLVMIntegerType(8)), "_arrayPtr", currentFunction, true);
				currentBlock.push_back(new bitcast(it.rhs.val, rhsEntity));
			}
			currentBlock.push_back(new icmp(
				it.op == cmpExprNode.opType.Equ ? icmp.condCode.eq : icmp.condCode.ne,
				it.lhs.val, it.rhs.val, value));
		} else {
			if (it.lhs.resultType != null && it.lhs.resultType.is_string) {
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
				currentBlock.push_back(new call(cmpFunc, new ArrayList<>(Arrays.asList(it.lhs.val, it.rhs.val)), value));
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
		}
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(value, it.trueBranch, it.falseBranch));
		}
		it.val = value;
	}

	@Override
	public void visit(ifStmtNode it) {
		basicBlock trueBranch = new basicBlock("if.trueBranch", currentFunction),
			falseBranch = new basicBlock("if.falseBranch", currentFunction),
			destination = new basicBlock("if.dest", currentFunction);
		it.cond.trueBranch = trueBranch;
		it.cond.falseBranch = falseBranch;
		it.cond.accept(this);

		currentFunction.blocks.add(trueBranch);
		currentBlock = trueBranch;
		if (it.trueNode != null) it.trueNode.accept(this);
		if (currentBlock.hasNoTerminalStmt()) currentBlock.push_back(new br(destination));
		currentFunction.blocks.add(falseBranch);
		currentBlock = falseBranch;
		if (it.falseNode != null) it.falseNode.accept(this);
		if (currentBlock.hasNoTerminalStmt()) currentBlock.push_back(new br(destination));
		currentFunction.blocks.add(destination);
		currentBlock = destination;
	}

	@Override public void visit(typeNode it) {}

	@Override public void visit(rootNode it) {
		currentFunction = programIREntry.functions.iterator().next();
		currentBlock = currentFunction.blocks.iterator().next();
		it.units.forEach(unit -> {if (unit.varDef != null) unit.accept(this);});
		register retVal = new register(new LLVMIntegerType(), "_retVal", currentFunction, true);
		currentBlock.push_back(new call(gScope.getMethodFunction("_g.main", true), new ArrayList<>(), retVal));
		currentBlock.push_back(new ret(retVal));
		currentFunction = null;
		it.units.forEach(unit -> {if (unit.varDef == null) unit.accept(this);});
	}

	@Override
	public void visit(subscriptionExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		register value = new register((LLVMSingleValueType) ((LLVMPointerType) it.lhs.val.type).pointeeType, "", currentFunction, true);
		register ptr = new register(it.lhs.val.type, "_valPtr", currentFunction, true);
		currentBlock.push_back(new getelementptr(it.lhs.val, new ArrayList<>(Collections.singletonList(it.rhs.val)) , ptr));
		it.ptr = ptr;
		currentBlock.push_back(new load(ptr, value));
		it.val = value;
		if (it.trueBranch != null) {
			assert it.falseBranch != null;
			currentBlock.push_back(new br(it.val, it.trueBranch, it.falseBranch));
		}
	}

	@Override
	public void visit(programUnitNode it) {
		if (it.varDef != null) it.varDef.accept(this);
		if (it.classDef != null) it.classDef.accept(this);
		if (it.funcDef != null) it.funcDef.accept(this);
	}
}
