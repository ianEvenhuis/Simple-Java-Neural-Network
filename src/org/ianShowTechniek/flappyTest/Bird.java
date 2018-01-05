package org.ianShowTechniek.flappyTest;

// Daniel Shiffman
// Nature of Code: Intelligence and Learning
// https://github.com/shiffman/NOC-S17-2-Intelligence-Learning

// This flappy bird implementation is adapted from:
// https://youtu.be/cXgA1d_E-jY&

import nn.Activation;
import nn.NeuralNetwork;
import nn.OneLayerNetwork;
import org.ianShowTechniek.Util.TestWindow;
import org.ianShowTechniek.Util.ToolBox;

import java.awt.*;
import java.util.List;

public class Bird {

    int x;
    int y;
    int r;
    double gravity;
    double lift;
    double velocity;

    public double score;
    double fitness = 0;

    NeuralNetwork brain;

    public Bird() {
        this(new OneLayerNetwork(4, 16, 2, 0.1, Activation.TAHN));
    }

    public Bird(NeuralNetwork brain) {
        // position and size of bird
        this.x = 64;
        this.y = TestWindow.size.height / 2;
        this.r = 12;
        // Gravity, lift and velocity
        this.gravity = 0.4;
        this.lift = -12;
        this.velocity = 0;
        // Is this a copy of another Bird or a new one?
        // The Neural Network is the bird's "brain"
        this.brain = brain;
        this.brain.mutate();
        // Score is how many frames it's been alive
        this.score = 0;
        // Fitness is normalized version of score
        this.fitness = 0;

        // Create a copy of this bird
    }

    public Bird copy() {
        return new Bird(this.brain);
    }

    // Display the bird
    public void show(Graphics2D g) {
        g.setColor(Color.green);
        g.fillRect(this.x - this.r, this.y - this.r, this.r * 2, this.r * 2);
    }

    // This is the key function now that decides
    // if it should jump or not jump!
    public void think(List<Pipe> pipes) {
        // First find the closest pipe
        Pipe closest = null;
        int record = Integer.MAX_VALUE;
        for (int i = 0; i < pipes.size(); i++) {
            int diff = pipes.get(i).x - this.x;
            if (diff > 0 && diff < record) {
                record = diff;
                closest = pipes.get(i);
            }
        }

        if (closest != null) {
            // Now create the inputs to the neural network
            double[] inputs = new double[4];
            // x position of closest pipe
            inputs[0] = ToolBox.map(closest.x, this.x, TestWindow.size.width, -1, 1);
            // top of closest pipe opening
            inputs[1] = ToolBox.map(closest.top, 0, TestWindow.size.height, -1, 1);
            // bottom of closest pipe opening
            inputs[2] = ToolBox.map(closest.bottom, 0, TestWindow.size.height, -1, 1);
            // bird's y position
            inputs[3] = ToolBox.map(this.y, 0, TestWindow.size.height, -1, 1);
            // Get the outputs from the network
            double[] action = this.brain.query(inputs);
            // Decide to jump or not!
            if (action[1] > action[0]) {
                this.up();
            }
        }
    }

    // Jump up
    public void up() {
        this.velocity += this.lift;
    }

    // Update bird's position based on velocity, gravity, etc.
    public void update() {
        this.velocity += this.gravity;
        this.velocity *= 0.9;
        this.velocity = Math.min(this.velocity, 3);
        this.y += this.velocity;
        // Keep it stuck to top or bottom
        if (this.y > TestWindow.size.height) {
            this.y = TestWindow.size.height;
            this.velocity = 0;
        }
        if (this.y < 0) {
            this.y = 0;
            this.velocity = 0;
        }
        // Every frame it is alive increases the score
        this.score++;
    }
}
