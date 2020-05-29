package GameObjects;

import TankStuff.TankDriver;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class GameObject{

    //these are shared will all children
    protected double x, y ,vx, vy, angle;
    protected BufferedImage img;
    protected Rectangle2D.Double hitBox;

    private  boolean removeThis = false;

    public GameObject(){};

    public GameObject(double x, double y, double vx, double vy, double angle, BufferedImage img){
        this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.angle = angle; this.img = img;
        this.hitBox = new Rectangle2D.Double(x,y,img.getWidth(),img.getHeight());
    }

    public void init(double x, double y, double vx, double vy, double angle, BufferedImage img){
        this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.angle = angle; this.img = img;
        this.hitBox = new Rectangle2D.Double(x,y,img.getWidth(),img.getHeight());
    }

    public void drawImage(Graphics g){
        AffineTransform rotation = AffineTransform.getTranslateInstance(x,y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.img,rotation,null);
    }

    public Rectangle2D.Double getHitBox(){
        return this.hitBox;
    }

    public boolean getRemoveThis(){
        return removeThis;
    }
    public void setRemoveThis(Boolean removeThis) {
        this.removeThis = removeThis;
    }

    public void nullThis(){
        this.y = TankDriver.WORLD_HEIGHT+9000;
        this.x = TankDriver.WORLD_WIDTH+9000;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

}