package GameObjects;

import java.awt.image.BufferedImage;

public class Rocket extends Weapon implements Collidable {

    Rocket(double x, double y, double vx, double vy, double angle, BufferedImage img, int senderTank) {
        super(x, y, vx, vy, angle, img, senderTank);
        dmg = 20;
        R = 3.3;
    }

    public void update(){
        super.update();
        this.moveForwards();
    }

    public void moveForwards(){
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

            else if (co instanceof Rocket){
                if (!((Rocket) co).isAlive() || ((Rocket) co).getSenderTank() == this.senderTank)
                    return;
                ((Rocket) co).explode();
                this.explode();
            }
            else if (co instanceof WallBreakable) {
                System.out.println("bullet collides with wallbreakable");
                ((WallBreakable) co).setRemoveThis(true);
            }
            if (!(co instanceof Bullet))
                this.explode();
        }
    }
}
