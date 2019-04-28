package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import object.Coin;
import object.Player;
import utils.Dialouge;
import utils.FontString;
import utils.Images;

public class World {
	
	public int[][] tiles = new int[150][100];
	public int[] tileCode = new int[16];
	
	public static boolean changingLevel = false;
	public static boolean dead, won;
	
	public int frame, count, coins, prevTime, count2, level, coinCounter;
	public float time;
	
	public Dialouge levelName;
	public FontString coinCount;
	public FontString timer;
	
	public Random random;
	
	public AudioInputStream ais;
	public Clip music;
	
	public ArrayList<Coin> coinsList = new ArrayList<Coin>();

	public World(Player player)
	{
		random = new Random();
		
		try 
		{
			ais = AudioSystem.getAudioInputStream(getClass().getResource("/audio/gamemusic.wav"));
		} catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		tileCode[0] = new Color(255, 255, 255).getRGB();
		tileCode[1] = new Color(255, 128, 128).getRGB();
		tileCode[2] = new Color(0, 0, 255).getRGB();
		tileCode[3] = new Color(0, 255, 0).getRGB();
		
		time = 60;
		level = 16;
		
		for(int x = 0; x < 150; x++)
		for(int y = 0; y < 100; y++)
		{
			tiles[x][y] = -1;
		}
		
		timer = new FontString();
		generate(player, level);
	}
	
