import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VierGewinnt implements VierGewinntLogic {

    private int[] board;

    public VierGewinnt() {
        board = new int[COLUMNS * ROWS];
    }

    public VierGewinnt(int[] board) {
        this.board = Arrays.copyOf(board, board.length);
    }

    public static VierGewinnt of(int[] board) {
        return new VierGewinnt(board);
    }


    //Start of Logic
    @Override
    public VierGewinnt playMove(int column, boolean turn) {
        VierGewinnt vg = VierGewinnt.of(board);

        if (column < 0 || column >= COLUMNS) throw new IllegalArgumentException("Column out of bounds");
        if (vg.getBoard(column, 0) != 0) throw new IllegalArgumentException("Column is full");

        if (!isGameOver()) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (vg.getBoard(column, row) == 0) {
                    vg.board[row * COLUMNS + column] = turn ? 1 : 2;
                    break;
                }
            }
        }

        return vg;
    }

    @Override
    public boolean isGameOver() {
        return Arrays.stream(board).allMatch(i -> i != 0) || checkForWin();
    }

    private boolean checkForWin() {
        StringBuilder order = new StringBuilder();

        //check for horizontal win
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                order.append(getBoard(c, r));
            }

            if (countPiecesInOrder(order.toString(), 4, true) == 1 ||
                    countPiecesInOrder(order.toString(), 4, false) == 1)
                return true;
            order.replace(0, order.length(), "");
        }

        //check for vertical win
        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {
                order.append(getBoard(c, r));
            }

            if (countPiecesInOrder(order.toString(), 4, true) == 1 ||
                    countPiecesInOrder(order.toString(), 4, false) == 1)
                return true;
            order.replace(0, order.length(), "");
        }

        //check for diagonal win \
        //based on formula: y = x - b
        for (int b = -ROWS + 1; b < COLUMNS; b++) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = x - b;
                if (y >= 0 && y < ROWS) {
                    order.append(getBoard(x, y));
                }
            }

            if (countPiecesInOrder(order.toString(), 4, true) == 1 ||
                    countPiecesInOrder(order.toString(), 4, false) == 1)
                return true;

            order.replace(0, order.length(), "");
        }

        //check for diagonal win /
        //based on formula: y = -x - b
        for (int b = 0; b >= -(COLUMNS - 1) - (ROWS - 1); b--) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = -x - b;
                if (y >= 0 && y < ROWS) {
                    order.append(getBoard(x, y));
                }
            }

            if (countPiecesInOrder(order.toString(), 4, true) == 1 ||
                    countPiecesInOrder(order.toString(), 4, false) == 1)
                return true;

            order.replace(0, order.length(), "");
        }

        return false;
    }
    //End of Logic

    //Start of Algorithm
    @Override
    public int bestMove() {
        int column = 0;
        int bestScore = -100;
        int score = 0;

        for (Integer m : getAvailableMoves(this)) {

            score = minimax(this.playMove(m, true), 4, false);

            if (score > bestScore) {
                bestScore = score;
                column = m;
            }
        }
        return column;
    }

    int count = 0;

    public int minimax(VierGewinnt v, int depth, boolean turn) {
        int score = 0;
        int bestScore = 0;

        //Abbruchbedingung
        if (depth == 0 || v.isGameOver()) {
            return evaluate(v, !turn);
        }


        //Gehe alle Moves in jedem Spiel durch
        for (Integer m : getAvailableMoves(v)) {
            System.out.println(v.playMove(m, turn));
            count++;

            if(turn) {
                bestScore = Integer.MIN_VALUE;
                score = minimax(v.playMove(m, true), depth - 1, false);

                if(score > bestScore) {
                    bestScore = score;
                }
            } else {
                bestScore = Integer.MAX_VALUE;
                score = -minimax(v.playMove(m, false), depth - 1, true);

                if(score < bestScore) {
                    bestScore = score;
                }
            }
        }

        System.out.println("Count: " + count);

        return bestScore;
    }

    public int evaluate(VierGewinnt v, boolean turn) {
        int score = 0;
        StringBuilder order = new StringBuilder();

        for(int r = 0; r < ROWS; r++) {
            order.append(v.getBoard(COLUMNS / 2, r));
        }
        score += 3 * countPiecesInOrder(order.toString(), 1, turn);
        order.replace(0, order.length(), "");


        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                order.append(v.getBoard(c, r));
            }

            score += 100 * countPiecesInOrder(order.toString(), 4, turn);
            score += 15 * countPiecesInOrder(order.toString(), 3, turn);
            score += 2 * countPiecesInOrder(order.toString(), 2, turn);

            order.replace(0, order.length(), "");
        }


        //check for vertical win
        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {
                order.append(v.getBoard(c, r));
            }

            score += 100 * countPiecesInOrder(order.toString(), 4, turn);
            score += 15 * countPiecesInOrder(order.toString(), 3, turn);
            score += 2 * countPiecesInOrder(order.toString(), 2, turn);

            order.replace(0, order.length(), "");
        }

        //check for diagonal win \
        //based on formula: y = x - b
        for (int b = -ROWS + 1; b < COLUMNS; b++) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = x - b;
                if (y >= 0 && y < ROWS) {
                    order.append(v.getBoard(x, y));
                }
            }

            score += 100 * countPiecesInOrder(order.toString(), 4, turn);
            score += 15 * countPiecesInOrder(order.toString(), 3, turn);
            score += 2 * countPiecesInOrder(order.toString(), 2, turn);

            order.replace(0, order.length(), "");
        }

        //check for diagonal win /
        //based on formula: y = -x - b
        for (int b = 0; b >= -(COLUMNS - 1) - (ROWS - 1); b--) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = -x - b;
                if (y >= 0 && y < ROWS) {
                    order.append(v.getBoard(x, y));
                }
            }

            score += 100 * countPiecesInOrder(order.toString(), 4, turn);
            score += 15 * countPiecesInOrder(order.toString(), 3, turn);
            score += 2 * countPiecesInOrder(order.toString(), 2, turn);

            order.replace(0, order.length(), "");
        }

        return score;
    }

    public List<Integer> getAvailableMoves(VierGewinnt vg) {
        List<Integer> moveList = new ArrayList<>();
        for (int column = 0; column < COLUMNS; column++) {
            if (vg.getBoard(column, 0) == 0) {
                moveList.add(column);
            }
        }

        return moveList;
    }


    //  scan = 4
    //  |--|
    //001111222
    public static int countPiecesInOrder(String order, int amount, boolean player) {
        String scan = player ? "1".repeat(amount) : "2".repeat(amount);
        if(order.length() <= amount) {
            return 0;
        }
        return StringUtils.countMatches(order, scan);
    }
    //End of Algorithm


    //Getter
    @Override
    public int getBoard(int column, int row) {
        if (row < 0 || row >= ROWS) throw new IllegalArgumentException("Row out of bounds");
        if (column < 0 || column >= COLUMNS) throw new IllegalArgumentException("Column out of bounds");
        return board[row * COLUMNS + column];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (getBoard(j, i) == 0) sb.append("-  ");
                else if (getBoard(j, i) == 1) sb.append("X  ");
                else if (getBoard(j, i) == 2) sb.append("O  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    //End of Getter
}
