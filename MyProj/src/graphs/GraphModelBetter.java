package graphs;

public class GraphModelBetter {
	SeparateChainingHashST<String, Queue<Node>> graph;
	Queue<Node> nodes;

	public GraphModelBetter() {
		graph = new SeparateChainingHashST<String, Queue<Node>>();
		nodes = new Queue<Node>();
	}

	public Queue<Node> getValues(String key) {
		return graph.get(key);
	}

	public void put(String key, String value) {
		if (graph.contains(key)) {
			Queue<Node> queue = graph.get(key);
			queue.enqueue(new Node(value));
		} else {
			Queue<Node> queue = new Queue<Node>();
			queue.enqueue(new Node(value));
			graph.put(key, queue);
			nodes.enqueue(new Node(key, Integer.MAX_VALUE));
		}
	}

	public void put(String key, String value, int v) {
		if (graph.contains(key)) {
			Queue<Node> queue = graph.get(key);
			queue.enqueue(new Node(value, v));
		} else {
			Queue<Node> queue = new Queue<Node>();
			queue.enqueue(new Node(value, v));
			graph.put(key, queue);
			nodes.enqueue(new Node(key, Integer.MAX_VALUE));
		}
	}

	public String dijkstras(String start, String end) {
		Queue<Node> q = new Queue<Node>();
		for (Node n : nodes) {
			q.enqueue(n);
		}
		Node v = findCopy(q, start);
		v.setDist(0);
		v.setPrev(null);
		while (!q.isEmpty()) {
			Node n = q.peek();
			for (Node node : q) {
				if (node.dist < n.dist) {
					n = node;
				}
			}
			q.remove(n);
			for (Node node : graph.get(n.data)) {
				int dist = node.dist + n.dist;
				node = findCopy(nodes, node.data);
				if (dist < node.dist) {
					node.setDist(dist);
					node.setPrev(n);
				}
			}
		}
		Node node = findCopy(nodes, end);
		if (node.prev != null || node.equals(end)) {
			int dist = node.dist;
			String ret = "";
			while (node != null) {
				ret =  node.data + "->" + ret;
				node = node.prev;
			}
			ret = ret.substring(0, ret.length() -2);
			return ret + "  distance: " + dist;
		}
		/*
		 * 1 S <- empty sequence 2 u <- target 3 if prev[u] is defined or u = source: //
		 * Do something only if the vertex is reachable 4 while u is defined: //
		 * Construct the shortest path with a stack S 5 insert u at the beginning of S
		 * // Push the vertex onto the stack 6 u <- prev[u] // Traverse from target to
		 * source
		 */

		return "";
	}

	public Node findCopy(Queue<Node> q, String o) {
		for (Node copy : q) {
			if (copy.equals(o)) {
				return copy;
			}
		}
		return null;
	}

	class Node {
		String data;
		Node prev;
		int dist;

		public Node(String String) {
			this.data = String;
			dist = 1;
		}

		public Node(String data, int dist) {
			this.data = data;
			this.dist = dist;
		}

		public String getString() {
			return data;
		}

		public void setDist(int d) {
			dist = d;
		}

		public void setPrev(Node n) {
			prev = n;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof String) {
				return o.equals(data);
			}
			Node tmp = (Node) o;
			return equals(tmp);
		}

		public boolean equals(Node o) {
			return o.getString().equals(data);
		}

	}
}