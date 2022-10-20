public class StudentPlayer extends Player{
    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    private final static int MAX_DEPTH = 10;
    private int result = 0;

    private boolean is3inCol(Board board) {
        if (board.getLastPlayerRow() <= 3 && board.getLastPlayerRow() > 0) {
            for (int i = board.getLastPlayerRow(); i < board.getLastPlayerRow() + 3; i++)
                if (board.getState()[i][board.getLastPlayerColumn()] != board.getLastPlayerIndex())
                    return false;
            return true;
        }
        return false;
    }

    private int score(Board board) {
        if (board.getWinner() == 2) return 10000000;
        if (board.getWinner() == 1) return -10000000;
        if (board.getWinner() == 0) return 0;
        if (is3inCol(board)) {
            if(board.getLastPlayerIndex() == 2) return 50;
            if(board.getLastPlayerIndex() == 1) return -50;
        }
        if (board.getLastPlayerColumn() == 3) {
            if(board.getLastPlayerIndex() == 2) return 10;
            if(board.getLastPlayerIndex() == 1) return -10;
        }
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
                if(depth == MAX_DEPTH && value > maxValue)
                    result = board.getValidSteps().get(p);
                /*if(depth == MAX_DEPTH && value == maxValue && new Random().nextInt(2) == 1)
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
        minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return result;
    }
}
