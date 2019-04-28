package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import utils.Images;

public class Coin {
	
	public float x, y, speed;
	public int count, frame, lifetime;
	
	public Coin(int x, int y, float speed)
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	public void update(Graphics2D g)
	{
		g.drawImage(Images.tiles[frame + 1], (int) x, (int) y, 32, 32, null);
		
		count++;
		lifetime++;
		
		if(count > 10)
		{
			frame++;
			if(frame > 5)
				frame = 0;
			count = 0;
		}
		
		y += speed;
	}
	
	public Rectangle bounds()
	{
		return new Rectangle((int) x, (int) y, 32, 32);
	}

}
