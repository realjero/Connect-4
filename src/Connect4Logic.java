import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//TODO: Die Logging-Ausgabe erlaubt es, den Algorithmus in seiner Arbeitsweise nachzuvollziehen
//TODO: DOKUMENTATION

/**
 * @author Jerome Habanz
 */
public class Connect4Logic implements Connect4 {

    /**
     * Each player has a 64 bit bitboard that represents the board.
     */
    private long[] bitboard;
    private long[] moves;
    private int counter;
    final int COLUMNS = Connect4.COLUMNS;
    final int ROWS = Connect4.ROWS;
    final int difficulty = 3;

    /**
     * The constructor initializes the bitboard, the moves array and the counter as a new game.
     */
    public Connect4Logic() {
        bitboard = new long[2];
        counter = 0;
        moves = new long[ROWS * COLUMNS];
    }

    /**
     * The constructor initializes object with the given values.
     *
     * @param bitboard bitboard of players
     * @param moves    past moves
     * @param counter  number of moves
     */
    public Connect4Logic(long[] bitboard, long[] moves, int counter) {
        this.bitboard = bitboard;
        this.moves = moves;
        this.counter = counter;
    }

    /**
     * Generates a copy of Connect4Logic.
     *
     * @param bitboard bitboard of players
     * @param moves    past moves
     * @param counter  number of moves
     * @return a copy of Connect4Logic
     */
    public static Connect4Logic valueOf(long[] bitboard, long[] moves, int counter) {
        return new Connect4Logic(Arrays.copyOf(bitboard, bitboard.length), Arrays.copyOf(moves, moves.length), counter);
    }

    /**
     * @param bitboard bitboard to compareTo
     * @return true if equal, false otherwise
     */
    public boolean compareTo(long[] bitboard) {
        return Arrays.equals(this.bitboard, bitboard);
    }


    /**
     * Plays a move on the board.
     *
     * @param column column to drop piece
     * @return a copy of Connect4Logic with given move
     */
    public Connect4Logic play(int column) {
        long move = getMove(column);

        if (column < 0 || column >= COLUMNS) throw new IllegalArgumentException("Invalid column");
        if (isGameOver()) throw new RuntimeException("Game Over");
        if (move == 0L) throw new IllegalArgumentException("Full column");

        Connect4Logic c = Connect4Logic.valueOf(this.bitboard, this.moves, this.counter);

        c.moves[c.counter] = move;
        c.bitboard[c.counter & 1] ^= c.moves[c.counter];
        c.counter++;
        return c;
    }

    /**
     * Undo the last move.
     *
     * @return a copy of Connect4Logic without the last move.
     */
    public Connect4Logic undoMove() {
        if (counter == 0) throw new RuntimeException("No moves to undo");
        Connect4Logic c = Connect4Logic.valueOf(this.bitboard, this.moves, this.counter);
        c.counter--;
        c.bitboard[c.counter & 1] ^= c.moves[c.counter];
        c.moves[c.counter] = 0;

        return c;
    }

    /**
     * @param bitboard bitboard of player
     * @return true if 4 pieces in a row, false otherwise
     */
    public boolean isWin(long bitboard) {
        int[] directions = {1, 7, 6, 8};
        long bb;
        for (int direction : directions) {
            bb = bitboard & (bitboard >> direction);
            if ((bb & (bb >> (2 * direction))) != 0) return true;
        }
        return false;
    }

    /**
     * @return 0 if game is not over, 1 if player 0 wins, 2 if player 1 wins, 3 if tied.
     */
    public int intIsGameOver() {
        if ((counter & 1) == 1 && isWin(bitboard[0])) return 1; //X wins
        if ((counter & 1) == 0 && isWin(bitboard[1])) return 2; //O wins
        if ((bitboard[0] ^ bitboard[1]) == 0b0111111_0111111_0111111_0111111_0111111_0111111_0111111L) return 3; //Tied
        return 0;
    }

