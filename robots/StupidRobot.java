package robots;



import infraestrutura.robots.AbstractThreadRobot;
import infraestrutura.util.StagePoint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import jogo.GameManager;



public class StupidRobot extends AbstractThreadRobot {

	Vector<StagePoint> pontosInvalidos = new Vector<StagePoint>(255);
	
	StagePoint posicaoAnterior = null;
	
	protected Random rd = new Random(System.nanoTime());	
	
	public StupidRobot(int x, int y) {
		super(x, y);
		posicaoAnterior = new StagePoint(GameManager.getInstance().getStartPoing().x,GameManager.getInstance().getStartPoing().x);
		this.color = Color.BLUE;
		this.setName("Stupid Bot");
		this.setAvatar("cinza.jpg");
	}
	
	public void addInvalidField(StagePoint p){
		if(!this.pontosInvalidos.contains(p)){
			this.pontosInvalidos.add(p);
			
		}
	}	
	
	public boolean autoMove(){
		if(isFieldInvalid()){
			addInvalidField(posicaoAtual);	
		}
				
		int x = posicaoAtual.x;
		int y = posicaoAtual.y;	
		
		// get the direction to move
		int route = Math.abs(rd.nextInt(4));
		
		switch(route){
			case 0: y--;break;
			case 1: y++;break;
			case 2: x--;break;
			case 3: x++;break;
		}
		
		if(isValidMove(x,y)){
			
		    if(  x != posicaoAnterior.x || y != posicaoAnterior.y || getWayCount(posicaoAtual.x, posicaoAtual.y) == 1){
					posicaoAnterior = posicaoAtual;
					boolean moved =  super.move(x, y);
					robotChanged();
					return moved;
			}
		}
		return false;
	}
	
	
	public int getWayCount(int x, int y){
	   int ways = 0;
	   	   
	   if(isValidMove(x-1,y)){
		   ways++;
		}
	    if(isValidMove(x,y-1)){
			ways++;	
		}
	    if(isValidMove(x,y+1)){
			ways++;	
		}
	    if(isValidMove(x+1,y) ){
			ways++; 
		}
	   return ways;
	}
	
	
	public boolean isFieldInvalid(){
		if(getWayCount(posicaoAtual.x,posicaoAtual.y)== 1){
			return true;
		}
		return false;
	}
	

	
	
	public void draw(Graphics g,float xRaster,float yRaster){
			
		if(pontosInvalidos != null && pontosInvalidos.size() > 0){
			Color oldColor = g.getColor();

			// draw all fields that marked as oneway
			for(int i = 0; i < pontosInvalidos.size(); i++){

					Point p = calculateDrawCoordinate(pontosInvalidos.get(i), xRaster, yRaster);
					g.fillRect(  Math.round(p.x+ xRaster/2-2),
								 Math.round(p.y+ yRaster/2-2),
							4,4);
			}
		}
		super.draw(g, xRaster, yRaster);
	}
	
	
	
	public boolean isValidMove(int x, int y){
		if(super.isValidMove(x, y) && !pontosInvalidos.contains(new StagePoint(x,y))){
			return true;
		}
		return false;
	}

	public void robotPositionChanged() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
