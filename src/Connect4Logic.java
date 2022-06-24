import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * @author Jerome Habanz
 */
public class Connect4Logic implements Connect4 {


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
    public Connect4Logic(Player startingPlayer) {
        this.board = new int[COLUMNS * ROWS];
        this.player = startingPlayer;
    }


    /**
     * The constructor initializes object with the given values.
     *
     * @param board          the board to initialize the game with
     * @param startingPlayer the player who starts the game
     */
    public Connect4Logic(int[] board, Player startingPlayer) {
        this.board = Arrays.copyOf(board, board.length);
        if (startingPlayer == Player.RED) {
            this.player = Player.RED;
        } else {
            this.player = Player.YELLOW;
        }
    }

    /**
     * static Method which generates a new Connect4Logic.
     *
     * @param board          the board to initialize the game with
     * @param startingPlayer the player who starts the game
     * @return a new Connect4Logic object game with the given values
     */
    public static Connect4Logic of(int[] board, Player startingPlayer) {
        return new Connect4Logic(board, startingPlayer);
    }


    /**
     * Method which generates a board with given play.
     *
     * @param column is the position to drop the coin
     * @return Connect4Logic object with played move
     */
    @Override
    public Connect4Logic playMove(int column) {
        Connect4Logic connect4 = Connect4Logic.of(this.board, this.player);

        if (column < 0 || column >= COLUMNS) throw new IllegalArgumentException("Column out of bounds");
        if (connect4.getBoard(column, 0) != 0) throw new IllegalArgumentException("Column is full");

        if (!isGameOver()) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (connect4.getBoard(column, row) == 0) {
                    if (connect4.player == Player.RED) {

                        connect4.board[column + row * COLUMNS] = 1;
                        connect4.player = Player.YELLOW;
                    } else if (connect4.player == Player.YELLOW) {

                        connect4.board[column + row * COLUMNS] = 2;
                        connect4.player = Player.RED;
                    }
                    break;
                }
            }
        }
        return connect4;
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
        int last = 0, count = 0;

        //check for horizontal win
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                //Falls aktuelles Feld leer || letztes Feld ungleich dem jetzigen Feld ||
                //Zurücksetzen des Counters & last
                if (getBoard(c, r) == 0 || last != getBoard(c, r)) {
                    count = 0;
                    last = 0;
                }

                //Zählen der einzelnen Felder
                if (getBoard(c, r) != 0) {
                    count++;
                    last = getBoard(c, r);
                }

                //Wenn 4 Felder in in einer Folge gefunden wurden
                if (count == 4) return true;
            }

            // Break
            count = 0;
            last = 0;
        }

        //check for vertical win
        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {
                //Falls aktuelles Feld leer || letztes Feld ungleich dem jetzigen Feld ||
                //Zurücksetzen des Counters & last
                if (getBoard(c, r) == 0 || last != getBoard(c, r)) {
                    count = 0;
                    last = 0;
                }

                //Zählen der einzelnen Felder
                if (getBoard(c, r) != 0) {
                    count++;
                    last = getBoard(c, r);
                }

                //Wenn 4 Felder in in einer Folge gefunden wurden
                if (count == 4) return true;
            }

            // Break
            count = 0;
            last = 0;
        }

        //check for diagonal win \
        for (int b = -ROWS + 1; b < COLUMNS; b++) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = x - b;
                if (y >= 0 && y < ROWS) {
                    if (getBoard(x, y) == 0 || last != getBoard(x, y)) {
                        count = 0;
                        last = 0;
                    }

                    //Zählen der einzelnen Felder
                    if (getBoard(x, y) != 0) {
                        count++;
                        last = getBoard(x, y);
                    }

                    //Wenn 4 Felder in in einer Folge gefunden wurden
                    if (count == 4) return true;
                }
            }

            // Break
            count = 0;
            last = 0;
        }

        //check for diagonal win /
        for (int b = 0; b >= -(COLUMNS - 1) - (ROWS - 1); b--) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = -x - b;

                if (y >= 0 && y < ROWS) {
                    if (getBoard(x, y) == 0 || last != getBoard(x, y)) {
                        count = 0;
                        last = 0;
                    }

                    //Zählen der einzelnen Felder
                    if (getBoard(x, y) != 0) {
                        count++;
                        last = getBoard(x, y);
                    }

                    //Wenn 4 Felder in in einer Folge gefunden wurden
                    if (count == 4) return true;
                }
            }

            // Break
            count = 0;
            last = 0;
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

        for (Integer m : getAvailableMoves()) {

            score = minimax(this.playMove(m), 3, player == Player.YELLOW);

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
     * @param v                is the Connect4Logic object
     * @param depth            maximum depth of the tree
     * @param maximizingPlayer is the player who is playing either 1 or 2
     * @return the score of the current board
     */
    public int minimax(Connect4Logic v, int depth, boolean maximizingPlayer) {
        int score;

        //Abbruchbedingung
        if (depth == 0 || v.isGameOver()) {
            return evaluate(v, !maximizingPlayer);
        }

        count++;
        System.out.println("Count: " + count);
        if (maximizingPlayer) {
            score = Integer.MIN_VALUE;
            for (Integer m : v.getAvailableMoves()) {
                score = Math.max(score, minimax(v.playMove(m), depth - 1, false));
            }
            return score;
        } else {
            score = Integer.MAX_VALUE;
            for (Integer m : v.getAvailableMoves()) {
                score = Math.min(score, minimax(v.playMove(m), depth - 1, true));
            }
            return score;
        }
    }


    /**
     * Evaluate the current board.
     *
     * @param c      is the Connect4Logic object
     * @param player is the player who is playing either 1 or 2
     * @return the score of the current board
     */
    public int evaluate(Connect4Logic c, boolean player) {
        int[] count = new int[3];
        int size = 50;
        for(int i = 0; i < size; i++) {
            count[random_game(c)]++;
        }

        //System.out.println(count[0]);
        //System.out.println(count[1]);
        //System.out.println(count[2]);
        //System.out.println();
        if(player) {
            return (int) ((float)count[1] / (float) size * 100);
        } else {
            return (int) ((float)count[2] / (float) size * 100);
        }
    }

    public int random_game(Connect4Logic c) {

        long startTime = System.nanoTime();
        while(!c.isGameOver()) {
            c = c.playMove(c.getAvailableMoves().get(new Random().nextInt(c.getAvailableMoves().size())));
        }

        long endTime = System.nanoTime();
        System.out.println("Time" + (float)(endTime - startTime)/1000000);


        if(c.checkForWin()) {
            return c.getPlayer() == Player.RED ? 1 : 2;
        }
        return 0;
    }


    /**
     * Check the top column for empty spaces and add to list.
     *
     * @return a list of all available moves
     */
    public List<Integer> getAvailableMoves() {
        List<Integer> moveList = new ArrayList<>();
        for (int column = 0; column < COLUMNS; column++) {
            if (getBoard(column, 0) == 0) {
                moveList.add(column);
            }
        }
        return moveList;
    }

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
     *
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
