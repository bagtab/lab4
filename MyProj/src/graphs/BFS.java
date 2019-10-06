package graphs;

public class BFS {
	public static void main(String[] args) {
		GraphModel graph = new GraphModel();
		while(StdIn.hasNextLine()) {
			String s = StdIn.readLine();
			graph.put(s.split("\\s+")[0], s.split("\\s+")[1]);
			graph.put(s.split("\\s+")[1], s.split("\\s+")[0]);
		}
		System.out.print(graph.bfs(args[0], args[1]));
	}
}
