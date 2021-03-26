package Assembly;

import java.util.ArrayList;
import java.util.HashMap;

public class asmEntry {
	public HashMap<String, String> stringData = new HashMap<>();
	public HashMap<String, Integer> integerData = new HashMap<>();
	public ArrayList<asmFunction> asmFunctions = new ArrayList<>();

	public asmEntry() {}
}
