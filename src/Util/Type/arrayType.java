package Util.Type;

public class arrayType extends Type {
	public Type elementType;
	public int dim;

	public arrayType(Type elementType, int dim) {
		this.elementType = elementType;
		this.dim = dim;
	}

	public Type subType() {return dim > 1 ? new arrayType(elementType, dim - 1) : elementType;}
}
