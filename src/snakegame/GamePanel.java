package snakegame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
	
	private BufferedImage snakeImage;
	private BufferedImage appleImage;
	private BufferedImage backgroundImage;
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
		try {
            snakeImage = ImageIO.read(new File("C:\\Users\\linde\\OneDrive\\Imagens\\TV-Snake-Eyes-icon.png"));
            appleImage = ImageIO.read(new File("C:\\Users\\linde\\OneDrive\\Imagens\\storm-shadow.jpg"));
            backgroundImage = ImageIO.read(new File("C:\\Users\\linde\\OneDrive\\Imagens\\gi-joe-simbolo.jpg"));
            
        } catch (IOException e) {
            e.printStackTrace();
            
        }
		
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		draw(g);
		
	}
	public void draw(Graphics g) {
		
		 if(running) { 
			 /*
		 for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
	            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
	            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
	        }
	        */
	        g.drawImage(appleImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, this);

	        for (int i = 0; i < bodyParts; i++) {
	            if (i == 0) {
	                g.drawImage(snakeImage, x[i], y[i], UNIT_SIZE, UNIT_SIZE, this);
	            } else {
	                g.setColor(Color.GRAY);
	                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
	            }
	        }
	        g.setColor(Color.red);
			g.setFont( new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Pontos: " +applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Pontos: " +applesEaten))/2, g.getFont().getSize());
		 }
		 else {
			 gameOver(g);
		 }
	    }
	public void newApple(){
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i = bodyParts - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
		}
		
		switch(direction) {
		case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
        case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
        case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
        case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
	}
	public void checkCollisions() {
		//caso a cabeça bata no próprio corpo
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		
		//caso a cabeça bata n borda esquerda
		if(x[0] < 0) {
			running = false;
		}
		//caso a cabeça bata na borda direita
		if(x[0] >= SCREEN_WIDTH) {
            running = false;
		}
		//caso a cabeça bata na borda de cima
		if(y[0] < 0) {
			running = false;
		}
		//caso a cabeça bata na borda de baixo
		if(y[0] >= SCREEN_HEIGHT) {
            running = false;
		}
		if(!running) {
            timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//texto de pontos
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Pontos: " +applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Pontos: " +applesEaten))/2, g.getFont().getSize());
		//texto de fim do jogo
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 50));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Fim de Jogo meu Parceiro", (SCREEN_WIDTH - metrics2.stringWidth("Fim de Jogo meu Parceiro"))/2, SCREEN_HEIGHT/2);
		
	}
    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	if(running) {
    		move();
    		checkApple();
    		checkCollisions();
    	}
    	repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter{
    	@Override
    	public void keyPressed(KeyEvent e) {
    		switch(e.getKeyCode()) {
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

}