    /**
     * @return true if game is over, false otherwise.
     */
    public boolean isGameOver() {
        return intIsGameOver() != 0;
    }


    /**
     * @param column column of piece
     * @param row    row of piece
     * @return state of piece at given row and column
     */
    public int getBoard(int column, int row) {
        long pos = (long) column * (ROWS + 1) + ROWS - 1 - row;
        if (((bitboard[0] >> pos) & 1) == 1) return 2;
        if (((bitboard[1] >> pos) & 1) == 1) return 1;
        return 0;
    }

    /**
     * @return true if X is turning next, false otherwise.
     */
    public boolean getPlayer() {
        return (counter & 1) == 1;
    }

    /**
     * @param column column to drop piece
     * @return lowest binary position in column that is empty
     */
    public long getMove(int column) {
        long move = 1L << (column * 7);
        long board = (bitboard[0] | bitboard[1]);
        for (int pos = 0; pos < ROWS; pos++) {
            if ((board & move) == 0) {
                return move;
            }
            move <<= 1;
        }
        return 0;
    }

    /**
     * @param column column of piece
     * @return position of the lowest empty row in column
     */
    public int getNextHeight(int column) {
        long move = getMove(column) >> (column * 7);
        if (move == 0L || move == 0b1000000L) return 0;
        return Long.toBinaryString(move).length();
    }

