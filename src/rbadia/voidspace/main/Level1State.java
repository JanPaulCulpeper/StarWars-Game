package rbadia.voidspace.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.PowerUp;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
public class Level1State extends LevelState {

	private static final long serialVersionUID = 1L;
	//protected GraphicsManager graphicsManager;
	protected BufferedImage backBuffer;
	protected MegaMan megaMan;
	protected XWing xWing;
	protected List<Bullet> bullets;
	protected List<Bullet> leftBullets;
	protected List<BigBullet> bigBullets;
	protected List<BigBullet> leftBigBullets;
	protected Floor[] floor;	
	protected int numPlatforms=8;
	protected Platform[] platforms;
	protected PowerUp powerUp;

	protected int damage=0;
	protected static final int NEW_MEGAMAN_DELAY = 500;
	protected static final int NEW_XWING_DELAY = 500;

	protected long lastXWingTime;
	protected long lastLifeTime;

	protected Rectangle xWingExplosion;

	protected Random rand;

	protected Font originalFont;
	protected Font bigFont;
	protected Font biggestFont;



	protected int levelXWingDestroyed = 0;

	// Constructors
	public Level1State(int level, MainFrame frame, GameStatus status, 
			LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super();
		this.setSize(new Dimension((int)(frame.getWidth()*0.9), (int)(frame.getWidth()*0.81)));
		this.setPreferredSize(new Dimension((int)(frame.getWidth()*0.9), (int)(frame.getWidth()*0.81)));
		this.setBackground(Color.BLACK);
		this.setLevel(level);
		this.setMainFrame(frame);
		this.setGameStatus(status);
		this.setGameLogic(gameLogic);
		this.setInputHandler(inputHandler);
		this.setSoundManager(soundMan);
		this.setGraphicsManager(graphicsMan);
		backBuffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setGraphics2D(backBuffer.createGraphics());
		rand = new Random();
	}

	// Getters
	public MegaMan getMegaMan() 				{ return megaMan; 		}
	public Floor[] getFloor()					{ return floor; 		}
	public int getNumPlatforms()				{ return numPlatforms; 	}
	public Platform[] getPlatforms()			{ return platforms; 	}
	public XWing getXWing() 					{ return xWing; 		}
	public List<Bullet> getBullets() 			{ return bullets; 		}
	public List<Bullet> getLeftBullets()		{return leftBullets;	}
	public List<BigBullet> getBigBullets()		{ return bigBullets;   	}
	public List<BigBullet> getLeftBigBullets()	{return leftBigBullets;	}
	public PowerUp getPowerUp() 				{ return powerUp;		}



	// Level state methods
	// The method associated with the current level state will be called 
	// repeatedly during each LevelLoop iteration until the next a state 
	// transition occurs
	// To understand when each is invoked see LevelLogic.stateTransition() & LevelLoop class

