package ch.zhaw.pm2.racetrack.pathfinder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.PositionVector;


public class Node<T> implements Comparable<Node<T>> {
	private final PositionVector position;
	private Integer distance = Integer.MAX_VALUE;
	private List<Node<T>> shortestPath = new LinkedList<>();
	private Map<Node, Integer> adjacentNodes = new HashMap<>();

	public Node(PositionVector position) {
		this.position = position;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public List<Node<T>> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node<T>> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public Map<Node, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public PositionVector getPosition() {
		return position;
	}

	public void addAdjacentNode(Node<T> node, int weight) {
		adjacentNodes.put(node, weight);
	}

	@Override
	public int compareTo(Node node) {
		return Integer.compare(distance, node.getDistance());
	}
}
