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
	
	public MinimaxImpl(BoardImpl board, PlayerImpl[] players) {
		this.board = board;
		this.emptyPos = new LinkedList<Position>();
		this.mainPlayer = players[0];
		this.oppPlayer = players[1];
		
		for (int i=0; i<board.getNRow(); i++) {
			for (int j=0;j<board.getNCol(i); j++) {
				if (board.getPosition(i, j).isEmpty()){
					emptyPos.add(board.getPosition(i, j));
				}
			}
		}
	}
	
	public Position MinimaxDecision() {
		int maxUtil, util;
		Position maxUtilPos = null;
		minDepth = board.getTotalEntries();
		currDepth = 0;
		
		Position pos;
		for (int i=0; i<this.emptyPos.size(); i++) {
			pos = this.emptyPos.removeFirst();
			board.setMove(pos.getX(), pos.getY(), mainPlayer);
			maxUtil = Utility.LOSE - 1;
			
			if ((util = MinimaxValue(State.MIN)) > maxUtil) {
				maxUtilPos = pos;
				maxUtil = util;
			}
			this.emptyPos.addLast(pos);
			board.removeMove(pos.getX(), pos.getY(), mainPlayer);
	
		}
		return maxUtilPos;
	}
	
	public int MinimaxValue(int state) {
		int winner, util;
		int size = this.emptyPos.size();
		
		if (this.currDepth >= this.minDepth){
			return (state == State.MAX) ? -10 : 10;
		} else if ((winner = board.getWinner()) > 0) {
			minDepth = currDepth;
			/* If win */
			if (winner == this.mainPlayer.getPiece()) {
				return Utility.WIN;
				
			/* If lose */
			} else if (winner == this.oppPlayer.getPiece()) {
				return Utility.LOSE;
			} else {
				return 0;
			}
		} else {
			currDepth++;
			Position pos;
			if (state == State.MAX) {
				maxUtil = Utility.LOSE - 1;
				for (int i=0; i<size; i++) {
					
					pos = this.emptyPos.get(0);
					this.emptyPos.remove(pos);
					board.setMove(pos.getX(), pos.getY(), mainPlayer);
					
					if ((util = MinimaxValue(State.MIN)) > maxUtil) {
						maxUtil = util;
					}
					this.emptyPos.addLast(pos);
					board.removeMove(pos.getX(), pos.getY(), mainPlayer);
				}
				currDepth--;
				return maxUtil;
			} else {
				minUtil = Utility.WIN + 1;
				for (int i=0; i<size; i++) {
					pos = this.emptyPos.removeFirst();
					board.setMove(pos.getX(), pos.getY(), mainPlayer);
					
					if ((util = MinimaxValue(State.MAX)) < minUtil) {
						minUtil = util;
					}
					
					this.emptyPos.addLast(pos);
					board.removeMove(pos.getX(), pos.getY(), mainPlayer);
				}
				currDepth--;
				return minUtil;
			
			}
			
		}
	}
}
