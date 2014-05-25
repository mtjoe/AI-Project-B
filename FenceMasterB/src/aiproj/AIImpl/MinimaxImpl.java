package aiproj.AIImpl;

import java.util.LinkedList;

import aiproj.fencemasterImpl.BoardImpl;
import aiproj.fencemasterImpl.PlayerImpl;
import aiproj.fencemasterImpl.Position;

public class MinimaxImpl {
	BoardImpl board;
	PlayerImpl mainPlayer, oppPlayer;
	LinkedList<Position> emptyPos;
	int minDepth, currDepth;
	int maxUtil, minUtil;

	/**
	 * Public constructor of the Minimax Implementation
	 * 
	 * @param board
	 *            - the current state of the board
	 * @param players
	 *            - 2 entry array of PlayerImpl, players[0] is the home player,
	 *            and players[1] is the opponent
	 */
	public MinimaxImpl(BoardImpl board, PlayerImpl[] players) {
		this.board = board;
		this.emptyPos = new LinkedList<Position>();
		this.mainPlayer = players[0];
		this.oppPlayer = players[1];

		// Get all the empty positions in the board
		for (int i = 0; i < board.getNRow(); i++) {
			for (int j = 0; j < board.getNCol(i); j++) {
				if (board.getPosition(i, j).isEmpty()) {
					emptyPos.add(board.getPosition(i, j));
				}
			}
		}
	}

	/**
	 * Loop through all children states of the current position, finding the
	 * child with the MAX Minimax value
	 */
	public Position MinimaxDecision() {
		minDepth = board.getTotalEntries();
		currDepth = 0;

		Position pos;
		int util;

		// The maximum util value and position of the children
		int maxUtil = Utility.LOSE - 1;
		Position maxUtilPos = null;

		// Loop through all empty positions
		for (int i = 0; i < this.emptyPos.size(); i++) {

			// Set move on an empty position (temporarily)
			pos = this.emptyPos.removeFirst();
			board.setMove(pos.getX(), pos.getY(), mainPlayer);

			// Find the Minimax value of each children, while finding the child
			// with the largest max
			if ((util = MinimaxValue(State.MIN)) > maxUtil) {
				maxUtilPos = pos;
				maxUtil = util;
			}

			// Remove move from the supposed empty position
			this.emptyPos.addLast(pos);
			board.removeMove(pos.getX(), pos.getY(), mainPlayer);
		}
		return maxUtilPos;
	}

	/**
	 * To find the minimax value of the state, recursive
	 * 
	 * @param state - the state of the board whose minimax value is requested
	 * @return Minimax value of the state (based on the minimax values of their
	 *         children)
	 */
	public int MinimaxValue(int state) {
		int winner, util;
		int size = this.emptyPos.size();

		// Skip if
		if (this.currDepth >= this.minDepth) {
			return (state == State.MAX) ? -10 : 10;

			// If the game is over
		} else if ((winner = board.getWinner()) > 0) {

			// If win, set minimum depth
			if (winner == this.mainPlayer.getPiece()) {
				minDepth = currDepth;
				return Utility.WIN;

				// If lose
			} else if (winner == this.oppPlayer.getPiece()) {
				return Utility.LOSE;

				// If draw
			} else {
				return Utility.DRAW;
			}

			// If the game is still going on
		} else {
			currDepth++;
			Position pos;

			// If the current game is in MAX configuration, the next should be
			// in MIN configuration, vice versa
			if (state == State.MAX) {
				maxUtil = Utility.LOSE - 1;

				// Loop through children (remaining empty positions), finding
				// the state with the maximum utility out of the children
				for (int i = 0; i < size; i++) {
					pos = this.emptyPos.get(0);

					// Set move to the chosen empty position in the board
					this.emptyPos.remove(pos);
					board.setMove(pos.getX(), pos.getY(), mainPlayer);

					// Find maximum minimax value
					if ((util = MinimaxValue(State.MIN)) > maxUtil) {
						maxUtil = util;
					}

					// Remove the move previously placed in the board
					this.emptyPos.addLast(pos);
					board.removeMove(pos.getX(), pos.getY(), mainPlayer);
				}

				currDepth--;
				return maxUtil;
			} else {
				minUtil = Utility.WIN + 1;

				// Loop through children (remaining empty positions), finding
				// the state with the minimum utility out of the children
				for (int i = 0; i < size; i++) {

					// Set move to the chosen empty position in the board
					pos = this.emptyPos.removeFirst();
					board.setMove(pos.getX(), pos.getY(), mainPlayer);

					// Find minimum minimax value
					if ((util = MinimaxValue(State.MAX)) < minUtil) {
						minUtil = util;
					}

					// Remove the move previously placed in the board
					this.emptyPos.addLast(pos);
					board.removeMove(pos.getX(), pos.getY(), mainPlayer);
				}
				currDepth--;
				return minUtil;

			}

		}
	}
}
