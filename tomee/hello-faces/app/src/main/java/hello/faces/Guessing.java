package hello.faces;

import java.io.Serializable;
import java.util.Random;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class Guessing implements Serializable {
    private int number = Math.abs(new Random().nextInt() % 1024);
    private int guessesUsed = 0;
    private int maxGuesses = 10;
    private int guess;
    private String outputStr;

    public void submit() {
        if (maxGuesses <= guessesUsed) {
            outputStr = "Failed to guess the number " + number + ". Try again!";
            number = Math.abs(new Random().nextInt() % 1024);
            guessesUsed = 0;
        }
        else if (guess < number) {
            outputStr = "Too low...";
            guessesUsed++;
        }
        else if (guess > number) {
            outputStr = "Too high...";
            guessesUsed++;
        }
        else {
            outputStr = "Winner in " + guessesUsed + " tries! Try again!";
            number = Math.abs(new Random().nextInt() % 1024);
            guessesUsed = 0;
        }
    }

    public int getGuess() {
        return this.guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public int getGuessesUsed() {
        return guessesUsed;
    }

    public void setGuessesUsed(int guessesUsed) {
        this.guessesUsed = guessesUsed;
    }

    public String getOutputStr() {
        return this.outputStr;
    }
}
