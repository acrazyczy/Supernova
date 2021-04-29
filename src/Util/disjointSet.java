package Util;

import java.util.LinkedHashMap;

public class disjointSet<T> {
	private final LinkedHashMap<T, T> parent = new LinkedHashMap<>();

	public T query(T node) {
		if (!parent.containsKey(node)) return node;
		T root = query(parent.get(node));
		parent.put(node, root);
		return root;
	}

	public void put(T node, T par) {parent.put(query(node), query(par));}
}
