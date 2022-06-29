public interface Connect4 {
    int COLUMNS = 7;
    int ROWS = 6;

    void play(int column);

    int bestMove();

    boolean isGameOver();

    int getBoard(int row, int column);

    boolean getPlayer();
}
