package rbadia.voidspace.main;

/**
 * Container for game flags and/or status variables.
 */
public class GameStatus {
	private boolean gameOver = false;
	private boolean gameWon = false;
	
	
	// status variables
	private boolean newMegaMan;
	private boolean newBoss;
	private boolean newXWing;
	private boolean newXWing2;
	private boolean newBigAsteroid;
	private long xWingsDestroyed = 0;


	private int livesLeft;
	private int level = 1;
	
	public GameStatus(){
		
	}
	
	public synchronized boolean isGameOver() {
		return gameOver;
	}
	
	public synchronized void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	/**
	 * Indicates if the game has and the "You Win!!!" message is displaying.
	 * @return if the game has ended and the "You Win!!!" message is displaying.
	 */
	public synchronized boolean isGameWon() {
		return gameWon;
	}
	
	public synchronized void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
	}
	
	/**
	 * Indicates if a new MegaMan should be created/drawn.
	 * @return if a new life should be created/drawn
	 */
	public synchronized boolean isNewMegaMan() {
		return newMegaMan;
	}

	public synchronized void setNewMegaMan(boolean newMegaMan) {
		this.newMegaMan = newMegaMan;
	}
		
	public synchronized boolean isNewBoss() {
		return newBoss;
	}

	public synchronized void setNewBoss(boolean newBoss) {
		this.newBoss = newBoss;
	}

	public synchronized boolean isNewBoss2() {
		return newBoss;
	}

	public synchronized void setNewBoss2(boolean newBoss) {
		this.newBoss = newBoss;
	}
	
	/**
	 * Indicates if a new asteroid should be created/drawn.
	 * @return if a new asteroid should be created/drawn
	 */
	public synchronized boolean isNewXWing() {
		return newXWing;
	}

	public synchronized void setNewXWing(boolean newXWing) {
		this.newXWing = newXWing;
	}
	
	public synchronized boolean isNewXWing2() {
		return newXWing2;
	}
	
	public synchronized void setNewXWing2(boolean newxWing2) {
		this.newXWing2 = newxWing2;
	}
	
	/**
	 * Indicates if a new big asteroid should be created/drawn.
	 * @return if a new big asteroid should be created/drawn
	 */
	public synchronized boolean isNewBigAsteroid() {
		return newBigAsteroid;
	}

	public synchronized void setNewBigAsteroid(boolean newBigAsteroid) {
		this.newBigAsteroid = newBigAsteroid;
	}

	/**
	 * Returns the number of asteroid destroyed. 
	 * @return the number of asteroid destroyed
	 */
	public synchronized long getXWingDestroyed() {
		return xWingsDestroyed;
	}

	public synchronized void setXWingsDestroyed(long xWingsDestroyed) {
		this.xWingsDestroyed = xWingsDestroyed;
	}

	/**
	 * Returns the number lives left.
	 * @return the number lifes left
	 */
	public synchronized int getLivesLeft() {
		return livesLeft;
	}

	public synchronized void setLivesLeft(int livesLeft) {
		this.livesLeft = livesLeft;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
