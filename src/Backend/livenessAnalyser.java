package Backend;

import Assembly.asmEntry;

public class livenessAnalyser implements asmVisitor {
	private final asmEntry programEntry;

	public livenessAnalyser(asmEntry programEntry) {
		this.programEntry = programEntry;
	}

	@Override
	public void run() {
	}
}
