package org.ianShowTechniek.flappyTest;

import org.ianShowTechniek.Util.TestWindow;
import org.ianShowTechniek.Util.ToolBox;

import java.awt.*;

public class Pipe {

    // How big is the empty space
    int spacing = (int) ToolBox.random(120,TestWindow.size.height / 2);
    // Where is th center of the empty space
    int centery = (int) ToolBox.random(spacing, TestWindow.size.height - spacing);

    // Top and bottom of pipe
    int top = centery - spacing / 2;
    int bottom = TestWindow.size.height - (centery + spacing / 2);
    // Starts at the edge
    int x = TestWindow.size.height;
    // Width of pipe
    int w = 50;
    // How fast
    double speed = 4;

    // Did this pipe hit a bird?
    public boolean hits(Bird bird) {
        if ((bird.y - bird.r) < this.top || (bird.y + bird.r) > (TestWindow.size.height - this.bottom)) {
            if (bird.x > this.x && bird.x < this.x + this.w) {
                return true;
            }
        }
        return false;
    }

    // Draw the pipe
    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        g.fill3DRect(this.x, 0, this.w, this.top, false);
        g.fill3DRect(this.x, TestWindow.size.height - this.bottom, this.w, this.bottom, true);
    }

    // Update the pipe
    public void update() {
        this.x -= this.speed;
    }

    // Has it moved offscreen?
    public boolean offScreen() {
        if (this.x < -this.w) {
            return true;
        } else {
            return false;
        }
    }

}
