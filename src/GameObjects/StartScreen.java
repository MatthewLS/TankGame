package GameObjects;

import TankStuff.TankDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class StartScreen extends JPanel {
    private JFrame menuFrame;
    private JButton start = new JButton("Start");
    private JButton end = new JButton("End");

    public void init(){
        TankDriver.getJF().setVisible(false);
        this.menuFrame = new JFrame("Tank Game");
        this.menuFrame.setSize(TankDriver.SCREEN_WIDTH,TankDriver.SCREEN_HEIGHT);

        this.menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.menuFrame.setLayout(new BorderLayout());
        this.menuFrame.setLocationRelativeTo(null);
        this.menuFrame.setVisible(true);
        this.menuFrame.setResizable(false);
        this.menuFrame.add(this);

        //label w my name
        JLabel myName = new JLabel("Matthew Stephens");
        //create new panel to hold content
        JPanel titlePanel = new JPanel();
        //load in titleImg and add that into a JLabel
        BufferedImage titleImg = TankDriver.imgList.get("gameTitle");
        JLabel titleLabel = new JLabel(new ImageIcon(titleImg));
        //add label that hold titleImg into panel
        titlePanel.add(titleLabel);
        //add panel onto frame
        menuFrame.add(titlePanel,BorderLayout.BEFORE_FIRST_LINE);
        titlePanel.setBackground(Color.BLACK);
//        menuFrame.add(myName,BorderLayout.PAGE_END);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(start);
        buttonPanel.add(end);
        buttonPanel.add(myName,BorderLayout.PAGE_END);
        menuFrame.add(buttonPanel,BorderLayout.SOUTH);

        start.addActionListener(actionEvent  -> {
            TankDriver.getJF().setVisible(true);
            menuFrame.setVisible(false);
            menuFrame.dispose();
        });

        end.addActionListener(actionEvent -> {
            System.exit(0);
        });
    }



}
