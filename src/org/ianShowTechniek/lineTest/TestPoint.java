package org.ianShowTechniek.lineTest;

import java.awt.*;

public class TestPoint {

    double x;
    double y;
    int value;
    private int size = 32;

    public TestPoint(double x, double y) {
        this.x = x;
        this.y = y;
        if (x > y)
            this.value = -1;
        else
            this.value = 1;
    }

    public double getX() {
        return x;
    }

    public TestPoint setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public TestPoint setY(double y) {
        this.y = y;
        return this;
    }

    public void render(Graphics2D g, boolean good) {
        if (value > 0)
            g.setColor(Color.white);
        else
            g.setColor(Color.BLUE);

        g.fillOval((int) x, (int) y, size, size);
        if (good) {
            g.setColor(Color.green);
            g.fillOval((int) x + size / 2, (int) y + size / 2, size / 2, size / 2);
        }else{
            g.setColor(Color.red);
            g.fillOval((int) x + size / 2, (int) y + size / 2, size / 2, size / 2);
        }

    }
}
