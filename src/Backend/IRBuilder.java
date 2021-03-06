package Backend;

import AST.*;
import LLVMIR.Instruction.br;
import LLVMIR.Instruction.jump;
import LLVMIR.basicBlock;
import LLVMIR.entry;
import LLVMIR.function;
import Util.Scope.Scope;
import Util.Scope.globalScope;

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

	}

	@Override
	public void visit(arrayLiteralNode it) {

	}

	@Override
	public void visit(varDefStmtNode it) {

	}

	@Override
	public void visit(returnStmtNode it) {

	}

	@Override
	public void visit(binaryExprNode it) {

	}

	@Override
	public void visit(assignExprNode it) {

	}

	@Override
	public void visit(whileStmtNode it) {

	}

	@Override
	public void visit(unaryExprNode it) {

	}

	@Override
	public void visit(suiteStmtNode it) {

	}

	@Override
	public void visit(logicExprNode it) {

	}

	@Override
	public void visit(constExprNode it) {

	}

	@Override
	public void visit(breakStmtNode it) {

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

	}

	@Override
	public void visit(cmpExprNode it) {

	}

	@Override
	public void visit(ifStmtNode it) {
		it.cond.accept(this);
		basicBlock trueBranch = new basicBlock(), falseBranch = new basicBlock();
		currentBlock.push_back(new br(it.cond.val, trueBranch, falseBranch));

		basicBlock destination = new basicBlock();
		currentBlock = trueBranch;
		it.trueNode.accept(this);
		currentBlock.push_back(new jump(destination));
		currentBlock = falseBranch;
		it.falseNode.accept(this);
		currentBlock.push_back(new jump(destination));
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
