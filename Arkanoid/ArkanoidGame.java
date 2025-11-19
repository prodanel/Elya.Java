package Arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class BrickDestroyedEvent extends EventObject {
    private final Brick brick;
    private final int score;

    public BrickDestroyedEvent(Object source, Brick brick, int score) {
        super(source);
        this.brick = brick;
        this.score = score;
    }

    public Brick getBrick() { return brick; }
    public int getScore() { return score; }
}

interface GameEventListener extends EventListener {
    void onBrickDestroyed(BrickDestroyedEvent event);
    void onGameOver();
    void onLevelCompleted();
}

class Brick {
    private int x, y;
    private int width, height;
    private Color color;
    private boolean destroyed;
    private int points;

    public Brick(int x, int y, int width, int height, Color color, int points) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.points = points;
        this.destroyed = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2d) {
        if (!destroyed) {
            g2d.setColor(color);
            g2d.fillRect(x, y, width, height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);
        }
    }

    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
    public int getPoints() { return points; }
    public int getX() { return x; }
    public int getY() { return y; }
}

class Ball {
    private int x, y;
    private int diameter;
    private int speedX, speedY;
    private Color color;

    public Ball(int x, int y, int diameter, Color color) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = color;
        this.speedX = 2;
        this.speedY = -2;
    }

    public void move() {
        x += speedX;
        y += speedY;
    }

    public void reverseX() { speedX = -speedX; }
    public void reverseY() { speedY = -speedY; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval(x, y, diameter, diameter);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x, y, diameter, diameter);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDiameter() { return diameter; }
    public int getSpeedX() { return speedX; }
    public int getSpeedY() { return speedY; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setSpeedX(int speedX) { this.speedX = speedX; }
    public void setSpeedY(int speedY) { this.speedY = speedY; }
}

class Paddle {
    private int x, y;
    private int width, height;
    private Color color;
    private int speed;

    public Paddle(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.speed = 10;
    }

    public void moveLeft() {
        x = Math.max(0, x - speed);
    }

    public void moveRight(int panelWidth) {
        x = Math.min(panelWidth - width, x + speed);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public void setX(int x) { this.x = x; }
}
class GameEngine {
    private Thread gameThread;
    private ArrayList<Brick> bricks;
    private Ball ball;
    private Paddle paddle;
    private List<GameEventListener> listeners;
    private int score;
    private int lives;
    private volatile boolean gameRunning;
    private int level;
    private final Object lock = new Object();

