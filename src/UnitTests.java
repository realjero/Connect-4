import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertFalse(Connect4Logic.of(board, Player.RED).isGameOver());

        //Volles Spielfeld -> Spiel beendet
        int[] board1 = new int[]{1, 2, 1, 2, 1, 2, 1,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                2, 1, 2, 1, 2, 1, 2};
        assertTrue(Connect4Logic.of(board1, Player.RED).isGameOver());

        testGameOverHori();
        testGameOverVert();
        testGameOverDiag1();
        testGameOverDiag2();
    }

    void testGameOverHori() {
        int[] board = {0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                1, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 1,
                1, 1, 1, 1, 0, 0, 0};

        assertTrue(Connect4Logic.of(board, Player.RED).isGameOver());
    }

    void testGameOverVert() {
        int[] board = {0, 1, 1, 0, 0, 0, 0,
                0, 0, 1, 1, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0,
                0, 1, 1, 0, 0, 0, 0,
                0, 1, 1, 0, 0, 0, 0};

        assertTrue(Connect4Logic.of(board, Player.RED).isGameOver());
    }

    void testGameOverDiag1() {
        int[] board = {1, 0, 1, 0, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 1, 1, 0, 0,
                0, 0, 0, 1, 0, 1, 0,
                0, 0, 0, 0, 1, 0, 1};

        assertTrue(Connect4Logic.of(board, Player.RED).isGameOver());
    }

    void testGameOverDiag2() {
        int[] board = {0, 1, 0, 1, 0, 1, 0,
                0, 0, 0, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0,
                0, 1, 1, 1, 0, 0, 0,
                1, 1, 1, 0, 0, 0, 0};

        assertFalse(Connect4Logic.of(board, Player.RED).isGameOver());
    }

    @Test
    void testGetAvailableMoves() {
        int[] board = new int[]{1, 2, 1, 2, 1, 0, 2,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                2, 1, 2, 1, 2, 1, 2};

        //assertEquals(1, Connect4Logic.of(board).getAvailableMoves(Connect4Logic.of(board)).size());
    }

    @Test
    void testMiniMax() {
        /*
        int[] board = new int[]{1, 2, 1, 0, 0, 0, 0,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                1, 2, 1, 1, 1, 2, 1,
                2, 1, 2, 2, 2, 1, 2,
                2, 1, 2, 1, 2, 1, 2};

         */

        Connect4Logic vg = new Connect4Logic(Player.RED);
        vg.minimax(vg, 7, true);
    }

    @Test
    void testCheck4InOrder() {
        Connect4Logic c4 = new Connect4Logic(Player.RED);
        assertEquals(1, c4.check4InOrder("121121121111"));
        assertEquals(2, c4.check4InOrder("112121212222"));
        assertEquals(0, c4.check4InOrder("2221212121212"));
        assertEquals(0, c4.check4InOrder("1212121222121"));
    }
}