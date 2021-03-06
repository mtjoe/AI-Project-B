package aiproj.fencemasterImpl;

import java.io.PrintStream;
import java.util.Set;

import aiproj.AIImpl.MinimaxImpl;
import aiproj.fencemaster.Move;
import aiproj.fencemaster.Piece;
import aiproj.fencemaster.Player;

public class Egama implements Player, Piece {
	BoardImpl board;
	PlayerImpl[] players;
	int connectedEdge = 0;
	int[] root = new int[2];
	int nMoves;
	String extensionMode = "OFF";
	String onWay = "OFF";
	int currX;
	int currY;
	Position lastOpponentMovePos;

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
		// System.out.println(this.onWay + " " + this.extensionMode + " " +
		// this.currX + ":" + this.currY);
		// Algorithm for best move
		int[] c = new int[2];

		this.nMoves = board.getNTotalMoves();

		/* FIRST MOVE: Random */
		if (nMoves <= 1) {

			/* First Move */
			this.root = this.getFirstMove();
			c = this.root;
			currX = this.root[0];
			currY = this.root[1];
		} else {

			/* Find direct winning position (1 ply) */
			if ((c = directWin()) == null) {
				/*
				 * If there is no direct win, check defense, check whether the
				 * opponent might win
				 */
				if ((c = defense()) == null) {
					if (nMoves <= (board.getTotalEntries() / 2)) {
						/* Offense algorithm */
						c = this.offense(this.currX, this.currY);
						this.currX = c[0];
						this.currY = c[1];
					} else {
						/*
						 * Algorithm for last half of the game, minimax
						 * algorithm
						 */
						System.out.println("THIS IS IT");
						MinimaxImpl minimaxImpl = new MinimaxImpl(board,
								this.players);
						Position best = minimaxImpl.MinimaxDecision();
						c = new int[2];
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
		this.lastOpponentMovePos = this.board.getPosition(m.Row,  m.Col);
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
	private int[] getFirstMove() {
		// If we are the first player, set in a bad position
		if (this.board.firstMove && this.board.getNTotalMoves() == 0) {
			return new int[] { 0, 0 };

		} else {
			// If we are the second player, check whether the opponent's first
			// move is in a bad position, if it is, do not swap, otherwise, swap
			if (!lastOpponentMovePos.isNonCorner) {
				int x = board.getNRow() / 4;
				int y = board.getNCol(x) / 2;

				return new int[] { x, y };
			} else {
				return new int[] {this.lastOpponentMovePos.getX(), this.lastOpponentMovePos.getY()};
			}
		}
		
	}
	/* Divide the board into 2 zones in terms of x-coord, then find the closest edge to connect */
	private void whichToConnect(int x, int y) {
		System.out.println("whichToConnect");

		if (x > (board.getNRow() / 2)) {
			this.extensionMode = "LowerSide";
		} else {
			this.extensionMode = "UpperSide";
		}
		
	}
	
	/* Set movement in the upperside of the board */
	private int[] setUpperSide(int x, int y) {
		System.out.println("setUpperSide: " + this.onWay);
		Position pos = board.getPosition(x, y);
		int[] c = new int[2];
		if (this.onWay.equals("OFF")) {
			
			if (((pos.getNeighborInDir("NE").getOwner()) == null) && (pos.getNeighborInDir("NE").isNonCorner)) {
				System.out.println("check NE");
				Position now = pos.getNeighborInDir("NE");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "NE";
				return c;
				
 			} else if (((pos.getNeighborInDir("W").getOwner()) == null) && (pos.getNeighborInDir("W").isNonCorner)) {
 				System.out.println("check W");
 				Position now = pos.getNeighborInDir("W");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "W";
				return c;
				
 			} else if (((pos.getNeighborInDir("E").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
 				System.out.println("check E");
 				Position now = pos.getNeighborInDir("E");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "E";
				return c;
				
 			} else if (((pos.getNeighborInDir("NW").getOwner()) == null) && (pos.getNeighborInDir("NW").isNonCorner)) {
 				System.out.println("check NW");
 				Position now = pos.getNeighborInDir("NW");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "NW";
				return c;
				
 			} else {
 				
 				if (((pos.getNeighborInDir("SW").getOwner()) == null) && (pos.getNeighborInDir("SW").isNonCorner)) {
 	 				Position now = pos.getNeighborInDir("SW");
 					c[0] = now.getX();
 					c[1] = now.getY();
 					this.onWay = "W";
 					return c;
 			
 				} else if (((pos.getNeighborInDir("SE").getOwner()) == null) && (pos.getNeighborInDir("SE").isNonCorner)) {
 	 				Position now = pos.getNeighborInDir("SE");
 					c[0] = now.getX();
 					c[1] = now.getY();
 					this.onWay = "E";
 					return c;
 				} else {
 					Position now = pos.getNeighborInDir(this.onWay);
 					c[0] = now.getX();
 					c[1] = now.getY();
 					return c;
 				}
 			}
		} else {
			if (this.onWay.equals("NE") && ((pos.getNeighborInDir("NE").getOwner()) != null) && (pos.getNeighborInDir("NE").isNonCorner)) {
				
				if (((pos.getNeighborInDir("NW").getOwner()) == null) && (pos.getNeighborInDir("NW").isNonCorner)) {
					System.out.println("check NW");
	 				Position now = pos.getNeighborInDir("NW");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
				
				} else if (((pos.getNeighborInDir("W").getOwner()) == null) && (pos.getNeighborInDir("W").isNonCorner)) {
	 				System.out.println("check W");
	 				Position now = pos.getNeighborInDir("W");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			
				} else if (((pos.getNeighborInDir("E").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
	 				System.out.println("check E");
	 				Position now = pos.getNeighborInDir("E");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			
			} else if (this.onWay.equals("NW") && ((pos.getNeighborInDir("NW").getOwner()) != null) && (pos.getNeighborInDir("NW").isNonCorner)) {
			
				if (((pos.getNeighborInDir("NE").getOwner()) == null) && (pos.getNeighborInDir("NE").isNonCorner)) {
					System.out.println("check NE");
	 				Position now = pos.getNeighborInDir("NE");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			
				} else if (((pos.getNeighborInDir("W").getOwner()) == null) && (pos.getNeighborInDir("W").isNonCorner)) {
	 				System.out.println("check W");
	 				Position now = pos.getNeighborInDir("W");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			
				} else if (((pos.getNeighborInDir("E").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
	 				System.out.println("check E");
	 				Position now = pos.getNeighborInDir("E");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			
			} else if (this.onWay.equals("W") && ((pos.getNeighborInDir("W").getOwner()) != null) && (pos.getNeighborInDir("W").isNonCorner)) {
			
				if (((pos.getNeighborInDir("NW").getOwner()) == null) && (pos.getNeighborInDir("NW").isNonCorner)) {
					System.out.println("check NW");
	 				Position now = pos.getNeighborInDir("NW");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
				
				} else if (((pos.getNeighborInDir("SW").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
	 				System.out.println("check SW");
	 				Position now = pos.getNeighborInDir("SW");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			
			} else if (this.onWay.equals("E") && ((pos.getNeighborInDir("E").getOwner()) != null) && (pos.getNeighborInDir("E").isNonCorner)) {
			
				if (((pos.getNeighborInDir("NE").getOwner()) == null) && (pos.getNeighborInDir("NE").isNonCorner)) {
					System.out.println("check NE");
	 				Position now = pos.getNeighborInDir("NE");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
				
				} else if (((pos.getNeighborInDir("SE").getOwner()) == null) && (pos.getNeighborInDir("SE").isNonCorner)) {
	 				System.out.println("check SE");
	 				Position now = pos.getNeighborInDir("SE");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			}
		}
		Position now = pos.getNeighborInDir(this.onWay);
		c[0] = now.getX();
		c[1] = now.getY();
		return c;
	}
	
	/* Set movement in the lowerside of the board */
	private int[] setLowerSide(int x, int y) {
		Position pos = board.getPosition(x, y);
		int[] c = new int[2];
		if (this.onWay.equals("OFF")) {
			if (((pos.getNeighborInDir("SE").getOwner()) == null) && (pos.getNeighborInDir("SE").isNonCorner)) {
 				Position now = pos.getNeighborInDir("SE");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "SE";
				return c;
				
 			} else if (((pos.getNeighborInDir("W").getOwner()) == null) && (pos.getNeighborInDir("W").isNonCorner)) {
 				System.out.println("check W");
 				Position now = pos.getNeighborInDir("W");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "W";
				return c;
				
 			} else if (((pos.getNeighborInDir("E").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
 				System.out.println("check E");
 				Position now = pos.getNeighborInDir("E");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "E";
				return c;
 			} else if (((pos.getNeighborInDir("SW").getOwner()) == null) && (pos.getNeighborInDir("SW").isNonCorner)) {
 				Position now = pos.getNeighborInDir("SW");
				c[0] = now.getX();
				c[1] = now.getY();
				this.onWay = "SW";
				return c;
 			}
			
		} else {
			if (this.onWay.equals("SE") && ((pos.getNeighborInDir("SE").getOwner()) != null) && (pos.getNeighborInDir("SE").isNonCorner)) {
				
				if (((pos.getNeighborInDir("NE").getOwner()) == null) && (pos.getNeighborInDir("NE").isNonCorner)) {
					System.out.println("check NE");
	 				Position now = pos.getNeighborInDir("NE");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
				
				} else if (((pos.getNeighborInDir("W").getOwner()) == null) && (pos.getNeighborInDir("W").isNonCorner)) {
	 				System.out.println("check W");
	 				Position now = pos.getNeighborInDir("W");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			
				} else if (((pos.getNeighborInDir("E").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
	 				System.out.println("check E");
	 				Position now = pos.getNeighborInDir("E");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			
			} else if (this.onWay.equals("SW") && ((pos.getNeighborInDir("SW").getOwner()) != null) && (pos.getNeighborInDir("SW").isNonCorner)) {
			
				if (((pos.getNeighborInDir("SE").getOwner()) == null) && (pos.getNeighborInDir("SE").isNonCorner)) {
					System.out.println("check SE");
	 				Position now = pos.getNeighborInDir("SE");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			
				} else if (((pos.getNeighborInDir("W").getOwner()) == null) && (pos.getNeighborInDir("W").isNonCorner)) {
	 				System.out.println("check W");
	 				Position now = pos.getNeighborInDir("W");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			
				} else if (((pos.getNeighborInDir("E").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
	 				System.out.println("check E");
	 				Position now = pos.getNeighborInDir("E");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			
			} else if (this.onWay.equals("W") && ((pos.getNeighborInDir("W").getOwner()) != null) && (pos.getNeighborInDir("W").isNonCorner)) {
			
				if (((pos.getNeighborInDir("SW").getOwner()) == null) && (pos.getNeighborInDir("SW").isNonCorner)) {
					System.out.println("check SW");
	 				Position now = pos.getNeighborInDir("SW");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
				
				} else if (((pos.getNeighborInDir("NW").getOwner()) == null) && (pos.getNeighborInDir("E").isNonCorner)) {
	 				System.out.println("check NW");
	 				Position now = pos.getNeighborInDir("NW");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			
			} else if (this.onWay.equals("E") && ((pos.getNeighborInDir("E").getOwner()) != null) && (pos.getNeighborInDir("E").isNonCorner)) {
			
				if (((pos.getNeighborInDir("SE").getOwner()) == null) && (pos.getNeighborInDir("SE").isNonCorner)) {
					System.out.println("check SE");
	 				Position now = pos.getNeighborInDir("SE");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
				
				} else if (((pos.getNeighborInDir("NE").getOwner()) == null) && (pos.getNeighborInDir("NE").isNonCorner)) {
	 				System.out.println("check NE");
	 				Position now = pos.getNeighborInDir("NE");
					c[0] = now.getX();
					c[1] = now.getY();
					return c;
	 			}
			}
		}
		Position now = pos.getNeighborInDir(this.onWay);
		c[0] = now.getX();
		c[1] = now.getY();
		return c;
	}
	
	/* Set the offense to make Tripod */
	private int[] makeTripod(int x, int y) {
		System.out.println("makeTripod");
		System.out.println(this.onWay + "- " + this.extensionMode + " "
				+ this.currX + ":" + this.currY);

		Position whereAt = board.getPosition(x, y);
		String whichEdge;
		if ((whereAt.isEdge && whereAt.isNonCorner)) {

			// whichEdge = whereAt.getWhichEdge();
			System.out.println("At the Edge");
			this.currX = this.root[0];
			this.currY = this.root[1];
			this.onWay = "OFF";
		} else {
			whichToConnect(x, y);
		}

		if (this.extensionMode == "UpperSide") {
			return setUpperSide(x, y);
		} else if (this.extensionMode == "LowerSide") {
			return setLowerSide(x, y);
		} else {
			return getRandomMove();
		}
	}

	private int[] offense(int x, int y) {

		Position whereAt = board.getPosition(x, y);
		String whichEdge;
		
		/* When the edge is reached */
		if ((whereAt.isEdge && whereAt.isNonCorner) ) {
			
			this.connectedEdge+=1;
			this.currX = this.root[0];
			this.currY = this.root[1];
			this.onWay = "OFF";
			x = this.currX;
			y = this.currY;
		}
		
		return makeTripod(x, y);
	}

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

		// Search for possibility of opponent win in first ply
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
				// Search for possibility of opponent win in second ply
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

		// Search for possibility of opponent win in second ply
		for (Position pos : neighborPos) {
			// Add move
			board.setMove(pos.getX(), pos.getY(), players[1], true);
			players[1].addPosition(pos);

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

						c[0] = pos.getX();
						c[1] = pos.getY();
						return c;
					}
					// remove move previously placed
					board.removeMove(posNeighbor.getX(), posNeighbor.getY(),
							players[1]);
					players[1].removePosition(posNeighbor);
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