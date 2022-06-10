
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //System.out.println(new Main().testGames(200) + "% Winrate");
        new Main().startGame();
    }

    public void startGame() {
        Connect4Logic connect4 = new Connect4Logic(Player.RED);
        Scanner scanner = new Scanner(System.in);

        while (!connect4.isGameOver()) {
            System.out.println(connect4);
            System.out.print("\nSpalte (0-6): ");
            connect4 = connect4.playMove(scanner.nextInt());


            System.out.println(connect4);
            connect4 = connect4.playMove(connect4.bestMove());
        }

        System.out.println(connect4);
        System.out.println("\nSpiel beendet!");
    }

    public boolean winner() {
        Connect4Logic connect4 = new Connect4Logic(Player.RED);
        Scanner scanner = new Scanner(System.in);

        while (!connect4.isGameOver()) {
            connect4 = connect4.playMove(connect4.bestMove());

            if(connect4.isGameOver()) {
                System.out.println(connect4);
                return true;
            } else {
                int m = connect4.getAvailableMoves().get(new Random().nextInt(connect4.getAvailableMoves().size()));
                connect4 = connect4.playMove(m);
            }

            if(connect4.isGameOver()) {
                System.out.println(connect4);
                return false;
            }
        }
        return false;
    }

    public int testGames(int size) {
        int won = 0;
        for(int i = 0; i < size; i++) {
            if(new Main().winner()) {
                won++;
            }
        }
        double percent = ((double)won / (double)size) * 100;
        return (int) percent;
    }
}
