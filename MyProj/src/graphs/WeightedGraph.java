package graphs;

import java.util.NoSuchElementException;

public class WeightedGraph {
	private static final String NEWLINE = System.getProperty("line.separator");

	private final int V;
	private int E;
	private Queue<Edge>[] adj;

	/**
	 * Initializes an empty WeightedGraph with {@code V} vertices and 0 edges. param
	 * V the number of vertices
	 *
	 * @param V number of vertices
	 * @throws IllegalArgumentException if {@code V < 0}
	 */
	public WeightedGraph(int V) {
		if (V < 0)
			throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (Queue<Edge>[]) new Queue[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new Queue<Edge>();
		}
	}

	/**
	 * input : int start - startpoint of search int end - endpoint of search output:
	 * Stack with path, start at top, end at bottom, empty if no such path
	 */
	public Stack<Integer> dfs(int start, int end) {
		int[] prev = new int[V];
		for (int i = 0; i < prev.length; i++) {
			prev[i] = -1;
		}
		boolean[] visited = new boolean[V];
		Stack<Integer> st = new Stack<Integer>();
		if (dfs(start, end, visited, prev)) {
			int tmp = end;
			do {
				st.push(tmp);
				tmp = prev[tmp];
			} while (tmp != -1);
		}
		return st;
	}