	public void generate(Player player, int lvl)
	{
		BufferedImage level;
		
		if(lvl == 1)
		{
			try 
			{
				music = AudioSystem.getClip();
				music.open(ais);
				music.loop(Clip.LOOP_CONTINUOUSLY);
				music.start();
			} catch (LineUnavailableException e) 
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try 
		{
			level = ImageIO.read(getClass().getResource("/levels/lvl_" + lvl + ".png"));
			for(int x = 0; x < 150; x++)
			for(int y = 0; y < 100; y++)
			{
				if(level.getRGB(x, y) == Color.BLACK.getRGB())
					tiles[x][y] = -1;
				if(level.getRGB(x, y) == tileCode[0])
					tiles[x][y] = 0;
				if(level.getRGB(x, y) == tileCode[1])
					tiles[x][y] = 1;
				if(level.getRGB(x, y) == tileCode[2])
				{
					player.x = 32 * x;
					player.y = 32 * y;
					tiles[x][y] = -1;
				}
				if(level.getRGB(x, y) == tileCode[3])
					tiles[x][y] = 7;
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String coinsString = "Coins - " + coins;
		
		switch(lvl)
		{
		case 1:
			levelName = new Dialouge("Level 1 - The Crevice");
			break;
		case 2:
			levelName = new Dialouge("Level 2 - Serpentine");
			break;
		case 3:
			levelName = new Dialouge("Level 3 - Descent");
			break;
		case 4:
			levelName = new Dialouge("Level 4 - Circles of Hell");
			break;
		case 5:
			levelName = new Dialouge("Level 5 - Platformer's Delight");
			break;
		case 6:
			levelName = new Dialouge("Level 6 - Box Therapy");
			break;
		case 7:
			levelName = new Dialouge("Level 7 - Four Courners");
			break;
		case 8:
			levelName = new Dialouge("Level 8 - Hourglass");
			break;
		case 9:
			levelName = new Dialouge("Level 9 - Wrap Around");
			break;
		case 10:
			levelName = new Dialouge("Level 10 - The Grid");
			break;
		case 11:
			levelName = new Dialouge("Level 11 - Pathway");
			break;
		case 12:
			levelName = new Dialouge("Level 12 - Caverns");
			break;
		case 13:
			levelName = new Dialouge("Level 13 - Spelunking");
			break;
		case 14:
			levelName = new Dialouge("Level 14 - Pentahex");
			break;
		case 15:
			levelName = new Dialouge("Level 15 - Wave");
			break;
		case 16:
			levelName = new Dialouge("The Gauntlet");
			break;
		default:
			levelName = new Dialouge("Invalid!");
			break;
		}
		coinCount = new FontString();
		coinCount.create(coinsString);
	}
	
	public void update(Graphics2D g, Player player, float width, float height)
	{
		g.setColor(Color.BLACK);
		
		coinCounter--;
		
		if(level == 16 && coinCounter < 0)
		{
			coinsList.add(new Coin(random.nextInt(500) + (int) player.x, random.nextInt(500) + (int) player.y + 500, random.nextInt(5)));
			coinCounter = random.nextInt(200) + 100;
		}
		
		count++;
		time -= 0.01f * (float) (coins + 1.f) / 15;
		
		int curTime = (int) time;
		
		String timeString = "Time - " + (int) (time + 1);
		
		if(curTime != prevTime) timer.create(timeString);
		
		if((int) time < 0) dead = true;
		
		if(count > 15)
		{
			count = 0;
			frame++;
			
			if(frame > 5) frame = 1;
		}
		
		g.fillRect(-5000, -5000, 15000, 5000);
		g.fillRect(-5000, 3200, 15000, 5000);
		g.fillRect(-5000, 0, 5000, 3200);
		g.fillRect(4800, 0, 5000, 3200);
		
		for(int x = 0; x < 150; x++)
		for(int y = 0; y < 100; y++)
		{
			Rectangle bounds = new Rectangle(x * 32, y * 32, 32, 32);
			if(tiles[x][y] != -1)
			{
				if(x > (player.x / 32) - 40 && x < (player.x / 32) + 40 && y > (player.y / 32) - 40 && y < (player.y / 32) + 40)
				{
					if(tiles[x][y] != 1) g.drawImage(Images.tiles[tiles[x][y]], x * 32, y * 32, 32, 32, null);
					else g.drawImage(Images.tiles[tiles[x][y] + frame], x * 32, y * 32, 32, 32, null);
				}
				
			}
			
			if((bounds.intersects(player.bottom()) || bounds.intersects(player.bottom2())) && tiles[x][y] == 0)
				player.isCollidingBottom = true;
			else if((bounds.intersects(player.bottom()) && bounds.intersects(player.bottom2())) && tiles[x][y] != 0)
				player.isCollidingBottom = false;
			
			if(bounds.intersects(player.top()) && tiles[x][y] == 0)
				player.isCollidingTop = true;
			else if(bounds.intersects(player.top()) && tiles[x][y] != 0)
				player.isCollidingTop = false;
			
			if((bounds.intersects(player.left()) || bounds.intersects(player.left2())) && tiles[x][y] == 0)
				player.isCollidingLeft = true;
			else if((bounds.intersects(player.left()) && bounds.intersects(player.left2())) && tiles[x][y] != 0)
				player.isCollidingLeft = false;
			
			if((bounds.intersects(player.right()) || bounds.intersects(player.right2())) && tiles[x][y] == 0)
				player.isCollidingRight = true;
			else if((bounds.intersects(player.right()) && bounds.intersects(player.right2())) && tiles[x][y] != 0)
				player.isCollidingRight = false;
			
			if(bounds.intersects(player.hit()) && tiles[x][y] == 7)
			{
				changingLevel = true;
				System.out.println("YES");
			}
			
			if(bounds.intersects(player.hit()) && tiles[x][y] == 1)
			{
				coins++;
				String coinsString = "Coins - " + coins;
				coinCount.create(coinsString);
				tiles[x][y] = -1;
			}
		}
		
		levelName.update(g, (int) (player.x - width / 2) + 20, (int) (player.y - height / 2) + 65);
		coinCount.update(g, (int) (player.x + width / 2) - 124, (int) (player.y - height / 2) + 75);
		timer.update(g, (int) (player.x - width / 2) + 40, (int) (player.y + height / 2));
		
		/*
		for(Coin c : coinsList)
		{
			if(c.lifetime < 2000)
			{
				c.update(g);
				if(player.hit().intersects(c.bounds()))
				{
					coins++;
					coinsList.remove(c);
				}
			}
			else
			{
				coinsList.remove(c);
			}
		}
		*/
		
		if(changingLevel)
		{
			count2++;
			if((count2 > 10 && count2 < 20) || (count2 > 30 && count2 < 40))
			{
				g.setColor(new Color(109, 149, 90));
				g.fillRect((int) player.x - (int) (width / 2), (int) player.y - (int) (height / 2), (int) width + 100, (int) height + 100);
			}
			if(count2 >= 40)
			{
				level++;
				if(level == 17)
				{
					Engine.STATE = 4;
					count2 = 0; 
					time = 60;
					changingLevel = false;
				}
				else
				{
					generate(player, level);
					count2 = 0;
					time = 60; 
					changingLevel = false;
				}
			}
		}
		
		if(dead)
		{
			count2++;
			music.stop();
			if((count2 > 10 && count2 < 20) || (count2 > 30 && count2 < 40))
			{
				g.setColor(new Color(109, 149, 90));
				g.fillRect((int) player.x - (int) (width / 2), (int) player.y - (int) (height / 2), (int) width + 100, (int) height + 100);
			}
			if(count2 >= 40)
			{
				Engine.STATE = 3;
				time = 60;
				level = 1;
				coins = 0;
				dead = false;
			}
		}
		
		if(level == 17)
		{
			Engine.STATE = 4;
			time = 60;
			level = 1;
			coins = 0;
		}
		
		prevTime = curTime;
	}
}
