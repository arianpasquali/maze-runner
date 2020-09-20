package infraestrutura.robots;

import jogo.GameManager;


abstract public class AbstractThreadRobot extends AbstractRobot implements Runnable{
	
	protected Thread th = null;
	protected int speed = 100;
	protected boolean pause = false;
	protected int dist = 1;
	
	
	
	/** attribut which enables the thread for running */
	private boolean run = false;
	
	public AbstractThreadRobot(Object args) {
		//apenas para fins de validação na hora do teste do classloader
	}
	
	public AbstractThreadRobot(int x, int y) {
		super(x, y);
		this.setName(this.getClass().getSimpleName());
	}

	
	/** starts the robot thread*/
	public void start(){
		run = true;
		th = new Thread(this);
		th.start();
	}
	
	
	/** stops the roboter thread */
	public void stop(){
		run = false;
	}

	/**
	 * this method sets the thread in pause mode
	 * @param state true = Pause / false = resume
	 */
	public void pause(boolean state){
		pause = state;
	}

	
	/** the thread method */
	public void run() {
		while(!targetReached() && run){
			
			if(!pause){
				startTime = System.currentTimeMillis(); 
				if(autoMove()){
					try{
						Thread.sleep(speed);
					}catch(InterruptedException e){}
				}
			}
		}		
	}
	
	public abstract boolean autoMove();


	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}


	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	
}
