package TankStuff;

import GameObjects.*;
import MapLoading.MapLoader;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static javax.imageio.ImageIO.read;

/**
 * Main driver class of Tank Example.
 * Class is responsible for loading resources and
 * initializing game objects. Once completed, control will
 * be given to infinite loop which will act as our game loop.
 * A very simple game loop.
 * @author anthony-pc
 */
public class TankDriver extends JPanel  {

    public static final int SCREEN_WIDTH = 1380, SCREEN_HEIGHT = 960, WORLD_WIDTH = 2048, WORLD_HEIGHT = 2048,  MINIMAP_WIDTH = 200, MINIMAP_HEIGHT = 150;

    public static HashMap<String,BufferedImage> imgList = new HashMap<>();
    public static HashMap<String, URL> soundURLs = new HashMap<>();

    private SoundPlayer bgMusic;
    private Graphics2D buffer;
    private BufferedImage world;
    private BufferedImage miniMap;
    private static boolean isRunning = true;
    private static int winner;


    private static JFrame jFrame;
    private Tank tank1;
    private Tank tank2;

    //gameObjects to be rendered/painted and checked for collisions
    private static CopyOnWriteArrayList<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<Collidable> collidables = new CopyOnWriteArrayList<>();



    public static void main(String[] args) {
        TankDriver tankDriver = new TankDriver();
        tankDriver.init();
        StartScreen sc = new StartScreen();
        sc.init();
        EndScreen es = new EndScreen();
        es.init();
        try {
            while (true) {
                if (isRunning) {
                    tankDriver.tank1.update();
                    tankDriver.tank2.update();
                    tankDriver.checkCollisions();
                    tankDriver.handleRemove();
                    tankDriver.repaint();
                    Thread.sleep(1000 / 144);
//                Timer timer = Timer
                }
                else {
                    es.exec(winner);
                }
            }
        } catch (InterruptedException ignored) {
        }
    }


    private void init() {
        jFrame = new JFrame("Tank Game");
        this.world = new BufferedImage(TankDriver.WORLD_WIDTH, TankDriver.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.miniMap = new BufferedImage(TankDriver.MINIMAP_WIDTH,TankDriver.MINIMAP_HEIGHT,BufferedImage.TYPE_INT_RGB);
        BufferedImage tank1Img = null, tank2Img = null, bulletImg = null, wallUnbreakableImg = null, wallBreakableImg = null,
                backgroundImg = null, powerUpImg = null, heartImg = null, rocketImg = null, shield1Img = null, shield2Img = null, gameTitleImg = null;
        AudioInputStream bgMusicStream = null;
        try {
            /*
             * There is a subtle difference between using class
             * loaders and File objects to read in resources for your
             * tank games. First, File objects when given to input readers
             * will use your project's working directory as the base path for
             * finding the file. Class loaders will use the class path of the project
             * as the base path for finding files. For Intellij, this will be in the out
             * folder. if you expand the out folder, the expand the production folder, you
             * will find a folder that has the same name as your project. This is the folder
             * where your class path points to by default. Resources, will need to be stored
             * in here for class loaders to load resources correctly.
             *
             * Also one important thing to keep in mind, Java input readers given File objects
             * cannot be used to access file in jar files. So when building your jar, if you want
             * all files to be stored inside the jar, you'll need to class loaders and not File
             * objects.
             *
             */
            //Using class loaders to read in resources
//            tank1Img = read(TankDriver.class.getClassLoader().getResource("../../resources/images/tank1.png"));
            //Using file objects to read in resources.
            tank1Img = read(new File("./resources/tanks/tank1.png"));
            tank2Img = read(new File("./resources/tanks/tank1.png"));
            bulletImg = read(new File("./resources/weapons/shell.png"));
            wallUnbreakableImg = read(new File("./resources/misc/Wall1.gif"));
            wallBreakableImg = read(new File("./resources/misc/Wall2.gif"));
            backgroundImg = read(new File("./resources/misc/Background.bmp"));
            powerUpImg = read(new File("./resources/misc/Pickup.gif"));
            heartImg = read(new File("./resources/misc/Heart.png"));
            rocketImg = read(new File("./resources/weapons/rocket.png"));
            shield1Img = read(new File("./resources/misc/shield1.gif"));
            shield2Img = read(new File("./resources/misc/shield2.gif"));
            gameTitleImg = read(new File("./resources/misc/Title.bmp"));

            URL bgMusicURL = TankDriver.class.getClassLoader().getResource("music/Music.mid");
            URL shootSoundURL = TankDriver.class.getClassLoader().getResource("music/Explosion_small.wav");
            URL bigExplosionURL = TankDriver.class.getClassLoader().getResource("music/Explosion_large.wav");
            try {
                bgMusicStream = AudioSystem.getAudioInputStream(bgMusicURL);
            } catch (UnsupportedAudioFileException e){
                System.out.println("TankDriver.init() error loading  music------" + e);
            }

            soundURLs.put("Background", bgMusicURL);
            soundURLs.put("Shoot", shootSoundURL);
            soundURLs.put("Explosion",bigExplosionURL);

            this.bgMusic = new SoundPlayer(bgMusicStream);
            this.bgMusic.playLoop();


            imgList.put("tank1",tank1Img);
            imgList.put("tank2",tank2Img);
            imgList.put("bullet",bulletImg);
            imgList.put("WallUnbreakable",wallUnbreakableImg);
            imgList.put("WallBreakable",wallBreakableImg);
            imgList.put("powerUpImg",powerUpImg);
            imgList.put("heart",heartImg);
            imgList.put("RocketPU",rocketImg);
            imgList.put("Rocket",rocketImg);
            imgList.put("shield1",shield1Img);
            imgList.put("shield2",shield2Img);
            imgList.put("gameTitle",gameTitleImg);
            imgList.put("background",backgroundImg);
            imgList.put("Heart",heartImg);
            imgList.put("HealthPU",heartImg);

        } catch (IOException ex  ){//UnsupportedAudioFileException ex) {
            System.out.println(ex.getMessage());
        }

        initBG(imgList.get("background"));

        tank1 = new Tank(200, 200, 0, 0, 0, tank1Img,1);
        TankControl tank1Control = new TankControl(tank1,KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_A,
                KeyEvent.VK_D,
                KeyEvent.VK_SPACE);



        tank2 = new Tank(1940,1940,0,0,-125,tank2Img,2);
        TankControl tank2Control = new TankControl(tank2, KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_ENTER);

        gameObjects.add(tank1);
        gameObjects.add(tank2);
        collidables.add(tank1);
        collidables.add(tank2);





        jFrame.setLayout(new BorderLayout());
        jFrame.add(this,BorderLayout.CENTER);

        jFrame.addKeyListener(tank1Control);
        jFrame.addKeyListener(tank2Control);
        jFrame.setSize(TankDriver.SCREEN_WIDTH, TankDriver.SCREEN_HEIGHT + 30);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        jFrame.setBackground(Color.gray);

        MapLoader mapLoader = new MapLoader();
        mapLoader.init();


    } //end of TankeDriver.init()

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // creates a graphics context which allows you to draw over the splash screen
        this.buffer = this.world.createGraphics();

        //draw over splash screen
        for (GameObject gameObject : gameObjects) {
            gameObject.drawImage(buffer);
        }

//        g2.drawImage(world,0,0,null);
        BufferedImage leftHalf = world.getSubimage(tank1.getCenterX(), tank1.getCenterY(), SCREEN_WIDTH/2, SCREEN_HEIGHT);
        BufferedImage rightHalf = world.getSubimage(tank2.getCenterX(),tank2.getCenterY(),SCREEN_WIDTH/2,SCREEN_HEIGHT);
        g2.drawImage(leftHalf,0,0,null);
        g2.drawImage(rightHalf,SCREEN_WIDTH/2 + 1,0,null);
        g2.drawImage(world.getScaledInstance(TankDriver.MINIMAP_WIDTH,TankDriver.MINIMAP_HEIGHT,Image.SCALE_DEFAULT),TankDriver.SCREEN_WIDTH/2-TankDriver.MINIMAP_WIDTH/2,TankDriver.SCREEN_HEIGHT-TankDriver.MINIMAP_HEIGHT-25,null);
    }

