public interface Connect4 {
    int COLUMNS = 7;
    int ROWS = 6;

    Connect4Logic play(int column);

    Connect4Logic undoMove();

    int bestMove();

    int intIsGameOver();

    boolean isGameOver();

    int getBoard(int row, int column);

    boolean getPlayer();

    int getNextHeight(int column);
}
