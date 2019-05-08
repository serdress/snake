
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

    public boolean canMove(Node nextNode) {
        headNode = body.get(0);
       System.out.println("Head node col:" + headNode.getCol() + "Head node row" + headNode.getRow());
        int col = nextNode.getCol();
        int row = nextNode.getRow();
       System.out.println("New node col:" + col + "New node row" + row);
        if (col == -1 || col > Config.numCols || row == -1 || row > Config.numRows) {
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
    
    public Node upNode() {
       return new Node(headNode.getRow() - 1, headNode.getCol());
    }
    
    public Node downNode() {
        return new Node(headNode.getRow() + 1, headNode.getCol());
    }
    
    public Node rightNode() {
        return new Node(headNode.getRow(), headNode.getCol() + 1);
    }
    
    public Node leftNode() {
        return new Node(headNode.getRow(), headNode.getCol() - 1);
    }

    public boolean avanceSnakeAndEats(Fruit fruit) {
        
        headNode = body.get(0);
        
            switch (direction) {
                case UP:
                    if (canMove(upNode())) {
                        body.add(0,upNode());
                    }
                    break;
                case DOWN:
                    if (canMove(downNode())) {
                        body.add(0,downNode());
                    }
                    break;
                case RIGHT:
                    if (canMove(rightNode())) {
                        body.add(0,rightNode());
                    }                    
                    break;
                case LEFT:
                    if (canMove(leftNode())) {
                        body.add(0,leftNode());
                    }
                    break;
            }
            if (!eats(fruit)) {
                body.remove(body.size() - 1);
            } else {
                
                return true;
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
    
      
   
    public void paint(Graphics2D g, int squareWidth, int squareHeight, int widthMargin, int heightMargin) {
        boolean first = true;
        for (Node node : body) {
             if (first) {
            Board.drawSquare(g, squareWidth, squareHeight, node.getRow(), node.getCol(), Color.green, widthMargin, heightMargin );
            first = false;
        } else {
            Board.drawSquare(g, squareWidth, squareHeight, node.getRow(), node.getCol(), Color.blue, widthMargin, heightMargin );
             }
        }
    }
}
