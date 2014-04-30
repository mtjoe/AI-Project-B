package aiproj.fencemasterImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Correspond to a slot in a BoardImpl. The objects of Type BoardImpl consist of 
 * many different positions.
 * 
 * @author Marisa Tjoe (566322) & Erlangga Satria Gama (570748)
 */
public class Position {

	/* ATTRIBUTES */

	// Relating to the main BoardImpl
	private BoardImpl b;
	private int n;

	// To signify the coordinates of this Position on the BoardImpl
	private int x, y;

	// Player that is in this BoardImpl, null if empty
	private PlayerImpl owner;

	// isEdge is true if this position is located on the Edge of the BoardImpl, and
	public boolean isEdge;
	public boolean isNonCorner;

	// Maps direction of neighbor to the Position the neighbor is in
	private HashMap<String, Position> neighbors;
	
	// If isEdge, the direction of the edge in the BoardImpl 
	private String whichEdge;

	/* PUBLIC CONSTRUCTOR */

	public Position(BoardImpl b, int x, int y) {
		this.b = b;
		this.x = x;
		this.y = y;
		n = b.getN();
		whichEdge = null;

		this.neighbors = new HashMap<String, Position>();

		this.owner = null;

		this.setIsEdge();
		this.setIsNonCorner();
		this.setWhichEdge();
	}

	/* SETTER METHODS */
	
	/**
	 * @return Set which side of player in the BoardImpl
	 */
	private void setWhichEdge() {
		if (this.isEdge && this.isNonCorner) {
			if (this.x == 0) {
				this.whichEdge = "N";

			} else if (this.x == ((2 * n) - 2)) {
				this.whichEdge = "S";

			} else if (this.y == 0) {

				if (this.x < (n - 1)) {
					this.whichEdge = "NW";

				} else {
					this.whichEdge = "SW";
				}

			} else {

				if (this.x < (n - 1)) {
					this.whichEdge = "NE";

				} else {
					this.whichEdge = "SE";
				}
			}
		}
	}

	/**
	 * Set the class variable "neighbors" in this position, which is an HashMap
	 * which maps the directions in String (e.g. "N", "NW", "SE", etc) to the
	 * Position in the respective adjacent direction Position.
	 * */
	public void setNeighbors() {
		HashMap<String, int[]> neighborsCoord = this.getNeighborsCoordinates();

		for (String str : neighborsCoord.keySet()) {
			int x = neighborsCoord.get(str)[0];
			int y = neighborsCoord.get(str)[1];
			if (Position.isValidPosition(n, x, y)) {
				this.neighbors.put(str, b.getPosition(x, y));
			}
		}
	}
	
	/**
	 * If this Position is an edge position, set the variable 'isEdge' as true,
	 * false otherwise
	 */
	private void setIsEdge() {
		if ((x == 0) || (y == 0) || (x == ((2 * n) - 2))
				|| ((y - x) == (n - 1))
				|| (y == (x + (n - 1) - ((x - n + 1) * 2)))) {
			this.isEdge = true;
		} else {
			this.isEdge = false;
		}
	}
	
	/**
	 * Set this Position to be occupied by Player p
	 */
	public void setOccupy(PlayerImpl p) {
		this.owner = p;
	}
	
	/**
	 * If this Position is a non-corner position, set the variable 'isNonCorner'
	 * as true, false otherwise
	 */
	private void setIsNonCorner() {
		if ((Math.abs(x) % (n - 1) == 0) && (Math.abs(y) % (n - 1) == 0)
				&& ((x != (n - 1)) || (y != (n - 1)))) {
			this.isNonCorner = false;
		} else {
			this.isNonCorner = true;
		}
	}
	
	/* GETTER METHODS */
	
	/**
	 * @return The player that is occupying this Position, null if empty
	 */
	public PlayerImpl getOwner() {
		return this.owner;
	}
	
	/**
	 * @return x-coordinate of this position
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * @return y-coordinate of this position
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * @return a HashMap, which maps the direction in String (e.g. "N", "NW",
	 *         "SE", etc) to a two-entry integer array, containing the
	 *         coordinates {x, y}
	 */
	private HashMap<String, int[]> getNeighborsCoordinates() {
		HashMap<String, int[]> neighborsCoord = new HashMap<String, int[]>();

		neighborsCoord.put("E", new int[] { x, y + 1 });
		neighborsCoord.put("W", new int[] { x, y - 1 });

		if (x < (n - 1)) {
			neighborsCoord.put("NW", new int[] { x - 1, y - 1 });
			neighborsCoord.put("NE", new int[] { x - 1, y });
			neighborsCoord.put("SW", new int[] { x + 1, y });
			neighborsCoord.put("SE", new int[] { x + 1, y + 1 });
		} else if (x == (n - 1)) {
			neighborsCoord.put("NW", new int[] { x - 1, y - 1 });
			neighborsCoord.put("NE", new int[] { x - 1, y });
			neighborsCoord.put("SW", new int[] { x + 1, y - 1 });
			neighborsCoord.put("SE", new int[] { x + 1, y });
		} else {
			neighborsCoord.put("NW", new int[] { x - 1, y });
			neighborsCoord.put("NE", new int[] { x - 1, y + 1 });
			neighborsCoord.put("SW", new int[] { x + 1, y - 1 });
			neighborsCoord.put("SE", new int[] { x + 1, y });
		}
		return neighborsCoord;
	}
	
	/**
	 * @return the direction of the edge if isEdge is true, null if isEdge is flase
	 */
	public String getWhichEdge() {
		return this.whichEdge;
	}
	
	/**
	 * @return an ArrayList, consisting of the neighboring positions that are
	 *         occupied by the Player occupying this Position, not including
	 *         Position prev from the ArrayList.
	 */
	public ArrayList<Position> getSameNeighbors(Position prev) {
		ArrayList<Position> sameNeighbors = new ArrayList<Position>();

		for (Position pos : this.neighbors.values()) {
			if ((this.getOwner() != null) && (pos.getOwner() != null)) {
				if (pos.getOwner().equals(this.owner)) {
					sameNeighbors.add(pos);
				}
			}
		}

		if (prev != null) {
			sameNeighbors.remove(prev);
		}

		return sameNeighbors;
	}
	
	/**
	 * @return an ArrayList, consisting of the valid neighboring Positions of
	 *         this Position
	 */
	public Collection<Position> getNeighbors() {
		return (this.neighbors.values());
	}
	
	/**
	 * @param dir - either one of {"W", "E", "NW", "NE", "SW", "SE"} 
	 * @return The neighbor of this Position in the given direction
	 */
	public Position getNeighborInDir(String dir) {
		return this.neighbors.get(dir);
	}

	/* HELPER METHODS */

	/**
	 * @return true if the position in coordinates (x, y) are valid positions
	 *         (i.e. withing the BoardImpl), false otherwise
	 */
	public static boolean isValidPosition(int n, int x, int y) {

		if ((x >= 0) && (y >= 0) && (x <= ((2 * n) - 2))) {
			if (x < n) {
				if ((y - x) <= (n - 1)) {
					return true;
				}
			} else {
				if (y <= (x + (n - 1) - ((x - n + 1) * 2))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return true if this position is not occupied by any player, false
	 *         otherwise
	 */
	public boolean isEmpty() {
		if (this.owner == null) {
			return true;
		} else {
			return false;
		}
	}
}
