package rbadia.voidspace.model;

public class Boss extends GameObject {
	
	public static final int DEFAULT_SPEED = 3;
	public static final int Y_OFFSET = 200;
	public static final int WIDTH = 40;
	public static final int Height = 60;
	
	public Boss(int xPos, int yPos){
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
