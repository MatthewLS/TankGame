package MapLoading;

import GameObjects.*;
import TankStuff.TankDriver;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/*
class MapLoader for the purpose of loading obstacles (walls, power ups, etc.) onto the map.
 */
public class MapLoader {

    Class<?> c;
    GameObject gameObject;
    private String className;
    private static HashMap<Integer, String> nameTable;
    static{
        nameTable = new HashMap<>();
        nameTable.put(0,"");
        nameTable.put(1,"WallUnbreakable");
        nameTable.put(2,"WallBreakable");
        nameTable.put(3,"HealthPU");
        nameTable.put(4,"RocketPU");
        nameTable.put(5,"SpeedPU");

        nameTable.put(9,"WallUnbreakable");
    }

    public void init(){
        int wallWidth = TankDriver.imgList.get("WallUnbreakable").getWidth();
        int wallHeight = TankDriver.imgList.get("WallUnbreakable").getHeight();

        try {
            InputStreamReader isr = new InputStreamReader(TankDriver.class.getClassLoader().getResourceAsStream("maps/map1.txt"));
            BufferedReader mapReader = new BufferedReader(isr);
            String row = mapReader.readLine();
            String mapInfo[] = row.split("  ");
            int numRows = Integer.parseInt(mapInfo[0]), numCols = Integer.parseInt(mapInfo[1]);


            for (int i = 0;i<numRows;i++){
                row = mapReader.readLine();
                mapInfo= row.split("\t");

                for (int j = 0;j<numCols;j++){
                    className = nameTable.get(Integer.parseInt(mapInfo[j]));
                    if (className.equals("")){
                        continue;
                    }
                    c = Class.forName("GameObjects." + className);
                    gameObject = (GameObject) c.getConstructor().newInstance();

                    //heart is smalls size so need to make a little adjustment where it init's
                    if (gameObject instanceof HealthPU){
                        gameObject.init(j*wallWidth - 17,i*wallHeight - 10,0,0,0,TankDriver.imgList.get(className));
                    } else
                        gameObject.init(j*wallWidth,i*wallHeight,0,0,0,TankDriver.imgList.get(className));


                    TankDriver.addToGameObjects(gameObject);
                    if (!mapInfo[j].equals("9")){
                        TankDriver.addToCollidables((Collidable)gameObject);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("MapLoader.init() problem------" + e);
        }
    }
}
