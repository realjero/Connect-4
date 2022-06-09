public interface VierGewinntLogic {
    int COLUMNS = 7;
    int ROWS = 6;

    VierGewinnt playMove(int column, boolean turn);

    int bestMove();

    boolean isGameOver();

    int getBoard(int row, int column);
}
