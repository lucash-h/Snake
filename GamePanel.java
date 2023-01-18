import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

public class GamePanel extends JPanel implements ActionListener { 

    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int unit_size = 25;
    static final int game_units = (screen_width*screen_height)/unit_size;
    static final int delay = 75;
    final int x[] = new int[game_units];
    final int y[] = new int[game_units];
    int body_parts = 6;
    int foodeaten = 0;
    int x_coordinate_food;
    int y_coordinate_food;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;



    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width,screen_height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }
    public void startGame() {
        newfood();
        running = true;
        timer = new Timer(delay, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
        //create easy to visualize matrix
            for(int i = 0; i < screen_height/unit_size; i++) {
                g.drawLine(i*unit_size,0,i * unit_size,screen_height);
                g.drawLine(0, i*unit_size, screen_width, i*unit_size);
            }
            //food creation
            g.setColor(Color.CYAN);
            g.fillOval(x_coordinate_food, y_coordinate_food, unit_size, unit_size);

            //head of snake
            for (int i = 0; i < body_parts; i++) {
                if(i == 0) { //head of snake
                    g.setColor(Color.MAGENTA);
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                } else {
                    g.setColor(Color.green);
                    g.setColor(new Color(random.nextInt(255)));
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
            }

        g.setColor(Color.red); // scoreboard
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " +foodeaten, (screen_width - metrics.stringWidth("Score: " +foodeaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }
    public void newfood() {
        x_coordinate_food = random.nextInt((int)(screen_width/unit_size))*unit_size;
        y_coordinate_food = random.nextInt((int)(screen_height/unit_size))*unit_size;
    }

    public void move() {
        for(int i = body_parts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - unit_size;
                break;
            case 'D':
                y[0] = y[0] + unit_size;
                break;
            case 'L':
                x[0] = x[0] - unit_size;
                break;
            case 'R':
                x[0] = x[0] + unit_size;
                break;
        }

    }

    public void checkfood() { // keeps track of body part to food and creates new food
        if((x[0] == x_coordinate_food) && (y[0] == y_coordinate_food)) {
            body_parts++;
            foodeaten++;
            newfood();

        }

    }

    public void checkCollisions() {
        for(int i = body_parts; i > 0; i--) {//checks if head has a collision with the body
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if(x[0] < 0) { // makes sure dont leave left border
            running = false;
        }

        if(x[0] > screen_width) { // makes sure dont leave right border
            running = false;
        }

        if(y[0] < 0) { // makes sure dont leave the top
            running = false;
        }

        if(y[0] > screen_height) { // makes sure dont leave the bottom
            running = false;
        }
    }

    public void gameOver(Graphics g) {
        
        g.setColor(Color.red); // scoreboard
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " +foodeaten, (screen_width - metrics1.stringWidth("Score: " +foodeaten)) / 2, g.getFont().getSize());

        
        g.setColor(Color.red); // game over message
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screen_width - metrics2.stringWidth("Game Over")) / 2, screen_height/2);


    }

    public class MyKeyAdapter extends KeyAdapter{
        // at override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) { // lets snake turn only 90 degrees

                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                break;
                
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                break;

                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                break;
                    
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                break;
            }

        }
    }
    // at override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkfood();
            checkCollisions();
        }
        repaint();
    }
    
}
