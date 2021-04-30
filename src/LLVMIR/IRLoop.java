package LLVMIR;

import java.util.LinkedHashSet;
import java.util.Set;

public class IRLoop {
	public Set<basicBlock> blocks = new LinkedHashSet<>();
	public Set<basicBlock> tails = new LinkedHashSet<>();
	public Set<IRLoop> children = new LinkedHashSet<>();
	public basicBlock preHead;

	public IRLoop() {}
}
