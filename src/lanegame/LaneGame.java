package lanegame;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
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
    static int stage = 0;
    //this creates the UI
    static GameFrame gf = new GameFrame();
    public static void playGame(){
        stage = 0;
        gf.lane = new lanes[5];
        for(int i = 0; i < gf.lane.length; i++){
            gf.lane[i] = new lanes();
        }
        gf.lane[4].setEnvironment("Aquatic");
        gf.setSize(1000,800);
        gf.setLocationRelativeTo(null);
        gf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gf.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3){
                    if(e.getY() < 600){
                        //USA.turn();
                        for(int i = 0; i < gf.lane.length; i++){
                            if(new Rectangle((gf.p.getWidth()/5)*i,0,gf.p.getWidth()/5,200).contains(e.getPoint())){
                                try{
                                    showInfo(gf.lane[i].USACard.cardName);
                                }catch(NullPointerException ex){

                                }
                            }
                        }
                    }
                    //show the description of any cards in the lane when clicked.
                    else{
                        for(int i = 0; i < USA.hand.size(); i++){
                            if(new Rectangle(99*i,650,99,150).contains(e.getPoint())){
                                showInfo(USA.hand.get(i).cardName);
                            }
                        }
                    }
                }
                else if(stage==0){
                    for(int i = 0; i < USA.hand.size(); i++){
                        if(new Rectangle(99*i,650,99,150).contains(e.getPoint())){
                            for(int z = 0; z < USA.hand.size(); z++){
                                USA.hand.get(z).selected = false;
                            }
                            USA.hand.get(i).selected = USA.hand.get(i).selected == false;
                        }
                    }
                    if(e.getY() < 650){
                        for(int i = 0; i < gf.lane.length; i++){
                            if(new Rectangle((gf.p.getWidth()/5)*i,400,gf.p.getWidth()/5,gf.p.getHeight()/5).contains(e.getPoint())){
                                for(int z = 0; z < USA.hand.size(); z++){
                                    if(USA.hand.get(z).selected){
                                        if(gf.lane[i].USACard.cardName.isEmpty() || gf.lane[i].USACard.cardName.equals("")){
                                            gf.lane[i].USACard = USA.hand.get(z);
                                            USA.hand.remove(z);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(gf.p.turnRect.contains(e.getPoint())){
                    System.out.println("Clicked");
                }
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
    public static void showInfo(String in){
        switch(in){
            case "M41 Walker Bulldog":
                m41Bulldog.showInfo();
                break;
            case "M60 Patton":
                m60patton.showInfo();
                break;
            case "Type 85":
                type85.showInfo();
                break;
            case "Coyote RV":
                coyote.showInfo();
                break;
            default:
                break;
        }
    }
}
//GameFrame class that creates a JFrame with the components for the game.
class GameFrame extends JFrame{
    static int turn = 0;
    GamePanel p = new GamePanel();
    JButton b = new JButton("");
    lanes lane[] = new lanes[5];
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    GameFrame(){
        /*this.setLayout(gbl);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy = 0;
        this.add(p,gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        gbc.gridy = 1;
        this.add(b,gbc);*/
        this.add(p);
        this.setVisible(true);
        this.setResizable(false);
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                p.frameUpdate();
                if(LaneGame.USA.health > 0 && LaneGame.USSR.health > 0 && LaneGame.USA.deck.size() > 0 && LaneGame.USSR.deck.size() > 0){
                    if(turn == 1){
                        //LaneGame.USSR.draw();
                    }
                }
            }
        },0,1000/p.frameRate);
    }
}
class turn{
    void newTurn(){
        
    }
    void phase(){
        
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
    }
    public void turn(){
        this.turn++;
        //draw a card from the deck each turn, unless the player has 10 or more cards.
        draw(1);
        //give the user money equal to what turn it is.
        this.money = this.turn;
    }
    public void draw(int count){
        for(int i = 0; i < count; i++){
            if(this.hand.size() < 10){
                this.hand.add(this.deck.get(0));
                this.deck.remove(0);
            }
        }
    }
}

//creates a GamePanel
class GamePanel extends JPanel{
    Rectangle turnRect = new Rectangle(570,820,80,80);
    Image arrowDown;
    int frameRate = 30;
    GamePanel(){
        try {
            arrowDown = ImageIO.read(new File("src\\lanegame\\ArrowDown.png")).getScaledInstance(30, 50, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            
        }
    }
    void frameUpdate(){
        this.repaint();
    }
    BufferedImage buffer = new BufferedImage(1000,800,BufferedImage.TYPE_INT_RGB);
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) buffer.getGraphics();
        
        //<editor-fold desc="Antialiasing">
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //</editor-fold>
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        for(int i = 0; i < LaneGame.gf.lane.length; i++){
            if(LaneGame.gf.lane[i].getEnvironment().equals("Aquatic"))
                g2d.setColor(Color.BLUE);
            else if(LaneGame.gf.lane[i].getEnvironment().equals("Normal"))
                g2d.setColor(Color.WHITE);
            g2d.fillRect((this.getWidth()/5)*i, 0, this.getWidth()/5, 600);
            g2d.setColor(Color.BLACK);
            g2d.drawLine((this.getWidth()/5)*i, 0, (this.getWidth()/5)*i, 600);
        }
        g2d.setFont(new Font("Times New Roman",Font.BOLD,14));
        g2d.drawLine(0, 600, this.getWidth(), 600);
        for(int i = 0; i < LaneGame.USA.hand.size(); i++){
            if(LaneGame.USA.hand.get(i).selected){
                g2d.drawImage(arrowDown, i*99+i+99/2-arrowDown.getWidth(null)/2, 560, null);
            }
            if(LaneGame.USA.hand.get(i).cardName.equals("M41 Walker Bulldog")){
                g2d.drawImage(m41Bulldog.getCardImage().getScaledInstance(99, 150, Image.SCALE_SMOOTH),i*99+i,615,null);
            }
            else if(LaneGame.USA.hand.get(i).cardName.equals("M60 Patton")){
                g2d.drawImage(m60patton.getCardImage().getScaledInstance(99, 150, Image.SCALE_SMOOTH),i*99+i,615,null);
            }
            else if(LaneGame.USA.hand.get(i).cardName.equals("Type 85")){
                g2d.drawImage(type85.getCardImage().getScaledInstance(99, 150, Image.SCALE_SMOOTH),i*99+i,615,null);
            }
            else if(LaneGame.USA.hand.get(i).cardName.equals("Coyote RV")){
                g2d.drawImage(coyote.getCardImage().getScaledInstance(99, 150, Image.SCALE_SMOOTH),i*99+i,615,null);
            }
            g2d.drawString(LaneGame.USA.hand.get(i).health+"", i*99+i+24, 737);
            g2d.drawString(LaneGame.USA.hand.get(i).strength+"", i*99+i+60, 737);
        }
        g2d.setColor(Color.WHITE);
        g2d.fillRect(turnRect.x, turnRect.y, turnRect.width, turnRect.height);
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(turnRect.x, turnRect.y, turnRect.width, turnRect.height);
        g2d.draw(turnRect);
        
        g.drawImage(buffer, 0, 0, null);
    }
}
//<editor-fold desc="Tanks">
    //<editor-fold desc="M41 Bulldog">
        final class m41Bulldog{
            public static boolean imageSet = false;
            public static Image cardImage;
            static final boolean amphibious = false;
            static final int health = 2, strength = 2, cost = 2;
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
            static public Image getCardImage(){
                if(imageSet == false){
                    try {
                        cardImage = ImageIO.read(new File("src\\lanegame\\M41BulldogCard.jpg"));
                        imageSet = true;
                    } catch (IOException ex) {

                    }
                }
                return cardImage;
            }
            static public void showInfo(){
                new infoPane(name,armaments,armour, crewSize, year ,type, getImage()).setVisible(true);
            }
        }
    //</editor-fold>
    //<editor-fold desc="M60 Patton">
        final class m60patton{
            public static boolean imageSet = false;
            public static Image cardImage;
            static public Image getCardImage(){
                if(imageSet == false){
                    try {
                        cardImage = ImageIO.read(new File("src\\lanegame\\M60PattonCard.jpg"));
                        imageSet = true;
                    } catch (IOException ex) {

                    }
                }
                return cardImage;
            }
            static final boolean amphibious = false;
            static final int health = 4, strength = 3, cost = 4;
            static final String name = "M60 Patton";
            static final String description = "US Main Battle Tank";
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
    //<editor-fold desc="Type 85">
        final class type85{
            public static boolean imageSet = false;
            public static Image cardImage;
            static public Image getCardImage(){
                if(imageSet == false){
                    try {
                        cardImage = ImageIO.read(new File("src\\lanegame\\Type85Card.jpg"));
                        imageSet = true;
                    } catch (IOException ex) {

                    }
                }
                return cardImage;
            }
            static final boolean amphibious = true;
            static final int health = 3, strength = 2, cost = 4;
            static final String name = "Type 85";
            static final String description = "Korean Light Amphibious Tank.";
            static final String[] armour = new String[]{
                "Unknown armour"
            };
            static final int crewSize = 4;
            static final int year = 1960;
            static final String type = "Main Battle Tank";

            static final String[] armaments = new String[]{
                "85mm main gun (Anti-Tank Guided Missile Capable)",
                "14.5mm AA heavy machine gun",
                "7.62mm general purpose machine gun"
            };
            static public Image getImage(){
                try {
                    return ImageIO.read(new File("src\\lanegame\\Type85.jpg"));
                } catch (IOException ex) {
                    return null;
                }
            }
            static public void showInfo(){
                new infoPane(name,armaments,armour, crewSize, year ,type, getImage()).setVisible(true);
            }
        }
    //</editor-fold>
    //<editor-fold desc="Coyote RV">
        final class coyote{
            public static boolean imageSet = false;
            public static Image cardImage;
            static public Image getCardImage(){
                if(imageSet == false){
                    try {
                        cardImage = ImageIO.read(new File("src\\lanegame\\CoyoteRVCard.jpg"));
                        imageSet = true;
                    } catch (IOException ex) {

                    }
                }
                return cardImage;
            }
            static final boolean amphibious = false;
            static final int health = 4, strength = 3, cost = 4;
            static final String name = "Coyote RV";
            static final String description = "Canadian Reconnaissance Vehicle.";
            static final String[] armour = new String[]{
                "N/A"
            };
            static final int crewSize = 4;
            static final int year = 1996;
            static final String type = "Reconnaissance";

            static final String[] armaments = new String[]{
                "M242 25mm chain gun",
                "C6 7.62mm coaxial machine gun",
                "C6 7.62mm pintle machine gun",
                "8 grenade launchers<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• 2 clusters of 4"
            };
            static public Image getImage(){
                try {
                    return ImageIO.read(new File("src\\lanegame\\CoyoteRV.jpg"));
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
    boolean amphibious = false;
    String cardDesc = "";
    String cardName = "";
    boolean selected = false;
    card(){
        //this generates the stats and name of each card.
        this.id = (int)(Math.floor(Math.random()*4));
        switch (id) {
            case 0:
                this.cardName = m60patton.name;
                this.cardDesc = m60patton.description;
                this.strength = m60patton.strength;
                this.health = m60patton.health;
                this.cost = m60patton.cost;
                this.type = m60patton.type;
                this.amphibious = m60patton.amphibious;
                break;
            case 1:
                this.cardName = type85.name;
                this.cardDesc = type85.description;
                this.strength = type85.strength;
                this.health = type85.health;
                this.cost = type85.cost;
                this.type = type85.type;
                this.amphibious = type85.amphibious;
                break;
            case 2:
                this.cardName = coyote.name;
                this.cardDesc = coyote.description;
                this.strength = coyote.strength;
                this.health = coyote.health;
                this.cost = coyote.cost;
                this.type = coyote.type;
                this.amphibious = coyote.amphibious;
                break;
            case 3:
                this.cardName = m41Bulldog.name;
                this.cardDesc = m41Bulldog.description;
                this.strength = m41Bulldog.strength;
                this.health = m41Bulldog.health;
                this.cost = m41Bulldog.cost;
                this.type = m41Bulldog.type;
                this.amphibious = m41Bulldog.amphibious;
                break;
            default:
                this.cardName = "Recruit";
                this.cardDesc = "";
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
                this.type=m60patton.type;
                this.amphibious = m60patton.amphibious;
                break;
            case "Type 85":
                this.cardName = type85.name;
                this.cardDesc = type85.description;
                this.strength = type85.strength;
                this.health = type85.health;
                this.cost = type85.cost;
                this.type=type85.type;
                this.amphibious = type85.amphibious;
                break;
            case "Coyote RV":
                this.cardName = coyote.name;
                this.cardDesc = coyote.description;
                this.strength = coyote.strength;
                this.health = coyote.health;
                this.cost = coyote.cost;
                this.type = coyote.type;
                this.amphibious = coyote.amphibious;
                break;
            case "M41 Walker Bulldog":
                this.cardName = m41Bulldog.name;
                this.cardDesc = m41Bulldog.description;
                this.strength = m41Bulldog.strength;
                this.health = m41Bulldog.health;
                this.cost = m41Bulldog.cost;
                this.type=m41Bulldog.type;
                this.amphibious = m41Bulldog.amphibious;
                break;
            case "Recruit":
                this.cardName = "Recruit";
                this.cardDesc = "";
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
                this.type="Nothing";
                break;
        }
    }
}
//used for lanes. This stores which cards are played.
class lanes{
    private String environment = "Normal";
    card USSRCard;
    card USACard;
    public void setEnvironment(String n){
        environment = n;
    }
    public String getEnvironment(){
        return environment;
    }
    lanes(){
        USSRCard = new card("");
        USACard = new card("");
    }
}