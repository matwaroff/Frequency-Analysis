/*
 * University of Central Florida
 * COP3330 - Fall 2015
 * Author:  Mathew Waroff
 */
package histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.lang.String;
import java.util.Arrays;

public class HistogramPanel extends JPanel {
   
   private List<String> sents;
   private int snum;
   private int[] charcounts = new int[26];
   
   public String readFile( File file ) {
      sents = new ArrayList();
      snum = -1;
      clearDisplay( this.getGraphics() );
      StringBuilder sb = new StringBuilder();
      try {
         Scanner scanner = new Scanner( new FileInputStream(file));
         while( scanner.hasNextLine() ) {
            sents.add( scanner.nextLine() );
            
         }
         
         for( int i = 0; i < sents.size(); i++ ) {
            sb.append(i + " : " + sents.get( i ) + "\n\n");
         }
      } catch (FileNotFoundException ex) {
         Logger.getLogger(HistogramPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
      return sb.toString().trim();
   }
   
   @Override
   public void paintComponent( Graphics gc ) {
      super.paintComponent( gc );
      if( sents != null && snum >= 0 && snum < sents.size() ) { 
         showHisto( snum, true );
      }
   }
   
   public void showHisto() {
      this.setBackground( Color.white );
      showHisto( snum, false );
   }
   
   public void showHisto( int n, boolean b ) {            
      if( sents != null && n >= 0 && n < sents.size() ) {
         snum = n;
         Graphics gc = this.getGraphics();
         clearDisplay( gc );
         drawLines( gc );
         drawHisto( gc );
  
      }
      else if( b && sents != null ) {
         JOptionPane.showMessageDialog(this, "Sentence index out of range");
      }
   }
   
   private void clearDisplay( Graphics gc ) {      
      gc.setColor( Color.WHITE );
      gc.fillRect(0,0,this.getWidth(),this.getHeight());
   }
   
   private void drawLines( Graphics gc ) {
        int leftxbound = (int)(.1*this.getWidth());
        int rightxbound = (int)(.9*this.getWidth());
        int topybound = (int)(.1*this.getHeight());
        int bottomybound = (int)(.9*this.getHeight());
        
        gc.setColor(Color.red);
        gc.drawLine(leftxbound, topybound, leftxbound, bottomybound);
        gc.drawLine(leftxbound, bottomybound, rightxbound, bottomybound);

   }
   
    private void drawHisto( Graphics gc ) {
        char[] alpha = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int rectwidth = (int)(.8*this.getWidth())/26;
        int rectheight = (int)(.8*this.getHeight());
        int startingheight = (int)(.1*this.getHeight());
        int curpos = (int)(.1*this.getWidth());
        setCharCounts();
        scale(charcounts);
        gc.setColor(Color.BLUE);
        
        for(int i = 0; i < 26; i++){
            int x = curpos + rectwidth*i;
            int y = startingheight + (rectheight - charcounts[i]);
            gc.drawRect(x, y, rectwidth, charcounts[i]);
            int xmiddle = x + rectwidth/2;
            gc.drawChars(alpha, i, 1, xmiddle, y+10);
        }
   }

    private void setCharCounts(){
        String string = sents.get(snum);
        string = string.toLowerCase();
        char temp;
        int[] chars = new int[256];
        
        for(int i = 0; i < sents.get(snum).length(); i++){
            temp = string.charAt(i);
            if(Character.isLetter(temp)){
                chars[(int)temp]++;
            }
        }
        
        int n = 0;
        for(int i = 97; i < 123; i++){
            
            charcounts[n] = chars[i];
            n++;
        }
        
    }
    
    private int findMax( int[] incounts ){
        int max = incounts[0];
        for(int i = 0; i < 26; i++){
            if(incounts[i] > max){
                max = incounts[i];
            }
        }
        return max;
    }
    
    private void scale( int[] incounts ){
        int max = findMax( incounts );
        int maxheight = (int)(.8*this.getHeight());
        for(int i = 0; i < charcounts.length; i++){
            double scaled = maxheight * (((double) incounts[i] ) / max);
            charcounts[i] = (int) Math.floor(scaled);
        }
    }
}
