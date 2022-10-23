public class StudentPlayer extends Player{

    private final static int MAX_DEPTH = 7;
    private int result = 0;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    private int getTopRowIndex(Board board, int col) {
        for (int row = 0; row < boardSize[0] ; row++)
            if (board.getState()[row][col] != 0)
                return row;
        return boardSize[0];
    }

    private int CountOf3InCol(Board board, int playerIndex) {
        int count = 0;
        for (int col = 0; col < boardSize[1]; col++) {
            int topRowIndex = getTopRowIndex(board, col);
            if (topRowIndex < boardSize[0] - nToConnect + 2 && topRowIndex != 0) {
                boolean fail = false;
                for (int row = topRowIndex; row < topRowIndex + nToConnect - 1; row++)
                    if (board.getState()[row][col] != playerIndex) {
                        fail = true;
                        break;
                    }
                if (!fail) count++;
            }
        }
        return count;
    }

    private int CountOf3InRow(Board board, int playerIndex) {
        int count = 0;

        int highestCol = boardSize[0] - 1;
        for (int col = 0; col < boardSize[1]; col++)
            highestCol = Math.min(highestCol, getTopRowIndex(board, col));

        for (int row = boardSize[0] - 1; row >= highestCol; row--) {
            for (int col = 0; col < boardSize[1] - nToConnect + 1; col++) {
                int tokens = 0;
                int empty = 0;
                for (int offset = 0; offset < nToConnect; offset++) {
                    if (board.getState()[row][col + offset] == playerIndex)
                        tokens++;
                    else if (board.getState()[row][col + offset] == 0)
                        empty++;
                    else
                        break;
                }
                if (tokens == 3 && empty == 1)
                    count++;
            }
        }

        return count;
    }

    private int CountOf3InDiagonally(Board board, int playerIndex) {
        int count = 0;

        int highestCol = boardSize[0] - 1;
        for (int col = 0; col < boardSize[1]; col++)
            highestCol = Math.min(highestCol, getTopRowIndex(board, col));
        highestCol = Math.max(highestCol, nToConnect - 1);

        for (int row = boardSize[0] - 1; row >= highestCol; row--) {
            for (int col = 0; col < boardSize[1] - nToConnect + 1; col++) {
                int tokens = 0;
                int empty = 0;
                for (int offset = 0; offset < nToConnect; offset++) {
                    if (board.getState()[row - offset][col + offset] == playerIndex)
                        tokens++;
                    else if (board.getState()[row - offset][col + offset] == 0)
                        empty++;
                    else
                        break;
                }
                if (tokens == 3 && empty == 1)
                    count++;
            }
        }

        return count;
    }

    private int CountOf3InSkewDiagonally(Board board, int playerIndex) {
        int count = 0;

        int highestCol = boardSize[0] - 1;
        for (int col = 0; col < boardSize[1]; col++)
            highestCol = Math.min(highestCol, getTopRowIndex(board, col));
        highestCol = Math.max(highestCol, nToConnect - 1);

        for (int row = boardSize[0] - 1; row >= highestCol; row--) {
            for (int col = 0; col < boardSize[1] - nToConnect + 1; col++) {
                int tokens = 0;
                int empty = 0;
                for (int offset = 0; offset < nToConnect; offset++) {
                    if (board.getState()[row - (nToConnect - offset - 1)][col + offset] == playerIndex)
                        tokens++;
                    else if (board.getState()[row - (nToConnect - offset - 1)][col + offset] == 0)
                        empty++;
                    else
                        break;
                }
                if (tokens == 3 && empty == 1)
                    count++;
            }
        }

        return count;
    }

    private int score(Board board) {
        if (board.getWinner() == 2) return 100;
        if (board.getWinner() == 1) return -100;
        if (board.getWinner() == 0) return 0;
        return CountOf3InCol(board, 2) + CountOf3InRow(board, 2) + CountOf3InDiagonally(board, 2) + CountOf3InSkewDiagonally(board, 2)
             - CountOf3InCol(board, 1) - CountOf3InRow(board, 1) - CountOf3InDiagonally(board, 1) - CountOf3InSkewDiagonally(board, 1);
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
