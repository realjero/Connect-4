import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {

    //Interface Tests
    @Test
    public void isGameOverHor() {
        Connect4 c = Connect4Logic.valueOf(Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "1110000" +
                "2222000"), new long[]{}, 7);
        assertTrue(c.isGameOver());
    }

    @Test
    public void isGameOverVer() {
        Connect4 c = Connect4Logic.valueOf(Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "2000000" +
                "2100000" +
                "2100000" +
                "2100000"), new long[]{}, 7);
        assertTrue(c.isGameOver());
    }

    @Test
    void isGameOverDiag() {
        Connect4 c = Connect4Logic.valueOf(Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0002000" +
                "0021000" +
                "0210000" +
                "2100000"), new long[]{}, 7);
        assertTrue(c.isGameOver());

        c = Connect4Logic.valueOf(Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0002000" +
                "0020000" +
                "0200000" +
                "2000000"), new long[]{}, 7);
        assertTrue(c.isGameOver());
    }

    @Test
    public void isTie() {
        Connect4 c = Connect4Logic.valueOf(Connect4Logic.toBitBoard("2121212" +
                "2121212" +
                "2121212" +
                "1212121" +
                "1212121" +
                "1212121"), new long[]{}, 8);
        assertEquals(3, c.intIsGameOver());
    }

    @Test
    public void getBoard() {
        Connect4 c = Connect4Logic.valueOf(Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "2222222"), new long[]{}, 0);
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < Connect4.COLUMNS; x++) {
            sb.append(c.getBoard(x, 5));
        }
        assertEquals("2222222", sb.toString());
    }

    //Logic Tests
    @Test
    public void play() {
        long[] bitboard = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "2121212");

        Connect4Logic c = new Connect4Logic();
        c = c.play(0);
        c = c.play(1);
        c = c.play(2);
        c = c.play(3);
        c = c.play(4);
        c = c.play(5);
        c = c.play(6);

        assertTrue(c.compareTo(bitboard));
    }

    @Test
    public void undoMove() {
        long[] bitboard = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "2121210");

        Connect4Logic c = new Connect4Logic();
        c = c.play(0);
        c = c.play(1);
        c = c.play(2);
        c = c.play(3);
        c = c.play(4);
        c = c.play(5);
        c = c.play(6);

        c = c.undoMove();

        assertTrue(c.compareTo(bitboard));
    }

    @Test
    public void getMove() {
        Connect4Logic c = new Connect4Logic();
        c = c.play(0);
        c = c.play(0);
        c = c.play(0);
        c = c.play(0);
        c = c.play(0);

        assertEquals(32L, c.getMove(0));
        assertEquals(128L, c.getMove(1));
    }

    @Test
    public void getAvailableMoves() {
        Connect4Logic c = Connect4Logic.valueOf(Connect4Logic.toBitBoard("2121212" +
                "2121212" +
                "2121212" +
                "1212121" +
                "1212121" +
                "1212121"), new long[]{}, 0);

        assertEquals(0, c.getAvailableMoves().size());
    }

    @Test
    public void toBitBoard() {
        assertEquals(8421504, Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0001000" +
                "0012000" +
                "0120000" +
                "1200000")[0]);
        assertEquals(16843009, Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0001000" +
                "0012000" +
                "0120000" +
                "1200000")[1]);
    }
}