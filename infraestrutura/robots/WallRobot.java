package infraestrutura.robots;


import infraestrutura.util.StagePoint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;

import jogo.GameManager;


/** this abstract class provides a framework for a robot that follows a wall
 * @author Stefan Barthel
 * @since  Feb 20, 2007
 *
 */
abstract public class WallRobot extends AbstractThreadRobot{

	/** Movement directions */
	public static final int LEFT 	= 0;
	public static final int UP 	= 1;
	public static final int RIGHT 	= 2;
	public static final int DOWN 	= 3;
	

	
	/** color of the inner Triangle */
	protected Color innerColor=Color.RED;
	
	/** current direction */
	protected int direction = RIGHT;
	
	/** all postions, where the robot was*/
	Vector<StagePoint> visitedFields = new Vector<StagePoint>(255);
	
	/**
	 * standard constructor
	 * @param x Start x position 
	 * @param y Start y position
	 * @param laby a valid labirinto
	 * @param dist a distance for field marking
	 */
	public WallRobot(int x, int y){
		super(x,y);
		color = Color.YELLOW;
		posicaoAtual = new StagePoint(GameManager.getInstance().getStartPoing()); 
		direction = RIGHT;
		speed = 100;
		setName(this.getClass().getSimpleName());
		
	}
	
	
	
	/** returns the first postion to check for a wall robot
	 *  @param direction current direction
	 * @return postion (Constant defined in the class header)
	 */	
	abstract protected int getFirstDirection(int direction);
	
	/**
	 * gets a new postion of a direction 
	 * @param direction constant of direction to go to 
	 * @param xPos current x position
	 * @param yPos current y postion
	 * @return direction or null if a wrong direktion ist given by the parameter
	 */
	protected StagePoint getPostionOfDirection(int direction ,int xPos,int yPos){
		if(direction == UP){
			return new StagePoint(xPos,yPos-1);
		}else if(direction == DOWN){
			return new StagePoint(xPos, yPos+1);
		}else if(direction == LEFT){
			return new StagePoint(xPos-1, yPos);
		}else if(direction == RIGHT){
			return new StagePoint(xPos+1, yPos);
		}
		// if wrong direction return null
		return null;
	}
	
	
	/** returns the next postion to check for a wall robot
	 * @param direction current direction
	 * @return postion (Constant defined in the class header)
	 */	
	abstract protected int getNextDirection(int direction);
	
	
	/** this method probides a draw Funktion for a grahpics object
	 * 	@param g a valid Graphics object
	 *  @param xRaster the raster width of the current labirinto
	 *  @param yRaster the raster height of the current labirinto
	 */ 
	public void draw(Graphics g,float xRaster,float yRaster){
		
//		 save the current color
		Color oldColor = g.getColor();
		
		super.draw(g, xRaster, yRaster);
				
//		 create and draw the inner triangle of the robot (Indicates the direction)
		Triangle t = new Triangle();
		t.draw(g, xRaster, yRaster);
		
		
		// set the color of this robot
		g.setColor(color);
				
		// draw the way if target was reached
		if(exitReached){
			for(StagePoint p : visitedFields){
				//calculate the postion within the raster
				Point pc = calculateDrawCoordinate(p, xRaster, yRaster);
				
				g.drawRect( pc.x+dist , pc.y+dist, ((int)xRaster)-(2*dist), ((int)yRaster)-(2*dist));
				
				
			}
		}
		
		
		
		// reset the old color
		g.setColor(oldColor);
		
		
		
	}
	
	/**
	 * this method moves the bot to its next position
	 * @return
	 */
	public boolean autoMove(){
		
		if(this.posicaoAtual != null ){

			int x = posicaoAtual.x;
			int y = posicaoAtual.y;
			
			int dir = getFirstDirection(direction);
			
			for(int i = 0; i < 4; i++ ){
				StagePoint p = getPostionOfDirection(dir, x, y);
				if(isValidMove(p.x,p.y)){
				
					visitedFields.add(posicaoAtual);
					posicaoAtual = p;
					direction  = dir;
					robotChanged();
					
					return true;
				}else{
					dir = getNextDirection(dir);
				}
				
			}
	
			return true;
		}
		return false;
	}
	
