package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import utils.Images;

public class Player 
{
	public float x, y;
	public float velX, velY;
	
	public float speed = 3;
	
	public int frame, direction, counter;
	
	public boolean isCollidingTop, isCollidingBottom, isCollidingLeft, isCollidingRight, isMovingRight, isMovingLeft, isJumping, isFalling;
	
	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void update(Graphics2D g)
	{
		if(isMovingRight)
		{
			velX = speed;
			direction = 1;
		}
		else if(isMovingLeft)
		{
			velX = -speed;
			direction = 2;
		}
		else
		{
			velX = 0;
			direction = 0;
		}
		
		if(direction == 0) frame = 0;
		if(direction == 3) frame = 9;
		
		if(direction == 1)
		{
			counter++;
			if(counter >= 15)
			{
				frame++;
				if(frame > 4) frame = 1;
				counter = 0;
			}
		}
		
		if(direction == 2)
		{
			counter++;
			if(counter >= 15)
			{
				frame--;
				if(frame < 5) frame = 8;
				counter = 0;
			}
		}
		
		collision();
		
		x += velX;
		y += velY;
		
		g.drawImage(Images.playerFrames[frame], (int) x, (int) y, 32, 64, null);
	}
	
	public void collision()
	{
		if(isFalling)
		{
			isJumping = false;
		}
		
		if(isCollidingTop)
		{
			velY *= -1;
			isCollidingTop = false;
		}
		
		if(isCollidingLeft)
		{
			if(velX < 0) velX = 0;
		}
		
		if(isCollidingRight)
		{
			if(velX > 0) velX = 0;
		}
		
		if(isCollidingBottom)
		{
			velY = 0;
			isFalling = false;
			if(isJumping)
			{
				velY = -4;
				isFalling = true;
			}
		}
		
		else
		{
			velY += 0.05f;
			if(velY > 4)
				velY = 4;
		}
	}
	
	public float getCamX(int width)
	{
		return x - (width / 2) + 32;
	}
	
	public float getCamY(int height)
	{
		return y - (height / 2) + 64;
	}
	
	public Rectangle bottom()
	{
		return new Rectangle((int) x + 4, (int) y + 60, 24, 4);
	}
	
	public Rectangle bottom2()
	{
		return new Rectangle((int) x + 4, (int) y + 60, 3, 4);
	}
	
	public Rectangle top()
	{
		return new Rectangle((int) x, (int) y, 28, 4);
	}
	
	public Rectangle top2()
	{
		return new Rectangle((int) x, (int) y, 3, 4);
	}
	
	public Rectangle left()
	{
		return new Rectangle((int) x, (int) y + 4, 4, 56);
	}
	
	public Rectangle left2()
	{
		return new Rectangle((int) x, (int) y + 4, 4, 4);
	}
	
	public Rectangle right()
	{
		return new Rectangle((int) x + 28, (int) y + 4, 4, 56);
	}
	
	public Rectangle right2()
	{
		return new Rectangle((int) x + 28, (int) y + 4, 4, 4);
	}
	
	public Rectangle hit()
	{
		return new Rectangle((int) x + 8, (int) y + 8, 16, 48);
	}
}
