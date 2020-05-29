package GameObjects;

import TankStuff.TankDriver;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class Weapon extends GameObject implements Collidable {

    protected int dmg;
    protected boolean isAlive = true;
    protected double R;
    protected int senderTank;

    Weapon(double x, double y, double vx, double vy, double angle, BufferedImage img, int senderTank){
        super(x,y,vx,vy,angle,img);
        this.senderTank=senderTank;
    }


    public int getSenderTank(){
        return this.senderTank;
    }

    public void explode(){
        //create an explosion
        this.nullThis();
        this.isAlive = false;
        this.hitBox.setRect(x,y,img.getWidth(),img.getHeight());
    }


    public boolean isAlive(){
        return isAlive;
    }

    public void update(){
        this.checkBorder();
        this.hitBox.setRect(x,y,img.getWidth(),img.getHeight());
        if (!this.isAlive)
            this.nullThis();
    }

    private void checkBorder(){
        if (x < 30 || x >= TankDriver.WORLD_WIDTH - 40 || y < 40 || y >= TankDriver.WORLD_HEIGHT - 40) {
            this.explode();
        }
    }

    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
    }



    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);

        Graphics2D g2d = (Graphics2D) g;
        if (this.isAlive())
            g2d.drawImage(this.img, rotation, null);
//        g2d.drawRect((int)x,(int)y,img.getWidth(),img.getHeight());

    }

}
