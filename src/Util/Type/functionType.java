package Util.Type;

import java.util.ArrayList;

public class functionType extends Type {
	public Type returnType;
	public ArrayList<Type> paraType;

	public functionType(Type returnType, ArrayList<Type> paraType) {
		this.returnType = returnType;
		this.paraType = paraType;
	}
}
