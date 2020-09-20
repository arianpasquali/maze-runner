package infraestrutura.robots;


import java.awt.Color;

import jogo.GameManager;





public class LeftWallRobot extends WallRobot{

	
	
	public LeftWallRobot(int x, int y){
		super(x,y);
		color = Color.CYAN;
		setName("left wall robot");
	}
	
	
	
	/**
	 * this method returns the first postion to check for a right wall robot
	 * @param direction current direction 
	 * @return new first direction
	 */
	@Override
	protected int getFirstDirection(int direction){
		if(direction == LEFT){
			return DOWN;
		}else{
			return --direction;
		}
	}
	
	/**
	 * this method returns the next postion to check for a right wall robot
	 * @param direction current direction
	 * @return new direction
	 */
	@Override
	protected int getNextDirection(int direction){
			if(direction == DOWN ){
				return LEFT;
			}
			return ++direction;	
	}



	public void robotPositionChanged() {
		// TODO Auto-generated method stub
		
	}
	
}



