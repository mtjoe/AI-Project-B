package aiproj.fencemasterImpl;

import aiproj.checkWin.*;

/**
 * The Board comes in the form of a 2-D ArrayList of Position Objects, to 
 * represent each slot in the Board.
 * 
 * @author Marisa Tjoe (566322) & Erlangga Satria Gama (570748)
 */
public class BoardImpl {
	private PlayerImpl[] playerImpls; 
	private Position bArray[][];
	private int n;
	private int nRow;
	private int nCol;
	int totalEntries;
	
	/* PUBLIC CONSTRUCTOR */
	
	/**
	 * Initializes an empty Board with number of sides n
	 * - Given the size of each sides of the board n, 
	 * 		- The number of rows of the Board would be ((2*n)-1)
	 * 		- The number of columns in rows 0 to (n-1) would be (n+i)
	 * 		- The number of columns in rows n to ((2*n)-2) would be ((3*n)-2-i)
	 * @param n -  Number of sides
	 */
	public BoardImpl(int n, PlayerImpl[] playerImpls){
		this.playerImpls = playerImpls;
		this.n = n;
		this.totalEntries = 0;
		
		nRow = (2*n)-1;
		
		bArray = new Position[nRow][];
		
		/* Loop through each row to initialize empty slots */
		
		// Initialize increasing rows (0 -- n) to null
		for (int i=0; i<n; i++) {
			nCol = n + i;
			totalEntries += nCol;
			
			Position[] rowArray = new Position[nCol];
			
			for (int j=0; j<nCol; j++){
				rowArray[j] = new Position(this, i, j); 
			}
			
			bArray[i] = rowArray;
		}
		
		// initialize decreasing rows (n+1 -- 2n-1) to null
		
		for (int i=n; i<nRow; i++) {
			nCol = (3 * n) - 2 - i;
			totalEntries += nCol;
			
			Position[] rowArray = new Position[nCol];
			
			for (int j=0; j<nCol; j++){
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
	public void setMove(int x, int y, PlayerImpl p){
		bArray[x][y].setOccupy(p);
		p.addPosition(this.getPosition(x, y), n);
		return;
	}
	
	/* GETTER METHODS */
	
	/**
	 * @return n, the size of each side in the board
	 */
	public int getN(){
		return this.n;
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
		return this.playerImpls;
	}
	
	/**
	 * Checks whether there is a winner in the board.
	 * @return The name of the winner if there is a winner, null if there is no winner
	 */
	public int getWinner(){
		PlayerImpl winner;
		
		LoopCheck loopCheck = new LoopCheck(this);
		if ((winner = loopCheck.check()) == null){
			TripodCheck tripodCheck = new TripodCheck(this);
			if ((winner = tripodCheck.check()) == null){
				// No winner yet
				return -1;
			} else {
				// Win by Tripod
				return winner.piece;
			}
		} else {
			// Win by Loop
			return winner.piece;
		}
	}
	
	public void printBoard() {
		// Print increasing rows (0 -- n)
		for (int i=0; i<n; i++) {
			nCol = n + i;
			
			for (int j=0; j<nCol; j++){
				System.out.print(this.getPosition(i, j).getOwner().piece + " "); 
			}
			System.out.println();
		}
		
		// Print decreasing rows (n+1 -- 2n-1)
		
		for (int i=n; i<nRow; i++) {
			nCol = (3 * n) - 2 - i;
			
			for (int j=0; j<nCol; j++){
				System.out.print(this.getPosition(i, j).getOwner().piece + " "); 
			}
			System.out.println();
		}
	}
}
