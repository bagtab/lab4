package graphs;
public class ConfirmEdge {
	public static void main(String[] args) {
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
		WeightedGraph g = new WeightedGraph(nr);
		for (int i = 0; i < edges.length; i++) {
			String s1 = edges[i].split("\\s+")[0];
			String s2 = edges[i].split("\\s+")[1];
			g.addSingleEdge(stringToInt.get(s1), stringToInt.get(s2));
		}
		if(g.checkPath(stringToInt.get(args[0]), stringToInt.get(args[1]))) {
			System.out.println("there is a path between " + args[0] + " and " + args[1]);
		}else {
			System.out.println("there is not a path between " + args[0] + " and " + args[1]);			
		}
	}
}
