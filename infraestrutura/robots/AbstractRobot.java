package infraestrutura.robots;

import infraestrutura.som.MidiPlayer;
import infraestrutura.util.StagePoint;
import infraestrutura.util.RobotChangeListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Vector;

import javax.sound.midi.Sequence;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jogo.GameManager;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;



abstract public class AbstractRobot implements RobotChangeListener{

	private MidiPlayer midiPlayerMusicaFinalFase;
    private Sequence musicaFinalFase;
	
    protected long startTime;
    
	protected StagePoint posicaoAtual = null;
	protected StagePoint targetPoint = null;
	protected Point  drawCoordinate = null;

	// coordinates for drawing on a panel
	protected int xCoordinate = 0;
	protected int yCoordinate = 0;

	/** status indicates if the exit was reached **/
	boolean exitReached = false;
	
	boolean showRaster = false;

	protected GameManager gameManager = null;
	protected Color color = Color.CYAN;

	private String avatar;

	/**the robot name */
	private String name = "ARobot";

	Vector<RobotChangeListener> robotChangeListener = new Vector<RobotChangeListener>(1);

	static protected int exitReachedCount = 0;

	protected int robotExitReachedNumber = 0;

	public AbstractRobot(){
		System.out.println("teste contruindo o objeto");
	}
	
	public AbstractRobot(StagePoint p){
		this(p. x, p.y);
	}
	
	public AbstractRobot(int x, int y){
		posicaoAtual = new StagePoint(x,y);

		this.gameManager = GameManager.getInstance();
		this.targetPoint = gameManager.getTargetPoint(); 

		setStartPoint();
		
		this.setName(this.getClass().getSimpleName());
		this.setAvatar(this.getClass().getSimpleName());
	}
	

	/** show the reaching the exit dialog 
	 * 
	 */
	public void showTargetReachedDiaolog() {

		midiPlayerMusicaFinalFase = new MidiPlayer();

		if (robotExitReachedNumber > 1) {
			musicaFinalFase = midiPlayerMusicaFinalFase
					.getSequence("musicGameOver.midi");
		} else {
			musicaFinalFase = midiPlayerMusicaFinalFase
					.getSequence("musicLevelEnd.midi");
		}

		midiPlayerMusicaFinalFase.play(musicaFinalFase, false);
		
		long endTime = System.currentTimeMillis(); 
		long raceTime =((endTime - startTime)); 
		
		String finalMessage = "";
		if(robotExitReachedNumber == 1){
			finalMessage = "YOU WIN!";
		}else{
			finalMessage = "YOU LOSE";
		}
		
	
		JOptionPane.showMessageDialog(null, name
				+ "," + finalMessage);
		
		
		
		robotChanged();
	}	

	static protected Point calculateDrawCoordinate(Point pos, float xRaster, float yRaster){
		Point retP = new Point();
		retP.x =Math.round((xRaster*((float)pos.x)));
		retP.y =Math.round((yRaster*((float)pos.y)));
		return retP;
	}

	public ImageIcon getIcon(){

	/*	BufferedImage i = new BufferedImage(0,0,BufferedImage.TYPE_INT_ARGB);
		Graphics g = i.getGraphics();
		g.setColor(this.color);
		g.fill3DRect(0,0,i.getWidth(),i.getHeight(),true);

		System.out.println("Imagem " + image);
		
		createOffscreenImage(0,0);		*/
		return new ImageIcon(image);

	}

	public void draw(Graphics g, float xRaster, float yRaster){
		

		if(gameManager != null){

			// calculate the position of the bot
			Point p = calculateDrawCoordinate(posicaoAtual,xRaster, yRaster);

			g =  paint(g,p.x,p.y);
			System.out.println("Ponto x: " + p.x);
			System.out.println("Ponto y: " + p.y);
			/*// if distance ist smaller than the robot representation size
			 * 
			 */
			int dist = 1;
			/*if(GameManager.getInstance().canShowRaster()){
				if(dist < (xRaster-(2*dist))&&
						dist < (xRaster-(2*dist)) &&
						xRaster-(2*dist)>10){
					// draw the bot as rectangle
					g.fillRect(p.x+dist, 
							   p.y+dist,
							   Math.round(xRaster-((float)(2*dist))),
							   Math.round(yRaster-((float)(2*dist))));
				}else{ // else draw a simple rect
					g.fillRect(p.x, 
							   p.y,
							   Math.round(xRaster),
							   Math.round(yRaster));
				}	
			}*/
			
		}
	}

