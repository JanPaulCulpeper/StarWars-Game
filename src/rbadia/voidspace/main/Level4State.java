package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.util.ArrayList;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.StormTrooper;
import rbadia.voidspace.sounds.SoundManager;

public class Level4State extends Level1State {
	private static final long serialVersionUID = 6330305833847871298L;
	
	public Asteroid asteroid2 = new Asteroid(0,0);
	public StormTrooper stormTrooper1 = new StormTrooper(50, SCREEN_HEIGHT - Floor.HEIGHT);//positioned left, looking right
	public StormTrooper stormTrooper2 = new StormTrooper(400, SCREEN_HEIGHT - Floor.HEIGHT);//positioned right, looking left
	private long lastTrooper1BulletTime;
	private long lastTrooper2BulletTime;

	private ArrayList<Bullet> trooper1Bullets = new ArrayList<Bullet>();
	private ArrayList<Bullet> trooper2Bullets = new ArrayList<Bullet>();
	
	//Getters
	public StormTrooper getStormTrooper1(){
		return stormTrooper1;
	}
	public StormTrooper getStormTrooper2(){
		return stormTrooper2;
		
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
		drawBullets();
		drawBigBullets();
		drawTrooper();
		drawTrooper2();
		drawTrooper1Bullets();
		drawTrooper2Bullets();
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

		
		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
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
			if(i<4)	{
				platforms[i].setLocation(50 + i*50, SCREEN_HEIGHT - 160 + i*40);
			}
			if(i==4) {
				platforms[i].setLocation(50 + i*50 , SCREEN_HEIGHT - 160 + 3*40);}
			if(i>4){	
				int k=4;
				platforms[i].setLocation(50 + i*50, SCREEN_HEIGHT - 40 - (i-k)*40 );
				k=k+2;
			}
			
		}
		return platforms;
	}
	
	//Codigo de enemigos
	
		// fall right trooper looking left
		public boolean Fall2(){
			StormTrooper trooper1 = this.getStormTrooper1();
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
			StormTrooper trooper2 = this.getStormTrooper2();
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
			StormTrooper trooper1 = this.getStormTrooper1();
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
			StormTrooper trooper2 = this.getStormTrooper2();
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
			if(stormTrooper1 == null)
			{
				stormTrooper1 = new StormTrooper(400, SCREEN_HEIGHT - Floor.HEIGHT);
				((GraphicsManager) getGraphicsManager()).drawStormTrooperLookingLeft(stormTrooper1, g2d, this);
			}
			if(!trooper1Dead)
			{
				if (Fall2() && Gravity2()) {
					stormTrooper1.translate(-stormTrooper1.getSpeed(), stormTrooper1.getSpeed()/2);
					
				}
				if(!Gravity2()) {
					if(stormTrooper1.getX() + stormTrooper1.getPixelsWide() > SCREEN_WIDTH)
					{
						
						stormTrooper1.setSpeed(-stormTrooper1.getSpeed());
						this.trooper1LookingRight = false;

						
					}
					else if (stormTrooper1.getX() < 0)
					{
						stormTrooper1.setSpeed(StormTrooper.DEFAULT_SPEED);
						this.trooper1LookingRight = true;

					}
					stormTrooper1.translate(stormTrooper1.getSpeed(), 0);
					
				}
				
				if(trooper1LookingRight)
				{
					((GraphicsManager)getGraphicsManager()).drawStormTrooperLookingRight(stormTrooper1, g2d, this);
				}
				else
				{
					((GraphicsManager)getGraphicsManager()).drawStormTrooperLookingLeft(stormTrooper1, g2d, this);
				}
				
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastTrooper1BulletTime) > 1000/2){
					lastTrooper1BulletTime = currentTime;
					fireTrooper1Bullet();
				}
			}
			else
			{
				stormTrooper1.setLocation(1000,1000);
				((GraphicsManager)getGraphicsManager()).drawStormTrooperLookingRight(stormTrooper2, g2d, this);
			}

			
			
			
		}

		
		//Right trooper looking left
		protected void drawTrooper2() {
			Graphics2D g2d = getGraphics2D();
			if(stormTrooper2 == null)
			{
				stormTrooper2 = new StormTrooper(50, SCREEN_HEIGHT - Floor.HEIGHT);
				((GraphicsManager) getGraphicsManager()).drawStormTrooperLookingLeft(stormTrooper2, g2d, this);
			}
			if(!trooper2Dead)
			{
				if (Fall3() && Gravity3()) {
					stormTrooper2.translate(-stormTrooper2.getSpeed(), stormTrooper2.getSpeed()/2);
					
				}
				if(!Gravity3()) {
					if(stormTrooper2.getX() + stormTrooper2.getPixelsWide() > SCREEN_WIDTH)
					{
						
						stormTrooper2.setSpeed(-stormTrooper2.getSpeed());
						this.trooper2LookingLeft = true;

						
					}
					else if (stormTrooper2.getX() < 0)
					{
						stormTrooper2.setSpeed(StormTrooper.DEFAULT_SPEED);
						this.trooper2LookingLeft = false;

					}
					stormTrooper2.translate(stormTrooper2.getSpeed(), 0);
					
				}
				
				if(trooper2LookingLeft)
				{
					((GraphicsManager)getGraphicsManager()).drawStormTrooperLookingLeft(stormTrooper2, g2d, this);
				}
				else
				{
					((GraphicsManager)getGraphicsManager()).drawStormTrooperLookingRight(stormTrooper2, g2d, this);
				}
				
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastTrooper2BulletTime) > 1000/2){
					lastTrooper2BulletTime = currentTime;
					fireTrooper2Bullet();
				}
			}
			else
			{
				stormTrooper2.setLocation(1000,1000);
				((GraphicsManager)getGraphicsManager()).drawStormTrooperLookingLeft(stormTrooper2, g2d, this);
			}
			
		}
		
		
		public void moveTrooper1Left(){
			if(stormTrooper1.getX() - stormTrooper1.getSpeed() >= 0){
				stormTrooper1.translate(-stormTrooper1.getSpeed(), 0);
			}
		}
		public void moveTrooper1Down(){
			for(int i=0; i<9; i++){
				if(stormTrooper1.getY() + stormTrooper1.getSpeed() + stormTrooper1.height < SCREEN_HEIGHT - floor[i].getHeight()/2){
					stormTrooper1.translate(0, 2);
				}
			}
		}
		public void moveTrooper2Right(){
			if(stormTrooper2.getX() + stormTrooper2.getSpeed() + stormTrooper2.width < SCREEN_WIDTH){
				stormTrooper2.translate(stormTrooper2.getSpeed(), 0);
			}
		}
		public void moveTrooper2Down(){
			for(int i=0; i<9; i++){
				if(stormTrooper2.getY() + stormTrooper2.getSpeed() + stormTrooper2.height < SCREEN_HEIGHT - floor[i].getHeight()/2){
					stormTrooper2.translate(0, 2);
				}
			}
		}
		
		
		
		public void fireTrooper1Bullet()
		{	
			Bullet bullet = new Bullet(stormTrooper1.x + stormTrooper1.width - Bullet.WIDTH/2, 
					stormTrooper1.y + stormTrooper1.width/2 - Bullet.HEIGHT + 2);;
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
		
		public void drawTrooper1Bullets()
		{
			Graphics2D g2d = getGraphics2D();
			for(int i=0; i<trooper1Bullets.size(); i++){
				Bullet bullet = trooper1Bullets.get(i);
				getGraphicsManager().drawBullet(bullet, g2d, this);

				boolean remove = this.moveTrooper1Bullet(bullet);
				if(remove){
					trooper1Bullets.remove(i);
					i--;
				}
			}
		}
		
		
		
		
		public void fireTrooper2Bullet()
		{	
			Bullet bullet = new Bullet(stormTrooper2.x + stormTrooper2.width - Bullet.WIDTH/2, 
					stormTrooper2.y + stormTrooper2.width/2 - Bullet.HEIGHT + 2);;
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
		
		public void drawTrooper2Bullets()
		{
			Graphics2D g2d = getGraphics2D();
			for(int i=0; i<trooper2Bullets.size(); i++){
				Bullet bullet = trooper2Bullets.get(i);
				getGraphicsManager().drawBullet(bullet, g2d, this);

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
				if (stormTrooper1.intersects(bullet))
				{
					getGameStatus().setAsteroidsDestroyed(getGameStatus().getAsteroidsDestroyed() + 500);
					getSoundManager().playScreamSound();
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 1;
					bullets.remove(i);
					
					if(levelAsteroidsDestroyed == 2) {
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
				if (stormTrooper1.intersects(bullet))
				{
					getGameStatus().setAsteroidsDestroyed(getGameStatus().getAsteroidsDestroyed() + 500);
					getSoundManager().playScreamSound();
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 1;
					leftBullets.remove(i);
					
					if(levelAsteroidsDestroyed == 2) {
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
				if (stormTrooper2.intersects(bullet))
				{
					getGameStatus().setAsteroidsDestroyed(getGameStatus().getAsteroidsDestroyed() + 500);
					getSoundManager().playScreamSound();
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 1;
					bullets.remove(i);
					
					if(levelAsteroidsDestroyed == 2) {
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
				if (stormTrooper2.intersects(bullet))
				{
					getGameStatus().setAsteroidsDestroyed(getGameStatus().getAsteroidsDestroyed() + 500);
					getSoundManager().playScreamSound();
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 1;
					leftBullets.remove(i);
					
					if(levelAsteroidsDestroyed == 2) {
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
				if(stormTrooper1.intersects(bigBullet)){
					// increase asteroids destroyed count
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					trooper1Dead = true;
					getSoundManager().playScreamSound();
					bigBullets.remove(i);
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 2;
					damage=0;
				}
			}
		}
		
		//check right big bullet with trooper 2 collision
		public void checkBigBulletTrooper2Collision(){
			GameStatus status = getGameStatus();
			for(int i=0; i<bigBullets.size(); i++){
				BigBullet bigBullet = bigBullets.get(i);
				if(stormTrooper2.intersects(bigBullet)){
					// increase asteroids destroyed count
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					trooper2Dead = true;
					getSoundManager().playScreamSound();
					bigBullets.remove(i);
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 2;
					damage=0;
				}
			}
		}
		
		//check left big bullet with trooper 1 collision
		public void checkLeftBigBulletTrooper1Collision() {
			GameStatus status = getGameStatus();
			for(int i=0; i<leftBigBullets.size(); i++){
				BigBullet bigBullet = leftBigBullets.get(i);
				if(stormTrooper1.intersects(bigBullet)){
					// increase asteroids destroyed count
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					trooper1Dead = true;
					getSoundManager().playScreamSound();
					leftBigBullets.remove(i);
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 2;
					damage=0;
				}
			}
		}
		
		//checck left big bullet with trooper 2 collision
		public void checkLeftBigBulletTrooper2Collision() {
			GameStatus status = getGameStatus();
			for(int i=0; i<leftBigBullets.size(); i++){
				BigBullet bigBullet = leftBigBullets.get(i);
				if(stormTrooper2.intersects(bigBullet)){
					// increase asteroids destroyed count
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					trooper2Dead = true;
					getSoundManager().playScreamSound();
					leftBigBullets.remove(i);
					levelAsteroidsDestroyed = levelAsteroidsDestroyed + 2;
					damage=0;
				}
			}
		}
		
		@Override
		public boolean isLevelWon() {
			if(getInputHandler().isNPressed()) return true; 
			return levelAsteroidsDestroyed >= 4;
			
		}
		
		
		@Override
		public void drawAsteroid() {
			
		}
		
	
	
}












