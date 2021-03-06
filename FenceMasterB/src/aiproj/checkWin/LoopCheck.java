package aiproj.checkWin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import aiproj.fencemasterImpl.BoardImpl;
import aiproj.fencemasterImpl.PlayerImpl;
import aiproj.fencemasterImpl.Position;

/**
 * Contains algorithm to check whether there are any loops in the given board.
 * 
 * @author Marisa Tjoe (566322) & Erlangga Satria Gama (570748)
 */
public class LoopCheck implements CheckLogic {
	// Board that needs loop-checking
	BoardImpl b; 
	
	// Keeps track of the visited Positions
	ArrayList<Position> visited; 
	
	// Keeps track of the current path taken by the graph searching algorithm
	LinkedList<Position> currentPath;
	
	// The player currently being checked
	PlayerImpl currentPlayer; 

	/* PUBLIC CONSTRUCTOR */

	public LoopCheck(BoardImpl b) {
		this.b = b;
	}

	/* MAIN METHODS */
	
	/**
	 * @return the winning Player is there is one, null if there is no winner
	 */
	public PlayerImpl check() {
		visited = new ArrayList<Position>();
		currentPath = new LinkedList<Position>();

		// Loop through each player
		for (PlayerImpl p : b.getPlayerImpls()) {
			currentPlayer = p;
			
			// Loop through each position that the player is occupying
			for (Position pos : p.positions) {
				if (!visited.contains(pos)) {
					if (DFS(pos, null) == true) {
						return p;
					}
				}
			}
		}
		return null;
	}

	/* HELPER METHODS */
	
	/**
	 * Recursive algorithm that will search through the board (graph form),
	 * while checking for loops and their validity
	 * 
	 * @return true if there is a loop in the board, false otherwise
	 */
	private boolean DFS(Position pos, Position prev) {
		visited.add(pos);
		currentPath.addLast(pos);

		// Loop through all the other neighbors of pos
		for (Position neighbor : pos.getSameNeighbors(prev)) {

			// If the current position has a neighbor that was visited before,
			// might be a loop and minimum of 5 Positions to form a valid loop
			if (visited.contains(neighbor)) {
				if ((currentPath.contains(neighbor)) && ((currentPath.indexOf(pos) - currentPath
						.indexOf(neighbor)) >= 5)) {
					
					// Make a List of Positions, containing all the
					// Positions in the suspected loop
					List<Position> currentLoop = currentPath.subList(
							currentPath.indexOf(neighbor),
							currentPath.indexOf(pos) + 1);

					// Checks whether center Position(s) is/are either owned
					// by a different player or is/are empty. If it is,
					// return true
					if (currentLoop.size() > 5){
						if (centerDifferent(currentLoop)) {
							return true;
						}
					}
					
				}
			} else {
				// If neighbor is already visited, apply DFS to it
				if (DFS(neighbor, pos)) {
					return true;
				}
			}
		}

		currentPath.remove(pos);
		return false;
	}

	/**
	 * Given List of Positions that forms a loop, checks whether the center of
	 * the loop is either empty or owned by a different player
	 * 
	 * @return true if center of currentLoop contains either an empty Position
	 *         or a Position owned by a different player
	 */
	private boolean centerDifferent(List<Position> currentLoop) {
		
		currentLoop = neatifyLoop(currentLoop);
		
		if (currentLoop == null) {
			return false;
		}

		// rowCurrentPath is a HashMap representation of the currentLoop, which
		// will map the x coordinates to a LinkedList containing the y
		// coordinates of all the Positions in the currentLoop
		HashMap<Integer, LinkedList<Integer>> rowCurrentPath = new HashMap<Integer, LinkedList<Integer>>();

		int minX = (2 * this.b.getN()) - 1;
		int maxX = 0;

		for (Position pos : currentLoop) {

			// Set up minX and maxX
			if (pos.getX() < minX) {
				minX = pos.getX();
			}

			if (pos.getX() > maxX) {
				maxX = pos.getX();
			}

			// Set up rowCurrentPath
			if (rowCurrentPath.containsKey(pos.getX())) {
				if (pos.getY() < rowCurrentPath.get(pos.getX()).getFirst()) {
					rowCurrentPath.get(pos.getX()).addFirst(pos.getY());
				} else {
					rowCurrentPath.get(pos.getX()).addLast(pos.getY());
				}
			} else {
				LinkedList<Integer> yPos = new LinkedList<Integer>();
				yPos.add(pos.getY());
				rowCurrentPath.put(pos.getX(), yPos);
			}
		}

		// Loop per row
		for (int currRow : rowCurrentPath.keySet()) {

			// Exclude checking of the upper-most and lower-most rows
			if ((currRow != minX) && (currRow != maxX)) {

				// Return true if there is a gap in between the leftmost
				// Position and the next loop Position in the row, and the gap
				// is either empty or owned by another player
				Position currColPos = this.b.getPosition(currRow,
						rowCurrentPath.get(currRow).getFirst()).getNeighborInDir("E");
				
				if (currColPos == null) {
					return false;
				}

				while (!(rowCurrentPath.get(currRow)
						.contains(currColPos.getY()))) {
					if (currColPos.isEmpty()
							|| !currColPos.getOwner()
									.equals(this.currentPlayer)) {
						return true;
					}
					currColPos = currColPos.getNeighborInDir("E");
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Prevent convoluted loop. Makes 3 cluttered adjacent positions reduced to only 2.
	 * @return new neatified loopArray
	 */
	private List<Position> neatifyLoop(List<Position> loopArray){
		int nCurr = 2;
		int size = loopArray.size();
		
		while (nCurr < size){
			while (b.isAdjacent(loopArray.get((nCurr < 0) ? (size + nCurr - 1) : nCurr), loopArray.get((nCurr < 2) ? (size + nCurr - 3): (nCurr-2)))) {
				loopArray.remove((nCurr <= 0)? (size+nCurr-2):(nCurr-1));
				size = loopArray.size();
				nCurr--;
				if (size <= 3) {
					
					return null;
				}
			}
			nCurr++;
		}
		return loopArray;
	}
}
