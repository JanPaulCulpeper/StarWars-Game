package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.XWing;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.PowerUp;
import rbadia.voidspace.model.RebelTrooper;
import rbadia.voidspace.sounds.SoundManager;

public class Level3State extends Level1State {
	private static final long serialVersionUID = 6330305833847871298L;

	public XWing xWing2 = new XWing(0,0);
	public PowerUp powerUp = new PowerUp(1/SCREEN_WIDTH+450,1/SCREEN_HEIGHT+32);//positioned left, looking right
	private boolean plat = true;



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
	public void doGettingReady() {
		clearScreen();
		setCurrentState(GETTING_READY);
		getGameLogic().drawGetReady();
		((LevelLogic)getGameLogic()).drawLevel3Intro();
		repaint();
		LevelLogic.delay(5000);
		//Changes music from "menu music" to "ingame music"
		MegaManMain.audioClip.close();
		MegaManMain.audioFile = new File("audio/mainGame.wav");
		try {
			MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.audioFile);
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
		drawXWingLeft();
		drawXWingRight();
		drawPowerUp();
		drawBullets();
		drawBigBullets();
		checkBullletXWingCollisions();
		checkLeftBullletXWingCollisions();
		checkBullletXWingCollisions2();
		checkLeftBullletXWingCollisions2();
		checkBigBulletXWingCollisions();
		checkBigBulletXWingCollisions2();
		checkLeftBigBulletXWingCollisions2();
		checkMegaManXWingCollisions();
		checkVaderXWingCollisions2();
		checkXWingFloorCollisions();
		checkXWingFloorCollisions2();
		checkVaderPowerUpCollision();

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getXWingDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));
	}

	protected void checkXWingFloorCollisions2() {
		for(int i=0; i<9; i++){
			if(xWing.intersects(floor[i])){
				removeXWing(xWing);

			}
		}
	}
	protected void checkVaderPowerUpCollision() {
		GameStatus status = getGameStatus();
		if(powerUp.intersects(megaMan)&& PowerUp.isVisibility()){
			status.setLivesLeft(status.getLivesLeft() + 5);
			PowerUp.setVisibility(false);
			//			removePowerUp(powerUp);

		}
	}

	protected void checkVaderXWingCollisions2() {
		GameStatus status = getGameStatus();
		if(xWing.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeXWing(xWing);
		}
	}

	//right big bullet colision with XWing 2
	protected void checkBigBulletXWingCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(xWing.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				levelXWingDestroyed++;
				damage=0;
			}
		}
	}

	//left big bullet bullet colision with XWing 2
	protected void checkLeftBigBulletXWingCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet bigBullet = leftBigBullets.get(i);
			if(xWing.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				levelXWingDestroyed++;
				damage=0;
			}
		}
	}
	//check right bullet XWing 2 collision
	protected void checkBullletXWingCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(xWing.intersects(bullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				levelXWingDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}
	//left bullet with XWing 2
	protected void checkLeftBullletXWingCollisions2() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBullets.size(); i++){
			Bullet bullet = leftBullets.get(i);
			if(xWing.intersects(bullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				levelXWingDestroyed++;
				damage=0;
				// remove bullet
				leftBullets.remove(i);
				break;
			}
		}
	}

	//Asteroid Left 
	protected void drawXWingLeft() {
		Graphics2D g2d = getGraphics2D();
		if((xWing2.getX() + xWing2.getPixelsWide() < SCREEN_WIDTH)) {
			xWing2.translate(xWing2.getSpeed(), xWing2.getSpeed()/2);
			getGraphicsManager().drawXWingRight(xWing2, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastXWingTime) > NEW_XWING_DELAY){

				xWing2.setLocation(-xWing2.getPixelsWide(),
						rand.nextInt(SCREEN_HEIGHT - xWing2.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawXWingExplosion(xWingExplosion, g2d , this);
			}
		}	
	}

	//xWIng Right
	protected void drawXWingRight() {
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
				getGraphicsManager().drawXWingExplosion(xWingExplosion, g2d , this);
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

	public XWing newXWingRight(Level1State screen){
		int xPos = (int) (screen.getWidth() - XWing.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - XWing.HEIGHT- 32));
		xWing = new XWing(xPos, yPos);
		return xWing;
	}
	protected void drawPowerUp() {
		// TODO Auto-generated method stub
		Graphics2D g2d = getGraphics2D();
		if(powerUp.isVisibility())
			((GraphicsManager)getGraphicsManager()).drawPowerUp(powerUp, g2d, this);

	}

	public XWing newXWing2(Level1State screen){
		int xPos = (int) (screen.getWidth() - XWing.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - XWing.HEIGHT- 32));
		xWing2 = new XWing(xPos, yPos);
		return xWing2;
	}
	
	@Override
	public boolean isLevelWon() {
		if(getInputHandler().isNPressed()) return true; 
		return levelXWingDestroyed >= 5;
		
	}
}