    public GameEngine() {
        this.listeners = new ArrayList<>();
        this.bricks = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.gameRunning = false;
        this.level = 1;

        initializeGame();
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            final double NS_PER_UPDATE = 1000000000.0 / 60.0; // 60 updates per second
            double delta = 0;

            while (true) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / NS_PER_UPDATE;
                lastTime = currentTime;

                while (delta >= 1) {
                    synchronized(lock) {
                        if (gameRunning) {
                            updateGame();
                        }
                    }
                    delta--;
                }

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void initializeGame() {
        paddle = new Paddle(250, 450, 100, 15, Color.BLUE);
        ball = new Ball(290, 430, 15, Color.RED);
        createBricks();
    }

    private void createBricks() {
        bricks.clear();
        int brickWidth = 50;
        int brickHeight = 20;
        int startX = 50;
        int startY = 50;

        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};
        int[] points = {50, 40, 30, 20, 10};

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                int x = startX + col * (brickWidth + 10);
                int y = startY + row * (brickHeight + 5);
                bricks.add(new Brick(x, y, brickWidth, brickHeight, colors[row], points[row]));
            }
        }
    }

    private void updateGame() {

        ball.move();

        checkWallCollisions();

        checkPaddleCollision();

        checkBrickCollisions();

        checkGameOver();

        checkLevelCompletion();
    }

    private void checkWallCollisions() {
        if (ball.getX() <= 0 || ball.getX() >= 585) {
            ball.reverseX();
        }
        if (ball.getY() <= 0) {
            ball.reverseY();
        }
    }

    private void checkPaddleCollision() {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
        }
    }

    private void checkBrickCollisions() {
        Iterator<Brick> iterator = bricks.iterator();
        while (iterator.hasNext()) {
            Brick brick = iterator.next();
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                brick.setDestroyed(true);
                ball.reverseY();

                score += brick.getPoints();
                fireBrickDestroyedEvent(brick, brick.getPoints());

                break;
            }
        }
    }

    private void checkGameOver() {
        if (ball.getY() > 500) {
            lives--;
            if (lives <= 0) {
                gameRunning = false;
                fireGameOverEvent();
            } else {
                resetBall();
            }
        }
    }

    private void resetBall() {
        ball.setX(290);
        ball.setY(430);
        ball.setSpeedX(2);
        ball.setSpeedY(-2);
    }

    private void checkLevelCompletion() {
        boolean allBricksDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                allBricksDestroyed = false;
                break;
            }
        }

        if (allBricksDestroyed) {
            level++;
            fireLevelCompletedEvent();
            initializeGame();
        }
    }

    public void startGame() {
        synchronized(lock) {
            gameRunning = true;
        }
    }

    public void pauseGame() {
        synchronized(lock) {
            gameRunning = false;
        }
    }

    public void resumeGame() {
        synchronized(lock) {
            gameRunning = true;
        }
    }

    public void resetGame() {
        synchronized(lock) {
            score = 0;
            lives = 3;
            level = 1;
            initializeGame();
        }
    }

    public void movePaddleLeft(int panelWidth) {
        synchronized(lock) {
            paddle.moveLeft();
        }
    }

    public void movePaddleRight(int panelWidth) {
        synchronized(lock) {
            paddle.moveRight(panelWidth);
        }
    }

    public void addGameEventListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void removeGameEventListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    private void fireBrickDestroyedEvent(Brick brick, int points) {
        BrickDestroyedEvent event = new BrickDestroyedEvent(this, brick, points);
        for (GameEventListener listener : listeners) {
            listener.onBrickDestroyed(event);
        }
    }

    private void fireGameOverEvent() {
        for (GameEventListener listener : listeners) {
            listener.onGameOver();
        }
    }

    private void fireLevelCompletedEvent() {
        for (GameEventListener listener : listeners) {
            listener.onLevelCompleted();
        }
    }

    public ArrayList<Brick> getBricks() {
        synchronized(lock) { return new ArrayList<>(bricks); }
    }

    public Ball getBall() {
        synchronized(lock) { return ball; }
    }

    public Paddle getPaddle() {
        synchronized(lock) { return paddle; }
    }

    public int getScore() {
        synchronized(lock) { return score; }
    }

    public int getLives() {
        synchronized(lock) { return lives; }
    }

    public int getLevel() {
        synchronized(lock) { return level; }
    }

    public boolean isGameRunning() {
        synchronized(lock) { return gameRunning; }
    }
}

class GamePanel extends JPanel implements GameEventListener {
    private GameEngine gameEngine;
    private javax.swing.Timer repaintTimer;

    public GamePanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        if (gameEngine != null) {
            this.gameEngine.addGameEventListener(this);
        }
        setPreferredSize(new Dimension(600, 500));
        setBackground(Color.WHITE);setFocusable(true);
        setDoubleBuffered(true);

