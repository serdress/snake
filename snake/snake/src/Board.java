
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.Timer;
/*
 * @author alu23847452v
 */

public class Board extends JPanel {

    private Snake snake;
    private Timer timer;
    private int deltaTime;
    private Graphics2D g2d;
    private boolean gameOver;
    private Fruit fruit;
    private MyKeyAdapter keyAdepter;
    public boolean fruitIsDrawed;
    private JDialog jDialog;
    private boolean isSnakeMoved;

    public Board() {
        //Llamas al constructor de JPanel para obtener sus métodos
        super();

        // Creamos los elementos y les damos sus atributos iniciales
        /* Booleano para editar la posición de la fruta
         sólo cuando la serpiente se lo haya comido*/
        fruitIsDrawed = false;
        //Tiempo de refresco de pantalla
        deltaTime = 100;
        // Lector de teclas
        keyAdepter = new MyKeyAdapter();
        addKeyListener(keyAdepter);
        setFocusable(true);

        timer = new Timer(deltaTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mainLoop();
            }
        });
        initGame();
    }

    public void initGame() {

        fruit = new Fruit(false, snake);
        snake = new Snake(3);
        snake.direction = snake.direction.RIGHT;
        snake.oldDirection = snake.direction.RIGHT;
        timer.start();
    }

    //Bucle de refresco
    public void mainLoop() {
        isSnakeMoved = false;
        if (snake.avanceSnakeAndEats(fruit)) {
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
        repaint();

    }

    public void setDialog(JDialog jDialog) {
        this.jDialog = jDialog;
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
        for (int row = 0; row < Config.numRows; row++) {
            for (int col = 0; col < Config.numCols; col++) {
                drawSquare(g2d, getSquareWidth(), getSquareHeight(), row, col, Color.black, getWidthMargin(), getHeightMargin());
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
        fruit = new Fruit(false, snake);
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
                        moveFruit();
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
