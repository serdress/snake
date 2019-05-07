
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
    private Board board;
    private Node node;
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
        node = body.get(0);
        int col = node.getCol();
        int row = node.getRow();
        if (col <= 0 || col == Config.numCols - 1 || row <= 0 || row == Config.numRows - 1) {
            return false;
        } else {
            return true;
        }
    }

    public void avanceSnake() {
        node = body.get(0);
        if (canMove()) {
            switch (direction) {
                case UP:
                        body.add(0, new Node(node.getRow() - 1, node.getCol()));
                    break;
                case DOWN:
                        body.add(0, new Node(node.getRow() + 1, node.getCol()));
                    break;
                case RIGHT:
                        body.add(0, new Node(node.getRow(), node.getCol() + 1));
                    break;
                case LEFT:
                        body.add(0, new Node(node.getRow(), node.getCol() - 1));
            }
            if (!eats(node)) {
                body.remove(body.size() - 1);
            }
        } else {
          //  board.initGame();
        }
    }

    public boolean eats(Node node) {
        /*         Fruit fruit = board.getFruit();
       Iterator it = body.iterator();
       System.out.println(fruit);
       int fruitCol = fruit.getCol();
       System.out.print(fruit);
        int fruitRow = fruit.getRow();
        node = (Node) it.next();
        if (node.getCol() == fruitCol && node.getRow() == fruitRow){
            System.out.println(node.getCol() + " FRUIT ADDED " + node.getRow());
            return true;
        }*/
        return false;
    }

    public void paint(Graphics2D g, int squareWidth, int squareHeight) {
        for (Node node : body) {
            System.out.println(node.getCol());
            System.out.println(node.getRow());
            Board.drawSquare(g, squareWidth, squareHeight, node.getRow(), node.getCol(), Color.blue);
        }
    }
}
