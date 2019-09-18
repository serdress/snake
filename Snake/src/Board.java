/****/
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
    private JDialog jDialogGameOver;
    private JDialog jDialogIntro;
    private boolean isSnakeMoved;
    private Date date;
    private Date fruitDate;
    private boolean intro;
    private boolean versus;
    private Snake snake2;
    private boolean follow;

    public Board() {
        super();
        follow = false;
        intro = true;
        fruitIsDrawed = false;
        deltaTime = 150;
        keyAdepter = new MyKeyAdapter();
        setFocusable(true);

        timer = new Timer(deltaTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mainLoop();
            }
        });

        timerIntro = new Timer(deltaTime, new ActionListener() {
            int counter = 0;

            public void actionPerformed(ActionEvent evt) {
                snake.body.add(0, snake.rightNode());
                snake.body.remove(snake.body.size() - 1);
                repaint();
                counter++;
                if (counter == Config.numCols / 2) {
                    timerIntro.stop();
                    showIntroDialog();
                    intro = false;
                    addKeyListener(keyAdepter);

                }
            }
        });

        initIntro();

    }

    public void initIntro() {
        date = new Date();
        fruitDate = new Date();
        snake = new Snake(3, 0);
        fruit = new Fruit(false, snake, true);
        snake.direction = snake.direction.RIGHT;
        snake.oldDirection = snake.direction.RIGHT;
        timerIntro.start();

    }

    private boolean isGamePlaying() {
        return timer.isRunning();
    }

    public void initGame() {

        date = new Date();
        fruitDate = new Date();
        fruit = new Fruit(false, snake, false);
        snake = new Snake(3, Config.numCols / 2);
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
        if (follow) {
            followFruit();
        } else {
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
        }

        isSnakeMoved = false;
        if (snake.avanceSnakeAndEats(fruit)) {
            moveFruit();
        }
        repaint();
    }

    public void followLoop() {
        followFruit();
        //  System.out.println("Fruit position: Col=" + fruit.getCol() + "Row=" + fruit.getRow());
        //  System.out.println("Snake position: Col=" + snake.getHead().getCol() + "Row=" + snake.getHead().getRow());
        if (snake.avanceSnakeAndEats(fruit)) {
            moveFruit();
        }
        repaint();
    }

    public void setDialogs(JDialog jDialogGameOver, JDialog jDialogIntro) {
        this.jDialogGameOver = jDialogGameOver;
        this.jDialogIntro = jDialogIntro;
    }

    public void situeSnake() {
        snake = new Snake(3, Config.numCols / 2);
        repaint();
    }

    public void hideFruit() {
        fruit = new Fruit(false, snake, true);
    }

    public void showGameOverDialog() {
        jDialogGameOver.pack();
        jDialogGameOver.setVisible(true);
    }

    public void showIntroDialog() {
        jDialogIntro.pack();
        jDialogIntro.setVisible(true);
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
        for (int row = 0; row < Config.numRows; row++) {
            for (int col = 0; col < Config.numCols; col++) {
                drawSquare(g2d, getSquareWidth(), getSquareHeight(), row, col, Color.pink, getWidthMargin(), getHeightMargin());
            }
        }
        // Pintamos los elementos del tablero

        snake.paint(g2d, getSquareWidth(), getSquareHeight(), getWidthMargin(), getHeightMargin());
        fruit.paint(g2d, getSquareWidth(), getSquareHeight(), getWidthMargin(), getHeightMargin());

    }

    public void moveFruit() {
        fruitDate = new Date();
        System.out.println("Fruit" + fruitDate.getSeconds());
        fruit = new Fruit(false, snake, false);
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
        return getWidth() / Config.numCols;
    }

    public int getSquareHeight() {
        return getHeight() / Config.numRows;
    }

    class MyKeyAdapter extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (intro) {
                return;
            }
            if (!isGamePlaying()) {
                initGame();
            }
            if (!isSnakeMoved) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        snake.oldDirection = snake.direction;
                        // Si no iba a la izquierda, ve a la derecha. Lo mismo con las otras direcciones 
                        if (snake.oldDirection != snake.direction.RIGHT) {
                            isSnakeMoved = true;
                            snake.direction = snake.direction.LEFT;
                            repaint();
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        snake.oldDirection = snake.direction;
                        if (snake.oldDirection != snake.direction.LEFT) {
                            isSnakeMoved = true;
                            snake.direction = snake.direction.RIGHT;
                            repaint();
                        }
                        break;
                    case KeyEvent.VK_UP:
                        snake.oldDirection = snake.direction;
                        if (snake.oldDirection != snake.direction.DOWN) {
                            isSnakeMoved = true;
                            snake.direction = snake.direction.UP;
                            repaint();
                        }
                        break;
                    case KeyEvent.VK_DOWN:
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
                        break;
                    case KeyEvent.VK_F:
                        // Enable/Disable follow mode
                        follow = !follow;
                }
            }
        }
    }

    public void followFruit() {
        int fruitCol = fruit.getCol();
        int fruitRow = fruit.getRow();
        
        if (snake.getHead().getCol() > fruitCol) {
            if (snake.canMove(snake.leftNode())) {
                snake.direction = snake.direction.LEFT;
            } else {
                if (snake.canMove(snake.upNode())) {
                    snake.direction = snake.direction.UP;
                } else {
                    if (snake.canMove(snake.downNode())) {
                        snake.direction = snake.direction.DOWN;
                    } else {
                        if (snake.canMove(snake.rightNode())) {
                            snake.direction = snake.direction.RIGHT;
                        }
                    }
                }
            }
        }
        if (snake.getHead().getCol() < fruitCol) {
            if (snake.canMove(snake.rightNode())) {
                snake.direction = snake.direction.RIGHT;
            } else {
                if (snake.canMove(snake.downNode())) {
                    snake.direction = snake.direction.DOWN;
                } else {
                    if (snake.canMove(snake.upNode())) {
                        snake.direction = snake.direction.UP;
                    } else {
                        if (snake.canMove(snake.leftNode())) {
                            snake.direction = snake.direction.LEFT;
                        }
                    }
                }
            }
        }
        if (snake.getHead().getCol() == fruitCol) {
            if (snake.getHead().getRow() > fruitRow) {
                if (snake.canMove(snake.upNode())) {
                    snake.direction = snake.direction.UP;
                } else {
                    if (snake.canMove(snake.leftNode())) {
                        snake.direction = snake.direction.LEFT;
                    } else {
                        if (snake.canMove(snake.rightNode())) {
                            snake.direction = snake.direction.RIGHT;
                        } else {
                            if (snake.canMove(snake.downNode())) {
                                snake.direction = snake.direction.DOWN;
                            }
                        }
                    }
                }
            } else {
                if (snake.canMove(snake.downNode())) {
                    snake.direction = snake.direction.DOWN;
                } else {
                    if (snake.canMove(snake.rightNode())) {
                        snake.direction = snake.direction.RIGHT;
                    } else {
                        if (snake.canMove(snake.leftNode())) {
                            snake.direction = snake.direction.LEFT;
                        } else {
                            if (snake.canMove(snake.upNode())) {
                                snake.direction = snake.direction.UP;
                            }
                        }
                    }
                }
            }
        }
    }

    public int getWidthMargin() {
        return (getWidth() - Config.numCols * getSquareWidth()) / 2;
    }

    public int getHeightMargin() {
        return (getHeight() - Config.numRows * getSquareHeight()) / 2;
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
