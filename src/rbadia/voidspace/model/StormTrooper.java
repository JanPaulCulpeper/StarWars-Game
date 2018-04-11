package rbadia.voidspace.model;

public class StormTrooper extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_SPEED = 1;
	public static final int Y_OFFSET = 5; //distance of trooper from bottom of screen
	public static final int WIDTH = 38;
	public static final int Height = 55;
	
	public StormTrooper(int xPos, int yPos){
		super(xPos, yPos, WIDTH, Height);
		this.setSpeed(DEFAULT_SPEED);
	}
	
	public int getInitialYOffset(){
		return Y_OFFSET;
	}
	
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}
