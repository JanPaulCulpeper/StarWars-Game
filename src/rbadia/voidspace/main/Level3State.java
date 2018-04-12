package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.util.Random;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.sounds.SoundManager;

public class Level3State extends Level1State {
	private static final long serialVersionUID = 6330305833847871298L;
	
	public Asteroid asteroid2 = new Asteroid(0,0);
	
	//Constructor
	public Level3State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
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
	public void updateScreen(){
		Graphics2D g2d = getGraphics2D();
		GameStatus status = this.getGameStatus();

		// save original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}
		clearScreen();
		((GraphicsManager) getGraphicsManager()).drawDeathStar(g2d);
		drawStars(50);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		drawAsteroid();
		drawNewAsteroid();
		drawBullets();
		drawBigBullets();
		checkBullletAsteroidCollisions();
		checkLeftBullletAsteroidCollisions();
		checkBullletAsteroidCollisions2();
		checkLeftBullletAsteroidCollisions2();
		checkBigBulletAsteroidCollisions();
		checkBigBulletAsteroidCollisions2();
		checkLeftBigBulletAsteroidCollisions2();
		checkMegaManAsteroidCollisions();
		checkMegaManAsteroidCollisions2();
		checkAsteroidFloorCollisions();
		checkAsteroidFloorCollisions2();
		
		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));
	}

	protected void checkAsteroidFloorCollisions2() {
		for(int i=0; i<9; i++){
			if(asteroid2.intersects(floor[i])){
				removeAsteroid(asteroid2);

			}
		}
	}
	
	protected void checkMegaManAsteroidCollisions2() {
		GameStatus status = getGameStatus();
		if(asteroid2.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroid(asteroid2);
		}
	}
	
	//right big bullet colision with asteroid 2
	protected void checkBigBulletAsteroidCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(asteroid2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				levelAsteroidsDestroyed++;
				damage=0;
			}
		}
	}
	
	//left big bullet bullet colision with asteroid 2
	protected void checkLeftBigBulletAsteroidCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet bigBullet = leftBigBullets.get(i);
			if(asteroid2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				levelAsteroidsDestroyed++;
				damage=0;
			}
		}
	}
	//check right bullet asteroid 2 collision
	protected void checkBullletAsteroidCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}
	//left bullet with asteroid 2
	protected void checkLeftBullletAsteroidCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBullets.size(); i++){
			Bullet bullet = leftBullets.get(i);
			if(asteroid2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				leftBullets.remove(i);
				break;
			}
		}
	}
	
	//Asteroid Left 
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		if((asteroid.getX() + asteroid.getPixelsWide() < SCREEN_WIDTH)) {
			asteroid.translate(asteroid.getSpeed(), asteroid.getSpeed()/2);
			getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){

				asteroid.setLocation(-asteroid.getPixelsWide(),
						rand.nextInt(SCREEN_HEIGHT - asteroid.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}	
	}
	
	//Asteroid Right
	protected void drawNewAsteroid() {
		Graphics2D g2d = getGraphics2D();
		if((asteroid2.getX() + asteroid2.getPixelsWide() >  0)) {
			asteroid2.translate(-asteroid2.getSpeed(), asteroid2.getSpeed()/2);
			getGraphicsManager().drawAsteroid(asteroid2, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				asteroid2.setLocation(SCREEN_WIDTH - asteroid2.getPixelsWide(),
					rand.nextInt(SCREEN_HEIGHT - asteroid2.getPixelsTall() - 32));
				}	
		else {
			// draw explosion
			getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
		}
	}	
}	
	private int direction = 1;
	Random rand = new Random();
	protected void drawPlatforms() {
		//draw platforms
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<getNumPlatforms(); i++){
			getGraphicsManager().drawPlatform(platforms[i], g2d, this, i);
			
			if(platforms[i].getX() + platforms[i].width >= SCREEN_WIDTH) {
				direction = -1;
			}
			else if(platforms[i].getX() <= 0) {
				direction = 1;
			}
			platforms[i].translate(direction*rand.nextInt(5), 0);
		}
	}
	

	

	
	public Asteroid newAsteroid2(Level1State screen){
		int xPos = (int) (screen.getWidth() - Asteroid.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - Asteroid.HEIGHT- 32));
		asteroid2 = new Asteroid(xPos, yPos);
		return asteroid2;
	}
	
	
	@Override
	public boolean isLevelWon() {
		if(getInputHandler().isNPressed()) return true; 
		return levelAsteroidsDestroyed >= 5;
		
	}
}
