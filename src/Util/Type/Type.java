package Util.Type;

public class Type {
	public boolean is_bool = false, is_int = false, is_void = false, is_string = false;

	public Type() {}
	public Type(String typeName) {
		switch (typeName) {
			case "bool" -> is_bool = true;
			case "int" -> is_int = true;
			case "string" -> is_string = true;
			case "void" -> is_void = true;
		}
	}
}
