package GameObjects;

import TankStuff.TankDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndScreen {
    private JFrame menuFrame;
    private JButton newGame = new JButton("New Game");
    private JButton end = new JButton("Quit");
    private JLabel winT = new JLabel();

    public void init(){
        this.menuFrame = new JFrame("Tank Game");
        this.menuFrame.setSize(TankDriver.SCREEN_WIDTH,TankDriver.SCREEN_HEIGHT);

        this.menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.menuFrame.setLayout(new BorderLayout());
        this.menuFrame.setLocationRelativeTo(null);
        this.menuFrame.setVisible(false);
        this.menuFrame.setResizable(false);
//        this.menuFrame.add(this);

        //create new panel to hold content
        JPanel titlePanel = new JPanel();


        winT.setPreferredSize(new Dimension(300,300));
        titlePanel.add(winT,BorderLayout.CENTER);
        menuFrame.add(titlePanel);

        //label w my name
        JLabel myName = new JLabel("Matthew Stephens");

        //add panel onto frame
        menuFrame.add(titlePanel,BorderLayout.BEFORE_FIRST_LINE);
        titlePanel.setBackground(Color.GREEN);
//        menuFrame.add(myName,BorderLayout.PAGE_END);



        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
//        buttonPanel.add(newGame);
        buttonPanel.add(end);
        buttonPanel.add(myName,BorderLayout.PAGE_END);
        menuFrame.add(buttonPanel,BorderLayout.SOUTH);

//        newGame.addActionListener(actionEvent -> {
//            TankDriver.startGame();
//            TankDriver.getJF().setVisible(true);
//            menuFrame.setVisible(false);
//            menuFrame.dispose();
//        });

        end.addActionListener(actionEvent -> {
            System.exit(0);
        });
    }

    public void exec(int tankID){
        TankDriver.getJF().setVisible(false);
        this.menuFrame.setVisible(true);
        winT.setText("Player " + tankID + " wins!");
    }
}
