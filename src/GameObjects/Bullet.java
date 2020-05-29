package GameObjects;

import TankStuff.TankDriver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

//TODO implements Collidable
public class Bullet extends Weapon implements Collidable{

//    private Image bulletIcon = new ImageIcon(this.getClass().getResource("weapons.Shell.gif")).getImage();

    public Bullet(double x, double y, double vx, double vy, double angle, BufferedImage img, int senderTank){
        super(x,y,vx,vy,angle,img,senderTank);
        moveForwards();
        dmg = 5;
        R = 2.5;
    }
    public void init(){
        dmg = 5;
        R = 2.5;
    }


    public void update(){
        super.update();
        this.moveForwards();
    }


    private void moveForwards() {
        vx =  R * Math.cos(Math.toRadians(angle));
        vy =  R * Math.sin(Math.toRadians(angle));
        x += vx;
        y += vy;
    }


    @Override
    public void handleCollision(Collidable co) {
        //if this weapon isn't active
        if (!this.isAlive())
            return;
        if (hitBox.intersects(((GameObject) co).getHitBox()) && !(co instanceof PowerUp)) {
            if (co instanceof Tank) {
                if (((Tank) co).getWeaponList().contains(this)) {
                    return;
                } else {
                    ((Tank)co).hit(dmg);
                }
            }

            else if (co instanceof Bullet){
                if (!((Bullet) co).isAlive() || ((Bullet) co).getSenderTank() == this.senderTank)
                    return;
                ((Bullet) co).explode();
                this.explode();
            }
            else if (co instanceof WallBreakable) {
                ((WallBreakable) co).setRemoveThis(true);
            }
            this.explode();
        }
    }
}