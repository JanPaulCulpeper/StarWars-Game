package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class Level4State extends Level1State {
	private static final long serialVersionUID = 6330305833847871298L;


	public RebelTrooper rebelTrooper1 = new RebelTrooper(50, SCREEN_HEIGHT - Floor.HEIGHT);//positioned left, looking right
	public RebelTrooper rebelTrooper2 = new RebelTrooper(400, SCREEN_HEIGHT - Floor.HEIGHT);//positioned right, looking left
	private long lastTrooper1BulletTime;
	private long lastTrooper2BulletTime;
	private ArrayList<Bullet> trooper1Bullets = new ArrayList<Bullet>();
	private ArrayList<Bullet> trooper2Bullets = new ArrayList<Bullet>();
	

	//Getters
	public RebelTrooper getRebelTrooper1(){
		return rebelTrooper1;
	}
	public RebelTrooper getRebelTrooper2(){
		return rebelTrooper2;

	}



	//Constructor
	public Level4State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
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
		((LevelLogic)getGameLogic()).drawLevel4Intro();
		repaint();
		LevelLogic.delay(7000);
		//Changes music from "menu music" to "ingame music"
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
		drawBullets();
		drawBigBullets();
		drawTrooper();
		drawTrooper2();
		drawRebel1Bullets();
		drawRebel2Bullets();
		checkBulletTrooper1Collisions();
		checkLeftBulletTrooper1Collisions();
		checkBulletTrooper2Collisions();
		checkLeftBulletTrooper2Collisions();
		checkTrooper1BulletMegamanCollisions();
		checkTrooper2BulletMegamanCollisions();
		checkBigBulletTrooper1Collision();
		checkBigBulletTrooper2Collision();
		checkLeftBigBulletTrooper1Collision();
		checkLeftBigBulletTrooper2Collision();
		checkBullletXWingCollisions();
		checkLeftBullletXWingCollisions();
		checkBigBulletXWingCollisions();
		checkLeftBigBulletXWingCollisions();
		checkMegaManXWingCollisions();
		drawXWingLeft();

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getXWingDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));
	}
	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<3)	{
				platforms[i].setLocation(50 + i*50, SCREEN_HEIGHT - 140 + i*40);
			}
			if(i>2 && i<5) {
				platforms[i].setLocation(SCREEN_WIDTH/2 + 150 - i*50 , SCREEN_HEIGHT/2 + 100 );}
			if(i>4){	
				int k=4;
				platforms[i].setLocation(50 + i*50, SCREEN_HEIGHT - 20 - (i-k)*40 );
				k=k+2;
			}

		}
		return platforms;
	}
	//Codigo de enemigos

	// fall right trooper looking left
	public boolean Fall2(){
		RebelTrooper trooper1 = this.getRebelTrooper1();
		Platform[] platforms = this.getPlatforms();
		for(int i=0; i<getNumPlatforms(); i++){
			if((((platforms[i].getX() < trooper1.getX()) && (trooper1.getX()< platforms[i].getX() + platforms[i].getWidth()))
					|| ((platforms[i].getX() < trooper1.getX() + trooper1.getWidth()) 
							&& (trooper1.getX() + trooper1.getWidth()< platforms[i].getX() + platforms[i].getWidth())))
					&& trooper1.getY() + trooper1.getHeight() == platforms[i].getY())
			{
				return false;
			}
		}
		return true;
	}
	// fall left trooper looking right
	public boolean Fall3(){
		RebelTrooper trooper2 = this.getRebelTrooper2();
		Platform[] platforms = this.getPlatforms();
		for(int i=0; i<getNumPlatforms(); i++){
			if((((platforms[i].getX() < trooper2.getX()) && (trooper2.getX()< platforms[i].getX() + platforms[i].getWidth()))
					|| ((platforms[i].getX() < trooper2.getX() + trooper2.getWidth()) 
							&& (trooper2.getX() + trooper2.getWidth()< platforms[i].getX() + platforms[i].getWidth())))
					&& trooper2.getY() + trooper2.getHeight() == platforms[i].getY()
					){
				return false;
			}
		}
		return true;
	}
	//gravity trooper looking left
	protected boolean Gravity2(){
		RebelTrooper trooper1 = this.getRebelTrooper1();
		Floor[] floor = this.getFloor();

		for(int i=0; i<9; i++){
			if((trooper1.getY() + trooper1.getHeight() -17 < SCREEN_HEIGHT - floor[i].getHeight()/2) 
					&& Fall2() == true){

				trooper1.translate(0 , 2);
				return true;

			}
		}
		return false;
	}
	//Gravity left looking right trooper
	protected boolean Gravity3(){
		RebelTrooper trooper2 = this.getRebelTrooper2();
		Floor[] floor = this.getFloor();

		for(int i=0; i<9; i++){
			if((trooper2.getY() + trooper2.getHeight() -17 < SCREEN_HEIGHT - floor[i].getHeight()/2) 
					&& Fall3() == true){

				trooper2.translate(0 , 2);
				return true;

			}
		}
		return false;
	}
	private boolean trooper1LookingRight = true;
	private boolean trooper2LookingLeft = true;
	private boolean trooper1Dead = false;
	private boolean trooper2Dead = false;

	//Left trooper looking right
	protected void drawTrooper() {
		Graphics2D g2d = getGraphics2D();
		if(rebelTrooper1 == null)
		{
			rebelTrooper1 = new RebelTrooper(400, SCREEN_HEIGHT - Floor.HEIGHT);
			((GraphicsManager) getGraphicsManager()).drawRebelTrooperLookingLeft(rebelTrooper1, g2d, this);
		}
		if(!trooper1Dead)
		{
			if (Fall2() && Gravity2()) {
				rebelTrooper1.translate(-rebelTrooper1.getSpeed(), rebelTrooper1.getSpeed()/2);

			}
			if(!Gravity2()) {
				if(rebelTrooper1.getX() + rebelTrooper1.getPixelsWide() > SCREEN_WIDTH)
				{

					rebelTrooper1.setSpeed(-rebelTrooper1.getSpeed());
					this.trooper1LookingRight = false;


				}
				else if (rebelTrooper1.getX() < 0)
				{
					rebelTrooper1.setSpeed(RebelTrooper.DEFAULT_SPEED);
					this.trooper1LookingRight = true;

				}
				rebelTrooper1.translate(rebelTrooper1.getSpeed(), 0);

			}

			if(trooper1LookingRight)
			{
				((GraphicsManager)getGraphicsManager()).drawRebelTrooperLookingRight(rebelTrooper1, g2d, this);
			}
			else
			{
				((GraphicsManager)getGraphicsManager()).drawRebelTrooperLookingLeft(rebelTrooper1, g2d, this);
			}

			long currentTime = System.currentTimeMillis();
			if((currentTime - lastTrooper1BulletTime) > 4000/2){
				lastTrooper1BulletTime = currentTime;
				fireTrooper1Bullet();
			}
		}
		else
		{
			rebelTrooper1.setLocation(1000,1000);
			//		((GraphicsManager)getGraphicsManager()).drawRebelTrooperLookingRight(stormTrooper2, g2d, this);
		}




	}


	//Right trooper looking left
	protected void drawTrooper2() {
		
		Graphics2D g2d = getGraphics2D();
		if(rebelTrooper2 == null)
		{
			rebelTrooper2 = new RebelTrooper(50, SCREEN_HEIGHT - Floor.HEIGHT);
			((GraphicsManager) getGraphicsManager()).drawRebelTrooperLookingLeft(rebelTrooper2, g2d, this);
		}
		if(!trooper2Dead)
		{
			if (Fall3() && Gravity3()) {
				rebelTrooper2.translate(-rebelTrooper2.getSpeed(), rebelTrooper2.getSpeed()/2);

			}
			if(!Gravity3()) {
				if(rebelTrooper2.getX() + rebelTrooper2.getPixelsWide() > SCREEN_WIDTH)
				{

					rebelTrooper2.setSpeed(-rebelTrooper2.getSpeed());
					this.trooper2LookingLeft = true;


				}
				else if (rebelTrooper2.getX() < 0)
				{
					rebelTrooper2.setSpeed(RebelTrooper.DEFAULT_SPEED);
					this.trooper2LookingLeft = false;

				}
				rebelTrooper2.translate(rebelTrooper2.getSpeed(), 0);

			}

			if(trooper2LookingLeft)
			{
				((GraphicsManager)getGraphicsManager()).drawRebelTrooperLookingLeft(rebelTrooper2, g2d, this);
			}
			else
			{
				((GraphicsManager)getGraphicsManager()).drawRebelTrooperLookingRight(rebelTrooper2, g2d, this);
			}

			long currentTime = System.currentTimeMillis();
			if((currentTime - lastTrooper2BulletTime) > 3000/2){
				lastTrooper2BulletTime = currentTime;
				fireRebel2Bullet();
			}
		}
		else
		{
			rebelTrooper2.setLocation(1000,1000);
			//			((GraphicsManager)getGraphicsManager()).drawRebelTrooperLookingLeft(stormTrooper2, g2d, this);
		}

	}


	public void moveTrooper1Left(){
		if(rebelTrooper1.getX() - rebelTrooper1.getSpeed() >= 0){
			rebelTrooper1.translate(-rebelTrooper1.getSpeed(), 0);
		}
	}
	public void moveTrooper1Down(){
		for(int i=0; i<9; i++){
			if(rebelTrooper1.getY() + rebelTrooper1.getSpeed() + rebelTrooper1.height < SCREEN_HEIGHT - floor[i].getHeight()/2){
				rebelTrooper1.translate(0, 2);
			}
		}
	}
	public void moveTrooper2Right(){
		if(rebelTrooper2.getX() + rebelTrooper2.getSpeed() + rebelTrooper2.width < SCREEN_WIDTH){
			rebelTrooper2.translate(rebelTrooper2.getSpeed(), 0);
		}
	}
	public void moveTrooper2Down(){
		for(int i=0; i<9; i++){
			if(rebelTrooper2.getY() + rebelTrooper2.getSpeed() + rebelTrooper2.height < SCREEN_HEIGHT - floor[i].getHeight()/2){
				rebelTrooper2.translate(0, 2);
			}
		}
	}



	public void fireTrooper1Bullet()
	{	
		Bullet bullet = new Bullet(rebelTrooper1.x + rebelTrooper1.width - Bullet.WIDTH/2, 
				rebelTrooper1.y + rebelTrooper1.width/2 - Bullet.HEIGHT + 2);;
				if(!this.trooper1LookingRight)
				{
					bullet.setSpeed(-bullet.getSpeed());
				}
				this.trooper1Bullets.add(bullet);
				this.getSoundManager().playBulletSound();

	}

	public boolean moveTrooper1Bullet(Bullet bullet)
	{
		if(bullet.getX() - bullet.getSpeed() <= this.getWidth() && bullet.getX() > 0)
		{

			bullet.translate(bullet.getSpeed(), 0);
			return false;
			
		}
		//remove bullet if out of frame
		return true;
	}

	public void drawRebel1Bullets()
	{
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<trooper1Bullets.size(); i++){
			Bullet bullet = trooper1Bullets.get(i);
			getGraphicsManager().drawRebelsbulletImg(bullet, g2d, this);

			boolean remove = this.moveTrooper1Bullet(bullet);
			if(remove){
				trooper1Bullets.remove(i);
				i--;
			}
		}
	}




	public void fireRebel2Bullet()
	{	
		Bullet bullet = new Bullet(rebelTrooper2.x + rebelTrooper2.width - Bullet.WIDTH/2, 
				rebelTrooper2.y + rebelTrooper2.width/2 - Bullet.HEIGHT + 2);
		if(this.trooper2LookingLeft)
		{
			bullet.setSpeed(-bullet.getSpeed());
		}
		this.trooper2Bullets.add(bullet);
		this.getSoundManager().playBulletSound();

	}

	public boolean moveTrooper2Bullet(Bullet bullet)
	{
		if(bullet.getX() - bullet.getSpeed() <= this.getWidth() && bullet.getX() > 0)
		{

			bullet.translate(bullet.getSpeed(), 0);
			return false;
		}
		//remove bullet if out of frame
		return true;
	}

	public void drawRebel2Bullets()
	{
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<trooper2Bullets.size(); i++){
			Bullet bullet = trooper2Bullets.get(i);
			getGraphicsManager().drawRebelsbulletImg(bullet, g2d, this);

			boolean remove = this.moveTrooper2Bullet(bullet);
			if(remove){
				trooper2Bullets.remove(i);
				i--;
			}
		}
	}


	protected void checkTrooper1BulletMegamanCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<trooper1Bullets.size(); i++){
			Bullet bullet = trooper1Bullets.get(i);
			if(megaMan.intersects(bullet)) {
				status.setLivesLeft(status.getLivesLeft() - 1);
				trooper1Bullets.remove(i);
				break;
			}
		}
	}
	//bullet de megaman con trooper left
	public void checkBulletTrooper1Collisions()
	{
		for(int i = 0; i < bullets.size(); i++)
		{
			Bullet bullet = bullets.get(i);
			if (rebelTrooper1.intersects(bullet))
			{
				getGameStatus().setXWingsDestroyed(getGameStatus().getXWingDestroyed() + 200);
				getSoundManager().playScreamSound();
				levelXWingDestroyed = levelXWingDestroyed + 1;
				bullets.remove(i);

				if(levelXWingDestroyed == 2) {
					getSoundManager().playScreamSound();
					trooper1Dead = true;
				}
				break;
			}
		}

	}

	//left bullet de megaman con trooper left
	public void checkLeftBulletTrooper1Collisions()
	{
		for(int i = 0; i < leftBullets.size(); i++)
		{
			Bullet bullet = leftBullets.get(i);
			if (rebelTrooper1.intersects(bullet))
			{
				getGameStatus().setXWingsDestroyed(getGameStatus().getXWingDestroyed() + 200);
				getSoundManager().playScreamSound();
				levelXWingDestroyed = levelXWingDestroyed + 1;
				leftBullets.remove(i);

				if(levelXWingDestroyed == 2) {
					getSoundManager().playScreamSound();
					trooper1Dead = true;
				}
				break;
			}
		}

	}

	protected void checkTrooper2BulletMegamanCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<trooper2Bullets.size(); i++){
			Bullet bullet = trooper2Bullets.get(i);
			if(megaMan.intersects(bullet)) {
				status.setLivesLeft(status.getLivesLeft() - 1);
				trooper2Bullets.remove(i);
				break;
			}
		}
	}

	//bullet de megaman con trooper right
	public void checkBulletTrooper2Collisions()
	{
		for(int i = 0; i < bullets.size(); i++)
		{
			Bullet bullet = bullets.get(i);
			if (rebelTrooper2.intersects(bullet))
			{
				getGameStatus().setXWingsDestroyed(getGameStatus().getXWingDestroyed() + 200);
				getSoundManager().playScreamSound();
				levelXWingDestroyed = levelXWingDestroyed + 1;
				bullets.remove(i);

				if(levelXWingDestroyed == 2) {
					getSoundManager().playScreamSound();
					trooper2Dead = true;
				}

				break;
			}
		}

	}

	//left bullet de megaman con trooper right
	public void checkLeftBulletTrooper2Collisions()
	{
		for(int i = 0; i < leftBullets.size(); i++)
		{
			Bullet bullet = leftBullets.get(i);
			if (rebelTrooper2.intersects(bullet))
			{
				getGameStatus().setXWingsDestroyed(getGameStatus().getXWingDestroyed() + 200);
				getSoundManager().playScreamSound();
				levelXWingDestroyed = levelXWingDestroyed + 1;
				leftBullets.remove(i);

				if(levelXWingDestroyed == 2) {
					getSoundManager().playScreamSound();
					trooper2Dead = true;
				}
				break;
			}
		}

	}

	//Check Right big bullet with trooper 1 collision
	public void checkBigBulletTrooper1Collision() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(rebelTrooper1.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 400);
				trooper1Dead = true;
				getSoundManager().playScreamSound();
				bigBullets.remove(i);
				levelXWingDestroyed = levelXWingDestroyed + 2;
				damage=0;
			}
		}
	}

	//check right big bullet with trooper 2 collision
	public void checkBigBulletTrooper2Collision(){
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(rebelTrooper2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 400);
				trooper2Dead = true;
				getSoundManager().playScreamSound();
				bigBullets.remove(i);
				levelXWingDestroyed = levelXWingDestroyed + 2;
				damage=0;
			}
		}
	}

	//check left big bullet with trooper 1 collision
	public void checkLeftBigBulletTrooper1Collision() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet bigBullet = leftBigBullets.get(i);
			if(rebelTrooper1.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 400);
				trooper1Dead = true;
				getSoundManager().playScreamSound();
				leftBigBullets.remove(i);
				levelXWingDestroyed = levelXWingDestroyed + 2;
				damage=0;
			}
		}
	}

	//checck left big bullet with trooper 2 collision
	public void checkLeftBigBulletTrooper2Collision() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			BigBullet bigBullet = leftBigBullets.get(i);
			if(rebelTrooper2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 400);
				trooper2Dead = true;
				getSoundManager().playScreamSound();
				leftBigBullets.remove(i);
				levelXWingDestroyed = levelXWingDestroyed + 2;
				damage=0;
			}
		}
	}

	@Override
	public boolean isLevelWon() {
		if(getInputHandler().isNPressed()) return true; 
		return levelXWingDestroyed >= 4;

	}
	
	