    public static void addToGameObjects(GameObject go){
        gameObjects.add(go);
    }
    public static void addToCollidables(Collidable obj) { collidables.add(obj);}



//    public static void endGame(){
//        isRunning = false;
//    }

    public void checkCollisions(){
        try{
        collidables.forEach(collidable -> {
            collidable.handleCollision(tank1);
            collidable.handleCollision(tank2);
            for (Weapon weapon : tank1.getWeaponList()) {
                if (weapon.isAlive())
                    weapon.handleCollision(collidable);
            }
            for (Weapon weapon : tank2.getWeaponList()) {
                if (weapon.isAlive())
                    weapon.handleCollision(collidable);
            }
        });
        } catch (Exception e){
            System.out.println("TankDriver.checkCollisions error-----------" + e);
        }
    }

    public void handleRemove(){
        GameObject obj;
        for (int i = 0; i < collidables.size();i++){
            if (((GameObject)collidables.get(i)).getRemoveThis()){
                obj = ((GameObject)collidables.get(i));
                gameObjects.remove(obj);
                collidables.remove(obj);
                obj.nullThis();
            }
        }
    }
    //try making an iterator and removing through that


    private void initBG(BufferedImage bg){
        try {
            int picW = bg.getWidth();
            int picH = bg.getHeight();
            int x = (world.getWidth() / picW) + 2;
            int y = (world.getHeight() / picH) + 2;
            int currX = 0, currY = 0;
            Background background;
            for (int i = 0;i<x+1;i++){
                for (int j = 0;j<y+1;j++){
                    background = new Background(currX,currY,0,0,0,bg);
                    gameObjects.add(background);
                    currX+=picW;
                }
                currX=0;
                currY+=picH;
            }
        } catch (Exception e){
            System.out.println("Failed to init the Background");
        }
    }

    public static JFrame getJF(){
        return jFrame;
    }

    public static void endGame(){
        isRunning=false;
    }

    public static void startGame(){
        isRunning=true;
    }

    public static void setWinner(int winner){
        TankDriver.winner = winner;
    }
}

