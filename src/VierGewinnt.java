import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Jerome Habanz
 */
public class VierGewinnt implements VierGewinntLogic {


    /**
     * The board is represented as array of integers.
     * The value of red = 1, yellow = 2, empty = 0.
     */
    private int[] board;
    private Player player;


    /**
     * The constructor initializes object with a empty board and starting player.
     *
     * @param startingPlayer the player who starts the game
     */
    public VierGewinnt(Player startingPlayer) {
        this.board = new int[COLUMNS * ROWS];
        this.player = startingPlayer;
    }


    /**
     * The constructor initializes object with the given values.
     *
     * @param board          the board to initialize the game with
     * @param startingPlayer the player who starts the game
     */
    public VierGewinnt(int[] board, Player startingPlayer) {
        this.board = Arrays.copyOf(board, board.length);
        this.player = startingPlayer;
    }

    /**
     * static Method which generates a new VierGewinnt.
     *
     * @param board          the board to initialize the game with
     * @param startingPlayer the player who starts the game
     * @return a new VierGewinnt object game with the given values
     */
    public static VierGewinnt of(int[] board, Player startingPlayer) {
        return new VierGewinnt(board, startingPlayer);
    }


    /**
     * Method which generates a board with given play.
     *
     * @param column is the position to drop the coin
     * @return VierGewinnt object with played move
     */
    @Override
    public VierGewinnt playMove(int column) {
        VierGewinnt vg = VierGewinnt.of(this.board, this.player);

        if (column < 0 || column >= COLUMNS) throw new IllegalArgumentException("Column out of bounds");
        if (vg.getBoard(column, 0) != 0) throw new IllegalArgumentException("Column is full");

        if (!isGameOver()) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (vg.getBoard(column, row) == 0) {
                    if (vg.player == Player.RED) {

                        vg.board[column + row * COLUMNS] = 1;
                        vg.player = Player.YELLOW;
                    } else if (vg.player == Player.YELLOW) {

                        vg.board[column + row * COLUMNS] = 2;
                        vg.player = Player.RED;
                    }
                    break;
                }
            }
        }
        return vg;
    }

    /**
     * Check if the game is over.
     *
     * @return TIE or checkForWin()
     */
    @Override
    public boolean isGameOver() {
        return Arrays.stream(board).allMatch(i -> i != 0) || checkForWin();
    }


    /**
     * Method which checks if there is a winner.
     * Checks: horizontal, vertical or diagonal orders of 4 coins.
     * <p>
     * diagonal win \ formula y = x - b
     * diagonal win / formula y = -x - b
     * <p>
     * based on every check a String will be generated which is then evaluated.
     *
     * @return if anyone has a winning order
     */
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


    /**
     * Check for any higher score and make it the best move.
     *
     * @return column of best move
     */
    @Override
    public int bestMove() {
        int column = 0;
        int bestScore = Integer.MIN_VALUE;
        int score;

        for (Integer m : getAvailableMoves(this)) {

            score = minimax(this.playMove(m), 1, false);

            if (score > bestScore) {
                bestScore = score;
                column = m;
            }
        }
        return column;
    }

    int count = 0;

    /**
     * Based on MiniMax algorithm.
     *
     * @param v                is the VierGewinnt object
     * @param depth            maximum depth of the tree
     * @param maximizingPlayer is the player who is playing either 1 or 2
     * @return the score of the current board
     */
    public int minimax(VierGewinnt v, int depth, boolean maximizingPlayer) {
        int score;

        //Abbruchbedingung
        if (depth == 0 || v.isGameOver()) {
            return evaluate(v, !maximizingPlayer);
        }

        count++;
        System.out.println("Count: " + count);
        if (maximizingPlayer) {
            score = Integer.MIN_VALUE;
            for (Integer m : getAvailableMoves(v)) {
                score = Math.max(score, minimax(v.playMove(m), depth - 1, false));
            }
            return score;
        } else {
            score = Integer.MAX_VALUE;
            for (Integer m : getAvailableMoves(v)) {
                score = Math.min(score, minimax(v.playMove(m), depth - 1, true));
            }
            return score;
        }
    }


    /**
     * Evaluate the current board.
     *
     * @param v    is the VierGewinnt object
     * @param player is the player who is playing either 1 or 2
     * @return the score of the current board
     */
    public int evaluate(VierGewinnt v, boolean player) {
        int score = 0;
        StringBuilder order = new StringBuilder();

        for (int r = 0; r < ROWS; r++) {
            order.append(v.getBoard(COLUMNS / 2, r));
        }
        score += 3 * countPiecesInOrder(order.toString(), 1, player);
        order.replace(0, order.length(), "");


        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                order.append(v.getBoard(c, r));
            }

            score += 100 * countPiecesInOrder(order.toString(), 4, player);
            score += 15 * countPiecesInOrder(order.toString(), 3, player);
            score += 2 * countPiecesInOrder(order.toString(), 2, player);

            order.replace(0, order.length(), "");
        }


        //check for vertical win
        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {
                order.append(v.getBoard(c, r));
            }

            score += 100 * countPiecesInOrder(order.toString(), 4, player);
            score += 15 * countPiecesInOrder(order.toString(), 3, player);
            score += 2 * countPiecesInOrder(order.toString(), 2, player);

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

            score += 100 * countPiecesInOrder(order.toString(), 4, player);
            score += 15 * countPiecesInOrder(order.toString(), 3, player);
            score += 2 * countPiecesInOrder(order.toString(), 2, player);

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

            score += 100 * countPiecesInOrder(order.toString(), 4, player);
            score += 15 * countPiecesInOrder(order.toString(), 3, player);
            score += 2 * countPiecesInOrder(order.toString(), 2, player);

            order.replace(0, order.length(), "");
        }

        return score;
    }


    /**
     * Check the top column for empty spaces and add to list.
     *
     * @param vg is the VierGewinnt object
     * @return a list of all available moves
     */
    public List<Integer> getAvailableMoves(VierGewinnt vg) {
        List<Integer> moveList = new ArrayList<>();
        for (int column = 0; column < COLUMNS; column++) {
            if (vg.getBoard(column, 0) == 0) {
                moveList.add(column);
            }
        }

        return moveList;
    }


    /**
     * Count matches of amount in order
     * @param order  horizontal, vertical or diagonal order of pieces
     * @param amount amount of pieces in order to be checked
     * @param player is the player who is playing either 1 or 2
     * @return count matches of amount in order
     */
    public static int countPiecesInOrder(String order, int amount, boolean player) {
        String scan = player ? "1".repeat(amount) : "2".repeat(amount);
        if (order.length() <= amount) {
            return 0;
        }
        return StringUtils.countMatches(order, scan);
    }
    //End of Algorithm


    /**
     * @param column column to show
     * @param row    row to show
     * @return state of board at row * COLUMNS + column
     */
    @Override
    public int getBoard(int column, int row) {
        if (row < 0 || row >= ROWS) throw new IllegalArgumentException("Row out of bounds");
        if (column < 0 || column >= COLUMNS) throw new IllegalArgumentException("Column out of bounds");
        return board[row * COLUMNS + column];
    }

    /**
     * @return current player either RED or YELLOW
     */
    @Override
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Represents the board as a string.
     * @return representation of board as String
     */
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
}
