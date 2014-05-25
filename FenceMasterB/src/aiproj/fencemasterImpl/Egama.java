package aiproj.fencemasterImpl;

import java.io.PrintStream;

import aiproj.AIImpl.MinimaxImpl;
import aiproj.fencemaster.Move;
import aiproj.fencemaster.Piece;
import aiproj.fencemaster.Player;

public class Egama implements Player, Piece {
	BoardImpl board;
	PlayerImpl[] players;

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
		
		int nMoves = board.getNTotalMoves();

		/* FIRST MOVE: Random */
		if (nMoves <= 2) {

			/* random */
			c = this.getRandomMove();
			while (!board.getPosition(c[0], c[1]).isEmpty()) {
				c = this.getRandomMove();
			}
		} else {
			
			/* TODO: Find direct winning position (1 ply) */
			
			
			/* TODO: Defense, check whether the opponent might win */
			
			
			if (nMoves <= 50) {
				/* TODO: Algorithm for first half of the game */
				
			} else {
				/* Algorithm for last half of the game, Minimax algorithm */
				System.out.println("THIS IS IT");
				MinimaxImpl minimaxImpl = new MinimaxImpl(board, this.players);
				Position best = minimaxImpl.MinimaxDecision();

				c[0] = best.getX();
				c[1] = best.getY();
			}
		}

		/* SET MOVE */
		board.setMove(c[0], c[1], players[0]);

		Move move = new Move(players[0].piece, false, c[0], c[1]);
		return move;
	}

	@Override
	public int opponentMove(Move m) {
		board.setMove(m.Row, m.Col, players[1]);
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
		int x = (int) (Math.random() * board.getNRow());
		int y = (int) (Math.random() * board.getNCol(x));

		return new int[] { x, y };
	}
}