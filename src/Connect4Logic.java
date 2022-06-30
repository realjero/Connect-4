import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


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
     * Plays a move on the board.
     *
     * @param column column to drop piece
     */
    @Override
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
     * @return true if the game is over, false otherwise
     */
    @Override
    public boolean isGameOver() {
        return isWin(bitboard[0]) || isWin(bitboard[1]) || (bitboard[0] ^ bitboard[1]) == 0b0111111_0111111_0111111_0111111_0111111_0111111_0111111L;
    }

    @Override
    public int getBoard(int column, int row) {
        long pos = (long) column * (ROWS + 1) + ROWS - 1 - row;
        if (((bitboard[0] >> pos) & 1) == 1) return 2;
        if (((bitboard[1] >> pos) & 1) == 1) return 1;
        return 0;
    }

    @Override
    public boolean getPlayer() {
        return (counter & 1) == 1;
    }


    /**
     * @param column column to drop piece
     * @return lowest binary position in column that is empty
     */
    public long getMove(int column) {
        long move = 1L << (column * 7);
        for (int pos = 0; pos < ROWS; pos++) {
            if (((bitboard[0] | bitboard[1]) & move) == 0) {
                return move;
            }
            move <<= 1;
        }
        return 0;
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
     * @return available moves
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
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                switch (getBoard(x, y)) {
                    case 2:
                        sb.append("  X");
                        break;
                    case 1:
                        sb.append("  O");
                        break;
                    default:
                        sb.append("  .");
                        break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static long[] toBitBoard(String s) {
        long[] bitboard = new long[2];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                long pos = c * (ROWS + 1) + ROWS - 1 - r;
                switch (s.charAt(r * COLUMNS + c)) {
                    case '2':
                        bitboard[0] ^= 1L << pos;
                        break;
                    case '1':
                        bitboard[1] ^= 1L << pos;
                        break;
                    default:
                        break;
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
        int column = 0;
        int bestScore = Integer.MIN_VALUE;
        int score;
        Connect4Logic c = this;

        for (Integer m : c.getAvailableMoves()) {

            //score = evaluate(c.play(m), (c.counter & 1) == 0);
            score = minimax(c.play(m), 2, (c.counter & 1) == 0);

            if (score > bestScore) {
                bestScore = score;
                column = m;
            }
        }
        return column;
    }

    /**
     * Based on MiniMax algorithm.
     *
     * @param connect4         is the Connect4Logic object
     * @param depth            maximum depth of the tree
     * @param maximizingPlayer is the player whos turn it is
     * @return the score of the current board
     */
    public int minimax(Connect4Logic connect4, int depth, boolean maximizingPlayer) {
        int score;

        Connect4Logic c = Connect4Logic.valueOf(connect4.bitboard, connect4.moves, connect4.counter);

        //Abbruchbedingung
        if (depth == 0 || c.isGameOver()) {
            return evaluate(c);
        }

        if (maximizingPlayer) {
            score = Integer.MIN_VALUE;
            for (Integer m : c.getAvailableMoves()) {
                score = Math.max(score, minimax(c.play(m), depth - 1, false));
            }
            return score;
        } else {
            score = Integer.MAX_VALUE;
            for (Integer m : c.getAvailableMoves()) {
                score = Math.min(score, minimax(c.play(m), depth - 1, true));
            }
            return score;
        }
    }

    /**
     * Evaluate the current board.
     *
     * @param c is the Connect4Logic object
     * @return the score of the current board
     */
    public int evaluate(Connect4Logic c) {
        int score = 0;
        int size = 200;
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

        while (!c.isGameOver()) {
            c = c.play(c.getAvailableMoves().get(new Random().nextInt(c.getAvailableMoves().size())));
        }
        if (c.isGameOver()) {
            return (c.counter & 1) == 1 ? -1 : 1;
        }
        return 0;
    }
}