    /**
     * @return list available moves
     */
    public List<Integer> getAvailableMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < COLUMNS; i++) {
            if (getMove(i) != 0L) {
                moves.add(i);
            }
        }
        return moves;
    }

    /**
     * @return Board as String
     */
    public String toString() {//jshell doesnt render this well
        StringBuilder sb = new StringBuilder("\n");

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                sb.append(switch (getBoard(x, y)) {
                    case 1 -> "X";
                    case 2 -> "O";
                    default -> ".";
                });
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @param s string to parse
     * @return bitboard of given string
     */
    public static long[] toBitBoard(String s) {
        long[] bitboard = new long[2];

        for (int r = 0; r < Connect4.ROWS; r++) {
            for (int c = 0; c < Connect4.COLUMNS; c++) {
                long pos = c * (Connect4.ROWS + 1) + Connect4.ROWS - 1 - r;
                switch (s.charAt(r * Connect4.COLUMNS + c)) {
                    case '2' -> bitboard[0] ^= 1L << pos;
                    case '1' -> bitboard[1] ^= 1L << pos;
                    default -> {
                    }
                }
            }
        }
        return bitboard;
    }


    /**
     * Check for any higher score and make it the best move.
     *
     * @return column of best move
     */
    public int bestMove() {
        Connect4Logic c = this;
        int column = 0;
        int bestScore = Integer.MIN_VALUE;
        int score;

        for (Integer m : c.getAvailableMoves()) {

            //score = evaluate(c.play(m)); // 85 ms //working
            //score = minimax(c.play(m), difficulty, false); // 2331 ms //working
            //score = negamax(c.play(m), difficulty, false); // 2441 ms //working?
            score = alphabeta(c.play(m), difficulty, Integer.MIN_VALUE, Integer.MAX_VALUE, false); // 103 ms //working?

            System.out.println("MOVE: " + m + " MAX: " + score);

            if (score > bestScore) {
                bestScore = score;
                column = m;
            }
        }

        System.out.println("Best move: " + column + " BestScore: " + bestScore + "\n");
        return column;
    }

    /**
     * Based on MiniMax algorithm.
     *
     * @param connect4         is the Connect4Logic object
     * @param depth            maximum depth of the tree
     * @param maximizingPlayer is the player whose turn it is
     * @return the score of the current board
     */
    public int minimax(Connect4Logic connect4, int depth, boolean maximizingPlayer) {
        int score;

        Connect4Logic c = Connect4Logic.valueOf(connect4.bitboard, connect4.moves, connect4.counter);

        //Abbruchbedingung
        if (depth == 0 || c.isGameOver()) {
            return maximizingPlayer ? -evaluate(c) : evaluate(c);
        }

        if (maximizingPlayer) {
            score = Integer.MIN_VALUE;
            for (Integer m : c.getAvailableMoves()) {
                score = Math.max(score, minimax(c.play(m), depth - 1, false));
                System.out.println("\t".repeat(difficulty + 1 - depth) + "MOVE: " + m + (maximizingPlayer ? " MAX: " : " MIN : ") + score);
            }
        } else {
            score = Integer.MAX_VALUE;
            for (Integer m : c.getAvailableMoves()) {
                score = Math.min(score, minimax(c.play(m), depth - 1, true));
                System.out.println("\t".repeat(difficulty + 1 - depth) + "MOVE: " + m + (maximizingPlayer ? " MAX: " : " MIN : ") + score);
            }
        }

        return score;
    }

    /**
     * Based on Negamax algorithm.
     *
     * @param connect4         is the Connect4Logic object
     * @param depth            maximum depth of the tree
     * @param maximizingPlayer is the player whose turn it is
     * @return the score of the current board
     */
    public int negamax(Connect4Logic connect4, int depth, boolean maximizingPlayer) {
        int score;

        Connect4Logic c = Connect4Logic.valueOf(connect4.bitboard, connect4.moves, connect4.counter);

        //Abbruchbedingung
        if (depth == 0 || c.isGameOver()) {
            return evaluate(c);
        }

        score = Integer.MIN_VALUE;
        for (Integer m : c.getAvailableMoves()) {
            score = Math.max(score, -negamax(c.play(m), depth - 1, !maximizingPlayer));
            System.out.println("\t".repeat(difficulty + 1 - depth) + "MOVE: " + m + (maximizingPlayer ? " MAX: " : " MIN : ") + score);
        }
        return score;
    }

    /**
     * Based on Negamax algorithm with alpha-beta pruning.
     *
     * @param connect4         is the Connect4Logic object
     * @param depth            maximum depth of the tree
     * @param alpha            lower bound of the score
     * @param beta             upper bound of the score
     * @param maximizingPlayer is the player whose turn it is
     * @return the score of the current board
     */
    public int alphabeta(Connect4Logic connect4, int depth, int alpha, int beta, boolean maximizingPlayer) {
        int score;

        Connect4Logic c = Connect4Logic.valueOf(connect4.bitboard, connect4.moves, connect4.counter);

        //Abbruchbedingung
        if (depth == 0 || c.isGameOver()) {
            return evaluate(c);
        }

        score = Integer.MIN_VALUE;
        for (Integer m : c.getAvailableMoves()) {
            score = Math.max(score, -alphabeta(c.play(m), depth - 1, -beta, -alpha, !maximizingPlayer));
            System.out.println("\t".repeat(difficulty + 1 - depth) + "MOVE: " + m + (maximizingPlayer ? " MAX: " : " MIN : ") + score);
            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                break;
            }
        }
        return score;
    }

    /**
     * Evaluate the current board.
     *
     * @param c is the Connect4Logic object
     * @return the score of the current board
     */
    public int evaluate(Connect4Logic c) {
        int score = 0;
        int size = 500;
        for (int i = 0; i < size; i++) {
            score += random_game(c);
        }
        return score;
    }

    /**
     * @param connect4 is the Connect4Logic object
     * @return endState of a random game {0, 1, -1}
     */
    public int random_game(Connect4Logic connect4) {
        Connect4Logic c = Connect4Logic.valueOf(connect4.bitboard, connect4.moves, connect4.counter);

        boolean player = c.getPlayer();

        while (!c.isGameOver()) {
            c = c.play(c.getAvailableMoves().get(new Random().nextInt(c.getAvailableMoves().size())));
        }

        return switch (c.intIsGameOver()) {
            case 1 -> player ? 1 : -1;
            case 2 -> player ? -1 : 1;
            default -> 0;
        };
    }
}
