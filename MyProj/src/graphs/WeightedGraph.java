package graphs;

import java.util.NoSuchElementException;
/**
 * heavily modified version of Graph from the book. Comments for already implemented 
 * functions are pretty much the same, but the functions might have been modified to implement weights
 *  
 * @author mrjoh
 *
 */
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
	 * Queue with path, start at top, end at bottom, empty if no such path
	 */
	public Queue<Integer> dfs(int start, int end) {
		int[] prev = new int[V];
		// sets every nodes' start value to -1
		for (int i = 0; i < prev.length; i++) {
			prev[i] = -1;
		}
		boolean[] visited = new boolean[V];
		// return Queue
		Queue<Integer> st = new Queue<Integer>();
		// if there is a path, put it on the return Queue
		if (dfs(start, end, visited, prev)) {
			int tmp = end;
			do {
				st.push(tmp);
				tmp = prev[tmp];
			} while (tmp != -1);
		}
		return st;
	}

	/**
	 * finds path from node v to node end through dfs
	 * 
	 * @param v       - start node
	 * @param end     - end node
	 * @param visited - boolean array for visited nodes
	 * @param prev    - int array for prev node
	 * @return true if path exists, else false
	 */
	private boolean dfs(int v, int end, boolean[] visited, int[] prev) {
		// sets v as visited
		visited[v] = true;
		// if v is end node, return true
		if (v == end) {
			return true;
		}
		// recursively call nodes connected to v with dfs-
		else {
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

	/**
	 * finds shortest path from node v to node end through dfs
	 * 
	 * @param start - start node
	 * @param end   - end node
	 * @return Queue with path from start to finish
	 */
	public Queue<Integer> bfs(int start, int end) {
		// queue used for order of bfs
		Queue<Integer> q = new Queue<Integer>();
		// Queue used in return
		Queue<Integer> st = new Queue<Integer>();
		boolean[] visited = new boolean[V];
		int[] prev = new int[V];
		// sets every nodes' start value to -1
		for (int i = 0; i < prev.length; i++) {
			prev[i] = -1;
		}
		// adds startvalue to queue
		visited[start] = true;
		q.enqueue(start);
		while (!q.isEmpty()) {
			// dfs algorithm loop
			Integer v = q.dequeue();
			if (v == end) {
				//ending loop if end node found
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
	/**
	 * calculates shortest weighted path from start node to end if path exists
	 * @param start
	 * @param end
	 * @return Queue containing path from start to end and total weight
	 */
	public Queue<String> dijkstra(int start, int end) {
		//array loging distance to start
		double[] dist = new double[V];
		//array logging previous node
		int[] prev = new int[V];
		//queue for nodes in graph
		Queue<Integer> q = new Queue<Integer>();
		for (int i = 0; i < V; i++) {
			//adds all nodes, sets distance to max value and previous node to -1 as null node
			q.enqueue(i);
			dist[i] = Double.MAX_VALUE;
			prev[i] = -1;
		}
		//sets distance for root node to 0
		dist[start] = 0;
		//dijkstra alghorithm
		while (!q.isEmpty()) {
			//finds node in q with smallest distance
			Integer v = q.peek();
			for (Integer i : q) {
				if (dist[i] < dist[v]) {
					v = i;
				}
			}
			q.remove(v);
			//sets new distance for all adjacent nodes to node v
			for (Edge e : adj[v]) {
				double weight = e.weight + dist[v];
				if (dist[e.vertix] > weight) {
					dist[e.vertix] = weight;
					prev[e.vertix] = v;
				}
			}
		}
		Queue<String> str = new Queue<String>();
		Integer v = end;
		//prepares Queue with path and total weight
		do {
			str.push(v + "");
			v = prev[v];
		} while (v != -1);
		str.push(dist[end] + "");
		return str;
	}
	/**
	 * finds component from Node node, and adds all nodes in component to added
	 * @param node - node from which we look for component
	 * @param added - queue with added nodes
	 */
	public void getComponent(Integer node, Queue<Integer> added) {
		added.enqueue(node);
		for (Edge e : adj[node]) {
			Integer i = e.vertix;
			if (!added.contains(i)) {
				getComponent(i, added);
			}
		}
	}
	/**
	 * checks if there is a path from <Node corresponding to start> to <Node corresponding to goal>
	 * @param start - Integer corresponding to start node
	 * @param goal - Integer corresponding to end node
	 * @return <true> if a path exists from path to goal, else <false>
	 */
	public boolean checkPath(Integer start, Integer goal) {
		Queue<Integer> queue = new Queue<Integer>();
		getComponent(start, queue);
		return queue.contains(goal);
	}
	/**
	 * 
	 * @return
	 */
	public Queue<Queue<Integer>> checkCyclePaths() {
		Queue<Queue<Integer>> queue = new Queue<Queue<Integer>>();
		for (int i = 0; i < V; i++) {
			for (Edge e : adj[i]) {
				if (checkPath(e.vertix, i)) {
					Queue<Integer> tmp = dfs(e.vertix, i);
					if(!tmp.sameOrder(queue)) {
						tmp.add(i);
						queue.add(tmp);						
					}
				}
			}
		}
		return queue;
	}
	/**
	 * extracts the biggest component and returns it in a queue.
	 * @return Queue<Integer> q with all nodes belonging to biggest component
	 */
	public Queue<Integer> biggestComponent() {
		Queue<Integer> q = new Queue<Integer>();
		for (int i = 0; i < V; i++) {
			q.enqueue(i);
		}
		Queue<Queue<Integer>> queues = new Queue<Queue<Integer>>();
		while (!q.isEmpty()) {
			Queue<Integer> tmp = new Queue<Integer>();
			getComponent(q.peek(), tmp);
			for (Integer i : tmp) {
				q.remove(i);
			}
			queues.enqueue(tmp);
		}
		Queue<Integer> biggest = queues.peek();
		for (Queue<Integer> tmp : queues) {
			if (tmp.size() > biggest.size()) {
				biggest = tmp;
			}
		}
		return biggest;
	}
	/**
	 * creates the minimal graph from the biggest component in the graph.
	 * @return Queue<String> with the edges in the minimal graph of the biggest component
	 */
	public Queue<String> minGraph() {
		//create a copy of our current graph
		WeightedGraph graph = new WeightedGraph(this);
		//reduces the graph with its smallest component
		graph.reduce(biggestComponent());
		Queue<Edge> edges = new Queue<Edge>();
		for (int i = 0; i < V; i++) {
			// add all edges to a queue of edges
			for (Edge e : graph.adj[i]) {
				edges.add(e);
			}
		}
		for (int i = 0; i < adj.length; i++) {
			// resets
			graph.adj[i] = new Queue<Edge>();
		}
		while (!edges.isEmpty()) {
			Edge e = getSmallestEdge(edges);
			edges.remove(e);
			Queue<Integer> tmp = new Queue<Integer>();
			graph.getComponent(e.root, tmp);
			if (!tmp.contains(e.vertix)) {
				graph.addEdge(e.root, e.vertix, e.weight);
			}
		}
		for (int i = 0; i < graph.V(); i++) {
			for (Edge e : graph.adj[i]) {
				edges.add(e);
			}
		}

		Queue<String> rep = new Queue<String>();
		for (Edge e : edges) {
			rep.add(e.root + " " + e.vertix + " " + e.weight);
		}
		return rep;
	}
	/**
	 * loops through a queue of edges and returns the smallest edge
	 * @param q - queue with edges
	 * @return Edge with smallest weight, i.e. the smallest edge
	 */
	private Edge getSmallestEdge(Queue<Edge> q) {
		Edge e = q.peek();
		for (Edge edge : q) {
			if (edge.weight < e.weight) {
				e = edge;
			}
		}
		return e;

	}
	public Queue<Integer> topologic(){
		Queue<Integer> permaBan = new Queue<Integer>();
		Queue<Integer> tempBan = new Queue<Integer>();
		Queue<Integer> nodes = new Queue<Integer>();
		for(Integer i = 0; i < V; i++) {
			nodes.add(i);
		}
		while(!nodes.isEmpty()) {
			visit(nodes.peek(), nodes, tempBan, permaBan);
		}
		return permaBan;
	}
	private void visit(Integer pop, Queue<Integer> nodes, Queue<Integer> tempBan, Queue<Integer> permaBan) {
		// TODO Auto-generated method stub
		if(nodes.contains(pop)) {
			nodes.remove(pop);
		}
		if(permaBan.contains(pop)) {
			return;
		}
		if(tempBan.contains(pop)) {
			System.out.println("Graph has cycle, break");
			 while(!nodes.isEmpty()) {
				 nodes.pop();
			 }
			 while(!permaBan.isEmpty()) {
				 permaBan.pop();
			 }
			 return;
		}
		tempBan.add(pop);
		for(Edge e: adj[pop]) {
			visit(e.vertix,nodes,tempBan,permaBan);
		}
		tempBan.remove(pop);
		permaBan.add(pop);
	}

	/**
	 L <- Empty list that will contain the sorted nodes
	while exists nodes without a permanent mark do
    select an unmarked node n
    visit(n)
	function visit(node n)
    if n has a permanent mark then return
    if n has a temporary mark then stop   (not a DAG)
    mark n with a temporary mark
    for each node m with an edge from n to m do
        visit(m)
    remove temporary mark from n
    mark n with a permanent mark
    add n to head of L
    */
	/**
	 * removes all edges from nodes not belonging to biggestComponent
	 * @param biggestComponent - Queue with integers belonging to a separate component
	 */
	private void reduce(Queue<Integer> biggestComponent) {
		for (int i = 0; i < V; i++) {
			if (!biggestComponent.contains(i)) {
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
			Queue<Edge> reverse = new Queue<Edge>();
			for (Edge e : G.adj[v]) {
				reverse.push(e);
			}
			for (Edge e : reverse) {
				adj[v].add(new Edge(v, e.vertix, e.weight));
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
		addSingleEdge(root, target, 1);
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
	/**
	 * privately made edge class with all things neede for this assignment
	 * @author mrjoh
	 *
	 */
	public class Edge {
		double weight;
		Integer vertix;
		Integer root;
		/**
		 * Creates Edge with standard weight 1
		 * @param root
		 * @param vertix
		 */
		public Edge(Integer root, Integer vertix) {
			this.vertix = vertix;
			weight = 1;
			this.root = root;
		}
		/**
		 * Standard constructor
		 * @param root
		 * @param vertix
		 * @param weight
		 */
		public Edge(Integer root, Integer vertix, double weight) {
			this.vertix = vertix;
			this.weight = weight;
			this.root = root;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Edge) {
				return equals((Edge) o);
			}
			return false;
		}

		public boolean equals(Edge e) {
			if (vertix == e.vertix && root == e.root && weight == e.weight) {
				return true;
			}
			if (root == e.vertix && vertix == e.root && weight == e.weight) {
				return true;
			}
			return false;
		}
	}
}
