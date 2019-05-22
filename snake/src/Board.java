
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.Timer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * @author alu23847452v
 */

public class Board extends JPanel {

    private Snake snake;
    private Timer timer;
    private Timer timerIntro;
    private int deltaTime;
    private Graphics2D g2d;
    private boolean gameOver;
    private Fruit fruit;
    private MyKeyAdapter keyAdepter;
    public boolean fruitIsDrawed;
    private JDialog jDialog;
    private boolean isSnakeMoved;
    private Date date;
    private Date fruitDate;
    private boolean intro;

    public Board() {
        super();
        fruitIsDrawed = false;
        deltaTime = 30;
        keyAdepter = new MyKeyAdapter();
        addKeyListener(keyAdepter);
        setFocusable(true);

        timer = new Timer(deltaTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mainLoop();
            }
        });

        timerIntro = new Timer(deltaTime, new ActionListener() {
            int counter = 0;

            public void actionPerformed(ActionEvent evt) {
                snake.body.add(0,snake.rightNode());
                snake.body.remove(snake.body.size() - 1);
                repaint();
                counter++;
                if (counter == Config.getIstance().numCols / 2) {
                    timerIntro.stop();
                    showGameOverDialog();
                    //initGame();
                }
            }
        });

        initIntro();

    }

    public void initIntro() {
        date = new Date();
        fruitDate = new Date();
        fruit = new Fruit(false, snake);
        snake = new Snake(3, 0);
        snake.direction = snake.direction.RIGHT;
        snake.oldDirection = snake.direction.RIGHT;
        timerIntro.start();

    }

    public void initGame() {

        date = new Date();
        fruitDate = new Date();
        fruit = new Fruit(false, snake);
        snake = new Snake(3, Config.getIstance().numCols / 2);
        snake.direction = snake.direction.RIGHT;
        snake.oldDirection = snake.direction.RIGHT;
        timer.start();
    }

    //Bucle de refresco
    public void mainLoop() {
        date = new Date();
     //   System.out.println("Date "+date.getSeconds());
        //   System.out.println("Fruit date "+fruitDate.getSeconds());
        if (date.getSeconds() > fruitDate.getSeconds() + 3) {
            moveFruit();
        }

        switch (snake.direction) {
            case UP:
                if (!snake.canMove(snake.upNode())) {
                    timer.stop();
                    showGameOverDialog();
                }
                break;
            case DOWN:
                if (!snake.canMove(snake.downNode())) {
                    timer.stop();
                    showGameOverDialog();
                }
                break;
            case RIGHT:
                if (!snake.canMove(snake.rightNode())) {
                    timer.stop();
                    showGameOverDialog();
                }
                break;
            case LEFT:
                if (!snake.canMove(snake.leftNode())) {
                    timer.stop();
                    showGameOverDialog();
                }
                break;
        }

        isSnakeMoved = false;
        if (snake.avanceSnakeAndEats(fruit)) {
            moveFruit();
        }
        repaint();
    }

    public void setDialog(JDialog jDialog) {
        this.jDialog = jDialog;
    }
    
    public boolean gameIsRunning() {
        return timer.isRunning();
    }
    public void showGameOverDialog() {
        jDialog.pack();
        jDialog.setVisible(true);
    }
    /* Método de la clase extendida, al Overridearlo,
     se llama al constructor pasándole los gráficos de nestro board.
     Este método se ejecutará por cada repaint()*/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Convierte los gráficos en gráficos 2D para poder dibujar
        Graphics2D g2d = (Graphics2D) g;
        //Va pintando en cada fila y columna el cuadrado 
        for (int row = 0; row < Config.getIstance().numRows; row++) {
            for (int col = 0; col < Config.getIstance().numCols; col++) {
                drawSquare(g2d, getSquareWidth(), getSquareHeight(), row, col, Color.pink, getWidthMargin(), getHeightMargin());
            }
        }
        // Pintamos los elementos del tablero
        fruit.paint(g2d, getSquareWidth(), getSquareHeight(), getWidthMargin(), getHeightMargin());
        snake.paint(g2d, getSquareWidth(), getSquareHeight(), getWidthMargin(), getHeightMargin());

    }

    public boolean gameOver() {
        return gameOver;
    }

    public void moveFruit() {
        fruitDate = new Date();
        System.out.println("Fruit" + fruitDate.getSeconds());
        fruit = new Fruit(false, snake);
    }

    // Hack
    public void moveFruitInFrontOfSnake() {
        switch (snake.direction) {
            case UP:
                fruit = new Fruit(snake.getHead().getCol(), snake.getHead().getRow() - 1);
                break;
            case DOWN:
                fruit = new Fruit(snake.getHead().getCol(), snake.getHead().getRow() + 1);
                break;
            case RIGHT:
                fruit = new Fruit(snake.getHead().getCol() + 1, snake.getHead().getRow());
                break;
            case LEFT:
                fruit = new Fruit(snake.getHead().getCol() - 1, snake.getHead().getRow());
                break;
        }
    }

    public int getSquareWidth() {
        return getWidth() / Config.getIstance().numCols;
    }

    public int getSquareHeight() {
        return getHeight() / Config.getIstance().numRows;
    }

    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (!isSnakeMoved) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!gameIsRunning()) initGame();
                        snake.oldDirection = snake.direction;
                        // Si no iba a la izquierda, ve a la derecha. Lo mismo con las otras direcciones 
                        if (snake.oldDirection != snake.direction.RIGHT) {
                            isSnakeMoved = true;
                            snake.direction = snake.direction.LEFT;
                            repaint();
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                         if (!gameIsRunning()) initGame();
                        snake.oldDirection = snake.direction;
                        if (snake.oldDirection != snake.direction.LEFT) {
                            isSnakeMoved = true;
                            snake.direction = snake.direction.RIGHT;
                            repaint();

                        }
                        break;
                    case KeyEvent.VK_UP:
                         if (!gameIsRunning()) initGame();
                        snake.oldDirection = snake.direction;
                        if (snake.oldDirection != snake.direction.DOWN) {
                            isSnakeMoved = true;
                            snake.direction = snake.direction.UP;
                            repaint();

                        }
                        break;
                    case KeyEvent.VK_DOWN:
                         if (!gameIsRunning()) initGame();
                        snake.oldDirection = snake.direction;
                        if (snake.oldDirection != snake.direction.UP) {
                            isSnakeMoved = true;
                            snake.direction = snake.direction.DOWN;
                            repaint();

                        }
                        break;
                    case KeyEvent.VK_Q:
                        // Hack
                        moveFruitInFrontOfSnake();
                }
            }
        }
    }

    public int getWidthMargin() {
        return (getWidth() - Config.getIstance().numCols * getSquareWidth()) / 2;
    }

    public int getHeightMargin() {
        return (getHeight() - Config.getIstance().numRows * getSquareHeight()) / 2;
    }

    public static void drawSquare(Graphics2D g, int squareWidth, int squareHeight, int row, int col, Color color, int widthMargin, int heightMargin) {
        int x = col * squareWidth + widthMargin;
        int y = row * squareHeight + heightMargin;
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth - 2, squareHeight - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight - 1, x, y);
        g.drawLine(x, y, x + squareWidth - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight - 1, x + squareWidth - 1, y + squareHeight - 1);
        g.drawLine(x + squareWidth - 1, y + squareHeight - 1, x + squareWidth - 1, y + 1);
    }
}