//	protected void drawXWingLeft() {
//		Graphics2D g2d = getGraphics2D();
//		GameStatus status = getGameStatus();
//		if((xWing.getX() + xWing.getWidth() >  0)){
//			xWing.translate(-xWing.getSpeed(), 0);
//			getGraphicsManager().drawXWingLeft(xWing, g2d, this);	
//		}
//		else {
//			long currentTime = System.currentTimeMillis();
//			if((currentTime - lastXWingTime) > NEW_XWING_DELAY){
//				// draw a new xWing
//				lastXWingTime = currentTime;
//				status.setNewXWing(false);
//				xWing.setLocation((int) (SCREEN_WIDTH - xWing.getPixelsWide()),
//						(rand.nextInt((int) (SCREEN_HEIGHT - xWing.getPixelsTall() - 32))));
//			}
//
//			else{
//				// draw explosion
//				getGraphicsManager().drawXWingExplosion(xWingExplosion, g2d, this);
//			}
//		}
//	}
	protected void checkBigBulletXWingCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(xWing.intersects(bigBullet)){
				// increase xWings destroyed count
				status.setXWingsDestroyed(status.getXWingDestroyed() + 100);
				removeXWing(xWing);
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
				damage=0;
				// remove bullet
				leftBullets.remove(i);
				break;
			}
		}
	}
//	protected void checkMegaManXWingCollisions() {
//		GameStatus status = getGameStatus();
//		if(xWing.intersects(megaMan)){
//			status.setLivesLeft(status.getLivesLeft() - 1);
//			removeXWing(xWing);
//		}
//	}
}












