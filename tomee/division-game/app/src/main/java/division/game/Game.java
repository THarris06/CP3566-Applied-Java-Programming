package division.game;

import java.util.Random;

public class Game {
    private int number = Math.abs(new Random().nextInt() % 1000);
    private int guessesUsed = 0;
    private int maxGuesses = 10;
    private int n1;
    private int n2;
    private String outputStr;

    public void submit() {
        outputStr = "hello from game.java";
    }
}
