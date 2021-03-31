package Assembly.Operand;

import java.util.Arrays;
import java.util.HashMap;

public class physicalReg extends reg {
	static public String[] pRegNames = {
		"zero", "ra", "sp", "gp", "tp",
		"t0", "t1", "t2", "s0", "s1",
		"a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
		"s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
		"t3", "t4", "t5", "t6"};

	static public String[] callerSavePRegNames = {
		"ra", "t0", "t1", "t2",
		"a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
		"t3", "t4", "t5", "t6"
	};

	static public String[] calleeSavePRegNames = {
		"s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11"
	};

	static public String[] allocatablePRegNames = {
		"a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
		"s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
		"t0", "t1", "t2", "t3", "t4", "t5", "t6",
		"ra"
	};

	static public HashMap<String, physicalReg> pRegs;
	static public HashMap<String, physicalReg> callerSavePRegs;
	static public HashMap<String, physicalReg> calleeSavePRegs;
	static public HashMap<String, physicalReg> allocatablePRegs;

	static public HashMap<physicalReg, virtualReg> pRegToVReg;

	static {
		pRegs = new HashMap<>();
		callerSavePRegs = new HashMap<>();
		calleeSavePRegs = new HashMap<>();
		allocatablePRegs = new HashMap<>();
		pRegToVReg = new HashMap<>();
		for (String name: pRegNames) pRegs.put(name, new physicalReg(name));
		for (String name: callerSavePRegNames) callerSavePRegs.put(name, pRegs.get(name));
		for (String name: calleeSavePRegNames) calleeSavePRegs.put(name, pRegs.get(name));
		for (String name: allocatablePRegNames) allocatablePRegs.put(name, pRegs.get(name));
		pRegs.forEach((name, pReg) -> {
			virtualReg vReg = new virtualReg(-Arrays.asList(pRegNames).indexOf(name) - 1);
			vReg.color = pReg;
			pRegToVReg.put(pReg, vReg);
		});
	}

	private final String name;

	public physicalReg(String name) {
		super();
		this.name = name;
	}

	@Override public String toString() {return name;}
}
