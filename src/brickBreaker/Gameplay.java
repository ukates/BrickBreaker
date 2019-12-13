package brickBreaker;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 2;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    public void paint(Graphics g) {
        //background
        g.setColor(Color.black);
        g.fillRect(1,1,692, 592);
        //draw blocks
        map.draw((Graphics2D)g);
        //borders
        g.setColor(Color.yellow);
        g.fillRect(0,0, 3, 592);
        g.fillRect(0,0, 692, 3);
        g.fillRect(686,0, 3, 592);
        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        //ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play) {
            Rectangle ball =new Rectangle(ballPosX, ballPosY, 20,
                20);
            Rectangle paddle = new Rectangle(playerX, 550,
                100, 8);
            if(ball.intersects(paddle)) {

                int paddleLPos = (int) paddle.getMinX();
                int ballLPos = (int) ball.getMinX();
                int first = paddleLPos + 49;
                int second = paddleLPos + 50;

                if(ballLPos < first) {
                    if(ballXdir > 0) {
                        ballXdir *= -1;
                    }
                    ballYdir = -ballYdir;
                }
                if(ballLPos > second) {
                    if(ballXdir < 0) {
                        ballXdir *= -1;
                    }
                    ballYdir = -ballYdir;
                }
            }

            A: for(int i = 0; i < map.map.length; i++) {
                for(int j = 0; j < map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY,
                            brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY,
                            20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPosX+=ballXdir;
            ballPosY+=ballYdir;
            if(ballPosX < 0) {
                ballXdir = -ballXdir;
            }
            if(ballPosY < 0) {
                ballYdir = -ballYdir;
            }
            if(ballPosX > 670) {
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
           if(playerX >= 600) {
               playerX = 600;
           } else {
               moveRight();
           }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
    }
    public void moveRight() {
        play = true;
        playerX += 20;
    }
    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}
