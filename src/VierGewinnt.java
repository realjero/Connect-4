import java.util.Arrays;

public class VierGewinnt implements VierGewinntLogic {

    int board[];

    public VierGewinnt() {
        board = new int[COLUMNS * ROWS];
    }

    public VierGewinnt(int[] board) {
        this.board = board;
    }

    public static VierGewinnt of(int[] board) {
        return new VierGewinnt(board);
    }

    @Override
    public VierGewinnt playMove(int column, boolean turn) {
        VierGewinnt newBoard = this;

        if (column < 0 || column >= COLUMNS) throw new IllegalArgumentException("Column out of bounds");
        if (newBoard.getBoard(column, 0) != 0) throw new IllegalArgumentException("Column is full");

        if (!isGameOver()) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (newBoard.getBoard(column, row) == 0) {
                    newBoard.board[row * COLUMNS + column] = turn ? 1 : 2;
                    break;
                }
            }
        }

        return newBoard;
    }

    @Override
    public int bestMove() {
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return Arrays.stream(board).allMatch(i -> i != 0) || checkForWin();
    }

    private boolean checkForWin() {
        int last = 0;
        int count = 0;

        //check for horizontal win
        for (int i = 0; i < ROWS * COLUMNS; i++) {

            //Falls aktuelles Feld leer || letztes Feld ungleich dem jetzigen Feld ||
            //Zurücksetzen des Counters & last
            if (board[i] == 0 || last != board[i] || i % COLUMNS == 0) {
                count = 0;
                last = 0;
            }

            //Zählen der einzelnen Felder
            if (board[i] != 0) {
                count++;
                last = board[i];
            }

            //Wenn 4 Felder in in einer Folge gefunden wurden
            if (count == 4) return true;
        }

        //check for vertical win
        for (int i = 0; i < ROWS * COLUMNS; i++) {

            //i horizontal to vertical j
            //i % ROWS == iterate over rows
            //i / ROWS == iterate over columns
            int j = (i % ROWS) * COLUMNS + i / ROWS;

            if (board[j] == 0 || last != board[j] || i % ROWS == 0) {
                count = 0;
                last = 0;
            }

            if (board[j] != 0) {
                count++;
                last = board[j];
            }

            if (count == 4) return true;
        }

        //check for diagonal win \
        //based on formula: y = -1*(-x + b)
        for (int b = -ROWS + 1; b < COLUMNS; b++) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = -1 * (-x + b);
                if (y >= 0 && y < ROWS) {
                    //System.out.print(x + "=x " + y + "=y    ");
                    //System.out.print(getBoard(x, y) + " ");


                    //Counter for diagonals\
                    if (getBoard(x, y) == 0 || last != getBoard(x, y)) {
                        count = 0;
                        last = 0;
                    }

                    if (getBoard(x, y) != 0) {
                        count++;
                        last = getBoard(x, y);
                    }

                    if (count == 4) return true;

                }
            }
            count = 0;
        }


        //check for diagonal win /
        //based on formula: y = -1*(x + b)
        for (int b = 0; b >= -(COLUMNS - 1) - (ROWS - 1); b--) {
            for (int x = 0; x < COLUMNS; x++) {
                int y = -1 * (x + b);
                if (y >= 0 && y < ROWS) {
                    //Counter for diagonals\
                    if (getBoard(x, y) == 0 || last != getBoard(x, y)) {
                        count = 0;
                        last = 0;
                    }

                    if (getBoard(x, y) != 0) {
                        count++;
                        last = getBoard(x, y);
                    }

                    if (count == 4) return true;
                }
            }
            count = 0;
        }

        return false;
    }

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
}
