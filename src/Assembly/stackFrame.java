package Assembly;

public class stackFrame {
	private int size;

	public stackFrame() {
		size = 0;
	}

	public int allocate(int m) {
		int ret = size;
		size += m;
		return ret;
	}
}