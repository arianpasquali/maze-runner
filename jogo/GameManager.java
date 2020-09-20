package jogo;

import infraestrutura.gui.LabyStage;
import infraestrutura.gui.UserInterface;
import infraestrutura.robots.AbstractRobot;
import infraestrutura.robots.AbstractThreadRobot;
import infraestrutura.som.MidiPlayer;
import infraestrutura.som.SoundManager;
import infraestrutura.util.IOUtils;
import infraestrutura.util.StagePoint;
import infraestrutura.util.RobotClassLoader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GameManager extends GameCore{

	static final long serialVersionUID = 1;

	// indica se deve ir para a próxima fase
    private boolean goToNextLevel;
    
    // quantidade máxima de fases (importante para a finalização)
    private static int QUANTIDADE_FASES = 3;
	
	// descomprimido, 11025Hz, 8-bit, mono, signed, little-endian
	private static final AudioFormat PLAYBACK_FORMAT = new AudioFormat(11025,8, 1, true, false);

	public static final int EASY = 1;
	public static final int MEDIUM = 2;
	public static final int HARD = 3;
	
	
	private MidiPlayer midiPlayerMusica;
	private MidiPlayer midiPlayerMusicaFinalFase;
	private SoundManager soundManager;
	private Sequence musica;
	private Sequence musicaFinalFase;
	
	private StagePoint startPoing;
	private StagePoint targetPoint;

	private LabyStage labyStage;
	private AbstractRobot[] robotsInStage;
	
	private UserInterface ui;
	
	private boolean showRaster = true;

	private int gameLavel;
	
	private static GameManager singleton;
		
	public static GameManager getInstance(){
		
		if(singleton!=null){
			return singleton;
		}else{		
			singleton = new GameManager();
			
			return  singleton;	
		}
	}		
	
	private GameManager() {
		super();
		
		init();					
	}

	private void init() {
		// configura Janela
		ui = new UserInterface(this);
		
        // carrega o primeiro mapa
		buildRandomMap();
		
        // carrega os recursos de som		
		soundManager = new SoundManager(PLAYBACK_FORMAT);
		
		midiPlayerMusica = new MidiPlayer();
		midiPlayerMusicaFinalFase = new MidiPlayer();

		musica = midiPlayerMusica.getSequence("music2.midi");
		musicaFinalFase = midiPlayerMusicaFinalFase.getSequence("musicLevelEnd.midi");

		//Seta estado da aplicação como stop
		stop();
	}

	public void buildRandomMap() {

		if(this.getGameLavel() == EASY){
			this.height = 20;
			this.width = 20;	
		}else
			if(this.getGameLavel() == MEDIUM){
				this.height = 30;
				this.width = 30;
			}else {
				this.height = 50;
				this.width = 50;
			}
		
		int sx = 0, sy = 1;

		labyMap = new char[height][width];

		maxdepth = 0;
		depth = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				labyMap[i][j] = WALL;
			}
		}
		drawMap(width, height, sx, sy);

		labyMap[sy][sx] = START;
		labyMap[ly][lx] = TARGET;

		startPoing = new StagePoint(sx, sy);
		targetPoint = new StagePoint(lx, ly);

		if (labyStage != null) {
			ui.getContentPane().remove(labyStage);
			labyStage = null;
		}

		
		labyStage = new LabyStage(labyMap);
		labyStage.setFocusable(true);
		
		labyStage.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "pressed");		

		ui.getContentPane().add(labyStage, BorderLayout.CENTER);
		ui.pack();		
	}

	public void createRobot(int index,String clazzName) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		System.out.println("createRobot : " + clazzName);
		System.out.println("new " + clazzName);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); 
		
		Class clazz = classLoader.loadClass(clazzName);
					
		System.out.println("clazz > " + clazz.toString());
		
		Class [] params = new Class[] {int.class,int.class};					
		
		Constructor constrs[] = clazz.getConstructors();
		for (Constructor constructor : constrs) {
			Class[] paramsTypes = constructor.getParameterTypes(); 
			for (Class class1 : paramsTypes) {
				System.out.println("Param : " + class1.getSimpleName());
			}
		}
		
		Constructor  constr = clazz.getConstructor(params);	
		AbstractThreadRobot absRobot  = (AbstractThreadRobot) constr.newInstance(new Object[]{startPoing.x,startPoing.y});
		robotsInStage[index] = absRobot;
		this.labyStage.addRobot(robotsInStage[index]);
		if(robotsInStage[index] instanceof KeyListener){
			System.out.println(robotsInStage[index] + " instanceof KeyListener");
			labyStage.addKeyListener((KeyListener)robotsInStage[index]);
		}
		
		labyStage.repaint();
		ui.repaint();				
	}
	
	
	public void distroyRobot(int index){
		
		this.labyStage.removeRobot(robotsInStage[index] );
		if(robotsInStage[index] instanceof KeyListener){
			labyStage.removeKeyListener((KeyListener)robotsInStage[index]);			
		}

		robotsInStage[index]  = null;
		ui.repaint();
	}

	public void resetStage(){
		System.out.println("Distroy all robots in stage");
		
		//Distroy all robots in stage
		for(int i =0; i < robotsInStage.length ; i++){
			if(robotsInStage[i]!= null) distroyRobot(i);
		}
	}
	
	public void start() {
		System.out.println("Start new Robot Threads");
		if (midiPlayerMusica != null)
			midiPlayerMusica.play(musica, true);
		
		
		//Start new Robot Threads
		for(int i =0; i < robotsInStage.length ; i++){
			if(robotsInStage[i] != null){
				if(robotsInStage[i] instanceof AbstractThreadRobot){
					((AbstractThreadRobot)robotsInStage[i]).start();
				}
			}		
		}	
	}

	public void stop() {
		stopSounds();

		for(int i =1; i < robotsInStage.length; i++){
			if(robotsInStage[i] != null){
				if(robotsInStage[i] instanceof AbstractThreadRobot){
					((AbstractThreadRobot)robotsInStage[i]).stop();	
				}
								
			}

		}

	}

	public void resume() {
		playSounds();
		
		for (int i = 1; i < robotsInStage.length; i++) {
			if (robotsInStage[i] != null && robotsInStage[i] instanceof AbstractThreadRobot)
				((AbstractThreadRobot) robotsInStage[i]).pause(false);
		}
	}
	
	public void pause() {	
		stopSounds();
		
		for (int i = 1; i < robotsInStage.length; i++) {
			if (robotsInStage[i] != null && robotsInStage[i] instanceof AbstractThreadRobot)
				((AbstractThreadRobot) robotsInStage[i]).pause(true);
		}
	}	
	
	public AbstractRobot[] getRobotsInStage() {
		return robotsInStage;
	}

	public StagePoint getStartPoing() {
		return startPoing;
	}



	public StagePoint getTargetPoint() {
		return targetPoint;
	}

	public void setRobotsInStage(AbstractRobot[] robotsInStage) {
		this.robotsInStage = robotsInStage;
	}
	
	// --------------------------------------------------------------------------------------

	/**
	 * main method
	 * 
	 * @param args
	 *            console arguments
	 */
	public static void main(String[] args) {
		

		GameManager lb = GameManager.getInstance();
		lb.setGameLavel(EASY);
		
		
	
		
		
		
		
	}

	public boolean canShowRaster() {
		return showRaster;
	}

	public void setShowRaster(boolean showRaster) {
		this.showRaster = showRaster;
	}

	public void playSounds() {
		System.out.println("playSounds()");
		
		if (midiPlayerMusica != null)
			midiPlayerMusica.play(musica, true);
	}
	
	public void stopSounds() {
		// TODO Auto-generated method stub
		System.out.println("stopSounds()");
		
		if (midiPlayerMusica != null)
			midiPlayerMusica.stop();
	}

	public void setGameLavel(int lavel) {
		// TODO Auto-generated method stub
		this.gameLavel = lavel;
	}

	public int getGameLavel() {
		return gameLavel;
	}
}
