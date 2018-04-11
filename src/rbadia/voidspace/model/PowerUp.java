package rbadia.voidspace.model;

import java.awt.Frame;
import java.awt.Graphics2D;

public class PowerUp extends GameObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_SPEED = 1;
	public static final int Y_OFFSET = 5;
	public static final int WIDTH = 10;
	public static final int Height = 10;
	public static boolean visibility = true;
	
	public PowerUp(int xPos, int yPos){
		super(xPos, yPos, PowerUp.WIDTH, PowerUp.Height);
		this.setSpeed(DEFAULT_SPEED);
	}
	
	
	public static boolean isVisibility() {
		
		return visibility;
		
	}


	public static void setVisibility(boolean b) {
		visibility=b;
	}


	public int getInitialYOffset(){
		return Y_OFFSET;
	}
	
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}
	

