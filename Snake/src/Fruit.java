
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
    private boolean special;
    private boolean intro;

    public Fruit(boolean special, Snake snake, boolean intro) {
        super(0, 0);
        this.special = special;
        Random rRC = new Random(); //Random Row/Col
        row = (rRC.nextInt(Config.numRows)); //New Random Row
        col = (rRC.nextInt(Config.numCols)); //New Random Col
        this.intro = intro;
    }

    public Fruit(int col, int row) {
        super(0, 0);
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    // Paint a fruit using the drawSquare method in Board
    public void paint(Graphics2D g, int squareWidth, int squareHeight, int widthMargin, int heightMargin) {
        if (intro) {
            return;
        }
        if (special) {
            Board.drawSquare(g, squareWidth, squareHeight, row, col, Color.red, widthMargin, heightMargin);
        } else {
            Board.drawSquare(g, squareWidth, squareHeight, row, col, Color.yellow, widthMargin, heightMargin);
        }
    }

}
