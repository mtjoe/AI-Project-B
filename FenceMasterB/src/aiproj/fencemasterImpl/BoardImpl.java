package aiproj.fencemasterImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aiproj.checkWin.LoopCheck;
import aiproj.checkWin.TripodCheck2;

/**
 * The Board comes in the form of a 2-D ArrayList of Position Objects, to 
 * represent each slot in the Board.
 * 
 * @author Marisa Tjoe (566322) & Erlangga Satria Gama (570748)
 */
public class BoardImpl {
	private PlayerImpl[] players; 
	private Position bArray[][];
	private int n;
	private int nRow;
	private int[] nCol;
	private int totalEntries;
	private int nTotalMoves;
	private Map<Integer, Set<Position>> playersNeighPositions;
	
	/* PUBLIC CONSTRUCTOR */
	
	/**
	 * Initializes an empty Board with number of sides n
	 * - Given the size of each sides of the board n, 
	 * 		- The number of rows of the Board would be ((2*n)-1)
	 * 		- The number of columns in rows 0 to (n-1) would be (n+i)
	 * 		- The number of columns in rows n to ((2*n)-2) would be ((3*n)-2-i)
	 * @param n -  Number of sides
	 */
	@SuppressWarnings("unchecked")
	public BoardImpl(int n, PlayerImpl[] playerImpls){
		this.players = playerImpls;
		this.n = n;
		this.totalEntries = 0;
		this.nTotalMoves = 0;
		this.playersNeighPositions = new HashMap<Integer, Set<Position>>();
		this.playersNeighPositions.put(playerImpls[0].piece, new HashSet<Position>());
		this.playersNeighPositions.put(playerImpls[1].piece, new HashSet<Position>());
		
		nRow = ((2 * n) - 1);
		nCol = new int[nRow];
		
		bArray = new Position[nRow][];
		
		/* Loop through each row to initialize empty slots */
		
		// Initialize increasing rows (0 -- n) to null
		for (int i=0; i<n; i++) {
			nCol[i] = n + i;
			totalEntries += nCol[i];
			
			Position[] rowArray = new Position[nCol[i]];
			
			for (int j=0; j<nCol[i]; j++){
				rowArray[j] = new Position(this, i, j); 
			}
			
			bArray[i] = rowArray;
		}
		
		// initialize decreasing rows (n+1 -- 2n-1) to null
		
		for (int i=n; i<nRow; i++) {
			nCol[i] = (3 * n) - 2 - i;
			totalEntries += nCol[i];
			
			Position[] rowArray = new Position[nCol[i]];
			
			for (int j=0; j<nCol[i]; j++){
				rowArray[j] = new Position(this, i, j); 
			}
			
			bArray[i] = rowArray;
		}
		
		// Set neighboring positions
		for (Position[] rowPos: this.bArray){
			for (Position pos: rowPos){
				pos.setNeighbors();
			}
		}
		
		return;
	}
	
	/**
	 * @return true if pos1 and pos2 are adjacent/neighbors, false otherwise
	 */
	public boolean isAdjacent(Position pos1, Position pos2){
		if (pos1.getNeighbors().contains(pos2)){
			return true;
		}
		return false;
	}
	
	/* SETTER METHODS */
	
	/**
	 * Set Poisition of with coordinates (x, y) to be occupied by PlayerImpl p
	 */
	public void setMove(int x, int y, PlayerImpl p, boolean temp){
		Position movePos = this.getPosition(x, y);
		bArray[x][y].setOccupy(p);
		p.addPosition(movePos);
		this.nTotalMoves++;
		
		// Set neighboring positions
		if (!temp){
			this.playersNeighPositions.get(p.piece).remove(movePos);
			this.playersNeighPositions.get((p.piece == 1) ? 2:1).remove(movePos);
			for (Position pos:movePos.getNeighbors()){
				if (pos.getOwner() == null){
					this.playersNeighPositions.get(p.piece).add(pos);
				}
			}
		}
	}
	
	public void removeMove(int x, int y, PlayerImpl p) {
		bArray[x][y].setOccupy(null);
		p.removePosition(this.getPosition(x, y));
		this.nTotalMoves--;
	}
	
	/* GETTER METHODS */
	
	/**
	 * @return n, the size of each side in the board
	 */
	public int getN(){
		return this.n;
	}
	
	public int getNTotalMoves() {
		return this.nTotalMoves;
	}
	
	public int getNRow() {
		return this.nRow;
	}
	
	public int getNCol(int row) {
		return this.nCol[row];
	}
	
	
	
	public int getTotalEntries() {
		return this.totalEntries;
	}
	
	/**
	 * @return Position with coordinates (x, y)
	 */
	public Position getPosition(int x, int y){
		// If out of bounds, return null
		if (Position.isValidPosition(n, x, y)){
			return bArray[x][y];
		}
		return null;
	}
	
	public PlayerImpl[] getPlayerImpls(){
		return this.players;
	}
	
	public Set<Position> getPlayerNeighbors(int piece) {
		return this.playersNeighPositions.get(piece);
	}
	
	/**
	 * Checks whether there is a winner in the board.
	 * @return The piece of the winner if there is a winner, 3 if draw, -1 if there is no winner
	 */
	public int getWinner(){
		PlayerImpl winner;
		
		if (this.isDraw()) {
			return 3;
		}
		
		LoopCheck loopCheck = new LoopCheck(this);
		if ((winner = loopCheck.check()) == null){
			TripodCheck2 tripodCheck = new TripodCheck2(this);
			if ((winner = tripodCheck.check()) == null){
				// No winner yet
				return -1;
			} else {
				// Win by Tripod
				System.out.println("WIN BY TRIPOD");
				return winner.piece;
			}
		} else {
			// Win by Loop
			System.out.println("WIN BY LOOP");
			return winner.piece;
		}
	}
	
	public boolean isDraw() {
		if (this.totalEntries == this.nTotalMoves) {
			return true;
		}
		return false;
	}
	
	public void printBoard() {
		Position pos;
		
		for (int i=0; i<nRow; i++) {
			for (int k=0; k<(nRow-nCol[i]); k++){ System.out.print(" "); }
			for (int j=0; j<nCol[i]; j++){
				pos = this.getPosition(i, j);
				System.out.print(pos.isEmpty() ? "- " : pos.getOwner().piece + " "); 
			}
			System.out.println();
		}
	}
}
