import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author alu20904877q
 */
public class Fruit extends Node {

    private int row;
    private int col;

    public Fruit(boolean special, Snake snake) {
        super(0, 0);

        Random rRC = new Random(); //Random Row/Col
        row = (rRC.nextInt(Config.numRows)); //New Random Row
        col = (rRC.nextInt(Config.numCols)); //New Random Col

    }
    
    public int getCol() {
        return this.col;
    }
    
    public int getRow() {
        return this.row;
    }

    public void paint(Graphics2D g, int squareWidth, int squareHeight) {
        Board.drawSquare(g, squareWidth, squareHeight, row, col, Color.orange);

    }
    
}
