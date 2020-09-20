package robots;




import infraestrutura.util.StagePoint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

import jogo.GameManager;




public class TreeRobot extends StupidRobot{

	Node root = null;
	Node curNode = null;
	Vector<Node> nodes = new Vector<Node>(255);
	
	/**
	 * standard constructor
	 * @param x Start x postion 
	 * @param y Start y postion
	 * @param laby a valid labirinto
	 * @param dist a distance for field marking
	 */
	public TreeRobot(int x, int y){
		super(x,y);
		color = Color.ORANGE;
		root = new Node(GameManager.getInstance().getStartPoing(),null);
		curNode = new Node(posicaoAtual,root);
		speed = 100;
		setName(this.getClass().getSimpleName());
		setAvatar(this.getClass().getSimpleName());
	}
	
	@Override
	public void showTargetReachedDiaolog(){
		
		for(int i = 0; i < nodes.size(); i++){
			if(!nodes.elementAt(i).isValid){ 
				nodes.remove(i);
				i--;
			}
		}
		robotChanged();
		super.showTargetReachedDiaolog();
	}
	
	
	public boolean autoMove(){
		
		if(curNode != null ){
					
			int x = curNode.x;
			int y = curNode.y;
						
			if(!nodes.contains(curNode)){
				nodes.add(curNode);
			}
			
			if(curNode.children.size() == 0){ 
				 if(isValidMove(x-1,y) ){
					 Node n = new Node(x-1,y,curNode);
					 if(!n.equals(posicaoAnterior)){
						 curNode.addChild(n);
					 }
				 }
				 if(isValidMove(x,y-1)){
					 Node n = new Node(x,y-1,curNode);
					 if(!n.equals(posicaoAnterior)){
						 curNode.addChild(n);
					 }	
				 }
				    if(isValidMove(x,y+1)){
			    	 Node n = new Node(x,y+1,curNode);
					 if(!n.equals(posicaoAnterior)){
						 curNode.addChild(n);
					 }		
				 }
				 if(isValidMove(x+1,y) ){
					 Node n = new Node(x+1,y,curNode);
					 if(!n.equals(posicaoAnterior)){
						 curNode.addChild(n);
					 } 
				 }
			}		
			
			Node n = curNode.getNextValidChild();
			if(n != null){
				posicaoAnterior = curNode;
				curNode = n;
				
			}else{
			    curNode.isValid = false;
			    curNode      = curNode.parent;
			    posicaoAnterior = curNode.parent;
			}
			robotChanged();
			this.posicaoAtual = curNode;
			return true;
		}

		return false;
	}

	
	public void keyPressed(KeyEvent e){
				
		if(speed > 0 ){
			if(e.getKeyCode()== KeyEvent.VK_UP){speed--;}
			else if(e.getKeyCode()== KeyEvent.VK_DOWN){  speed++;}
			else if(e.getKeyCode()== KeyEvent.VK_LEFT){  speed++;}
			else if(e.getKeyCode()== KeyEvent.VK_RIGHT){ speed--;}
		}
	}
	
	public void draw(Graphics g,float xRaster,float yRaster){
		
		// draw position
		Color oldColor = g.getColor();
		//g.setColor(color);
		super.draw(g, xRaster, yRaster);
		
		// draw visited fields
		if(nodes != null){
				
			for(int i = 0; i < nodes.size(); i++){
					Node p = (Node)nodes.get(i); 
						
					if(p.isValid) { g.setColor(Node.validColor);}
					else g.setColor(Node.invalidColor);
					
					Point pc = calculateDrawCoordinate(p, xRaster, yRaster);
									
					g.drawRect( pc.x+dist , pc.y+dist, Math.round(xRaster-(2*dist)), Math.round(yRaster-(2*dist)));			
			}
			
		}
		//g.setColor(oldColor);
	
	}
}


class Node extends StagePoint implements Comparable{
	
	static final long serialVersionUID = 1;
	
	protected ArrayList<Node> children = new ArrayList<Node>();
	protected Point position = null;
	protected boolean isValid = true;
	protected int curChildNumber = 0;
	Node parent = null;
	
	public static final Color validColor = Color.GREEN;
	public static final Color invalidColor = Color.RED;
	
	public Node(int x ,int y,Node parent){
		super(x,y);
		this.parent = parent;
	}
	public Node(StagePoint p,Node parent){
		this(p.x,p.y,parent);
	}
	
	public void addChild(Node n){
		children.add(n);
	}
		
	public Node getChild(int index){
		if(index < 0 || index >= children.size()){
			return null;
		}
		else return (Node) children.get(index);
	}

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		
		Node n= (Node)arg0;
		if(n.position.x == this.position.x &&
		   n.position.y == this.position.y){
			return 0;
		}		
		return -1;
	}	
	
	public Node getNextValidChild(){
		for(int i = curChildNumber ; i < children.size() ; i++){
			Node n = children.get(i);
			if(n.isValid){
				curChildNumber = i+1;
				return n;
			}
		}
		return null;
	}
	
	
	
}