	@Override
	public void doStart() {	

		setStartState(START_STATE);
		setCurrentState(getStartState());
		// init game variables
		bullets = new ArrayList<Bullet>();
		leftBullets = new ArrayList<Bullet>();
		bigBullets = new ArrayList<BigBullet>();
		leftBigBullets = new ArrayList<BigBullet>();


		GameStatus status = this.getGameStatus();

		status.setGameOver(false);
		status.setNewXWing(false);

		// init the life and the xWing
		newMegaMan();
		newFloor(this, 9);
		newPlatforms(getNumPlatforms());
		newXWing(this);
		lastXWingTime = -NEW_XWING_DELAY;
		lastLifeTime = -NEW_MEGAMAN_DELAY;

		bigFont = originalFont;
		biggestFont = null;

		// Display initial values for scores
		getMainFrame().getDestroyedValueLabel().setForeground(Color.BLACK);
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getXWingDestroyed()));
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));

	}

	@Override
	public void doInitialScreen() {
		Graphics2D g2d = getGraphics2D();
		setCurrentState(INITIAL_SCREEN);
		clearScreen();
		((GraphicsManager)getGraphicsManager()).drawMenuPicture(g2d);
		getGameLogic().drawInitialMessage();

	};

	@Override
	public void doGettingReady() {
		clearScreen();
		setCurrentState(GETTING_READY);
		getGameLogic().drawGetReady();
		((LevelLogic)getGameLogic()).drawLevel1Intro();
		repaint();
		LevelLogic.delay(15000);
		//Changes music from "menu music" to "maingame music"
		MegaManMain.audioClip.close();
		MegaManMain.playingAudio = new File("audio/Star-Wars-The-Imperial-March-_Darth-Vader_s-Theme_.wav");
		try {
			MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.playingAudio);
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
	public void doPlaying() {
		setCurrentState(PLAYING);
		updateScreen();
	};

	@Override
	public void doNewMegaman() {
		setCurrentState(NEW_MEGAMAN);
	};

	@Override
	public void doLevelWon(){
		MegaManMain.audioClip.stop();
		setCurrentState(LEVEL_WON);
		getGameLogic().drawYouWin();
		repaint();
		LevelLogic.delay(5000);
	}

	@Override
	public void doGameOverScreen(){
		//		MegaManMain.audioClip.stop();
		setCurrentState(GAME_OVER_SCREEN);
		getGameLogic().drawGameOver();
		getMainFrame().getDestroyedValueLabel().setForeground(new Color(128, 0, 0));
		repaint();
		LevelLogic.delay(1500);

	}

	@Override
	public void doGameOver(){
		this.getGameStatus().setGameOver(true);

	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(getSize().getWidth()/SCREEN_WIDTH,getSize().getHeight()/SCREEN_HEIGHT);
		g2.drawImage(backBuffer, 0, 0,  this);
	}

	/**
	 * Update the game screen's backbuffer image.
	 */
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
		drawBullets();
		drawBigBullets();
		checkBullletXWingCollisions();
		checkLeftBullletXWingCollisions();
		checkBigBulletXWingCollisions();
		checkLeftBigBulletXWingCollisions();
		checkMegaManXWingCollisions();
		checkXWingFloorCollisions();



		// update xWings destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getXWingDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));
	}



	protected void checkXWingFloorCollisions() {
		for(int i=0; i<9; i++){
			if(xWing.intersects(floor[i])){
				removeXWing(xWing);

			}
		}
	}

	protected void checkMegaManXWingCollisions() {
		GameStatus status = getGameStatus();
		if(xWing.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeXWing(xWing);
		}
	}

	//checks right big bullet xWing collision
	protected void checkBigBulletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(xWing.intersects(bigBullet)){
				// increase xWings destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				levelXWingDestroyed++;
				damage=0;
			}
		}
	}

	//checks left big bullet xWing collision
	protected void checkLeftBigBulletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet bigBullet = leftBigBullets.get(i);
			if(xWing.intersects(bigBullet)){
				// increase xWings destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
				levelXWingDestroyed++;
				damage=0;

			}
		}
	}

	//checks right bullet xWing collision
	protected void checkBullletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(xWing.intersects(bullet)){
				// increase xWings destroyed count
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
	//checks left bullet xWing collision
	protected void checkLeftBullletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBullets.size(); i++){
			Bullet bullet = leftBullets.get(i);
			if(xWing.intersects(bullet)){
				// increase xWings destroyed count
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

	protected void drawBigBullets() {
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			getGraphicsManager().drawBigBullet(bigBullet, g2d, this);

			boolean remove = this.moveBigBullet(bigBullet, bigBullet);
			if(remove){
				bigBullets.remove(i);
				i--;
			}
		}
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet leftbigBullet = leftBigBullets.get(i);
			getGraphicsManager().drawBigBullet(leftbigBullet, g2d, this);

			boolean remove = this.moveBigBullet(leftbigBullet, leftbigBullet);
			if(remove){
				leftBigBullets.remove(i);
				i--;
			}
		}


	}

	protected void drawBullets() {
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			getGraphicsManager().drawBullet(bullet, g2d, this);

			boolean remove =   this.moveBullet(bullet, bullet);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		//draws bullets to left
		for(int i=0; i<leftBullets.size(); i++){
			Bullet leftBullet = leftBullets.get(i); 
			getGraphicsManager().drawBullet(leftBullet, g2d, this);

			boolean remove = this.moveBullet(leftBullet, leftBullet);
			if(remove){
				leftBullets.remove(i);
				i--;
			}
		}
	}

	protected void drawXWingLeft() {
		Graphics2D g2d = getGraphics2D();
		GameStatus status = getGameStatus();
		if((xWing.getX() + xWing.getWidth() >  0)){
			xWing.translate(-xWing.getSpeed(), 0);
			getGraphicsManager().drawXWingLeft(xWing, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastXWingTime) > NEW_XWING_DELAY){
				// draw a new xWing
				lastXWingTime = currentTime;
				status.setNewXWing(false);
				xWing.setLocation((int) (SCREEN_WIDTH - xWing.getPixelsWide()),
						(rand.nextInt((int) (SCREEN_HEIGHT - xWing.getPixelsTall() - 32))));
			}

			else{
				// draw explosion
				getGraphicsManager().drawXWingExplosion(xWingExplosion, g2d, this);
			}
		}
	}


	protected void drawMegaMan() {

		Graphics2D g2d = getGraphics2D();

		//Fall 
		if((Gravity() == true) || ((Gravity() == true) && (Fire() == true || Fire2() == true)) 
				&& (getInputHandler().isLeftPressed() == false && getInputHandler().wasLeftReleased()==false))
		{
			getGraphicsManager().drawMegaFallR(megaMan, g2d, this);
		}		
		//Draw Megaman
		if((Gravity()==false) && ((Fire()==false) && (Fire2()==false)) && (getInputHandler().isStill()) && ((getInputHandler().wasRightReleased())
				|| !(getInputHandler().wasLeftReleased())))
		{
			getGraphicsManager().drawMegaMan(megaMan, g2d, this);
		}
		//Megaman Fire Right
		if((Fire() == true || Fire2() == true) && (Gravity() == false) &&(FireLeft() == false) && (LeftFire2() == false) && ((getInputHandler().wasLeftReleased() == false) && getInputHandler().isLeftPressed() == false))
		{
			((GraphicsManager) getGraphicsManager()).drawMegaFireR(megaMan, g2d, this);
		}	
		//Megaman run right
		if((Gravity()==false) && (Fire()==false) && (Fire2()==false) && (FireLeft() == false) && (LeftFire2() == false) && getInputHandler().isRightPressed()) 
		{
			((GraphicsManager)getGraphicsManager()).drawMegaRunR(megaMan, g2d, this);
		}
		//Megaman fire left
		if((FireLeft() == true || LeftFire2() == true ) && (Gravity() == false) && (getInputHandler().wasLeftReleased() || getInputHandler().isLeftPressed()))
		{
			((GraphicsManager)getGraphicsManager()).drawMegaFireLeft(megaMan, g2d, this);
		}
		else {
			//Megaman look left
			if((Gravity()==false) && (Fire()==false) && (Fire2()==false) && (getInputHandler().isStill()) && (getInputHandler().wasLeftReleased()))
			{
				((GraphicsManager) getGraphicsManager()).drawMegaLookLeft(megaMan, g2d, this);
			}
			//Megaman run left
			if((Gravity()==false) && (Fire()==false) && (Fire2()==false) && getInputHandler().isLeftPressed()) 
			{
				((GraphicsManager)getGraphicsManager()).drawMegaRunL(megaMan, g2d, this);
			}

		}
	}

	protected void drawPlatforms() {
		//draw platforms
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<getNumPlatforms(); i++){
			getGraphicsManager().drawPlatform(platforms[i], g2d, this, i);
		}
	}

	protected void drawFloor() {
		//draw Floor
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<9; i++){
			getGraphicsManager().drawFloor(floor[i], g2d, this, i);	
		}
	}

	protected void clearScreen() {
		// clear screen
		Graphics2D g2d = getGraphics2D();
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
	}

	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	protected void drawStars(int numberOfStars) {
		Graphics2D g2d = getGraphics2D();
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	@Override
	public boolean isLevelWon() {
		if(getInputHandler().isNPressed()) return true; 
		return levelXWingDestroyed >= 3;

	}

	protected boolean Gravity(){
		MegaMan megaMan = this.getMegaMan();
		Floor[] floor = this.getFloor();

		for(int i=0; i<9; i++){
			if((megaMan.getY() + megaMan.getHeight() -17 < SCREEN_HEIGHT - floor[i].getHeight()/2) 
					&& Fall() == true){

				megaMan.translate(0 , 2);
				return true;

			}
		}
		return false;
	}

	//Bullet fire pose
	protected boolean Fire(){
		MegaMan megaMan = this.getMegaMan();
		List<Bullet> bullets = this.getBullets();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if((bullet.getX() > megaMan.getX() + megaMan.getWidth()) && 
					(bullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60)){
				return true;
			}
		}
		return false;
	}
	//megaman fires left
	protected boolean FireLeft() {
		MegaMan megaman = this.getMegaMan();
		List<Bullet> leftBullets = this.getLeftBullets();
		for(int i = 0 ; i < leftBullets.size() ; i++) {
			Bullet leftBullet = leftBullets.get(i);
			if((leftBullet.getX() <= megaman.getX() + megaman.getWidth() + 60) && (leftBullet.getX() >= megaman.getX() - 60)) {
				return true;
			}
		}
		return false;
	}

	//BigBullet fire pose
	protected boolean Fire2(){
		MegaMan megaMan = this.getMegaMan();
		List<BigBullet> bigBullets = this.getBigBullets();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if((bigBullet.getX() > megaMan.getX() + megaMan.getWidth()) && 
					(bigBullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//Left BigBullet fire pose
	protected boolean LeftFire2(){
		MegaMan megaMan = this.getMegaMan();
		List<BigBullet> leftbigBullets = this.getLeftBigBullets();
		for(int i=0; i<leftbigBullets.size(); i++){
			BigBullet leftbigBullet = leftbigBullets.get(i);
			if((leftbigBullet.getX() > megaMan.getX() + megaMan.getWidth()) && 
					(leftbigBullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//Platform Gravity
	public boolean Fall(){
		MegaMan megaMan = this.getMegaMan(); 
		Platform[] platforms = this.getPlatforms();
		for(int i=0; i<getNumPlatforms(); i++){
			if((((platforms[i].getX() < megaMan.getX()) && (megaMan.getX()< platforms[i].getX() + platforms[i].getWidth()))
					|| ((platforms[i].getX() < megaMan.getX() + megaMan.getWidth()) 
							&& (megaMan.getX() + megaMan.getWidth()< platforms[i].getX() + platforms[i].getWidth())))
					&& megaMan.getY() + megaMan.getHeight() == platforms[i].getY()
					){
				return false;
			}
		}
		return true;
	}

	public void removeXWing(XWing xwing){
		// "remove" xWing
		xWingExplosion = new Rectangle(
				xwing.x,
				xwing.y,
				xwing.getPixelsWide(),
				xwing.getPixelsTall());
		xwing.setLocation(-xwing.getPixelsWide(), -xwing.getPixelsTall());
		this.getGameStatus().setNewXWing(true);
		lastXWingTime = System.currentTimeMillis();
		// play xWing explosion sound
		this.getSoundManager().playAsteroidExplosionSound();
	}

	/**
	 * Fire a bullet from life.
	 */
	public void fireBullet(){
		if(getInputHandler().isLeftPressed() == false && getInputHandler().wasLeftReleased() == false) {
			Bullet bullet = new Bullet(megaMan.x + megaMan.width - Bullet.WIDTH/2,
					megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
			bullets.add(bullet);
			this.getSoundManager().playBulletSound();
		}

		else {
			int xPos = megaMan.x;
			int yPos = megaMan.y + megaMan.width/2 - Bullet.HEIGHT + 2;
			Bullet leftBullet = new Bullet(xPos, yPos);
			leftBullet.setSpeed(-12);
			leftBullets.add(leftBullet);
			this.getSoundManager().playBulletSound();

		}
	}

	/**
	 * Fire the "Power Shot" bullet
	 */
	public void fireBigBullet(){
		if(getInputHandler().isLeftPressed() == false && getInputHandler().wasLeftReleased() == false) {
			BigBullet bigBullet = new BigBullet(megaMan.x + megaMan.width - BigBullet.WIDTH/2,
					megaMan.y + megaMan.width/2 - BigBullet.HEIGHT +2);
			bigBullets.add(bigBullet);
			this.getSoundManager().playBulletSound();
		}
		else {
			int xPos = megaMan.x;
			int yPos = megaMan.y + megaMan.width/2 - BigBullet.HEIGHT + 4;
			BigBullet  leftbigBullet = new BigBullet(xPos, yPos);
			leftbigBullet.setSpeed(-12);
			leftBigBullets.add(leftbigBullet);
			this.getSoundManager().playBulletSound();
		}



	}

	/**
	 * Move a bullet once fired.
	 * @param bullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBullet(Bullet leftBullets , Bullet bullet ){

		if(getInputHandler().isLeftPressed() == false && getInputHandler().wasLeftReleased() == false) {
			if(bullet.getY() - bullet.getSpeed() >= 0){
				bullet.translate(bullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
		}

		else { //LeftBullet
			if(bullet.getY() - bullet.getSpeed() >= 0){
				leftBullets.translate(leftBullets.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
		}
	}

	/** Move a "Power Shot" bullet once fired.
	 * @param bigBullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBigBullet(BigBullet leftBigBullet, BigBullet bigBullet ){
		if(getInputHandler().isLeftPressed() == false && getInputHandler().wasLeftReleased() == false) {
			if(bigBullet.getY() - bigBullet.getSpeed() >= 0){
				bigBullet.translate(bigBullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
		}
		else {//left big bullet
			if(bigBullet.getY() - bigBullet.getSpeed() >= 0){
				leftBigBullet.translate(leftBigBullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
		}

	}

	/**
	 * Create a new MegaMan (and replace current one).
	 */
	public MegaMan newMegaMan(){
		this.megaMan = new MegaMan((SCREEN_WIDTH - MegaMan.WIDTH) / 2, (SCREEN_HEIGHT - MegaMan.HEIGHT - MegaMan.Y_OFFSET) / 2);
		return megaMan;
	}

	public Floor[] newFloor(Level1State screen, int n){
		floor = new Floor[n];
		for(int i=0; i<n; i++){
			this.floor[i] = new Floor(0 + i * Floor.WIDTH, SCREEN_HEIGHT- Floor.HEIGHT/2);
		}

		return floor;
	}

	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0 , SCREEN_HEIGHT/2 + 140 - i*40);

		}
		return platforms;
	}

	/**
	 * Create a new xWing.
	 */
	public XWing newXWing(Level1State screen){
		int xPos = (int) (SCREEN_WIDTH - XWing.WIDTH);
		int yPos = rand.nextInt((int)(SCREEN_HEIGHT - XWing.HEIGHT- 32));
		xWing = new XWing(xPos, yPos);
		return xWing;
	}

	/**
	 * Move the megaMan up
	 * @param megaMan the megaMan
	 */
	public void moveMegaManUp(){
		if(megaMan.getY() - megaMan.getSpeed() >= 0){
			megaMan.translate(0, -megaMan.getSpeed()*2);
		}
	}

	/**
	 * Move the megaMan down
	 * @param megaMan the megaMan
	 */
	public void moveMegaManDown(){
		for(int i=0; i<9; i++){
			if(megaMan.getY() + megaMan.getSpeed() + megaMan.height < SCREEN_HEIGHT - floor[i].getHeight()/2){
				megaMan.translate(0, 2);
			}
		}
	}

	/**
	 * Move the megaMan left
	 * @param megaMan the megaMan
	 */
	public void moveMegaManLeft(){
		if(megaMan.getX() - megaMan.getSpeed() >= 0){
			megaMan.translate(-megaMan.getSpeed(), 0);
		}
	}
	/**
	 * @param megaMan the megaMan
	 */
	public void moveMegaManRight(){
		if(megaMan.getX() + megaMan.getSpeed() + megaMan.width < SCREEN_WIDTH){//Colocar esta.
			megaMan.translate(megaMan.getSpeed(), 0);
		}
	}
	public void speedUpMegaMan() {
		megaMan.setSpeed(megaMan.getDefaultSpeed() * 2 +1);
	}

	public void slowDownMegaMan() {
		megaMan.setSpeed(megaMan.getDefaultSpeed());
	}
}
