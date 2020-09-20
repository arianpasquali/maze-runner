package infraestrutura.robots;


import java.awt.Color;

import jogo.GameManager;





public class RightWallRobot extends WallRobot{

	
	
	public RightWallRobot(int x, int y){
		super(x,y);
		color = Color.YELLOW;
		setName(this.getClass().getSimpleName());
		
	}
	
	/**
	 * this method returns the first postion to check for a right wall robot
	 * @param direction current direction 
	 * @return new first direction
	 */
	protected int getFirstDirection(int direction){
		if(direction == DOWN){
			return LEFT;
		}else{
			return ++direction;
		}
	}
	
	/**
	 * this method returns the next postion to check for a right wall robot
	 * @param direction current direction
	 * @return new direction
	 */
	protected int getNextDirection(int direction){
			if(direction == LEFT ){
				return DOWN;
			}
			return --direction;	
	}

	public void robotPositionChanged() {
		// TODO Auto-generated method stub
		
	}
}



