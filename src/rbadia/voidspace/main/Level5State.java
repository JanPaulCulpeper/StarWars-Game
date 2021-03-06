package rbadia.voidspace.main;


import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.PowerUp;
import rbadia.voidspace.sounds.SoundManager;

public class Level5State extends Level1State {

	public Boss boss = new Boss(SCREEN_WIDTH - 100, SCREEN_HEIGHT/2);
	public PowerUp powerUp = new PowerUp(SCREEN_WIDTH-335,1/SCREEN_HEIGHT+236);//positioned left & right
	private long lastBossBulletTime;
	private ArrayList<Bullet> bossBullets = new ArrayList<Bullet>();
	private int directionY = 1;
	private int directionX = 1;
	private int BOSS_HEALTH = 15;

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
	public void doGettingReady() {
		clearScreen();
		setCurrentState(GETTING_READY);
		getGameLogic().drawGetReady();
		((LevelLogic)getGameLogic()).drawLevel5Intro();
		repaint();
		LevelLogic.delay(15000);
		//Changes music from "menu music" to "maingame music"
		MegaManMain.audioClip.close();
		MegaManMain.bossAudio = new File("audio/bossBattleAudio.wav");
		try {
			MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.bossAudio);
			MegaManMain.audioClip.open(MegaManMain.audioStream);
			MegaManMain.audioClip.start();
			MegaManMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	};

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
		drawXWingLeft();
		drawBullets();
		drawPowerUp();
		drawBigBullets();
		checkBullletXWingCollisions();
		checkLeftBullletXWingCollisions();
		checkBigBulletXWingCollisions();
		checkLeftBigBulletXWingCollisions();
		checkMegaManXWingCollisions();
		checkXWingFloorCollisions();
		drawTheBoss();
		drawBossBullet();
		moveBossY();
		checkBulletToBossCollision();
		checkBigBulletToBossCollition();
		checkBossBulletToPlayerCollision();
		checkPlayerBossCollision();
		checkVaderPowerUpCollision();
		drawBox();


		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getXWingDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));

	}
	public void drawBox() {
		Graphics2D g2d = getGraphics2D();
		int DEFAULT_START = 300;
		int DEFAULT_END = 100;
		g2d.drawRect(DEFAULT_START, 10, DEFAULT_END, 20);
		g2d.setColor(Color.RED);
		if (BOSS_HEALTH == 20) {
			g2d.fillRect(DEFAULT_START, 12, DEFAULT_END, 17);
		}
		else g2d.fillRect(DEFAULT_START + (5*(20-BOSS_HEALTH)), 12, DEFAULT_END-(5*(20-BOSS_HEALTH)), 17);
	}
	@Override
	protected void drawXWingLeft() {
		Graphics2D g2d = getGraphics2D();
		if((xWing.getX() + xWing.getPixelsWide() >  0)) {
			xWing.translate(-xWing.getSpeed(), xWing.getSpeed()/2);
			getGraphicsManager().drawXWingLeft(xWing, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastXWingTime) > NEW_XWING_DELAY){

				xWing.setLocation(SCREEN_WIDTH - xWing.getPixelsWide(),
						rand.nextInt(SCREEN_HEIGHT - xWing.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawXWingExplosion(xWingExplosion, g2d, this);
			}
		}	
	}
	public void checkBulletToBossCollision(){
		for (int i = 0; i < bullets.size(); i++) {

			Bullet bullet = bullets.get(i);
			if(boss.intersects(bullet)) {
				getGameStatus().setXWingsDestroyed(getGameStatus().getXWingDestroyed() + 500);
				//TODO Han hit sound
				BOSS_HEALTH--;
				bullets.remove(i);
			}
		}
	}
	public void checkBigBulletToBossCollition() {
		for (int i = 0; i < bigBullets.size(); i++) {
			BigBullet bigBullet = bigBullets.get(i);
			if(boss.intersects(bigBullet)) {
				getGameStatus().setXWingsDestroyed(getGameStatus().getXWingDestroyed() + 500);
				//TODO Han sound
				bigBullets.remove(i);
				BOSS_HEALTH = BOSS_HEALTH - 2;
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
	protected void checkBigBulletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(xWing.intersects(bigBullet)){
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				damage=0;
			}
		}
	}

	//checks left big bullet asteroid collision
	@Override
	protected void checkLeftBigBulletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet bigBullet = leftBigBullets.get(i);
			if(xWing.intersects(bigBullet)){
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				damage=0;

			}
		}
	}

	@Override
	protected void checkBullletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(xWing.intersects(bullet)){
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}

	//checks left bullet asteroid collision
	@Override
	protected void checkLeftBullletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBullets.size(); i++){
			Bullet bullet = leftBullets.get(i);
			if(xWing.intersects(bullet)){
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
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
			getGraphicsManager().drawBossBullet(bullet, g2d, this);

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
		return BOSS_HEALTH == 0;

	}

	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(SCREEN_WIDTH - i*50 - 100 , SCREEN_HEIGHT/2 - 140 + i*40);
		}
		return platforms;

	}
	protected void checkVaderPowerUpCollision() {
		GameStatus status = getGameStatus();
		if(powerUp.intersects(megaMan)&& PowerUp.isVisibility()){
			status.setLivesLeft(status.getLivesLeft() + 3);
			PowerUp.setVisibility(false);


		}
	}
	protected void drawPowerUp() {
		// TODO Auto-generated method stub
		Graphics2D g2d = getGraphics2D();
		if(powerUp.isVisibility())
			((GraphicsManager)getGraphicsManager()).drawPowerUp3(powerUp, g2d, this);

	}
}
