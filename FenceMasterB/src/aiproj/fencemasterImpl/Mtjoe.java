package aiproj.fencemasterImpl;

import java.io.PrintStream;

import aiproj.fencemaster.Move;
import aiproj.fencemaster.Piece;
import aiproj.fencemaster.Player;

public class Mtjoe implements Player, Piece {
	BoardImpl board;
	PlayerImpl[] players;
	boolean inDanger = false;

	@Override
	public int init(int n, int p) {
		// Initialize players
		players = new PlayerImpl[2];
		players[0] = new PlayerImpl(p);
		players[1] = new PlayerImpl((p == Piece.BLACK) ? WHITE : BLACK);
		
		// Initialize Board
		board = new BoardImpl(n, players);
		return 1;
	}

	@Override
	public Move makeMove() {
		// TODO: Algorithm for best move
		int[] c = new int[2];
		
		/* random */
		c = this.getRandomMove();
		while (!board.getPosition(c[0], c[1]).isEmpty()){
			c = this.getRandomMove();
		}
		
		/* SET MOVE */
		board.setMove(c[0], c[1], players[0], false);
		
		/* TODO: set inDanger */
		
		Move move = new Move(players[0].piece, false, c[0], c[1]);
		return move;
	}

	@Override
	public int opponentMove(Move m) {
		board.setMove(m.Row, m.Col, players[1], false);
		return 1;
	}
	
	@Override
	public int getWinner() {
		return board.getWinner();
	}

	@Override
	public void printBoard(PrintStream output) {
		board.printBoard();
	}
	
	/* HELPER FUNCTIONS */
	
	private int[] getRandomMove() {
		
		/* RANDOM ALGORITHM */
		int x = (int)(Math.random() * board.getNRow());
		int y = (int)(Math.random() * board.getNCol(x));
		
		return new int[]{x, y};
	}
}
