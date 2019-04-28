package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Dialouge 
{
	public String message;
	public BufferedImage[] msg;
	
	private int counter = 0;
	public int activation, lastIndex;
	
	public boolean moreDialouge = false;
	
	public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.!?%/', ";
	
	public Dialouge(String message)
	{
		this.message = message;
		msg = new BufferedImage[150];
		
		create();
	}
	
	public void create()
	{
		for(int i = 0; i < message.length(); i++)
		{
			char c = message.charAt(i + lastIndex);
			
			if(i == 150 || c == '|')
			{
				moreDialouge = true;
				lastIndex = i + 1;
				break;
			}
			
			int index = CHARACTERS.indexOf(c);
			
			int x = index % 10;
			int y = index / 10;
			
			msg[i] = Images.font.getSubimage(x * 6, y * 8, 6, 8);
			
			if(i + lastIndex == message.length() - 1)
			{
				moreDialouge = false;
				lastIndex = 0;
				for(int j = i + 1; j < 150; j++)
				{
					msg[j] = null;
				}
				break;
			}
		}
		
		activation = 0;
	}
	
	public void update(Graphics2D g, int x, int y)
	{
		int curX = 0;
		int curY = 10;
		for(int i = 0; i < activation; i++)
		{
			if(i < message.length())
			{
				char c = message.charAt(i);
				if(curX > 40 && c == ' ')
				{
					curX = -1;
					curY += 32;  
				}
			}
			
			g.drawImage(msg[i], 20 + (curX * 12) + x, curY + y, 12, 16, null);
			curX++;
		}
		
		counter++;
		
		if(counter > 10)
		{
			if(activation < 150) activation++;
			counter = 0;
		}
	}
}
