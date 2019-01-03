package lanegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import javax.imageio.ImageIO;
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
    
    public static boolean contains(Point a, Point b, Point c){
        //checks if Point c is contained between Point a and Point b
        if(c.x > a.x && c.x < b.x && c.y > a.y && c.y < b.y)
            return true;
        return false;
    }
    //this creates the UI
    public static void playGame(){
        GameFrame gf = new GameFrame();
        gf.lane = new lanes[5];
        gf.setSize(1000,800);
        gf.setLocationRelativeTo(null);
        gf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gf.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getY() < 600){
                    //USA.turn();
                    if(new Rectangle(0,0,gf.p.getWidth()/5,200).contains(e.getPoint())){
                        try{
                            JOptionPane.showMessageDialog(null, gf.lane[0].USSRCard.cardDesc,gf.lane[0].USSRCard.cardName,JOptionPane.INFORMATION_MESSAGE);
                        }catch(NullPointerException ex){
                            
                        }
                    }
                }
                else{
                    for(int i = 0; i < USA.hand.size(); i++){
                        if(new Rectangle(99*i,650,99,150).contains(e.getPoint())){
                            JOptionPane.showMessageDialog(null, USA.hand.get(i).cardDesc, USA.hand.get(i).cardName, JOptionPane.INFORMATION_MESSAGE);
                            System.out.println(USA.hand.get(i).cardName);
                            switch(USA.hand.get(i).cardName){
                                case "M41 Walker Bulldog":
                                    m41Bulldog.showInfo();
                                    break;
                                case "M60 Patton":
                                    m60patton.showInfo();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                //show the description of any cards in the lane when clicked.
                
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
    lanes lane[] = new lanes[5];
    GameFrame(){
        this.add(p);
        this.setVisible(true);
        this.setResizable(false);
        
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
                    this.hand.get(i).cardDesc = "Transformed from Recruit";
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
            g2d.fillRoundRect(i*99, 650, 99, 150, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(i*99, 650, 99, 150, 10, 10);
            g2d.drawString(LaneGame.USA.hand.get(i).cardName, i*99+20, 670);
        }
        g.drawImage(buffer, 0, 0, null);
    }
}
class descriptions{
        public static final String
            und = "Undefined";
}
//<editor-fold desc="Tanks">
    //<editor-fold desc="M41 Bulldog">
        final class m41Bulldog{
            static final int health = 4, strength = 3, cost = 4;
            static final String name = "M41 Walker Bulldog";
            static final String description = "US Light Tank";
            static final String[] armour = new String[]{
                "25.4mm turret front",
                "25mm turret sides and rear",
                "12.7mm turret top",
                "31.7mm at 45° hull nose plate",
                "25.4mm at 30° hull glacis plate",
                "19mm hull rear",
                "9.25mm hull floor"
            };
            static final int crewSize = 4;
            static final int year = 1949;
            static final String type = "Light Tank";

            static final String[] armaments = new String[]{
                "76mm M32A1 rifled cannon",
                ".30cal M1919A4 coaxial machine gun",
                ".50cal Browning M2 roof-mounted machine gun"
            };
            static public Image getImage(){
                try {
                    return ImageIO.read(new File("src\\lanegame\\M41Bulldog.jpg"));
                } catch (IOException ex) {
                    return null;
                }
            }
            static public void showInfo(){
                new infoPane(name,armaments,armour, crewSize, year ,type, getImage()).setVisible(true);
            }
        }
    //</editor-fold>
    //<editor-fold desc="M60 Patton">
        final class m60patton{
            static final int health = 4, strength = 3, cost = 4;
            static final String name = "M60 Patton";
            static final String description = "US Main Battle Tank. Takes 1 less damage from all sources.";
            static final String[] armour = new String[]{
                "3.67 in (93 mm) at 65°",
                "8.68 in (220 mm) LoS"
            };
            static final int crewSize = 4;
            static final int year = 1960;
            static final String type = "Main Battle Tank";

            static final String[] armaments = new String[]{
                "M68 105 mm cannon",
                ".50 BMG M85 mounted on M19 Commanders Cupola",
                "7.62×51mm NATO M73 machine gun"
            };
            static public Image getImage(){
                try {
                    return ImageIO.read(new File("src\\lanegame\\M60Patton.jpg"));
                } catch (IOException ex) {
                    return null;
                }
            }
            static public void showInfo(){
                new infoPane(name,armaments,armour, crewSize, year ,type, getImage()).setVisible(true);
            }
        }
    //</editor-fold>
//</editor-fold>
class card{
    String type = "";
    int id = 0;
    int cost = 0;
    int strength = 1;
    int health = 1;
    String cardDesc = "";
    String cardName = "";
    card(){
        //this generates the stats and name of each card.
        this.id = (int)(Math.floor(Math.random()*5));
        switch (id) {
            case 0:
                this.cardName = "M60 Patton";
                this.cardDesc = m60patton.description;
                this.strength = m60patton.strength;
                this.health = m60patton.health;
                this.cost = m60patton.cost;
                break;
            case 1:
                this.cardName = "Cannon";
                this.cardDesc = descriptions.und;
                this.strength = 5;
                this.health = 2;
                this.cost = 3;
                break;
            case 2:
                this.cardName = "Cavalry";
                this.cardDesc = descriptions.und;
                this.strength = 2;
                this.health = 3;
                this.cost = 2;
                break;
            case 3:
                this.cardName = this.cardName = m41Bulldog.name;
                this.cardDesc = m41Bulldog.description;
                this.strength = m41Bulldog.strength;
                this.health = m41Bulldog.health;
                this.cost = m41Bulldog.cost;
                break;
            default:
                this.cardName = "Recruit";
                this.cardDesc = descriptions.und;
                this.cost = 0;
                this.strength = 1;
                this.health = 1;
                break;
        }
    }
    card(String c){
        switch (c) {
            case "M60 Patton":
                this.cardName = m60patton.name;
                this.cardDesc = m60patton.description;
                this.strength = m60patton.strength;
                this.health = m60patton.health;
                this.cost = m60patton.cost;
                break;
            case "Cannon":
                this.cardName = "Cannon";
                this.cardDesc = descriptions.und;
                this.strength = 5;
                this.health = 2;
                this.cost = 3;
                break;
            case "Calvary":
                this.cardName = "Cavalry";
                this.cardDesc = descriptions.und;
                this.strength = 2;
                this.health = 3;
                this.cost = 2;
                break;
            case "M41 Walker Bulldog":
                this.cardName = m41Bulldog.name;
                this.cardDesc = m41Bulldog.description;
                this.strength = m41Bulldog.strength;
                this.health = m41Bulldog.health;
                this.cost = m41Bulldog.cost;
                break;
            case "Recruit":
                this.cardName = "Recruit";
                this.cardDesc = descriptions.und;
                this.cost = 0;
                this.strength = 1;
                this.health = 1;
                break;
            default:
                this.cardName = "";
                this.cardDesc = "";
                this.cost = 0;
                this.strength = 0;
                this.health = 0;
                break;
        }
    }
}
//used for lanes. This stores which cards are played.
class lanes{
    String environment = "";
    card USSRCard = new card("empty");
    card USACard = new card("empty");
    lanes(String environment){
        this.environment = environment;
    }
}