package graphs;

public class DFS {
	public static void main(String[] args) {
		GraphModel graph = new GraphModel();
		while(StdIn.hasNextLine()) {
			String s = StdIn.readLine();
			graph.put(s.split("\\s+")[0], s.split("\\s+")[1]);
			graph.put(s.split("\\s+")[1], s.split("\\s+")[0]);
		}
		Queue<String> res = graph.dfs(args[0], args[1]);
		if(res.isEmpty()) {
			System.out.println("no such path");
		}else {
			System.out.print(res.dequeue());
			while(!res.isEmpty()) {
				System.out.print("->" + res.dequeue());	
			}
		}
	}
}
