package ch.zhaw.pm2.racetrack.pathfinder;

import java.util.*;

import ch.zhaw.pm2.racetrack.Track;
import model.Direction;
import model.PositionVector;
import model.SpaceType;

public class PathFinder {
	List<Node<PositionVector>> nodes = new ArrayList<>();
	private Track track;

	public PathFinder(PositionVector startPosition, Track racetrack) {
		Node<PositionVector> start = new Node(startPosition);
		nodes.add(start);
		this.track = racetrack;
		setNodes();
		settleNodes(start);
	}

	private void settleNodes(Node<PositionVector> start) {
		new Dijkstra<PositionVector>().calculateShortestPaths(start);
	}

	private void setNodes() {
		PositionVector candidate;
		for (int y = 0; y < track.getFields().size(); y++) {
			for (int x = 0; x < track.getFields().get(y).size(); x++) {
				candidate = new PositionVector(x, y);
				if (isNode(candidate)) {
					nodes.add(new Node<PositionVector>(candidate));
				}
			}
		}
		blockWrongPath();
		setNeighbouringNodes();
	}

	private boolean isNode(PositionVector canidate) {
		if (track.getSpaceTypeAtPosition(canidate) == SpaceType.TRACK
				|| isFinishLine(track.getSpaceTypeAtPosition(canidate))) {
			return true;
		}
		return false;
	}

	private boolean isFinishLine(SpaceType spaceType) {
		return (spaceType == SpaceType.FINISH_DOWN || spaceType == SpaceType.FINISH_LEFT
				|| spaceType == SpaceType.FINISH_RIGHT || spaceType == SpaceType.FINISH_UP);
	}
	
	private void blockWrongPath() {
		List<PositionVector> wall = new ArrayList<>();
		for (int y = 0; y < track.getFields().size(); y++ ) {
			for (int x = 0; x < track.getFields().get(y).size(); x++) {
				if (track.getCharRepresentationAtPosition(y, x) == SpaceType.FINISH_RIGHT.getSpaceChar()){
					wall.add(new PositionVector(x+1, y));
				} else if (track.getCharRepresentationAtPosition(y, x) == SpaceType.FINISH_LEFT.getSpaceChar()) {
					wall.add(new PositionVector(x-1, y));
				} else if (track.getCharRepresentationAtPosition(y, x) == SpaceType.FINISH_UP.getSpaceChar()) {
					wall.add(new PositionVector(x, y-1));
				} else if (track.getCharRepresentationAtPosition(y, x) == SpaceType.FINISH_DOWN.getSpaceChar()) {
					wall.add(new PositionVector(x, y+1));
				}
			}
		}

		wall.forEach(wallPositionVector -> {
			Iterator<Node<PositionVector>> nodeIterator = nodes.iterator();
			while (nodeIterator.hasNext()) {
				Node<PositionVector> node = nodeIterator.next();
				if (node.getPosition().getX() == wallPositionVector.getX() && node.getPosition().getY() == wallPositionVector.getY()) {
					nodeIterator.remove();
				}
			}
		});
	}
	
	private void setNeighbouringNodes() {
		for (Node<PositionVector> node : nodes) {
			for (Direction direction : Direction.values()) {
				for (Node<PositionVector> candidate : nodes) {
					if (candidate.getPosition().equals(node.getPosition().add(direction.vector))) {
						node.addAdjacentNode(candidate, 1);
					}
				}
			}
		}
	}
	
	/**
	 * This method looks for the shortest path to reach a finish line in the correct direction, starting from the provided start position
	 * @return List<PositionVector> containing every waypoint of the shortest path
	 */
	public List<PositionVector> getShortestPath() {
		List<List<Node<PositionVector>>> finishPaths = new ArrayList<>();
		for (Node<PositionVector> node : nodes) {
			if (isFinishLine(track.getSpaceTypeAtPosition(node.getPosition()))) {
				finishPaths.add(node.getShortestPath());
			}
		}
		return translateToPositionVectorList(getShortest(finishPaths));
	}

	private List<Node<PositionVector>> getShortest(List<List<Node<PositionVector>>> finishPaths) {
		List<Node<PositionVector>> shortest = finishPaths.get(0);
		for (List<Node<PositionVector>> candidate : finishPaths) {
			if (candidate.size() < shortest.size()) {
				shortest = candidate;
			}
		}
		return shortest;
	}
	
	private List<PositionVector> translateToPositionVectorList(List<Node<PositionVector>> list) {
		List<PositionVector> positions = new ArrayList<>();
		for (Node<PositionVector> node : list) {
			positions.add(node.getPosition());
		}
		return positions;
	}
}
