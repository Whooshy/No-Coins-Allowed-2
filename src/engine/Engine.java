package engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import object.Player;
import utils.Dialouge;
import utils.Images;

public class Engine extends Canvas implements Runnable, KeyListener, MouseListener
{
	private Thread thread;
	private boolean isRunning = false;
	
	public static boolean LMB = false;
	
	public int frameCount, fps;
	
	private JFrame frame;
	
	public static int width, height, mX, mY;
	public static int STATE = 1;
	
	public Images images;
	public World world;
	public Player player;
	
	public Dialouge menu;
	
	public Dialouge play;
	public Dialouge help;
	public Dialouge quit;
	public Dialouge dead;
	public Dialouge won;
	
	public Dialouge helpScreen;
	public Dialouge helpScreen2;
	
	public Engine()
	{
		frame = new JFrame("LD44");
		
		this.setPreferredSize(new Dimension(800, 600));
		
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		frame.add(this, BorderLayout.CENTER);
		this.addKeyListener(this);
		this.addMouseListener(this);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		images = new Images();
		player = new Player(0, 0);
		world = new World(player);
		
		menu = new Dialouge("No Coins Allowed 2");
		
		play = new Dialouge("Play");
		help = new Dialouge("Help");
		quit = new Dialouge("Quit");
		dead = new Dialouge("Game Over");
		won = new Dialouge("You Won!");
		
		helpScreen = new Dialouge("The goal is simple. Don't collect the coins. Coin collecting increases the speed of the timer, and if that timer counts down to 0, you're a goner.");
		helpScreen2 = new Dialouge("Press ESC to exit this screen.");
		
		start();
	}
	
	public synchronized void start()
	{
		isRunning = true;
		
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		try
		{
			thread.join();
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		new Engine();
	}
	
	public void run()
	{
		long timer = System.currentTimeMillis();
		while(isRunning)
		{
			frameCount++;
			
			try 
			{
				thread.sleep(5);
			} catch(InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			update();
			
			if(System.currentTimeMillis() - timer >= 1000)
			{
				fps = frameCount;
				
				System.out.println("FPS: " + fps);
				frameCount = 0;
				timer += 1000;
			}
		}
		stop();
	}
	
	public void update()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		width = frame.getWidth();
		height = frame.getHeight();
		
		Graphics graphics = bs.getDrawGraphics();
		Graphics2D g = (Graphics2D) graphics;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
				
		if(STATE == 0)
		{
			g.translate(-player.getCamX(width), -player.getCamY(height));
			world.update(g, player, width, height);
			player.update(g);
			g.translate(player.getCamX(width), player.getCamY(height));
		}
		if(STATE == 1)
		{
			menu.update(g, width / 2 - 150, height / 2 - 200);
			
			play.update(g, width / 2 - 75, height / 2 + 100);
			help.update(g, width / 2 - 75, height / 2 + 125);
			quit.update(g, width / 2 - 75, height / 2 + 150);
			
			if(Engine.LMB && mY > height / 2 + 100 && mY < height / 2 + 120)
			{
				STATE = 0;
				world = new World(player);
				world.level = 1;
				world.generate(player, world.level);
			}
			if(Engine.LMB && mY > height / 2 + 125 && mY < height / 2 + 145)
				STATE = 2;
			if(Engine.LMB && mY > height / 2 + 150 && mY < height / 2 + 170)
				System.exit(0);
		}
		if(STATE == 2)
		{
			helpScreen.update(g, 20, 20);
			helpScreen2.update(g, 20, 200);
		}
		if(STATE == 3)
		{
			dead.update(g, width / 2 - 90, height / 2 - 50);
		}
		if(STATE == 4)
		{
			won.update(g, width / 2 - 60, height / 2 - 50);
		}
		
		graphics.dispose();
		bs.show();
	}

	public void keyPressed(KeyEvent e) 
	{
		int k = e.getKeyCode();
		
		if(k == e.VK_A)
			player.isMovingLeft = true;
		if(k == e.VK_D)
			player.isMovingRight = true;
		if(k == e.VK_SPACE && player.isCollidingBottom)
		{
			player.isJumping = true;
			player.isFalling = false;
		}
		if(k == e.VK_ESCAPE && STATE >= 2)
			STATE = 1;
	}

	public void keyReleased(KeyEvent e) 
	{
		int k = e.getKeyCode();
		
		if(k == e.VK_A)
			player.isMovingLeft = false;
		if(k == e.VK_D)
			player.isMovingRight = false;
	}

	public void keyTyped(KeyEvent e) 
	{
		
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e)
	{
		Engine.LMB = true;
		
		mX = e.getX();
		mY = e.getY();
	}

	public void mouseReleased(MouseEvent e)
	{
		Engine.LMB = false;
		
		mX = -1;
		mY = -1;
	}
}