	private boolean dfs(int v, int end, boolean[] visited, int[] prev) {
		visited[v] = true;
		if (v == end) {
			return true;
		} else {
			for (Edge e : adj[v]) {
				int i = e.vertix;
				if (!visited[i]) {
					prev[i] = v;
					if (dfs(i, end, visited, prev)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Stack<Integer> bfs(int start, int end) {
		Queue<Integer> q = new Queue<Integer>();
		Stack<Integer> st = new Stack<Integer>();
		boolean[] visited = new boolean[V];
		int[] prev = new int[V];
		for (int i = 0; i < prev.length; i++) {
			prev[i] = -1;
		}
		visited[start] = true;
		q.enqueue(start);
		while (!q.isEmpty()) {

			Integer v = q.dequeue();
			if (v == end) {
				do {
					st.push(v);
					v = prev[v];
				} while (v != -1);

			} else {
				for (Edge e : adj[v]) {
					int i = e.vertix;
					if (!visited[i]) {
						visited[i] = true;
						prev[i] = v;
						q.enqueue(i);
					}
				}
			}
		}
		return st;
	}

	public Stack<String> dijkstra(int start, int end) {
		double[] dist = new double[V];
		int[] prev = new int[V];
		Queue<Integer> q = new Queue<Integer>();
		for (int i = 0; i < V; i++) {
			q.enqueue(i);
			dist[i] = Double.MAX_VALUE;
			prev[i] = -1;
		}
		dist[start] = 0;
		while (!q.isEmpty()) {
			Integer v = q.peek();
			for (Integer i : q) {
				if (dist[i] < dist[v]) {
					v = i;
				}
			}
			q.remove(v);
			for (Edge e : adj[v]) {
				double weight = e.weight + dist[v];
				if (dist[e.vertix] > weight) {
					dist[e.vertix] = weight;
					prev[e.vertix] = v;
				}
			}
		}
		Stack<String> str = new Stack<String>();
		Integer v = end;
		do {
			str.push(v + "");
			v = prev[v];
		} while (v != -1);
		str.push(dist[end] + "");
		return str;
		/*
		 * 1 function Dijkstra(Graph, source): 2 3 create vertex set Q 4 5 for each
		 * vertex v in Graph: 6 dist[v] <- INFINITY 7 prev[v] <- UNDEFINED 8 add v to Q
		 * 10 dist[source] <- 0 11 12 while Q is not empty: 13 u <- vertex in Q with min
		 * dist[u] 14 15 remove u from Q 16 17 for each neighbor v of u: // only v that
		 * are still in Q 18 alt <- dist[u] + length(u, v) 19 if alt < dist[v]: 20
		 * dist[v] <- alt 21 prev[v] <- u 22 23 return dist[], prev[]
		 */
	}
	public void getComponent(Integer node, Queue<Integer> added){
		added.enqueue(node);
		for(Edge e: adj[node]) {
			Integer i = e.vertix;
			if(!added.contains(i)) {
				getComponent(i, added);
			}
		}
	}
	public boolean checkPath(Integer start, Integer goal) {
		Queue<Integer> queue = new Queue<Integer>();
		getComponent(start, queue);
		return queue.contains(goal);
	}
	public Queue<Stack<Integer>> checkCyclePaths(){
		Queue<Stack<Integer>> queue = new Queue<Stack<Integer>>();
		for(int i = 0; i < V; i++) {
			for(Edge e: adj[i]) {
				if(checkPath(e.vertix, i)) {
					Stack<Integer> tmp = dfs(e.vertix, i);
					tmp.push(i);
					queue.add(tmp);
				}
			}
		}
		return queue;
	}
	public Queue<Integer> biggestComponent() {
		Queue<Integer> q = new Queue<Integer>();
		for (int i = 0; i < V; i++) {
			q.enqueue(i);
		}
		Queue<Queue<Integer>> queues = new Queue<Queue<Integer>>();
		while(!q.isEmpty()) {
			Queue<Integer> tmp = new Queue<Integer>();
			getComponent(q.peek(), tmp);
			for(Integer i: tmp) {
				q.remove(i);
			}
			queues.enqueue(tmp);
		}
		Queue<Integer> biggest = queues.peek();
		for(Queue<Integer> tmp: queues) {
			if(tmp.size() > biggest.size()) {
				biggest = tmp;
			}
		}
		return biggest;
	}
	
	public Queue<String> minGraph() {
		WeightedGraph graph = new WeightedGraph(V);
		graph.reduce(biggestComponent());
		Queue<Edge> edges = new Queue<Edge>();
		for(int i = 0; i < V; i++) {
			for(Edge e: adj[i]) {
				edges.add(e);
			}
		}
		for(int i = 0; i < adj.length; i++) {
			graph.adj[i] = new Queue<Edge>();
		}
		while(!edges.isEmpty()) {
			Edge e = getSmallestEdge(edges);
			edges.remove(e);
			Queue<Integer> tmp = new Queue<Integer>();
			graph.getComponent(e.root, tmp);
			if(!tmp.contains(e.vertix)) {
				graph.addEdge(e.root, e.vertix, e.weight);
			}
		}
		for(int i = 0; i < graph.V(); i++) {
			for(Edge e: graph.adj[i]) {
				edges.add(e);
			}
		}
		
		Queue<String> rep = new Queue<String>();
		for(Edge e : edges) {
			rep.add(e.root+ " " + e.vertix +" " +e.weight);
		}
		return rep;
	}
	private Edge getSmallestEdge(Queue<Edge> q) {
		Edge e = q.peek();
		for(Edge edge:q) {
			if(edge.weight<e.weight) {
				e = edge;
			}
		}
		return e;
		
	}

	private void reduce(Queue<Integer> biggestComponent) {
		for(int i = 0; i < V; i++) {
			if(!biggestComponent.contains(i)) {
				adj[i] = new Queue<Edge>();
			}
		}
		
	}

	/**
	 * Initializes a WeightedGraph from the specified input stream. The format is
	 * the number of vertices <em>V</em>, followed by the number of edges
	 * <em>E</em>, followed by <em>E</em> pairs of vertices, with each entry
	 * separated by whitespace.
	 *
	 * @param in the input stream
	 * @throws IllegalArgumentException if the endpoints of any edge are not in
	 *                                  prescribed range
	 * @throws IllegalArgumentException if the number of vertices or edges is
	 *                                  negative
	 * @throws IllegalArgumentException if the input stream is in the wrong format
	 */
	public WeightedGraph(In in) {
		try {
			this.V = in.readInt();
			if (V < 0)
				throw new IllegalArgumentException("number of vertices in a WeightedGraph must be nonnegative");
			adj = (Queue<Edge>[]) new Queue[V];
			for (int v = 0; v < V; v++) {
				adj[v] = new Queue<Edge>();
			}
			int E = in.readInt();
			if (E < 0)
				throw new IllegalArgumentException("number of edges in a WeightedGraph must be nonnegative");
			for (int i = 0; i < E; i++) {
				int v = in.readInt();
				int w = in.readInt();
				validateVertex(v);
				validateVertex(w);
				addEdge(v, w);
			}
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("invalid input format in WeightedGraph constructor", e);
		}
	}

	/**
	 * Initializes a new WeightedGraph that is a deep copy of {@code G}.
	 *
	 * @param G the WeightedGraph to copy
	 */
	public WeightedGraph(WeightedGraph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < G.V(); v++) {
			// reverse so that adjacency list is in same order as original
			Stack<Integer> reverse = new Stack<Integer>();
			for (Edge e : G.adj[v]) {
				reverse.push(e.vertix);
			}
			for (int w : reverse) {
				adj[v].add(new Edge(v, w));
			}
		}
	}

	/**
	 * Returns the number of vertices in this WeightedGraph.
	 *
	 * @return the number of vertices in this WeightedGraph
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the number of edges in this WeightedGraph.
	 *
	 * @return the number of edges in this WeightedGraph
	 */
	public int E() {
		return E;
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
	}

	/**
	 * Adds the undirected edge v-w to this WeightedGraph.
	 *
	 * @param v one vertex in the edge
	 * @param w the other vertex in the edge
	 * @throws IllegalArgumentException unless both {@code 0 <= v < V} and
	 *                                  {@code 0 <= w < V}
	 */
	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		E++;
		adj[v].add(new Edge(v, w));
		adj[w].add(new Edge(w, v));
	}

	public void addEdge(int v, int w, double val) {
		validateVertex(v);
		validateVertex(w);
		E++;
		adj[v].add(new Edge(v, w, val));
		adj[w].add(new Edge(w, v, val));
	}

	/**
	 * Returns the vertices adjacent to vertex {@code v}.
	 *
	 * @param v the vertex
	 * @return the vertices adjacent to vertex {@code v}, as an iterable
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Iterable<Edge> adj(int v) {
		validateVertex(v);
		return adj[v];
	}
	public void addSingleEdge(int root, int target, double weight) {
		validateVertex(root);
		validateVertex(target);
		E++;
		adj[root].add(new Edge(root, target, weight));
	}
	public void addSingleEdge(int root, int target) {
		addSingleEdge(root, target,1);
	}
	/**
	 * Returns the degree of vertex {@code v}.
	 *
	 * @param v the vertex
	 * @return the degree of vertex {@code v}
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public int degree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	/**
	 * Returns a string representation of this WeightedGraph.
	 *
	 * @return the number of vertices <em>V</em>, followed by the number of edges
	 *         <em>E</em>, followed by the <em>V</em> adjacency lists
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " vertices, " + E + " edges " + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (Edge w : adj[v]) {
				s.append(w.vertix + " ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	/**
	 * Unit tests the {@code WeightedGraph} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		In in = new In(args[0]);
		WeightedGraph G = new WeightedGraph(in);
		StdOut.println(G);
	}

	public class Edge {
		double weight;
		Integer vertix;
		Integer root;
		public Edge(Integer root, Integer vertix) {
			this.vertix = vertix;
			weight = 1;
			this.root = root;
		}

		public Edge(Integer root, Integer vertix, double weight) {
			this.vertix = vertix;
			this.weight = weight;
			this.root = root;
		}
		@Override
		public boolean equals(Object o) {
			if(o instanceof Edge) {
				return equals((Edge) o);
			}
			return false;
		}
		public boolean equals(Edge e) {
			if(vertix == e.vertix && root == e.root && weight == e.weight) {
				return true;
			}
			if(root == e.vertix && vertix == e.root && weight == e.weight) {
				return true;
			}
			return false;
		}
	}
}
