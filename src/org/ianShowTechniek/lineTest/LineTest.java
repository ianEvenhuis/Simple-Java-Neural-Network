package org.ianShowTechniek.lineTest;

import org.ianShowTechniek.Util.ToolBox;
import nn.Activation;
import nn.NeuralNetwork;
import nn.OneLayerNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.RenderingHints.*;
import static java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;

public class LineTest {

    List<TestPoint> points = new ArrayList<>();

    NeuralNetwork network;
    boolean train = true;

    public LineTest() {
        initFrame(); //init's the frame
        setupPoints();
        network = new OneLayerNetwork(3, 100, 1, 0.50, Activation.TAHN, Activation.DTAHN);
        loop(); //starts the loop
    }

    private void setupPoints() {
        points.clear();
        for (int i = 0; i < 200; i++) {
            points.add(new TestPoint(ToolBox.random(size.width), ToolBox.random(size.width)));
        }
    }

    int loops = 0;

    private void render(BufferedImage screen) {
        Graphics2D g = (Graphics2D) screen.getGraphics();
        clearScreen(g, screen);
        int faults = 0;
        int good = 0;
        for (TestPoint point : points) {
            double[] in = {point.x, point.y, 1};
            double[] tar = new double[1];
            tar[0] = point.value;
            if (train) {

                tar[0] = point.value;
                network.train(in, tar);
                if (loops % 100 == 0)
                    network.mutate();
            }
            double[] ans = network.query(in);
            if (ans[0] == point.value) {
                good++;
                point.render(g, true);
            } else {
                faults++;
                //System.out.println("tar[] = " + tar[]);
                point.render(g, false);
            }
        }
        if (faults == 0 || good == points.size()) {
            System.out.println("We got no erros we did it : " + loops);
            System.out.println("Round " + loops + " \n Score : \n Size :" + points.size() + " \n Good: " + good + "\n Fault: " + faults);
            setupPoints();
            loops = 0;
        } else {
            //System.out.println("Round " + loops + " \n Score : \n Size :" + points.size() + " \n Good: " + good + "\n Fault: " + faults);
        }
        loops++;
    }


    private void tick() {

    }

    //<editor-fold desc="Frame Variable">
    //Debug stats to keep track on
    private int lastFPS = 0;
    private int refreshRate = 170;
    private long last_time = System.nanoTime();
    private static float delta;
    private static boolean debug = true;
    private static boolean running = true;
    //stuff needed to setup display Frame
    private BufferedImage screen;
    private JFrame frame;
    private Dimension size = new Dimension(800, 800);
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
            while (unr >= 1 || running) {
                render++;
                delta = getDeltaTime();
                render(screen);
                tick();
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

    public static void main(String[] args) {
        new LineTest();
    }
    //</editor-fold>
}
