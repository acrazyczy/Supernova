package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class call extends statement {
	public final function callee;
	public final ArrayList<entity> parameters;

	public call(function callee, ArrayList<entity> parameters) {
		super();
		this.callee = callee;
		this.parameters = parameters;
	}

	public call(function callee, ArrayList<entity> parameters, entity dest) {
		super(dest);
		this.callee = callee;
		this.parameters = parameters;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = parameters.stream().filter(p -> p instanceof register)
			.map(p -> (register) p).collect(Collectors.toSet());
		if (dest != null) ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return parameters.stream().filter(p -> p instanceof register)
			.map(p -> (register) p).collect(Collectors.toSet());
	}

	@Override
	public void replaceUse(entity oldReg, entity newReg) {
		for (int i = 0;i < parameters.size();++ i)
			if (parameters.get(i) == oldReg)
				parameters.set(i, newReg);
	}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		assert false; // Oops!
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		assert false; // Oops!
		return false;
	}

	@Override public statement clone() {return dest == null ? new call(callee, new ArrayList<>(parameters)) : new call(callee, new ArrayList<>(parameters), dest);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		if (dest != null) dest = replacer.apply((register) dest);
		for (ListIterator<entity> argItr = parameters.listIterator();argItr.hasNext();) {
			entity argv = argItr.next();
			if (argv instanceof register) argItr.set(replacer.apply((register) argv));
		}
	}

	@Override public String toString() {
		String ret = "";
		if (this.dest != null) ret += this.dest + " = ";
		ret += "call " + callee.functionToString(parameters);
		return ret;
	}
}
