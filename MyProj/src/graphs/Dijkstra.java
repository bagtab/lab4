package graphs;

public class Dijkstra {
	public static void main(String[] args) {
		GraphModelBetter graph = new GraphModelBetter();
		int i = 1;
		while(StdIn.hasNextLine()) {
			String s = StdIn.readLine();
			graph.put(s.split("\\s+")[0], s.split("\\s+")[1],i);
			graph.put(s.split("\\s+")[1], s.split("\\s+")[0],i);
			i++;
		}
		System.out.print(graph.dijkstras(args[0], args[1]));
	}
}