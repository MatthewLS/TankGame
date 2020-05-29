package GameObjects;

import java.awt.image.BufferedImage;

public abstract class PowerUp extends GameObject {

    protected boolean isAlive = true;

    PowerUp(){}

    public PowerUp(double x, double y, double vx, double vy, double angle, BufferedImage img) {
        super(x, y, vx, vy, angle, img);
    }

    public void init(double x, double y, double vx, double vy, double angle, BufferedImage img){
        super.init(x, y, vx, vy, angle, img);
    }

}
