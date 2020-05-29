package GameObjects;

import TankStuff.TankDriver;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class WallBreakable extends GameObject implements Collidable {
    private Boolean canRemove = false;

    public WallBreakable(){}
    public WallBreakable(double x, double y, double vx, double vy, double angle,BufferedImage img){
        super(x,y,vx,vy,angle,img);
    }

    @Override
    public void init(double x, double y, double vx, double vy, double angle,BufferedImage img){
        super.init(x, y, vx, vy, angle, img);
    }

    public void Break() {
        this.setRemoveThis(true);
    }

    @Override
    public void handleCollision(Collidable co) {
        if (this.hitBox.intersects(((GameObject)co).getHitBox())){
            if (co instanceof Tank){
                System.out.println("breakable collision with Tank");
                ((Tank)co).setCanMove(false);
                ((Tank)co).setX(((Tank)co).getOldX());
                ((Tank)co).setY(((Tank)co).getOldY());
            }
        }
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
//        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);

    }


}
