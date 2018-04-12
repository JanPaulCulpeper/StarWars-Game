package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.util.ArrayList;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level5State extends Level1State {
	
	public Boss boss = new Boss(SCREEN_WIDTH - 100, SCREEN_HEIGHT/2);
	private long lastBossBulletTime;
	private ArrayList<Bullet> bossBullets = new ArrayList<Bullet>();
	private int direction = 1;
	
	public Boss getBoss(){
		return boss;
	}

	public Level5State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6258208923988886788L;
	
	@Override
	public void doStart() {	
		super.doStart();
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
	}
	
	@Override
	public void updateScreen() {
		Graphics2D g2d = getGraphics2D();
		GameStatus status = this.getGameStatus();

		// save original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}
		
		clearScreen();
		((GraphicsManager) getGraphicsManager()).drawDeathStar(g2d);
		//TODO new backround
		drawStars(50);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		drawTheBoss();
		drawAsteroid();
		drawBullets();
		drawBigBullets();
		checkBullletAsteroidCollisions();
		checkLeftBullletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkLeftBigBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();
		moveBossUp();
		//TODO rememebr to add all collision and draw methods
		
		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));

	}
	
	@Override
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		if((asteroid.getX() + asteroid.getPixelsWide() >  0)) {
			asteroid.translate(-asteroid.getSpeed(), asteroid.getSpeed()/2);
			getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){

				asteroid.setLocation(SCREEN_WIDTH - asteroid.getPixelsWide(),
						rand.nextInt(SCREEN_HEIGHT - asteroid.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}	
	}
	
	public void checkBulletBossCollision(){
		//TODO
		//suma el levelAsteroidDestroyed
	}
	//TODO collision de player bullets con boss
	//TODO collision de boss bullets con player
	
	
	//checks right big bullet asteroid collision
	@Override
	protected void checkBigBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(asteroid.intersects(bigBullet)){
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				damage=0;
			}
		}
	}
	
	//checks left big bullet asteroid collision
	@Override
	protected void checkLeftBigBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet bigBullet = leftBigBullets.get(i);
			if(asteroid.intersects(bigBullet)){
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				damage=0;
				
			}
		}
	}
	
	@Override
	protected void checkBullletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid.intersects(bullet)){
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}
	
	//checks left bullet asteroid collision
	@Override
	protected void checkLeftBullletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBullets.size(); i++){
			Bullet bullet = leftBullets.get(i);
			if(asteroid.intersects(bullet)){
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				damage=0;
				// remove bullet
				leftBullets.remove(i);
				break;
			}
		}
	}
	
	//draw boss
	protected void drawTheBoss(){
		Graphics2D g2d = getGraphics2D();
		((GraphicsManager) getGraphicsManager()).drawBoss(boss, g2d, this);
		
	}
	
	
	//move
	public void moveBossUp(){
		if (boss.getY() + boss.height >= SCREEN_HEIGHT) {
			direction = -1;
		}
		else if (boss.getY() <= 0) {
			direction = 1;
		}
		boss.translate(0, 3*direction);
			
	}
	//TODO
	//fire boss bullet
	//draw boss bullet
	
	@Override
	public boolean isLevelWon() {
		if(getInputHandler().isNPressed()) return true; 
		return levelAsteroidsDestroyed >= 20;
		
	}
	
	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(SCREEN_WIDTH - i*50 - 100 , SCREEN_HEIGHT/2 - 140 + i*40);
		}
		return platforms;

	}

}
