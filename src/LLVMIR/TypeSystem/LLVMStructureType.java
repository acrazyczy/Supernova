package LLVMIR.TypeSystem;

import java.util.ArrayList;
import java.util.Objects;

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
			ret.append(" ").append(types.iterator().next());
			for (int i = 1;i < types.size();++ i) ret.append(", ").append(types.get(i));
		}
		ret.append(" }>");
		return ret.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LLVMStructureType that = (LLVMStructureType) o;
		return size == that.size && name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, size);
	}
}
