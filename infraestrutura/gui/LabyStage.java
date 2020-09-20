package infraestrutura.gui;
import infraestrutura.robots.AbstractRobot;
import infraestrutura.util.RobotChangeListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import jogo.GameManager;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;



public class LabyStage extends JPanel implements RobotChangeListener{
	
	protected char [][] labyMap = null;
	protected Vector<AbstractRobot> robots = new Vector<AbstractRobot>(3);
	
	protected BufferedImage bi = null; 
	
	public LabyStage(char [][] _labyMap){
		this.labyMap = _labyMap;
		this.setFocusable(true);
	}
	
	public void addRobot(AbstractRobot r){
		//this.addKeyListener(r);
		robots.add(r);
		
		try{
			r.addRobotChangeListener(this);
		}catch(ClassCastException ce){}
	}
	
	public void removeRobot(AbstractRobot r){
		if(r != null){
			//this.removeKeyListener(r);
			
			
			robots.remove(r);
		}
	}
	
	public void setLabyrinth(char [][] _labyMap){
		this.labyMap = _labyMap;
		repaint();
	}
	
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		if(this.labyMap != null){
			
			int width  = this.getWidth();
			int height = this.getHeight();
			
			// if the size of the drawing plane has changed, create a new image in the same size
			if(bi == null ||  bi.getWidth()!= width || bi.getHeight() != height){
				bi= new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
			}
			
			Graphics bg = bi.getGraphics();
			
						
			float xRaster = ((float)width) / ((float)labyMap[0].length);
			float yRaster = ((float)height) /((float)labyMap.length);
			int xPos = 0;
			int yPos = 0;
							
			for(int i = 0; i<labyMap.length;i++){
				
				yPos = Math.round( ((float)i) * yRaster );
				for(int j = 0; j<labyMap[0].length;j++){
		    		
		    		xPos = Math.round(((float)j) * xRaster);
		    	
		    		switch(labyMap[i][j]){
		    		
			    		case GameManager.START : {
			    			bg.setColor(Color.CYAN);			    			
			    			bg.fillRect(xPos,yPos,Math.round(xRaster)+1,Math.round(yRaster)+1);
			    			break;
			    		}
			    		case GameManager.TARGET : {
			    			//bg.setColor(Color.GREEN);
			    			
			    			//princesa.JPG
			    			Graphics2D g2 = (Graphics2D) g;
			    			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			    			Image princesaImg = new ImageIcon( Thread.currentThread().getContextClassLoader().getResource("images/princesa.JPG" ) ).getImage();
			    			System.out.println(princesaImg);
			    			System.out.println("xPos : " + xPos);
			    			System.out.println("yPos : " + yPos);
			    			g2.drawImage(princesaImg,xPos,yPos,this);
			    			
			    			g.drawImage(princesaImg,xPos,yPos,this);			    			

			    			//bg.fillRect(xPos,yPos,Math.round(xRaster)+1,Math.round(yRaster)+1);
			    			break;
			    		}
			    		
			    		case GameManager.WAY : {
			    			bg.setColor(Color.BLACK);
			    			
			    			//bg = paint(bg,xPos,yPos,"images/background1.JPG");
			    			bg.fillRect(xPos,yPos,Math.round(xRaster)+1,Math.round(yRaster)+1);
			    			break;
			    		}
			    		
			    		case GameManager.WALL : {
			    			//bg.setColor(Color.WHITE);
			    			bg = paint(bg,xPos,yPos,"images/blocos.JPG");
			    			break;
			    		}
			    		
			    		case GameManager.MUSHROM : {
			    			//bg.setColor(Color.WHITE);
			    			bg = paint(bg,xPos,yPos,"images/oneUp.jpg");
			    			break;
			    		}
			    		
			    		
			    	}		    		
		    	}
		    }
			
			for(int i = robots.size()-1; i>=0; i--){	
				((AbstractRobot)robots.elementAt(i)).draw(bg,xRaster,yRaster);
			}
			
			g.drawImage(bi,0,0,this);
			
			
		}
		
	}

	public void robotPositionChanged() {
		// TODO Auto-generated method stub
		this.repaint();
	}
		
	/**
	 *	SHOW AVATAR 
	 */
	
	private BufferedImage bfImage;

	public Graphics2D paint(Graphics g,int x, int y,String imagem) {
		Graphics2D g2 = (Graphics2D) g;

		int largura = 34 - x;
		int altura = 25 - y;

		
		if (bfImage == null){
			createOffscreenImage(largura,altura,imagem);
		}
		g2.drawImage(bfImage, x, y, null);
		
		return g2;
	}

	private void createOffscreenImage(int largura, int altura,String imagem) {

		bfImage = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = bfImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(imagem); 
			
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			BufferedImage image = decoder.decodeAsBufferedImage();
			in.close();
			g2.drawImage(image, 0, 0, largura, altura, null);
			
		} catch (Exception e) {
			System.out.print(e);
		}  
	}
	
	
	private class LabyUIListener implements ActionListener,KeyListener,infraestrutura.util.RobotChangeListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void robotPositionChanged() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
