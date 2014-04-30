package aiproj.fencemasterImpl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Player class represents the players in the game. each player will have 
 * a short name and a full name
 * 
 * @author Marisa Tjoe (566322) & Erlangga Satria Gama (570748)
 */
public class PlayerImpl {

	/* ATTRIBUTES */

	int piece;
	

	// Arraylist of Positions owned by this Player
	public ArrayList<Position> positions;

	// Positions that is isEdge and isNonCorner owned by this Player, will be
	// the starting point to finding the Tripod in the board
	public HashMap<String, ArrayList<Position>> startingPoints;

	/* PUBLIC CONSTRUCTOR */

	public PlayerImpl(int piece) {
		this.piece = piece;
		this.positions = new ArrayList<Position>();
		startingPoints = new HashMap<String, ArrayList<Position>>();

		// Initialize starting Points
		for (String dir : new String[] { "N", "NW", "NE", "S", "SW", "SE" }) {
			startingPoints.put(dir, new ArrayList<Position>());
		}
	}

	/**
	 * Add a Position of the position array, listing all the Positions that this
	 * Player are occupying
	 * 
	 * @param pos
	 */
	public void addPosition(Position pos, int n) {
		this.positions.add(pos);

		// Pre-set hash map startingPoints, whose function is to group together
		// the non-corner, edge positions that are on the same side.
		if (pos.isEdge && pos.isNonCorner) {
			// get which edge this position is in the board
			startingPoints.get(pos.getWhichEdge()).add(pos);

		}
	}
}
