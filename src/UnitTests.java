import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitTests {

    @Test
    void testGameOver() {
        //Leeres Spielfeld -> Spiel nicht beendet
        int[] board = new int[]{0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0};
        assertEquals(false, VierGewinnt.of(board).isGameOver());

        //Volles Spielfeld -> Spiel beendet
        int[] board1 = new int[]{1, 2, 1, 2, 1, 2, 1,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                2, 1, 2, 1, 2, 1, 2};
        assertEquals(true, VierGewinnt.of(board1).isGameOver());

        testGameOverHori();
        testGameOverVert();
        testGameOverDiag1();
        testGameOverDiag2();
    }

    void testGameOverHori() {
        int board[] = {0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                1, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 1,
                1, 1, 1, 1, 0, 0, 0};

        assertEquals(true, VierGewinnt.of(board).isGameOver());
    }

    void testGameOverVert() {
        int board[] = {0, 1, 1, 0, 0, 0, 0,
                0, 0, 1, 1, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0,
                0, 1, 1, 0, 0, 0, 0,
                0, 1, 1, 0, 0, 0, 0};

        assertEquals(true, VierGewinnt.of(board).isGameOver());
    }

    void testGameOverDiag1() {
        int board[] = {1, 0, 1, 0, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 1, 1, 0, 0,
                0, 0, 0, 1, 0, 1, 0,
                0, 0, 0, 0, 1, 0, 1};

        assertEquals(true, VierGewinnt.of(board).isGameOver());
    }

    void testGameOverDiag2() {
        int board[] = {0, 1, 0, 1, 0, 0, 0,
                0, 0, 0, 1, 0, 1, 0,
                0, 0, 0, 1, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0,
                0, 1, 0, 1, 0, 0, 0,
                1, 0, 1, 0, 0, 0, 0};

        assertEquals(true, VierGewinnt.of(board).isGameOver());
    }
}