        repaintTimer = new javax.swing.Timer(8, e -> repaint());
        repaintTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameEngine != null) {
            ArrayList<Brick> bricks = gameEngine.getBricks();
            Ball ball = gameEngine.getBall();
            Paddle paddle = gameEngine.getPaddle();

            for (Brick brick : bricks) {
                brick.draw(g2d);
            }

            paddle.draw(g2d);
            ball.draw(g2d);

            drawGameInfo(g2d);
        }
    }

    private void drawGameInfo(Graphics2D g2d) {
        if (gameEngine == null) return;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Счет: " + gameEngine.getScore(), 10, 470);
        g2d.drawString("Жизни: " + gameEngine.getLives(), 10, 490);
        g2d.drawString("Уровень: " + gameEngine.getLevel(), 500, 470);

        if (!gameEngine.isGameRunning()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("ПАУЗА", 250, 250);
        }
    }

    @Override
    public void onBrickDestroyed(BrickDestroyedEvent event) {
        // Автоматически перерисовывается таймером
    }

    @Override
    public void onGameOver() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Игра окончена! Ваш счет: " + gameEngine.getScore(),
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);
            gameEngine.resetGame();
        });
    }

    @Override
    public void onLevelCompleted() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Уровень " + (gameEngine.getLevel() - 1) + " пройден!",
                    "Level Completed", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        if (gameEngine != null) {
            this.gameEngine.addGameEventListener(this);
        }
    }
}

public class ArkanoidGame extends JFrame {
    private GameEngine gameEngine;
    private GamePanel gamePanel;

    public ArkanoidGame() {
        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setTitle("Арканоид");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameEngine = new GameEngine();

        gamePanel = new GamePanel(gameEngine);

        JPanel controlPanel = createControlPanel();

        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);JButton startButton = new JButton("Старт");
        JButton pauseButton = new JButton("Пауза");
        JButton resetButton = new JButton("Сброс");
        JButton exitButton = new JButton("Выход");

        JLabel controlLabel = new JLabel("Управление: ← → стрелки, пробел - пауза");

        panel.add(startButton);
        panel.add(pauseButton);
        panel.add(resetButton);
        panel.add(controlLabel);
        panel.add(exitButton);

        startButton.addActionListener(e -> {
            gameEngine.startGame();
            gamePanel.requestFocus();
        });

        pauseButton.addActionListener(e -> {
            if (gameEngine.isGameRunning()) {
                gameEngine.pauseGame();
            } else {
                gameEngine.resumeGame();
            }
            gamePanel.requestFocus();
        });

        resetButton.addActionListener(e -> {
            gameEngine.resetGame();
            gamePanel.repaint();
            gamePanel.requestFocus();
        });

        exitButton.addActionListener(e -> System.exit(0));

        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Игра");
        JMenuItem newGameItem = new JMenuItem("Новая игра");
        JMenuItem pauseItem = new JMenuItem("Пауза");
        JMenuItem exitItem = new JMenuItem("Выход");

        JMenu helpMenu = new JMenu("Справка");
        JMenuItem aboutItem = new JMenuItem("Об игре");

        gameMenu.add(newGameItem);
        gameMenu.add(pauseItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        helpMenu.add(aboutItem);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        newGameItem.addActionListener(e -> {
            gameEngine.resetGame();
            gamePanel.repaint();
            gamePanel.requestFocus();
        });

        pauseItem.addActionListener(e -> {
            if (gameEngine.isGameRunning()) {
                gameEngine.pauseGame();
            } else {
                gameEngine.resumeGame();
            }
            gamePanel.requestFocus();
        });

        exitItem.addActionListener(e -> System.exit(0));

        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Арканоид\n\nУправление:\n← → - движение платформы\nПробел - пауза\n\nЦель: разбить все кирпичи!",
                    "Об игре", JOptionPane.INFORMATION_MESSAGE);
        });

        return menuBar;
    }

    private void setupEventHandlers() {
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        gameEngine.movePaddleLeft(gamePanel.getWidth());
                        break;
                    case KeyEvent.VK_RIGHT:
                        gameEngine.movePaddleRight(gamePanel.getWidth());
                        break;
                    case KeyEvent.VK_SPACE:
                        if (gameEngine.isGameRunning()) {
                            gameEngine.pauseGame();
                        } else {
                            gameEngine.resumeGame();
                        }
                        break;
                }
            }
        });

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gamePanel.requestFocus();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ArkanoidGame().setVisible(true);
        });
    }
}