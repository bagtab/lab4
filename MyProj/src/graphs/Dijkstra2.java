package graphs;

public class Dijkstra2 {
	public static void main(String[] args) {
		//creates a mapping from a state name to a number and vice versa
		SequentialSearchST<String, Integer> stringToInt = new SequentialSearchST<String, Integer>();
		SequentialSearchST<Integer, String> intToString = new SequentialSearchST<Integer, String>();
		String[] edges = StdIn.readAllLines();
		int nr = 0;
		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < 2; j++) {
				String str = edges[i].split("\\s+")[j];
				if (!stringToInt.contains(str)) {
					stringToInt.put(str, nr);
					intToString.put(nr, str);
					nr++;
				}
			}
		}
		//creates graph and adds edges
		WeightedGraph g = new WeightedGraph(nr);
		for (int i = 0; i < edges.length; i++) {
			String s1 = edges[i].split("\\s+")[0];
			String s2 = edges[i].split("\\s+")[1];
			g.addEdge(stringToInt.get(s1), stringToInt.get(s2), i+1);
		}
		//starts dijkstra search for shortest path
		Queue<String> st = g.dijkstra(stringToInt.get(args[0]), stringToInt.get(args[1]));
		String out = "";
		out+= "distance from " +args[0]+ " to " +args[1] + "is " + st.pop() + "\n";
		if (!st.isEmpty()) {
			out += intToString.get(Integer.parseInt(st.pop()));
		}
		while (!st.isEmpty()) {
			out += "->" + intToString.get(Integer.parseInt(st.pop()));
		}
		//prints string representation of shortest path
		System.out.println(out);
	}

}
