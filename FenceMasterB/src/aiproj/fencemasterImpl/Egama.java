package aiproj.fencemasterImpl;

import java.io.PrintStream;

import aiproj.fencemaster.*;

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
		
		int x = (int)(Math.random() * board.nRow);
		int y = (int)(Math.random() * board.nCol[x]);
		
		while (!board.getPosition(x, y).isEmpty()){
			x = (int)(Math.random() * board.nRow);
			y = (int)(Math.random() * board.nCol[x]);
		}
		
		
		board.setMove(x, y, players[0]);
		
		Move move = new Move(players[0].piece, false, x, y);
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
