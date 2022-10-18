import static java.lang.Math.max;
import static java.lang.Math.min;

public class StudentPlayer extends Player{
    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    private final static int MAX_DEPTH = 10;
    private int result = 0;

    private boolean is3InCol(Board board) {
        int nInACol = 0;

        int startRow = max(0, board.getLastPlayerRow() - 3 + 1);
        int endRow = min(boardSize[0], board.getLastPlayerRow() + 3);

        for (int r = startRow; r < endRow; r++) {
            if (board.getState()[r][board.getLastPlayerColumn()] == board.getLastPlayerIndex()) {
                nInACol++;
                if (nInACol == 3) {
                    if ((board.getLastPlayerRow() - 1 >= 0) && board.getState()[board.getLastPlayerRow() - 1][board.getLastPlayerColumn()] != 1 && board.getState()[board.getLastPlayerRow() - 1][board.getLastPlayerColumn()] != 2)
                        return true;
                }
            } else
                nInACol = 0;
        }
        return false;
    }

    private int score(Board board) {
        if (board.getWinner() == 2) return 100000;
        if (board.getWinner() == 1) return -100000;
        return 0;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean ai) {
        if (board.getLastPlayerIndex() == 2 && board.getLastPlayerColumn() == 3) return 60;
        //if (board.getLastPlayerIndex() == 1 && board.getLastPlayerColumn() == 3) return -60;
        if (board.getLastPlayerIndex() == 2 && is3InCol(board)) return 50 * depth;
        //if (board.getLastPlayerIndex() == 1 && is3InCol(board)) return -50 * depth;
        if (depth == 0 || board.gameEnded()){
            return score(board) * depth;
        }

        if (ai) {
            int maxValue = Integer.MIN_VALUE;
            for(int p = 0; p < board.getValidSteps().size(); p++) {
                Board nextBoard = new Board(board);
                nextBoard.step(2, board.getValidSteps().get(p));
                int value = minimax(nextBoard, depth - 1, alpha, beta, false);
                if(depth == MAX_DEPTH && value > maxValue)
                    result = board.getValidSteps().get(p);
                maxValue = Math.max(maxValue, value);
                alpha = Math.max(alpha, value);
                if(beta <= alpha)
                    break;
            }
            return maxValue;
        }
        else {
            int minValue = Integer.MAX_VALUE;
            for(int p = 0; p < board.getValidSteps().size(); p++) {
                Board nextBoard = new Board(board);
                nextBoard.step(1, board.getValidSteps().get(p));
                int value = minimax(nextBoard, depth - 1, alpha, beta, true);
                minValue = Math.min(minValue, value);
                beta = Math.min(beta, value);
                if(beta <= alpha)
                    break;
            }
            return minValue;
        }
    }

    @Override
    public int step(Board board) {
        minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return result;
    }
}
