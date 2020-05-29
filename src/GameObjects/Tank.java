package GameObjects;

import TankStuff.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject implements Collidable{

    private int tankId;
    private ArrayList<Weapon> weaponList;
    private HealthSys healthSys;
    private SoundPlayer shoot;

    private int rocketCount = 0;
    private final double R = 2;
    private final double ROTATION_SPEED = 1.5;


    private double cameraX = TankDriver.WORLD_WIDTH-TankDriver.SCREEN_WIDTH/2;
    private double cameraY = TankDriver.WORLD_HEIGHT-TankDriver.SCREEN_HEIGHT;

    private double oldX,oldY;
    private boolean canMove = true;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed = false;


    public Tank(double x, double y, double vx, double vy, double angle, BufferedImage tankImage,int tankId) {
        super(x,y,vx,vy,angle,tankImage);
        this.tankId = tankId;
        weaponList = new ArrayList<>();
        for (int i = 0; i < 5;i++){
            Bullet b = new Bullet(10000,10000,0,0,0,TankDriver.imgList.get("bullet"),tankId);
            b.setIsAlive(false);
            weaponList.add(b);
        }
        oldX = x;
        oldY = y;
        this.healthSys = new HealthSys(x,y+img.getHeight(),this);
        healthSys.init();
        TankDriver.addToGameObjects(healthSys);
        try{
            AudioInputStream shoot = AudioSystem.getAudioInputStream(TankDriver.soundURLs.get("Shoot"));
            this.shoot = new SoundPlayer(shoot);
        } catch(Exception  e){
            System.out.println("Tank()shoot sound failed to load------" + e);
        }
    }

//    public Rectangle2D getHitBox(){
//        return hitBox.getBounds();
//    }


    public void toggleUpPressed() {
        this.UpPressed = true;
    }
    public void toggleDownPressed() {
        this.DownPressed = true;
    }
    public void toggleRightPressed() {
        this.RightPressed = true;
    }
    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }
    public void unToggleUpPressed() {
        this.UpPressed = false;
    }
    public void unToggleDownPressed() {
        this.DownPressed = false;
    }
    public void unToggleRightPressed() {
        this.RightPressed = false;
    }
    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }
    public void toggleShootPressed(){this.shootPressed = true;}
    public void unToggleShootPressed(){this.shootPressed = false;}
    private void rotateLeft() {
        this.angle -= this.ROTATION_SPEED;
    }
    private void rotateRight() {
        this.angle += this.ROTATION_SPEED;
    }
    private void moveBackwards() {
        vx =  R * Math.cos(Math.toRadians(angle));
        vy =  R * Math.sin(Math.toRadians(angle));
        x -= vx;
        y -= vy;
        checkBorder();
    }
    private void moveForwards() {
        vx =  R * Math.cos(Math.toRadians(angle));
        vy =  R * Math.sin(Math.toRadians(angle));
        x += vx;
        y += vy;
        checkBorder();
    }


    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.shootPressed && this.hasAmmo()){
            this.shoot();
        }
        updateBullets();
        healthSys.update();
        hitBox.setRect(this.x,this.y,img.getWidth(),img.getHeight());

        if(!canMove){
            this.canMove = true;
            return;
        }
        oldX = this.x;oldY = this.y;
    }

    private void updateBullets() {
        for (Weapon b : weaponList) {
            if (b.isAlive()){
                b.update();
            }
        }
    }

    private void shoot(){
        for (Weapon w : weaponList){
            if (!w.isAlive()){
                shoot.play();
                weaponList.remove(w);

                if (rocketCount>0){
                    w = new Rocket(x,y,vx,vy,angle,TankDriver.imgList.get("Rocket"),this.tankId);
                    rocketCount--;
                } else
                    w = new Bullet(x,y,vx,vy,angle,TankDriver.imgList.get("bullet"),this.tankId);
                w.setIsAlive(true);
                weaponList.add(w);
                TankDriver.addToGameObjects(w);
                TankDriver.addToCollidables((Collidable)w);
                break;
            }
        }
        this.unToggleShootPressed();
    }

    private boolean hasAmmo(){
        for (Weapon b : weaponList){
            if (!b.isAlive())
                return true;
        }
        return false;
    }
    //make things blow up -short
    //shield,weapon,health power ups

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= TankDriver.WORLD_WIDTH - 88) {
            x = TankDriver.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= TankDriver.WORLD_HEIGHT - 88) {
            y = TankDriver.WORLD_HEIGHT - 88;
        }
    }

    public void hit(int dmg){
        this.healthSys.hit(dmg);
    }

    public void respawn(){
        if (this.tankId == 1){
            this.x = 60;this.y = 60;
            this.angle = -316;
        } else {
            this.x = 1940;this.y = 1940;
            this.angle = -125;
        }
    }



    @Override
    public void handleCollision(Collidable co) {}

    public void setCanMove(boolean flag){
        this.canMove = flag;
    }

    public ArrayList<Weapon> getWeaponList(){
        return this.weaponList;
    }

    public int getTankId(){
        return this.tankId;
    }


    //Positional stuff
    public double getOldX(){
        return oldX;
    }
    public double getOldY(){
        return oldY;
    }
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public void setX(double d){this.x = d;}
    public void setY(double d){this.y = d;}

    public int getCenterX(){
        int deltaX =250;//deltaX is for positioning the tank to be center in the X axis
        int edgeNum = TankDriver.WORLD_WIDTH - TankDriver.SCREEN_WIDTH/2;
        if(this.x - deltaX <= 0) return 0;      //for the case where the tank is at the edge of the map
        if(this.x - deltaX + TankDriver.SCREEN_WIDTH/2 >= TankDriver.WORLD_WIDTH) return edgeNum;
        else {
            cameraX = this.x - 250;
            return (int)cameraX;
        }
    }

    public int getCenterY(){
        final int FIXEDY = TankDriver.SCREEN_HEIGHT/2;
        if (this.y - FIXEDY < 0) return 0;
        if (this.y + FIXEDY > TankDriver.WORLD_HEIGHT){
            return (int)cameraY;
        }
        cameraY = this.y - FIXEDY;
        return (int)cameraY;
    }

    public void addLife(){
        healthSys.addLife();
    }
    protected void addRockets(){
        this.rocketCount+=2;
    }

    public HealthSys getHealthSys(){
        return this.healthSys;
    }

    @Override
    public String toString() {
        return "x=" + (int)x + ", y=" + (int)y + ", angle=" + (int)angle;
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), img.getWidth() / 2.0, img.getHeight() / 2.0);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, rotation, null);

//        g2d.setColor(Color.CYAN);
//        g2d.drawRect((int)x,(int)y,this.tankImage.getWidth(),this.tankImage.getHeight());
    }
}