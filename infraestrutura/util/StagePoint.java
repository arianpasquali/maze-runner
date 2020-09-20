package infraestrutura.util;
import java.awt.Point;


public class StagePoint extends Point {

	protected boolean isValid = true;
	
	public StagePoint (int x, int y){
		super(x,y);
	}
	public StagePoint (){
		super();
	}
	
	public StagePoint (Point p){
		super(p.x, p.y);
	}
	
	/** sets the point to a valid or invalid way point
	 * 
	 * @param b enable/disable 
	 */
	public void setValid(boolean b){
		isValid = b;
	}
	
	public boolean isValid(){
		return isValid;
	}
	
	public boolean equals(Object o){
		if(o!= null){
			Point p = (Point)o;
		
			if(x == p.x && y == p.y){
				return true;
			}
		}	
		return false;
	}
}
