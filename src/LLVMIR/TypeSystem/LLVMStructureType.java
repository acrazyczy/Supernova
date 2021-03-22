package LLVMIR.TypeSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringJoiner;

public class LLVMStructureType extends LLVMAggregateType {
	private ArrayList<LLVMSingleValueType> types;
	String name;
	int size;

	public LLVMStructureType() {
		super();
		types = new ArrayList<>();
		size = 0;
	}

	public void registerMember(LLVMSingleValueType memberType) {
		types.add(memberType);
		size += memberType.size();
	}

	@Override
	public int size() {return size;}

	@Override
	public String toString() {
		return "%struct." + name;
	}
}
