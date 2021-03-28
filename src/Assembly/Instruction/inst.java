package Assembly.Instruction;

import Assembly.Operand.physicalReg;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.function.BiFunction;

abstract public class inst {
	public inst pre = null, suf = null;
	private String comment = null;

	public inst() {}
	public inst(String comment) {this.comment = comment;}

	public inst linkBefore(ArrayList<inst> newInst) {
		if (newInst.isEmpty()) return null;
		inst lst = this, tmp = pre, cur = null;
		for (int i = newInst.size() - 1;i >= 0;-- i) {
			cur = newInst.get(i);
			(cur.suf = lst).pre = suf;
		}
		if (tmp == null) return cur;
		(tmp.suf = cur).pre = tmp;
		return null;
	}

	@Override abstract public String toString();

	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {}
}
