package graphs;

public class GraphModel {
	SeparateChainingHashST<String, Queue<String>> graph;

	public GraphModel() {
		graph = new SeparateChainingHashST<String, Queue<String>>();
	}
	public Queue<String> getValues(String key) {
		return graph.get(key);
	}

	public void put(String key, String value) {
		if (graph.contains(key)) {
			Queue<String> queue = graph.get(key);
			queue.enqueue(value);
		}else {
			Queue<String> queue = new Queue<String>();
			queue.enqueue(value);
			graph.put(key, queue);
		}
	}

	public Queue<String> dfs(String start, String end) {
		Queue<String> banList = new Queue<String>();
		Queue<String> retList = new Queue<String>();
		dfs(start, end, retList, banList);
		return retList;
	}

	private void dfs(String start, String end, Queue<String> retList, Queue<String> banList) {
		retList.enqueue(start);
		banList.enqueue(start);
		if (start.equals(end)) {
			return;
		}
		for (String val : getValues(start)) {
			if (!banList.contains(val)) {
				dfs(val, end, retList, banList);
				if (retList.contains(end)) {
					return;
				}
			}
		}
		retList.pop();
	}
	public String bfs(String start, String end) {
		Queue<String> q = new Queue<String>();
		Queue<String> banList = new Queue<String>();
		SeparateChainingHashST<String, String> path = new SeparateChainingHashST<String, String>();
		q.enqueue(start);
		banList.enqueue(start);
		path.put(start, "");
		while(!q.isEmpty()) {
			String search = q.dequeue();
			for(String v:graph.get(search)) {
				if(!banList.contains(v)) {
					path.put(v, path.get(search) + "->" + search);
					q.enqueue(v);
					banList.enqueue(v);
				}
			}
		}
		if(path.contains(end)) {
			return (path.get(end) + "->" + end).substring(2);
		}
		return "no such path";
	}
}
