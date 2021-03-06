package LLVMIR.TypeSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringJoiner;

public class LLVMStructureType extends LLVMAggregateType {
	private ArrayList<LLVMFirstClassType> types;
	String name;
	int size;

	@Override
	public int size() {return size;}

	@Override
	public String toString() {
		return "%struct." + name;
	}
}
