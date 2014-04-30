package aiproj.checkWin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import aiproj.fencemasterImpl.*;

/**
 * Contains algorithm to check whether there are any tripods in the given BoardImpl.
 * 
 * @author Marisa Tjoe (566322) & Erlangga Satria Gama (570748)
 */
public class TripodCheck implements CheckLogic{
	BoardImpl b;
	
	ArrayList<Position> visited = new ArrayList<Position>();
	LinkedList<Position> queue = new LinkedList<Position>();
	HashMap<String, ArrayList<Position>> startingPoints = new HashMap<String, 
													   ArrayList<Position>>();
	ArrayList<String> visitedEdge;
	int tripod = 1;
	int minimTripod = 3;
	
	public TripodCheck(BoardImpl b){
		this.b = b;
		visitedEdge = new ArrayList<String>();
	}

	/**
	* @return The last element in queue, or null if there is nothing in queue
	*/
	private Position getCurrentFromQueue() {
		Position current;

		if (this.queue.size() > 0) {
			current = this.queue.getLast();
			return current;

		} else {
			return null;
		}
	}

	/**
	* @return Current node, if the neighbors is already visited before. 
	* Otherwise, return the neighbors node for further exploration
	* in runDFS function. Return null, if the goal state is found and there
	* is nothing in queue or tripod is found.
	*/
	private Position checkCurrent(Position neighbors, Position current) {
		Position result = current;

		/* If not yet visited, mark it as visited*/
		if (!this.visited.contains(neighbors)) {
			this.visited.add(neighbors);

			/* Check if the neighbor is the edge non-corner*/
			if (neighbors.isEdge && neighbors.isNonCorner) {
				
				/*
				* If it is in the same other side of the BoardImpl, check the 
				* queue
				*/
				if (this.visitedEdge.contains(neighbors.getWhichEdge())) {
					
					/*
					* If current is the edge, then explore the neighbor to 
					* find other neighbors from this point
					*/
					if (current.isEdge) {
						result = neighbors;
					
					/*
					* If current is not at the edge, then find the last 
					* position from the queue
					*/	
					} else {
						result =  getCurrentFromQueue();
					}
				
				/* If not, then it must be the goal state */	
				} else {
					this.tripod++;
					this.visitedEdge.add(neighbors.getWhichEdge());
					
					/* No node to check*/	
					if (this.tripod == this.minimTripod) {
						result =  null;

					/* Check the queue*/
					} else {	
						result =  getCurrentFromQueue();
					}
				}

			/* If it is not the edge, return it for further exploration */	
			} else {
				result =  neighbors;
			}
		}

		/* Return current, to find other unvisited node */	
		return result;	
	}
	
	/**
	 * @return True if there is a tripod, false if not 
	 */
	private boolean runDFS(Position pos) {
		
		int numLoop = 0;
		Position before = null;
		Position current = pos;
		Position neighborPoint = null;
		Position currentBe = current;
		

		/* 
		* Keep looping when there is something to read and tripod is not yet 
		* found
		*/
		while (current != null && this.tripod != 3) {
			
			numLoop = 0;
			visited.add(current);
			
			/* Get the array of the same neighbor */
			ArrayList<Position> neighborsArray = current.getSameNeighbors(
																	  before);
			
			/* If the same neighbor only 1 */
			if (neighborsArray.size() == 1) {
					neighborPoint = neighborsArray.get(0);
					currentBe = checkCurrent(neighborPoint, current);
					
			/* 
			* If the neighbors are more than 1, put the current in queue for 
			* later check, then find the unvisited neighbor and explore it
			*/	
			} else if (neighborsArray.size() > 1){			
				
				/* If the node was put in queue already, chop it from queue */
				if (!this.queue.contains(current)) {
					this.queue.addLast(current);
				} else {
					this.queue.remove(current);
				}
				
				/* Find the unvisited neighbor */
				for (Position neighbors:neighborsArray) {

					currentBe = checkCurrent(neighbors, current);
					numLoop++;

					if (currentBe != current) {
						break;
					
					/*
					* To avoid the cycle of the loop, find the node in the 
					* queue for further checking when there is no unvisited 
					* node
					*/
					} else if (currentBe == current && 
						 				   numLoop == neighborsArray.size()) {
						
						/* If the existing node is in the queue, remove it
						 * so that it is not exploring the same node again
						 */ 
						if (this.queue.contains(currentBe)) {
							this.queue.remove(currentBe);
						}

						currentBe = getCurrentFromQueue();
						break;
					}		
				}
				
			/* 
			* If there is no same neighbor, find node in the queue if there 
			* is node in queue, if not, then stop 
			*/
			} else if (neighborsArray.size() == 0) {
				currentBe = getCurrentFromQueue();
				
				if (currentBe == null) {
					break;
				}
			} 
			
			/* Store the current node, and move it to the neighbor */
			before = current;
			current = currentBe;
				
		} 
		/* If tripod is found, return true */
		if (this.tripod == 3) {
			return true;
		
		} else {
			return false;
		}
	}
	
	/**
	 * @return The PlayerImpl that wins the game with Tripod, or null if no winner 
	 * is found
	 */
	public PlayerImpl check() {	
		
		/* Loop through each PlayerImpl in the BoardImpl */
		for (PlayerImpl player:b.getPlayerImpls()) {
			startingPoints = player.startingPoints;
			tripod = 1;
			this.visitedEdge.clear();
			this.visited.clear();	
			this.queue.clear();

			/* Loop through each edge non-corner side in the BoardImpl */
			for (Entry<String, ArrayList<Position>> whichEdge:
				    	 					      startingPoints.entrySet()) {
				
				if (!this.visitedEdge.contains(whichEdge.getKey())) {
					this.visitedEdge.add(whichEdge.getKey());

					/* Check if there is any PlayerImpl in the current side */
					if (whichEdge.getValue().size() > 0) {
						
						/* 
						* Get the position in the edge non-corner of the 
						* PlayerImpl
						*/
						for (Position pos:whichEdge.getValue()) {
							
							/* 
							* If not yet visited, run the search to explore 
							* the nodes
							*/
							if (!this.visited.contains(pos)) {
								
								if (runDFS(pos)) {
									return player;
						
								}
							}		
						}
					}
				}
			} 
		}
		return null;
	}
}

