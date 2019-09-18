//

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
    public List<Node> body;
    private Graphics2D g2d;
    Iterator it;
    public boolean player2;
    private int remainGrow;

    /* Se construye la serpiente a base de
     nodos situados desde la mitad del tablero hacia atras */
    public Snake(int numNodes, int col) {
        body = new ArrayList<Node>();
        for (int i = 0; i < numNodes; i++) {

            body.add(new Node(Config.numRows / 2, col - i));
        }
    }

    public Snake(int numNodes, int col, boolean player2) {
        direction = direction.RIGHT;
        body = new ArrayList<Node>();
        for (int i = 0; i < numNodes; i++) {
            body.add(new Node((Config.getInstance().numRows / 4), (Config.getInstance().numCols / 2) - i));

        }
        this.player2 = player2;
        remainGrow = 0;
        // turning = false;
        // gameOver = false;
    }

    public boolean canMove(Node nextNode) {
        //   System.out.println("Head node col:" + body.get(0).getCol() + "Head node row" + body.get(0).getRow());
        int col = nextNode.getCol();
        int row = nextNode.getRow();
        // System.out.println("New node col:" + col + "New node row" + row);
        if (col == -1 || col >= Config.numCols || row == -1 || row >= Config.numRows) {
            return false;
        } else {
            for (Node tail : body) {
                if (tail != body.get(0)) {
                    if (tail.getCol() == col && tail.getRow() == row) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public Node getHead() {
        Node node = body.get(0);
        return node;
    }

    public Node upNode() {
        return new Node(body.get(0).getRow() - 1, body.get(0).getCol());
    }

    public Node downNode() {
        return new Node(body.get(0).getRow() + 1, body.get(0).getCol());
    }

    public Node rightNode() {
        return new Node(body.get(0).getRow(), body.get(0).getCol() + 1);
    }

    public Node leftNode() {
        return new Node(body.get(0).getRow(), body.get(0).getCol() - 1);
    }

    public boolean avanceSnakeAndEats(Fruit fruit) {

        switch (direction) {
            case UP:
                if (canMove(upNode())) {
                    body.add(0, upNode());
                } else {
                    return false;
                }
                break;
            case DOWN:
                if (canMove(downNode())) {
                    body.add(0, downNode());
                } else {
                    return false;
                }
                break;
            case RIGHT:
                if (canMove(rightNode())) {
                    body.add(0, rightNode());
                } else {
                    return false;
                }
                break;
            case LEFT:
                if (canMove(leftNode())) {
                    body.add(0, leftNode());
                } else {
                    return false;
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
        int fruitCol = fruit.getCol();
        int fruitRow = fruit.getRow();
        return body.get(0).getCol() == fruitCol && body.get(0).getRow() == fruitRow;
    }

    public void paint(Graphics2D g, int squareWidth, int squareHeight, int widthMargin, int heightMargin) {
        for (int i = 1; i < body.size(); i++) {
            Board.drawSquare(g, squareWidth, squareHeight, body.get(i).getRow(), body.get(i).getCol(), Color.lightGray, widthMargin, heightMargin);
        }
        Board.drawSquare(g, squareWidth, squareHeight, body.get(0).getRow(), body.get(0).getCol(), Color.darkGray, widthMargin, heightMargin);
    }
}
