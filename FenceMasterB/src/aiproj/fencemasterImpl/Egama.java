package aiproj.fencemasterImpl;

import java.io.PrintStream;

import aiproj.fencemaster.*;

public class Egama implements Player, Piece {
	BoardImpl board;
	PlayerImpl[] players;

	@Override
	public int init(int n, int p) {
		// Initialize players
		players[0] = new PlayerImpl(p);
		players[1] = new PlayerImpl((p == Piece.BLACK) ? WHITE : BLACK);
		
		// Initialize Board
		board = new BoardImpl(n, players);
		return 1;
	}

	@Override
	public Move makeMove() {
		// TODO: Algorithm for best move
		
		
		Move move = new Move();
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
	
}
