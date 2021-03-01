package LLVMIR.TypeSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringJoiner;

public class LLVMStructureType extends LLVMAggregateType {
	private ArrayList<LLVMFirstClassType> types;

	@Override
	public int size() {
		//to do
		return 0;
	}

	@Override
	public String toString() {
		StringJoiner ret =new StringJoiner(", ","<{ "," }>");
		types.forEach(type -> ret.add(type.toString()));
		return ret.toString();
	}
}
