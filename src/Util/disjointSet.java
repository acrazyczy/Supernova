package Util;

import java.util.HashMap;

public class disjointSet<T> {
	private final HashMap<T, T> parent = new HashMap<>();

	public T query(T node) {
		if (!parent.containsKey(node)) return node;
		T root = query(parent.get(node));
		parent.put(node, root);
		return root;
	}

	public void put(T node, T par) {parent.put(query(node), query(par));}
}
