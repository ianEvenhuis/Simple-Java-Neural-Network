package org.ianShowTechniek.Util;

import nn.Activation;
import nn.NeuralNetwork;
import nn.OneLayerNetwork;
import org.ianShowTechniek.lineTest.LineTest;
import org.ianShowTechniek.lineTest.TestPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.RenderingHints.*;
import static java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;

public abstract class TestWindow {

    public TestWindow(int widht, int height) {
        size = new Dimension(widht, height);
        initFrame(); //init's the frame

    }

    public void start(){
        loop(); //starts the loop
    }

    public abstract void render(Graphics2D g);

    private void startRender(BufferedImage screen) {
        Graphics2D g = (Graphics2D) screen.getGraphics();
        clearScreen(g, screen);
        render(g);
    }

    //<editor-fold desc="Frame Variable">
    //Debug stats to keep track on
    private int lastFPS = 0;
    public int refreshRate = 60;
    private long last_time = System.nanoTime();
    private static float delta = 0;
    private static boolean debug = true;
    private static boolean running = true;
    //stuff needed to setup display Frame
    private BufferedImage screen;
    private JFrame frame;
    public static Dimension size = new Dimension(800, 800);
    private boolean fullScreen = false;
    //</editor-fold>

    //<editor-fold desc="Basic Render Setup">

    private void loop() {
        int render = 0;
        double fpsTimer = System.currentTimeMillis();
        double nsPerRender = 1000000000.0 / refreshRate;
        double then = System.nanoTime();
        double unr = 0;
        System.out.println("starting loop");
        clearScreen((Graphics2D) screen.getGraphics(), screen);
        while (running) {
            double nt = (System.nanoTime() - then);
            unr += nt / nsPerRender;
            then = nt + then;
            while (unr >= 1 && running) {
                render++;
                delta = getDeltaTime();
                startRender(screen);
                endRender(screen, frame.getGraphics());
                --unr;
            }
            if (System.currentTimeMillis() - fpsTimer > 5000) {
                if (debug)
                    System.out.printf("%d render %n", render / 5);
                lastFPS = render;
                render = 0;
                fpsTimer += 5000;
            }
        }
    }

    private void endRender(BufferedImage screen, Graphics onScreen) {
        onScreen.drawImage(screen, 0, 0, frame.getWidth(), frame.getHeight(), null);
    }

    private void initFrame() {
        frame = new JFrame();
        if (fullScreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        } else {
            frame.setSize(size);
        }
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        screen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = screen.createGraphics();
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

    protected float getDeltaTime() {
        long time = System.nanoTime();
        float delta_time = (int) ((time - last_time) / 10000000);
        last_time = time;
        return delta_time;
    }

    public void clearScreen(Graphics2D g, BufferedImage screen) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    }
    //</editor-fold>


}
