package lanegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.*;
class infoPane extends JDialog{
    infoPane(String tankName, String[] armament, String armour[], int crewSize, int year, String type, Image tankImage){
        this.setSize(800,400);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLocationRelativeTo(null);
        this.setTitle(tankName);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        this.add(p);
        p.setLayout(gbl);
        JLabel tn = new JLabel(tankName);
        String arma = "";
        for(int i = 0; i < armament.length; i++){
            arma+=armament[i];
            if(i != armament.length){
                arma+="<br>";
            }
        }
        String armours = "";
        for(int i = 0; i < armour.length; i++){
            armours+=armour[i];
            if(i != armour.length){
                armours+="<br>";
            }
        }
        gbc.weighty = 0.5;
        JLabel arms = new JLabel("<html>"+arma+"</html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        p.add(new JLabel("Name:     "),gbc);
        gbc.gridwidth = 1;
        gbc.gridx+=2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        p.add(tn,gbc);
        
        gbc.gridx = 0;
        gbc.gridy+=1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        p.add(new JLabel("Type:     "),gbc);
        gbc.gridx+=2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        p.add(new JLabel(""+type),gbc);
        
        gbc.gridx = 0;
        gbc.gridy+=1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        p.add(new JLabel("Armament:     "),gbc);
        gbc.gridx+=2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        p.add(arms,gbc);
        
        gbc.gridx = 0;
        gbc.gridy+=1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        p.add(new JLabel("Armour:     "),gbc);
        gbc.gridx+=2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        p.add(new JLabel("<html>"+armours+"</html>"),gbc);
        
        gbc.gridx = 0;
        gbc.gridy+=1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        p.add(new JLabel("Crew:     "),gbc);
        gbc.gridx+=2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        p.add(new JLabel(""+crewSize),gbc);
        
        gbc.gridx = 0;
        gbc.gridy+=1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        p.add(new JLabel("Year:     "),gbc);
        gbc.gridx+=2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        p.add(new JLabel(""+year),gbc);
        
        //add the image
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;
        gbc.gridheight = gbl.getLayoutDimensions()[1].length;;
       // gbc.gridwidth=4;
        gbc.weighty=1;
        gbc.weightx=1;
        gbc.fill = GridBagConstraints.BOTH;
        imageContainer imgc = new imageContainer(tankImage);
        p.add(imgc,gbc);
        imgc.repaint();
    }
}
class imageContainer extends JPanel{
    private Image imageToPaint = null;
    imageContainer(Image img){
        imageToPaint = img;
    }
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(imageToPaint.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH),0,0,null);
    }
}