	@Override
	public ImageIcon getIcon(){
		/*ImageIcon ic = super.getIcon();
		Image im = ic.getImage();
		Graphics g = im.getGraphics();
		Triangle t = new Triangle();
		t.draw(g, ic.getIconWidth(),ic.getIconHeight());
		return new ImageIcon(im);*/
		
		BufferedImage i = new BufferedImage(20,20,BufferedImage.TYPE_INT_ARGB);
		Graphics g = i.getGraphics();
		g.setColor(this.color);
		g.fill3DRect(0,0,i.getWidth(),i.getHeight(),true);
		direction = RIGHT;
		Triangle t = new Triangle();
		t.draw(g, i.getWidth(),i.getHeight());
		
		
		return new ImageIcon(i);
	}
	
	
	/**
	 * This class provides a triangle (a polygone with three points) 
	 *  : Triangle
	 * @author Stefan Barthel
	 * @since  Feb 20, 2007
	 *
	 */
	class Triangle {
		/** color of the triangle*/
		private Color color = null;
		
		/** coordinates of the three points*/
		private int[] xCoord = new int[3];
		private int[] yCoord = new int[3];
	
		/** standard constructor without a parameter setzs the color of the triangle to red */
		public Triangle(){
			this(Color.RED);
		}
		
		/** constructor 2 sets the color of the triangle 
		 * @param c Color of the triangle
		 */
		public Triangle(Color c){
			this.color = c;
		}
		
		
		/** 
		 * drawing Funktion of the triangle, draws the triangle onto a Graphics plane 
		 * @param g a valid Graphics object
		 * @param xRaster  the raster width of the current labirinto
		 * @param yRaster the raster height of the current labirinto
		 */
		public void draw(Graphics g,float xRaster,float yRaster){
			// save the old color 
			Color oldColor = g.getColor();
			
			// set the own color to draw
			g.setColor(color);
			
			// calculate the points of the triangle polygone
			calcTriangle( xRaster, yRaster);

			// draw the triangle 
			g.fillPolygon(xCoord, yCoord,3);
			
			// restor the saved color
			g.setColor(oldColor);
			
		}
		
		
		
		/** 
		 * this method calculates the points of the triangle
		 * @param xRast size of the raster in x direction
		 * @param yRast size of the raster in y direction
		 */
		private void calcTriangle(float xRast, float yRast){
		    // space to the raster bound
			int space = 2+dist;
			
			// save the raster lokaly
			float xRaster = xRast ;
			float yRaster = yRast ;
			
			// calculate the minimum x,y position of the triangle
			int xCoordinate = Math.round(xRaster*((float)posicaoAtual.x)+((float)space));
			int yCoordinate = Math.round(yRaster*((float)posicaoAtual.y)+((float)space));
			
			// redruce the raster by the doubled space
			xRaster -=2*space;
			yRaster -=2*space;
			
			
			// decide which the current direction ist, and create the corresponding triangle
			switch (direction){
			
			
				case UP: 
					xCoord[0]= Math.round(xCoordinate+(xRaster/2));
					xCoord[1]= Math.round(xCoordinate+xRaster);
					xCoord[2]= xCoordinate;
					yCoord[0]= yCoordinate;
					yCoord[1]= Math.round(yCoordinate+yRaster);
					yCoord[2]= Math.round(yCoordinate+yRaster);
					break;
				case DOWN: 
					xCoord[0]= Math.round(xCoordinate+(xRaster/2));
					xCoord[1]= Math.round(xCoordinate+xRaster);
					xCoord[2]= xCoordinate;
					yCoord[0]= Math.round(yCoordinate+yRaster);
					yCoord[1]= yCoordinate;
					yCoord[2]= yCoordinate;
					break;
				case RIGHT: 
					xCoord[0]= xCoordinate;
					xCoord[1]= Math.round(xCoordinate+xRaster);
					xCoord[2]= xCoordinate;
					yCoord[0]= yCoordinate;
					yCoord[1]= Math.round(yCoordinate+(yRaster/2));
					yCoord[2]= Math.round(yCoordinate+yRaster);
					break;
				case LEFT: 
					xCoord[0]= Math.round(xCoordinate+xRaster);
					xCoord[1]= xCoordinate;
					xCoord[2]= Math.round(xCoordinate+xRaster);
					yCoord[0]= yCoordinate;
					yCoord[1]= Math.round(yCoordinate+(yRaster/2));
					yCoord[2]= Math.round(yCoordinate+yRaster);
					break;
		
			}
		}
}
}
