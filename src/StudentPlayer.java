import java.util.Random;

public class StudentPlayer extends Player{

    private final static int MAX_DEPTH = 12;
    private int result = 0;
    private Random random;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        random = new Random();
    }

    private boolean is3InCol(Board board) {
        if (board.getLastPlayerRow() <= 3 && board.getLastPlayerRow() > 0) {
            for (int i = board.getLastPlayerRow(); i < board.getLastPlayerRow() + 3; i++)
                if (board.getState()[i][board.getLastPlayerColumn()] != board.getLastPlayerIndex())
                    return false;
            return true;
        }
        return false;
    }

    private int getTopRowIndex(Board board, int col) {
        for (int row = 0; row < boardSize[0] ; row++)
            if (board.getState()[row][col] != -1)
                return row;
        return boardSize[0];
    }

    private boolean is3InCol(Board board, int playerIndex) {
        for (int col = 0; col < 7; col++) {
            int topRowIndex = getTopRowIndex(board, col);
            if (topRowIndex < boardSize[0] - nToConnect + 1 && topRowIndex > 0) {
                boolean fail = false;
                for (int row = topRowIndex; row < topRowIndex + nToConnect - 1; row++)
                    if (board.getState()[row][col] != playerIndex) {
                        fail = true;
                        break;
                    }
                if (!fail) return true;
            }
        }
        return false;
    }

    private int score(Board board) {
        if (board.getWinner() == 2) return 100;
        if (board.getWinner() == 1) return -100;
        if (board.getWinner() == 0) return 0;
        if (is3InCol(board, 1)) return -50;
        if (is3InCol(board, 2)) return 50;
        /*if (board.getLastPlayerColumn() == 3) {
            if(board.getLastPlayerIndex() == 2) return 10;
            if(board.getLastPlayerIndex() == 1) return -10;
        }*/
        return 0;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean ai) {
        if (board.gameEnded() || depth == 1)
            return score(board) * depth;

        if (ai) {
            int maxValue = Integer.MIN_VALUE;
            for(int p = 0; p < board.getValidSteps().size(); p++) {
                Board nextBoard = new Board(board);
                nextBoard.step(2, board.getValidSteps().get(p));
                int value = minimax(nextBoard, depth - 1, alpha, beta, false);
                /*if (value == 0)
                    value = minimax(board, depth - 1, alpha, beta, false);*/
                if(depth == MAX_DEPTH && value > maxValue)
                    result = board.getValidSteps().get(p);
                /*if(depth == MAX_DEPTH && value == maxValue && random.nextInt(2) == 0)
                    result = board.getValidSteps().get(p);*/
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
        //System.out.println(minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
        minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return result;
    }
}
