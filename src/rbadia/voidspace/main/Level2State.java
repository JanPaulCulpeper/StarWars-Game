package rbadia.voidspace.main;
import java.awt.Graphics2D;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Level very similar to LevelState1.  
 * Platforms arranged in triangular form. 
 * Asteroids travel at 225 degree angle
 */
public class Level2State extends Level1State {

	private static final long serialVersionUID = -2094575762243216079L;

	// Constructors
	public Level2State(int level, MainFrame frame, GameStatus status, 
			LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}

	@Override
	public void doStart() {	
		super.doStart();
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
	}

	@Override
	protected void drawXWingRight() {
		Graphics2D g2d = getGraphics2D();
		if((xWing.getX() + xWing.getPixelsWide() >  0)) {
			xWing.translate(-xWing.getSpeed(), xWing.getSpeed()/2);
			getGraphicsManager().drawXWingLeft(xWing, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){

				xWing.setLocation(SCREEN_WIDTH - xWing.getPixelsWide(),
						rand.nextInt(SCREEN_HEIGHT - xWing.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawXWingExplosion(xWingExplosion, g2d, this);
			}
		}	
	}

	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<4)	platforms[i].setLocation(50+ i*50, SCREEN_HEIGHT/2 + 140 - i*40);
			if(i==4) platforms[i].setLocation(50 +i*50, SCREEN_HEIGHT/2 + 140 - 3*40);
			if(i>4){	
				int k=4;
				platforms[i].setLocation(50 + i*50, SCREEN_HEIGHT/2 + 20 + (i-k)*40 );
				k=k+2;
			}
		}
		return platforms;
	}
}
