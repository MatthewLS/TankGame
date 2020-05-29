package GameObjects;

import TankStuff.TankDriver;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class HealthSys extends GameObject {

    private Rectangle2D healthBar;
    private int hp = 50,lives = 3;
    private BufferedImage heartImg = TankDriver.imgList.get("Heart");

    private Tank t;
    private double x,y;
    
    HealthSys(double x, double y,Tank t){
        healthBar = new Rectangle2D.Double(x,y,100,10);
        this.t = t;
        this.x = x;this.y = y;
    }

    public void init(){

    }

    public void update(){
        checkForDeath();
        x = t.getX();
        y = t.getY()+ TankDriver.imgList.get("tank1").getHeight();
    }

    private void checkForDeath(){
        if (hp <= 0){
            if (lives > 0){
                lives--;
                t.respawn();
                hp=50;
            } else {
                int tID = t.getTankId();
                if (tID == 1)
                    TankDriver.setWinner(2);
                else
                    TankDriver.setWinner(1);
                TankDriver.endGame();
            }
        }
    }

    public void hit(int dmg){
        hp-=dmg;
    }
    public void addLife(){
        this.lives+=1;
    }



    @Override
    public void drawImage(Graphics g){
//        AffineTransform rotation = AffineTransform.getTranslateInstance(x,y);
//        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawRect((int)x,(int)y,51,9);
        if (hp >30)
            g2.setColor(Color.GREEN);
        else if (hp > 10)
            g2.setColor(Color.ORANGE);
        else
            g2.setColor(Color.RED);

        g2.fillRect((int)x+1,(int)y+1,hp,8);

        //add Hearts
        for (int i =0; i < lives;i++){
            g2.drawImage(heartImg,((int)x+i*5)-25,(int)y-5,null,null);
        }
    }
}
