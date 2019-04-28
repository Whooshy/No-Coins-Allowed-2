package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images 
{
	public static BufferedImage font;
	
	public static BufferedImage tileset;
	public static BufferedImage player;
	public static BufferedImage bg;
	public static BufferedImage melee;
	
	public static BufferedImage[] playerFrames = new BufferedImage[10];
	public static BufferedImage[] tiles = new BufferedImage[16];
	
	public static BufferedImage[] melee_particles = new BufferedImage[6];
	
	public Images()
	{
		try 
		{
			font = ImageIO.read(getClass().getResource("/font/font.png"));
			tileset = ImageIO.read(getClass().getResource("/tiles/tileset_green.png"));
			player = ImageIO.read(getClass().getResource("/objects/player.png"));
			bg = ImageIO.read(getClass().getResource("/levels/sky.png"));
			melee = ImageIO.read(getClass().getResource("/particles/melee.png"));
			
			for(int i = 0; i < 16; i++)
			{
				int x = i % 4;
				int y = i / 4;
				
				tiles[i] = tileset.getSubimage(x * 16, y * 16, 16, 16);
			}
			
			for(int i = 0; i < 10; i++)
			{
				playerFrames[i] = player.getSubimage(i * 16, 0, 16, 32);
			}
			
			for(int i = 0; i < 6; i++)
			{
				melee_particles[i] = melee.getSubimage(i * 16, 0, 16, 16);
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
