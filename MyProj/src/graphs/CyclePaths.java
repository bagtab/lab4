package graphs;

public class CyclePaths {
	public static void main(String[] args) {
		// creates a mapping from a state name to a number and vice versa
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
		// creates graph and adds edges
		WeightedGraph g = new WeightedGraph(nr);
		for (int i = 0; i < edges.length; i++) {
			String s1 = edges[i].split("\\s+")[0];
			String s2 = edges[i].split("\\s+")[1];
			g.addSingleEdge(stringToInt.get(s1), stringToInt.get(s2));
		}
		//adds edge that creates a cyclePath
		g.addSingleEdge(30, 0);
		//
		Queue<Queue<Integer>> q = g.checkCyclePaths();
		for (Queue<Integer> s : q) {
			String str = intToString.get(s.dequeue());
			for (Integer i : s) {
				str = intToString.get(i) + " " + str;
			}
			// prints result 
			System.out.println(str);
		}
	}
}
