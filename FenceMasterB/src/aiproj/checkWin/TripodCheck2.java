package aiproj.checkWin;

import java.util.ArrayList;

import aiproj.fencemasterImpl.BoardImpl;
import aiproj.fencemasterImpl.PlayerImpl;
import aiproj.fencemasterImpl.Position;

public class TripodCheck2 implements CheckLogic {
	BoardImpl board;
	ArrayList<Position> visited;
	ArrayList<String> visitedEdges;
	PlayerImpl currentPlayer;
			
	public TripodCheck2(BoardImpl b){
		this.board = b;
		this.visitedEdges = new ArrayList<String>();
	}
	
	@Override
	public PlayerImpl check() {
		visited = new ArrayList<Position>();
		
		// Loop through each player
		for (PlayerImpl p : board.getPlayerImpls()) {
			
			currentPlayer = p;
			
			// Loop through each position that the player is occupying
			for (Position pos : p.positions) {
				if (!visited.contains(pos)) {
					this.visitedEdges.clear();
					if (DFS(pos, null) == true) {
						return p;
					}
				}
			}
		}
		return null;
	}
	
	public boolean DFS(Position pos, Position prev) {
		visited.add(pos);
		
		if (pos.isEdge && pos.isNonCorner) {
			if (!visitedEdges.contains(pos.getWhichEdge())) {
				visitedEdges.add(pos.getWhichEdge());
			}
		}
		
		if (visitedEdges.size() == 3) {
			return true;
		}

		// Loop through all the other neighbors of pos
		for (Position neighbor : pos.getSameNeighbors(prev)) {

			if (!visited.contains(neighbor)) {
				// If neighbor is already visited, apply DFS to it
				if (DFS(neighbor, pos)) {
					return true;
				}
			}
		}
		return false;
	}
}
