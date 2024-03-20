package ch.zhaw.pm2.racetrack.pathfinder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class Dijkstra<T> {

	public void calculateShortestPaths(Node<T> source) {
		source.setDistance(0);
		Set<Node<T>> settledNodes = new HashSet<>();
		Queue<Node<T>> unsettledNodes = new PriorityQueue<>(Collections.singleton(source));
		while (!unsettledNodes.isEmpty()) {
			Node<T> currentNode = unsettledNodes.poll();

			for (Entry<Node, Integer> entry : currentNode.getAdjacentNodes().entrySet()) {
				if (!settledNodes.contains(entry.getKey())) {
					evaluateShortestPath(entry.getKey(), entry.getValue(), currentNode);
					unsettledNodes.add(entry.getKey());
				}
			}
			settledNodes.add(currentNode);
		}
	}

	private void evaluateShortestPath(Node<T> adjacentNode, Integer distance, Node<T> sourceNode) {
		Integer newDistance = sourceNode.getDistance() + distance;
		if (newDistance < adjacentNode.getDistance()) {
			adjacentNode.setDistance(newDistance);
			List<Node<T>> newShortest = sourceNode.getShortestPath();
			newShortest.add(sourceNode);
			adjacentNode.setShortestPath(newShortest);
		}
	}
}
