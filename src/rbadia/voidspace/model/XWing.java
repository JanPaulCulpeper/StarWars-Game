package rbadia.voidspace.model;

public class XWing extends GameObject {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_SPEED = 4;

	public static final int WIDTH = 72;
	public static final int HEIGHT = 54;

	public XWing(int xPos, int yPos) {
		super(xPos, yPos, XWing.WIDTH, XWing.HEIGHT);
		this.setSpeed(DEFAULT_SPEED);
	}

	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}
