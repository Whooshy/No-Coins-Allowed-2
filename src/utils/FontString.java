package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FontString 
{
	public BufferedImage[] msg;
	
	private int counter = 0;
	public int activation, lastIndex;
	
	public boolean moreDialouge = false;
	
	public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.!?%/', ";
	
	public FontString()
	{
		
	}
	
	public void create(String message)
	{
		msg = new BufferedImage[message.length()];
		for(int i = 0; i < message.length(); i++)
		{
			char c = message.charAt(i + lastIndex);
			
			if(i == 90 || c == '|')
			{
				moreDialouge = true;
				lastIndex = i + 1;
				break;
			}
			
			int index = CHARACTERS.indexOf(c);
			
			int x = index % 10;
			int y = index / 10;
			
			msg[i] = Images.font.getSubimage(x * 6, y * 8, 6, 8);
		}
		
		activation = 0;
	}
	
	public void update(Graphics2D g, int x, int y)
	{
		int curX = 0;
		int curY = 10;
		for(int i = 0; i < msg.length; i++)
		{
			g.drawImage(msg[i], x + (i * 12), y, 12, 16, null);
		}
	}
}
