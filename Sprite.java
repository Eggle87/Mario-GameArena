import java.awt.image.BufferedImage;

/**
 * Models a simple, solid rectangle. 
 * This class represents a Rectabgle object. When combined with the GameArena class,
 * instances of the Sprite class can be displayed on the screen.
 */
public class Sprite
{
	// The following instance variables define the
	// information needed to represent a Sprite
	// Feel free to more instance variables if you think it will 
	// support your work... 
	
	private double xPosition;			// The X coordinate of this Sprite
	private double yPosition;			// The Y coordinate of this Sprite
	private double width;				// The width of this Sprite
	private double height;				// The height of this Sprite
	private BufferedImage img;
	private int layer;				// The layer this Sprite is on.


	public Sprite() {}	
	
	/**
	 * Constructor. Creates a Sprite with the given parameters.
	 * @param x The x co-ordinate position of top left corner of the Sprite (in pixels)
	 * @param y The y co-ordinate position of top left corner of the Sprite (in pixels)
	 * @param w The width of the Sprite (in pixels)
	 * @param h The height of the Sprite (in pixels)
	 */
	public Sprite(double x, double y, double w, double h, BufferedImage i)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.width = w;
		this.height = h;
		this.img = i;
		this.layer = 0;
	}	
									
	/**
	 * Constructor. Creates a Sprite with the given parameters.
	 * @param x The x co-ordinate position of top left corner of the Sprite (in pixels)
	 * @param y The y co-ordinate position of top left corner of the Sprite (in pixels)
	 * @param w The width of the Sprite (in pixels)
	 * @param h The height of the Sprite (in pixels)
	 * @param layer The layer this Sprite is to be drawn on. Objects with a higher layer number are always drawn on top of those with lower layer numbers.
	 */
	public Sprite(double x, double y, double w, double h, BufferedImage i, int layer)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.width = w;
		this.height = h;
		this.img = i;
		this.layer = layer;
	}	
			
	/**
	 * Obtains the current position of this Sprite.
	 * @return the X coordinate of this Sprite within the GameArena.
	 */
	public double getXPosition()
	{
		return xPosition;
	}

	/**
	 * Obtains the current position of this Sprite.
	 * @return the Y coordinate of this Sprite within the GameArena.
	 */
	public double getYPosition()
	{
		return yPosition;
	}

	/**
	 * Moves the current position of this Sprite to the given X co-ordinate
	 * @param x the new x co-ordinate of this Sprite
	 */
	public void setXPosition(double x)
	{
		this.xPosition = x;
	}

	/**
	 * Moves the current position of this Sprite to the given Y co-ordinate
	 * @param y the new y co-ordinate of this Sprite
	 */
	public void setYPosition(double y)
	{
		this.yPosition = y;
	}

	/**
	 * Obtains the width of this Sprite.
	 * @return the width of this Sprite,in pixels.
	 */
	public double getWidth()
	{
		return width;
	}

	/**
	 * Sets the width of this Sprite to the given value
	 * @param w the new width of this Sprite, in pixels.
	 */
	public void setWidth(double w)
	{
		width = w;
	}

	/**
	 * Obtains the height of this Sprite.
	 * @return the height of this Sprite,in pixels.
	 */
	public double getHeight()
	{
		return height;
	}

	/**
	 * Sets the height of this Sprite to the given value
	 * @param h the new height of this Sprite, in pixels.
	 */
	public void setHeight(double h)
	{
		height = h;
	}

	/**
	 * Obtains the image of this Sprite.
	 * @return the image of this Sprite.
	 */
	public BufferedImage getImage()
	{
		return img;
	}

	/**
	 * Obtains the image of this Sprite.
	 * @return the image of this Sprite.
	 */
	public void setImage(BufferedImage i)
	{
		img = i;
	}

	/**
	 * Obtains the layer of this Sprite.
	 * @return the layer of this Sprite.
	 */
	public int getLayer()
	{
		return layer;
	}

	/**
	 * Moves this Sprite by the given amount.
	 * 
	 * @param dx the distance to move on the x axis (in pixels)
	 * @param dy the distance to move on the y axis (in pixels)
	 */
	public void move(double dx, double dy)
	{
		xPosition += dx;
		yPosition += dy;
	}

	/**
	 * Determines if this Sprite is overlapping the given rectangle.
	 * 
	 * @param r the rectangle to test for collision
	 * @return true of this rectangle is overlapping the rectangle r, false otherwise.
	 */
	public boolean collides(Sprite r)
	{
		return (xPosition + width > r.xPosition && xPosition < r.xPosition + r.width) && (yPosition + height > r.yPosition && yPosition < r.yPosition + r.height);
	}
}