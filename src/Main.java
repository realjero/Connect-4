
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Main().testGames(200) + "% Winrate");
    }

    public void startGame() {
        VierGewinnt vg = new VierGewinnt(Player.RED);
        Scanner scanner = new Scanner(System.in);

        while (!vg.isGameOver()) {
            System.out.println(vg);
            System.out.print("\nSpalte (0-6): ");
            vg = vg.playMove(scanner.nextInt());


            System.out.println(vg);
            vg = vg.playMove(vg.bestMove());
        }

        System.out.println(vg);
        System.out.println("\nSpiel beendet!");
    }

    public boolean winner() {
        VierGewinnt vg = new VierGewinnt(Player.RED);
        Scanner scanner = new Scanner(System.in);

        while (!vg.isGameOver()) {
            vg = vg.playMove(vg.bestMove());

            if(vg.isGameOver()) {
                System.out.println(vg);
                return true;
            } else {
                int m = vg.getAvailableMoves(vg).get(new Random().nextInt(vg.getAvailableMoves(vg).size()));
                vg = vg.playMove(m);
            }

            if(vg.isGameOver()) {
                System.out.println(vg);
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
