package org.ianShowTechniek.flappyTest;

import org.ianShowTechniek.Util.TestWindow;
import org.ianShowTechniek.Util.ToolBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class flappyTest extends TestWindow {

    // Daniel Shiffman
    // Nature of Code: Intelligence and Learning
    // https://github.com/shiffman/NOC-S17-2-Intelligence-Learning

    // This flappy bird implementation is adapted from:
    // https://youtu.be/cXgA1d_E-jY&

    // How big is the population
    public static int totalPopulation = 250;
    // All active birds (not yet collided with pipe)
    List<Bird> activeBirds = new ArrayList<>();
    // All birds for any given population
    List<Bird> allBirds = new ArrayList<>();
    // Pipes
    List<Pipe> pipes = new ArrayList<>(); //change to arrayList
    // A frame counter to determine when to add a pipe
    int counter = 0;
    int cycles = 3;
    Bird bestBird = new Bird();


    // All time high score
    double highScore = 0;

    // Training or just showing the current best
    boolean runBest = false;

    public flappyTest() {
        super(600, 400);
        refreshRate = 120;
        // Create a population
        for (int i = 0; i < totalPopulation; i++) {
            Bird bird = new Bird();
            activeBirds.add(bird);
        }
        allBirds.addAll(activeBirds);
        start();
    }

    public void render(Graphics2D g) {
        // How many times to advance the game
        for (int n = 0; n < cycles; n++) {
            // Show all the pipes
            for (int i = 0; i < pipes.size(); i++) {
                pipes.get(i).update();
                if (pipes.get(i).offScreen()) {
                    pipes.remove(i);
                }
            }
            // Are we just running the best bird
            for (int i = activeBirds.size() - 1; i >= 0; i--) {
                Bird bird = activeBirds.get(i);
                // Bird uses its brain!
                bird.think(pipes);
                bird.update();
                // Check all the pipes
                for (int j = 0; j < pipes.size(); j++) {
                    if (pipes.get(j).hits(activeBirds.get(i))) {
                        activeBirds.remove(i);
                        break;
                    }
                }
            }
            // Add a new pipe every so often
            if (counter % 75 == 0) {
                pipes.add(new Pipe());
            }
            counter++;
        }
        // What is highest score of the current population
        double tempHighScore = 0;
        // If we're training


        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        for (Bird activeBird : activeBirds) {
            activeBird.show(g);
        }
        // If we're out of birds go to the next generation
        if (activeBirds.isEmpty()) {
            nextGeneration();
            System.out.println("We are empty");
        }

        g.setColor(Color.red);
        g.drawString("Temp HighScore " + activeBirds.size(), 10, 60);
        g.drawString(" Counter" + counter, 10, 85);
    }

    //<editor-fold desc="Game Loops">
    // Start the game over
    public void resetGame() {
        counter = 0;
        pipes = new ArrayList<>();
    }

    // Create the next generation
    public void nextGeneration() {
        resetGame();
        // Normalize the fitness values 0-1
        normalizeFitness(allBirds);
        // Generate a new set of birds
        activeBirds = generate(allBirds);
        allBirds.clear();
        // Copy those birds to another array
        allBirds.addAll(activeBirds);
    }

    // Generate a new population of birds
    public List<Bird> generate(List<Bird> oldBirds) {
        List<Bird> newBirds = new ArrayList<>();
        for (int i = 0; i < oldBirds.size(); i++) {
            // Select a bird based on fitness
            Bird bird = poolSelection(oldBirds);
            newBirds.add(bird);
        }
        return newBirds;
    }

    // Normalize the fitness of all birds
    public void normalizeFitness(List<Bird> birds) {
        // Make score exponentially better?
        for (Bird bird : birds) {
            bird.score = Math.pow(bird.score, 2);
        }
        // Add up all the scores
        int sum = 0;
        for (Bird bird : birds) {
            sum += bird.score;
        }
        // Divide by the sum
        for (Bird bird : birds) {
            bird.fitness = bird.score / sum;
        }
    }

    // An algorithm for picking one bird from an array
    // based on fitness
    public Bird poolSelection(List<Bird> birds) {
        // Start at 0
        int index = 0;
        // Pick a random number between 0 and 1
        double r = ToolBox.random(1);
        // Keep subtracting probabilities until you get less than zero
        // Higher probabilities will be more likely to be fixed since they will
        // subtract a larger number towards zero
        while (r > 0) {
            r -= birds.get(index).fitness;
            index += 1;

            // And move on to the next


        }
        // Go back one
        index -= 1;
        // Make sure it's a copy!
        // (this includes mutation)
        return birds.get(index).copy();
    }


    //</editor-fold>


    public static void main(String[] args) {
        new flappyTest();
    }
}
