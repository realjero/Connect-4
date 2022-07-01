import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {

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

    @Test
    public void isGameOver() {
        long[] bitboard = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "1111000");
        long[] bitboard1 = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "1000000" +
                "1000000" +
                "1000000" +
                "1000000");
        long[] bitboard2 = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0001000" +
                "0010000" +
                "0100000" +
                "1000000");
        long[] bitboard3 = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "2222000");
        long[] bitboard4 = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "2000000" +
                "2000000" +
                "2000000" +
                "2000000");
        long[] bitboard5 = Connect4Logic.toBitBoard("0000000" +
                "0000000" +
                "0002000" +
                "0020000" +
                "0200000" +
                "2000000");
        assertTrue(Connect4Logic.valueOf(bitboard, new long[]{}, 0).isGameOver());
        assertTrue(Connect4Logic.valueOf(bitboard1, new long[]{}, 0).isGameOver());
        assertTrue(Connect4Logic.valueOf(bitboard2, new long[]{}, 0).isGameOver());
        assertTrue(Connect4Logic.valueOf(bitboard3, new long[]{}, 0).isGameOver());
        assertTrue(Connect4Logic.valueOf(bitboard4, new long[]{}, 0).isGameOver());
        assertTrue(Connect4Logic.valueOf(bitboard5, new long[]{}, 0).isGameOver());
    }

    @Test
    void play() {
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

        assertTrue(c.compare(bitboard));
    }
}