import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;
private MSButton[][] benjaminButton; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs; //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(400, 600);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    
    
    benjaminButton = new MSButton [NUM_ROWS][NUM_COLS];
    bombs = new ArrayList <MSButton>();
    for(int a = 0; a < NUM_ROWS; a++)
    {
        for(int b = 0; b < NUM_COLS; b++)
        {
            benjaminButton[a][b] = new MSButton(a, b);
        }
    }
    //declare and initialize buttons
    setBombs();
}
public void setBombs()
{
    
    while(bombs.size() < 11)
    {
        int row = (int)(Math.random()*NUM_ROWS);
        int col = (int)(Math.random()*NUM_COLS);
        if(bombs.contains(benjaminButton[row][col]) == false)
        {
            bombs.add(benjaminButton[row][col]);
        }
    }
}
public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    for(int a = 0; a < benjaminButton.length; a++)
    {
        for(int b = 0; b < benjaminButton.length; b++)
        {
            if(benjaminButton[a][b].isMarked())
            {
                return true;
            }
        }
    }
    return false;
}
public void displayLosingMessage()
{
    if(isWon() == false)
    {
        fill(255);
        stroke(255);
        text("Game Over!", 200, 500);
    }
}
public void displayWinningMessage()
{
    if(isWon() == true)
    {
        fill(255);
        stroke(255);
        text("You Win!", 200, 500);
    }
}
public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); 
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
        clicked = true;
        if(keyPressed == true)
        {
            marked = !marked;
        }
        else if(bombs.contains(this))
        {
            displayLosingMessage();
        }
        else if(countBombs(r, c) > 0)
        {
            label = "" + countBombs(r, c);
        }
        else 
        {
            if(isValid(r-1, c) && benjaminButton[r-1][c].clicked == false)
            {
                benjaminButton[r-1][c].mousePressed();
            } 
            if(isValid(r, c-1) && benjaminButton[r][c-1].clicked == false)
            {
                benjaminButton[r][c-1].mousePressed();
            }
            if(isValid(r-1, c-1) && benjaminButton[r-1][c-1].clicked == false)
            {
                benjaminButton[r-1][c-1].mousePressed();
            }
            if(isValid(r+1, c) && benjaminButton[r+1][c].clicked == false)
            {
                benjaminButton[r+1][c].mousePressed();
            }
            if(isValid(r, c+1) && benjaminButton[r][c+1].clicked == false)
            {
                benjaminButton[r][c+1].mousePressed();
            }
            if(isValid(r+1, c+1) && benjaminButton[r+1][c+1].clicked == false)
            {
                benjaminButton[r+1][c+1].mousePressed();
            }
            if(isValid(r+1, c-1) && benjaminButton[r+1][c-1].clicked == false)
            {
                benjaminButton[r+1][c-1].mousePressed();
            }
            if(isValid(r-1, c+1) && benjaminButton[r-1][c+1].clicked == false)
            {
                benjaminButton[r-1][c+1].mousePressed();
            }
        }
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
             fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)                                           
    {                                           
        label = newLabel;                                           
    }                                           
    public boolean isValid(int r, int c)                                            
    {                                           
        if(r < 20 && r >= 0 && c < 20 && c >= 0)                                            
        {                                           
            return true;                                            
        }                                           
        return false;                                           
    }                                           
    public int countBombs(int row, int col)                                         
    {                                           
        int numBombs = 0;                                           
        if(isValid(r-1, c) == true && bombs.contains(benjaminButton[row-1][col]))                                            
        {                                           
            numBombs++;                                         
        }
        if(isValid(r, c-1) == true && bombs.contains(benjaminButton[row][col-1]))
        {
            numBombs++;
        }
        if(isValid(r-1, c-1) == true && bombs.contains(benjaminButton[row-1][col-1]))
        {
            numBombs++;
        }
        if(isValid(r+1, c) == true && bombs.contains(benjaminButton[row+1][col]))
        {
            numBombs++;
        }
        if(isValid(r, c+1) == true && bombs.contains(benjaminButton[row][col+1]))
        {
            numBombs++;
        }
        if(isValid(r+1, c+1) == true && bombs.contains(benjaminButton[row+1][col+1]))
        {
            numBombs++;
        }
        if(isValid(r+1, c-1) == true && bombs.contains(benjaminButton[row+1][col-1]))
        {
            numBombs++;
        }
        if(isValid(r-1, c+1) == true && bombs.contains(benjaminButton[row-1][col+1]))
        {
            numBombs++;
        } 
        return numBombs;                                            
    }                                           
}                                           
                                            
                                            
                                            
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
