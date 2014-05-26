package aiproj.fencemasterImpl;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

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
		// Algorithm for best move
		int[] c = new int[2];

		int nMoves = board.getNTotalMoves();

		/* FIRST MOVE: Random */
		if (nMoves <= 2) {

			/* Random */
			c = this.getRandomMove();
			while (!board.getPosition(c[0], c[1]).isEmpty()) {
				c = this.getRandomMove();
			}
		} else {

			/* Find direct winning position (1 ply) */
			if ((c = directWin()) == null) {
				/*
				 * If there is no direct win, check defense, check whether
				 * the opponent might win
				 */
				if ((c = defense()) == null) {
					if (nMoves <= (board.getTotalEntries())) {
						/* TODO: Algorithm for first half of the game */

						/* Random */
						c = this.getRandomMove();
						while (!board.getPosition(c[0], c[1]).isEmpty()) {
							c = this.getRandomMove();
						}
					} else {
						/*
						 * Algorithm for last half of the game, minimax
						 * algorithm
						 */
						System.out.println("THIS IS IT");
						MinimaxImpl minimaxImpl = new MinimaxImpl(board,
								this.players);
						Position best = minimaxImpl.MinimaxDecision();

						c[0] = best.getX();
						c[1] = best.getY();
					}
				}
			}
		}

		/* SET MOVE */
		board.setMove(c[0], c[1], players[0], false);

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
		int x = (int) (Math.random() * board.getNRow());
		int y = (int) (Math.random() * board.getNCol(x));

		return new int[] { x, y };
	}

	/**
	 * Check whether there is a possibility that the opponent might win in the
	 * next two moves (not our move)
	 * 
	 * @return the move that will cause the opponent to win, null if none
	 */
	private int[] defense() {
		int[] c = new int[2];

		// Get neighboring positions of the opponents positions
		Set<Position> neighborPos = board.getPlayerNeighbors(players[1].piece);

		// Loop through
		for (Position pos : neighborPos) {

			// Add move
			board.setMove(pos.getX(), pos.getY(), players[1], true);
			players[1].addPosition(pos);

			if (board.getWinner() == players[1].piece) {
				// remove move previously placed
				board.removeMove(pos.getX(), pos.getY(), players[1]);
				players[1].removePosition(pos);

				c[0] = pos.getX();
				c[1] = pos.getY();
				return c;
			} else {
				for (Position posNeighbor : pos.getNeighbors()) {
					if (posNeighbor.getOwner() == null) {
						// Add move
						board.setMove(posNeighbor.getX(), posNeighbor.getY(),
								players[1], true);
						players[1].addPosition(posNeighbor);

						if (board.getWinner() == players[1].piece) {
							// remove move previously placed
							board.removeMove(posNeighbor.getX(),
									posNeighbor.getY(), players[1]);
							players[1].removePosition(posNeighbor);

							c[0] = posNeighbor.getX();
							c[1] = posNeighbor.getY();
							return c;
						}
						// remove move previously placed
						board.removeMove(posNeighbor.getX(),
								posNeighbor.getY(), players[1]);
						players[1].removePosition(posNeighbor);
					}
				}
			}
			// remove move previously placed
			board.removeMove(pos.getX(), pos.getY(), players[1]);
			players[1].removePosition(pos);
		}

		return null;
	}

	/**
	 * Check whether there is a single ply move that let us win
	 * 
	 * @return [x, y] coordinates of the move, if none, return null
	 */
	private int[] directWin() {
		int[] c = new int[2];
		// Get neighboring positions of our positions
		Set<Position> neighborPos = board.getPlayerNeighbors(players[0].piece);

		for (Position pos : neighborPos) {
			// Add move
			board.setMove(pos.getX(), pos.getY(), players[0], true);
			players[0].addPosition(pos);

			if (board.getWinner() == players[0].piece) {
				// remove move previously placed
				board.removeMove(pos.getX(), pos.getY(), players[0]);
				players[0].removePosition(pos);
				
				c[0] = pos.getX();
				c[1] = pos.getY();

				return c;
			}
			// remove move previously placed
			board.removeMove(pos.getX(), pos.getY(), players[0]);
			players[0].removePosition(pos);
		}
		return null;
	}
}