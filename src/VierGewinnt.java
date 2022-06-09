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

    //  |  |
    //001111222
    public static int countPiecesInOrder(String order, int amount, boolean player) {
        return 0;
        //return StringUtils.countMatches(order, "11");
    }
    private boolean checkForWin() {
        int last = 0;
        int count = 0;

        //check for horizontal win
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                //Falls aktuelles Feld leer || letztes Feld ungleich dem jetzigen Feld
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
            count = 0;
            last = 0;
        }

        //check for vertical win
        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {
                //Falls aktuelles Feld leer || letztes Feld ungleich dem jetzigen Feld
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
            count = 0;
            last = 0;
        }

        //check for diagonal win \
        //based on formula: y = x - b
        for (int b = -ROWS + 1; b < COLUMNS; b++) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = x - b;
                if (y >= 0 && y < ROWS) {
                    //Counter for diagonals
                    //Falls aktuelles Feld leer || letztes Feld ungleich dem jetzigen Feld
                    //Zurücksetzen des Counters & last
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
            count = 0;
            last = 0;
        }

        //check for diagonal win /
        //based on formula: y = -x - b
        for (int b = 0; b >= -(COLUMNS - 1) - (ROWS - 1); b--) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = -x - b;
                if (y >= 0 && y < ROWS) {
                    //Falls aktuelles Feld leer || letztes Feld ungleich dem jetzigen Feld
                    //Zurücksetzen des Counters & last
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
            count = 0;
            last = 0;
        }

        return false;
    }
    //End of Logic

    //Start of Algorithm
    @Override
    public int bestMove() {
        return 0;
    }


    int count = 0;

    public int minimax(VierGewinnt v, int depth, boolean turn) {

        //Abbruchbedingung
        if (depth == 0 || v.isGameOver()) {
            return 1;
        }


        //Gehe alle Moves in jedem Spiel durch
        for (Integer m : getAvailableMoves(v)) {
            System.out.println(v.playMove(m, turn));
            count++;
            minimax(v.playMove(m, turn), depth - 1, !turn);
        }

        System.out.println("Count: " + count);

        return 0;
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
