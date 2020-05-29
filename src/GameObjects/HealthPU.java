package GameObjects;

import java.awt.image.BufferedImage;

public class HealthPU extends PowerUp implements Collidable{

    public HealthPU(){ }

    public HealthPU(double x, double y, double vx, double vy, double angle, BufferedImage img){
        super(x,y,vx,vy,angle,img);
    }

    public void init(double x, double y, double vx, double vy, double angle, BufferedImage img){
        super.init(x,y,vx,vy,angle,img);
    }

    @Override
    public void handleCollision(Collidable co) {
        if (this.hitBox.intersects(((GameObject)co).getHitBox()) && (co instanceof Tank) && this.isAlive){
            ((Tank)co).addLife();
            this.isAlive = false;
            this.nullThis();
        }
    }
}