	/**
	 * check if the given parameter are valid coordinates to walk to 
	 * @param x the targeted x postion
	 * @param y the targeted y postion
	 * @return return true if the postion is a way 
	 */
	public boolean isValidMove(int x, int y){
		try{
			// if it is a way
			
			char [][] labyMap = GameManager.getInstance().getLabyMap(); 
			
			if(labyMap[y][x]== GameManager.WAY || labyMap[y][x]== GameManager.TARGET  || labyMap[y][x]== GameManager.MUSHROM){
				// if it is not a diagonal movement
				if( x == posicaoAtual.x || y == posicaoAtual.y ){
					return true;				
				}
			}

		}catch(ArrayIndexOutOfBoundsException e){}

		return false; 
	}


	public void addRobotChangeListener(RobotChangeListener rcl){
		this.robotChangeListener.add(rcl);
	}

	public void removeRobotChangeListener(RobotChangeListener rcl){
		this.robotChangeListener.remove(rcl);
	}

	public boolean move(int x, int y){
		if(isValidMove(x,y)){
			this.posicaoAtual = new StagePoint(x,y);
			robotChanged();
			return true;
		}
		return false;
	}

	protected void robotChanged(){
		for(int i = robotChangeListener.size()-1; i >=0; i--){
			robotChangeListener.elementAt(i).robotPositionChanged();
		}
	}

	public void setStartPoint(){	
				
		if(GameManager.getInstance().isValidMove(posicaoAtual.x-1,posicaoAtual.y)){
			posicaoAtual.x--;
		}else if(GameManager.getInstance().isValidMove(posicaoAtual.x,posicaoAtual.y-1)){
			posicaoAtual.y--;	
		}else if(GameManager.getInstance().isValidMove(posicaoAtual.x,posicaoAtual.y+1)){
			posicaoAtual.y ++;	
		}else if(GameManager.getInstance().isValidMove(posicaoAtual.x+1,posicaoAtual.y)){
			posicaoAtual.x ++; 
		}
	}


	/**
	 * checks if the bot reaches the exit 
	 * @return returns true if the exit is reached 
	 */
	public boolean targetReached(){	
		if( !exitReached && targetPoint.equals(posicaoAtual)){
			exitReached = true;
			exitReachedCount ++;
			this.robotExitReachedNumber = exitReachedCount;
			robotChanged();
			showTargetReachedDiaolog();
			return true;

		}else if(exitReached){
			return true;
		}
		return false;
	}



	/**
	 *	SHOW AVATAR 
	 */
	private BufferedImage image;

	public Graphics2D paint(Graphics g,int x, int y) {
		
		Graphics2D g2 = (Graphics2D) g;

		int largura = x;
		int altura = y;

		if (image == null){
			createOffscreenImage(34, 25);
		}
			

		
		g2.drawImage(image,x, y, null);	
		
		return g2;
	}

	private void createOffscreenImage(String avatar,int largura, int altura) {
		System.out.println("createOffscreenImage");
		
		image = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		try {			
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(avatar); 
			
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			BufferedImage image = decoder.decodeAsBufferedImage();
			in.close();
			System.out.println();
			System.out.println("largura : " + largura);
			System.out.println("altura : " + altura);
			
			g2.drawImage(image,0,0,  largura, altura, null);
			
		} catch (Exception e) {
			System.out.print(e);
		}  
	}
	private void createOffscreenImage(int largura, int altura) {
		createOffscreenImage(avatar,largura, altura);
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		//this.avatar = "avatars" + File.separatorChar +  avatar + ".jpg";
		this.avatar = "avatars" + File.separatorChar +  "marioIcon.JPG";
		
	}

	public void hello(String txt){
		System.out.println("eeeeeeeeeeeeeeeeeeeeeeeee" + txt) ;
	}
}
