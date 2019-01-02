/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanegame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

/**
 *
 * @author Daniel
 */
public class LaneGame {
    //create players
    static player USA = new player();
    static player USSR = new player();
    //main class just plays the game. I would remove this, but I want to be able to run just this file instead of going through the main menu.
    public static void main(String[] args) {
        playGame();
    }
    //this creates the UI
    public static void playGame(){
        GameFrame gf = new GameFrame();
        gf.setSize(1000,800);
        gf.setLocationRelativeTo(null);
        gf.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getY() < 600)
                    USA.turn();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
            
        });
    }
}
//GameFrame class that creates a JFrame with the components for the game.
class GameFrame extends JFrame{
    GamePanel p = new GamePanel();

    GameFrame(){
        this.add(p);
        this.setVisible(true);
        this.setResizable(false);
        
        lanes lane[] = new lanes[5];
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                p.frameUpdate();
                if(LaneGame.USA.health > 0 && LaneGame.USSR.health > 0 && LaneGame.USA.deck.size() > 0 && LaneGame.USSR.deck.size() > 0){
                    //System.out.println("\n\nUSA:");
                    //LaneGame.USA.turn();

                    //for(int i = 0; i < USA.hand.size(); i++){
                        //System.out.println(USA.hand.get(i).cardName);
                    //}
                    //System.out.println("\nUSSR:");
                    //LaneGame.USSR.turn();

                    //for(int i = 0; i < USSR.hand.size(); i++){
                        //System.out.println(USSR.hand.get(i).cardName);
                    //}
                }
            }
        },0,1000/p.frameRate);
        
    }
}

class player{
    ArrayList<card> deck = new ArrayList<>();
    ArrayList<card> hand = new ArrayList<>();
    int money = 1;
    int health = 10;
    int turn = 0;
    player(){
        for(int i = 0; i < 40; i++){
            this.deck.add(new card());
        }
        for(int i = 0; i < 4; i++){
            this.hand.add(this.deck.get(0));
            this.deck.remove(0);
        }
        /*        for(int i = 0; i < this.deck.size(); i++){
        System.out.println(this.deck.get(i).cardName);
        }*/
    }
    public void turn(){
        this.turn++;
        //at the start of a new turn, turn all recruits in the player's hand into soldiers.
        if(turn != 1){
            for(int i = 0; i < this.hand.size(); i++){
                if(this.hand.get(i).cardName.equals("Recruit")){
                    this.hand.get(i).cardName = "Soldier";
                    this.hand.get(i).strength = 2;
                    this.hand.get(i).health = 2;
                    this.hand.get(i).cost = 2;
                }
            }
        }
        //draw a card from the deck each turn, unless the player has 10 or more cards.
        if(this.hand.size() < 10){
            this.hand.add(this.deck.get(0));
            this.deck.remove(0);
        }
        //give the user money equal to what turn it is.
        this.money = this.turn;
    }
}

//creates a GamePanel
class GamePanel extends JPanel{
    int frameRate = 30;
    GamePanel(){
       
    }
    void frameUpdate(){
        this.repaint();
    }
    BufferedImage buffer = new BufferedImage(1000,800,BufferedImage.TYPE_INT_RGB);
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) buffer.getGraphics();
        //draw the background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getWidth());
        //draw the lane dividers
        g2d.setColor(Color.BLACK);
        g2d.drawLine((this.getWidth()/5), 0, (this.getWidth()/5), 600);
        g2d.drawLine((this.getWidth()/5)*2, 0, (this.getWidth()/5)*2, 600);
        g2d.drawLine((this.getWidth()/5)*3, 0, (this.getWidth()/5)*3, 600);
        g2d.drawLine((this.getWidth()/5)*4, 0, (this.getWidth()/5)*4, 600);
        g2d.drawLine(0, 600, this.getWidth(), 600);
        for(int i = 0; i < LaneGame.USA.hand.size(); i++){
            //draw the cards in the user's hand.
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect((int)(i*99), 650, 99, 150, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect((int)(i*99), 650, 99, 150, 10, 10);
        }
        g.drawImage(buffer, 0, 0, null);
    }
}
class card{
    int id = 0;
    int cost = 0;
    int strength = 1;
    int health = 1;
    
    String cardName = "";
    card(){
        //this generates the stats and name of each card.
        this.id = (int)(Math.floor(Math.random()*5));
        switch (id) {
            case 0:
                this.cardName = "Soldier";
                this.strength = 2;
                this.health = 1;
                this.cost = 1;
                break;
            case 1:
                this.cardName = "Cannon";
                this.strength = 5;
                this.health = 2;
                this.cost = 3;
                break;
            case 2:
                this.cardName = "Cavalry";
                this.strength = 2;
                this.health = 3;
                this.cost = 2;
                break;
            case 3:
                this.cardName = "Tank";
                this.strength = 2;
                this.health = 3;
                this.cost = 2;
                break;
            default:
                this.cardName = "Recruit";
                this.cost = 0;
                this.strength = 1;
                this.health = 1;
                break;
        }
    }
}
//used for lanes. This will have effects in the future.
class lanes{
    String environment = "";
    card USSRCard = null;
    card USACard = null;
    lanes(String environment){
        this.environment = environment;
    }
}