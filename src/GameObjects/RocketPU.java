package GameObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RocketPU extends PowerUp implements Collidable {

//    private Image rocketIcon = new ImageIcon(this.getClass().getResource("weapons.Rocket.gif")).getImage();

    public RocketPU() {
    }

    public RocketPU(double x, double y, double vx, double vy, double angle, BufferedImage img) {
        super(x, y, vx, vy, angle, img);
    }

    public void init(double x, double y, double vx, double vy, double angle, BufferedImage img) {
        super.init(x, y, vx, vy, angle, img);
    }



    @Override
    public void handleCollision(Collidable co) {
        if (this.hitBox.intersects(((GameObject) co).getHitBox()) && (co instanceof Tank) && this.isAlive) {
            //add rocket function
            ((Tank)co).addRockets();

            this.isAlive = false;
            this.nullThis();

        }
    }
}