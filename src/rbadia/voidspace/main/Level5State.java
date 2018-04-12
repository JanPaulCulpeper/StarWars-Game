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
	private int directionY = 1;
	private int directionX = 1;
	
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
		drawAsteroid();
		drawBullets();
		drawBigBullets();
		checkBullletAsteroidCollisions();
		checkLeftBullletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkLeftBigBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();
		drawTheBoss();
		drawBossBullet();
		moveBossY();
//		moveBossX();
		checkBulletToBossCollision();
		checkBigBulletToBossCollition();
		checkBossBulletToPlayerCollision();
		checkPlayerBossCollision();

			
		
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
	public void checkBulletToBossCollision(){
		for (int i = 0; i < bullets.size(); i++) {
			
			Bullet bullet = bullets.get(i);
			if(boss.intersects(bullet)) {
				getGameStatus().setAsteroidsDestroyed(getGameStatus().getAsteroidsDestroyed() + 500);
				//TODO Han hit sound
				levelAsteroidsDestroyed = levelAsteroidsDestroyed + 1;
				bullets.remove(i);
			}
		}
	}
	public void checkBigBulletToBossCollition() {
		for (int i = 0; i < bigBullets.size(); i++) {
			BigBullet bigBullet = bigBullets.get(i);
			if(boss.intersects(bigBullet)) {
				getGameStatus().setAsteroidsDestroyed(getGameStatus().getAsteroidsDestroyed() + 500);
				//TODO Han sound
				bigBullets.remove(i);
				levelAsteroidsDestroyed = levelAsteroidsDestroyed + 2;
			}
		}
		
	}
	public void checkBossBulletToPlayerCollision() {
		for (int i = 0; i < bossBullets.size(); i++) {
			Bullet bullet = bossBullets.get(i);
			if(megaMan.intersects(bullet)) {
				getGameStatus().setLivesLeft(getGameStatus().getLivesLeft() - 1);
				bossBullets.remove(i);
			}
			
		}
	}
	public void checkPlayerBossCollision() {
		if(boss.intersects(megaMan)) {
			getGameStatus().setLivesLeft(getGameStatus().getLivesLeft() - 1);
		}
	}

	
	
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
		
		//bullet frequancy
		long currentTime = System.currentTimeMillis();
		if((currentTime - lastBossBulletTime) > 1000/2){
			lastBossBulletTime = currentTime;
			fireBossBullet();
		}
	}
	
	
	//move boss
	public void moveBossY(){
		if (boss.getY() + boss.height >= SCREEN_HEIGHT) {
			directionY = -1;
		}
		else if (boss.getY() + 20 <= 0) {
			directionY = 1;
		}
		boss.translate(0, 3*directionY);
			
	}
	
	//hacer que dispare a amos lados para usar este metodo
//	public void moveBossX() {
//		if(boss.getX() + boss.width >= SCREEN_WIDTH) {
//			directionX = -1;
//		}
//		else if(boss.getX() <=0) {
//			directionX = 1;
//		}
//		boss.translate(2*directionX, 0);
//	}
//	
	public void fireBossBullet() {
		
		Bullet bullet = new Bullet(boss.x + boss.width - Bullet.WIDTH/2, boss.y + boss.width/2 - Bullet.HEIGHT + 2);
		bullet.setSpeed(-bullet.getSpeed());
		this.bossBullets.add(bullet);
		//TODO change sound
		this.getSoundManager().playBulletSound();
	}
	
	public boolean moveBossBullet(Bullet bullet) {
		if(bullet.getX() - bullet.getSpeed() <= SCREEN_WIDTH && bullet.getX() > 0) {
			bullet.translate(bullet.getSpeed(), 0);
			return false;
		}
		//remove bullet if out of frame
		return true;
	}
	
	public void drawBossBullet() {
		Graphics2D g2d = getGraphics2D();
		for(int i = 0; i<bossBullets.size(); i++) {
			Bullet bullet = bossBullets.get(i);
			//TODO change image
			getGraphicsManager().drawBullet(bullet, g2d, this);
			
			boolean remove = this.moveBossBullet(bullet);
			if(remove) {
				bossBullets.remove(i);
			i--;
			}
		}
	}
	
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
