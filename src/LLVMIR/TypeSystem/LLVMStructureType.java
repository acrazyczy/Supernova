package LLVMIR.TypeSystem;

import java.util.ArrayList;

public class LLVMStructureType extends LLVMAggregateType {
	public final ArrayList<LLVMSingleValueType> types;
	String name;
	int size;

	public LLVMStructureType(String name) {
		super();
		types = new ArrayList<>();
		size = 0;
		this.name = name;
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

	public String classDefString() {
		StringBuilder ret = new StringBuilder(this + " = type <{");
		if (!types.isEmpty()) {
			ret.append(" ").append(types.get(0));
			for (int i = 1;i < types.size();++ i) ret.append(", ").append(types.get(i));
		}
		ret.append(" }>");
		return ret.toString();
	}
}
