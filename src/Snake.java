
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author alu23847452v
 */
public class Snake {

    protected Directions direction;
    protected Directions oldDirection;
    private int remainingGrow;
    private List<Node> body;
    private Graphics2D g2d;
    private int deltaTime;
    private Node headNode;
    Iterator it;

    /* Se construye la serpiente a base de
     nodos situados desde la mitad del tablero hacia atras */
    public Snake(int numNodes) {
        body = new ArrayList<Node>();
        for (int i = 0; i < numNodes; i++) {
            body.add(new Node(Config.numRows / 2, Config.numCols / 2 - i));
        }
    }

    public boolean canMove() {
        headNode = body.get(0);
        int col = headNode.getCol();
        int row = headNode.getRow();
        System.out.println(col + " " + row);
        if (col < 0 || col == Config.numCols || row < 0 || row == Config.numRows) {
            return false;
        } else {
            for (Node tail : body) {
                if (tail != headNode) {
                    if (tail.getCol() == col && tail.getRow() == row) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public boolean avanceSnakeAndEats(Fruit fruit) {
        
        headNode = body.get(0);
        if (canMove()) {
            switch (direction) {
                case UP:
                    body.add(0, new Node(headNode.getRow() - 1, headNode.getCol()));
                    break;
                case DOWN:
                    body.add(0, new Node(headNode.getRow() + 1, headNode.getCol()));
                    break;
                case RIGHT:
                    body.add(0, new Node(headNode.getRow(), headNode.getCol() + 1));
                    break;
                case LEFT:
                    body.add(0, new Node(headNode.getRow(), headNode.getCol() - 1));
            }
            if (!eats(fruit)) {
                body.remove(body.size() - 1);
            } else {
                
                return true;
            }
        }
        return false;
    }

    public boolean eats(Fruit fruit) {
        headNode = body.get(0);
        int fruitCol = fruit.getCol();
        int fruitRow = fruit.getRow();
        if (headNode.getCol() == fruitCol && headNode.getRow() == fruitRow) {
            return true;
        }
        return false;
    }

    public void paint(Graphics2D g, int squareWidth, int squareHeight) {
        for (Node node : body) {
            Board.drawSquare(g, squareWidth, squareHeight, node.getRow(), node.getCol(), Color.blue);
        }
    }
}
