package rbadia.voidspace.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Handles general level logic and status.
 */
public class LevelLogic {

	private long lastBulletTime;
	private long lastExchangeTime;
	private long lastBigBulletTime;
	private int stack= 0;
	private int mute = 0;

	private LevelState levelState;

	protected Font originalFont;
	protected Font bigFont;
	protected Font biggestFont;

	/**
	 * Create a new game logic handler
	 * @param gameScreen the game screen
	 */
	public LevelLogic(){
	}

	public LevelState getLevelState() {
		return levelState;
	}

	public void setLevelState(LevelState levelState) {
		this.levelState = levelState;
	}

	public int getMute(){
		return mute;
	}


	/**
	 * Prepare for a new game.
	 */
	public void newGame(){

		// prepare game screen
		//levelState.doLevelStart();

		// delay to display "Get Ready" message for 1.5 seconds
	}

	/**
	 * Actions to take when the game is over.
	 */
	public void gameOver(){
		//getLevelState().getGameStatus().setGameStarted(false);
		getLevelState().getGameStatus().setGameOver(true);

		levelState.doGameOverScreen();

		// delay to display "Game Over" message for 3 seconds
		Timer timer = new Timer(5000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getLevelState().getGameStatus().setGameOver(false);
			}
		});
		timer.setRepeats(false);
		timer.start();

		//Change music back to menu screen music
		MegaManMain.audioClip.close();
		MegaManMain.playingAudio = new File("audio/menuScreen.wav");
		try {
			MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.playingAudio);
			MegaManMain.audioClip.open(MegaManMain.audioStream);
			MegaManMain.audioClip.start();
			MegaManMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Actions to take if game is won.
	 */

	//GAME LOOPS ON THE FIRST GAMESCREEN AND RESETS ALL VARIABLE COUNTERS
	public void gameWon(){
		//status.setGameStarted(false);  //SENDS TO MAIN SCREEN/ IF COMMENTED OUT LOOPS THE GAME
		getLevelState().getGameStatus().setGameWon(true);
		levelState.doLevelWon();

		// delay to display "Game Won" message for 3 seconds
		Timer timer = new Timer(3000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getLevelState().getGameStatus().setGameWon(false);
			}
		});
		timer.setRepeats(false);
		timer.start();

		//Change music back to menu screen music
		MegaManMain.audioClip.close();
		MegaManMain.playingAudio = new File("audio/star_wars.wav");
		try {
			MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.playingAudio);
			MegaManMain.audioClip.open(MegaManMain.audioStream);
			MegaManMain.audioClip.start();
			MegaManMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Draws the "Game Over" message.
	 */
	protected void drawGameOver() {

		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		// set original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		String gameOverStr = "Game Over";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameOverStr);
		if(strWidth > LevelState.SCREEN_WIDTH - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(gameOverStr);
		}
		int ascent = fm.getAscent();
		int strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		int strY = (LevelState.SCREEN_HEIGHT + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.RED);
		g2d.drawString(gameOverStr, strX, strY);
	}

	protected void drawYouWin() {

		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		// set original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		String youWinStr = "Level " + levelState.getLevel() + " Completed";

		//Font currentFont = biggestFont == null? bigFont : biggestFont;
		Font currentFont = originalFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 5).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(youWinStr);
		if(strWidth > LevelState.SCREEN_WIDTH - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(youWinStr);
		}
		int ascent = fm.getAscent();
		int strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		int strY = (LevelState.SCREEN_HEIGHT + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(youWinStr, strX, strY);

	}

	/**
	 * Display initial game title screen.
	 */
	protected void drawInitialMessage() {

		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		String gameTitleStr = "VADER:\r\n ";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameTitleStr);
		if(strWidth > LevelState.SCREEN_WIDTH - 10){
			bigFont = currentFont;
			biggestFont = currentFont;
			fm = g2d.getFontMetrics(currentFont);
			strWidth = fm.stringWidth(gameTitleStr);
		}
		g2d.setFont(bigFont);
		int ascent = fm.getAscent();
		int strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		int strY = (LevelState.SCREEN_HEIGHT + ascent)/2 - 2*ascent;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(gameTitleStr, strX, strY);

		String gameTitleStr2 =  "a Star Wars Story";

		Font currentFont2 = biggestFont == null? bigFont : biggestFont;
		float fontSize2 = currentFont.getSize2D();
		bigFont = currentFont2.deriveFont(fontSize2 + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm2 = g2d.getFontMetrics(bigFont);
		int strWidth2 = fm2.stringWidth(gameTitleStr2);
		if(strWidth2 > LevelState.SCREEN_WIDTH - 10){
			bigFont = currentFont2;
			biggestFont = currentFont2;
			fm2 = g2d.getFontMetrics(currentFont2);
			strWidth2 = fm2.stringWidth(gameTitleStr2);
		}
		g2d.setFont(bigFont);
		int ascent2 = fm2.getAscent();
		int strX2 = (LevelState.SCREEN_WIDTH - strWidth2)/2;
		int strY2 = (LevelState.SCREEN_HEIGHT + 3*ascent2)/2 - 2*ascent2;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(gameTitleStr2, strX2, strY2);
		
		String gameTitleStr3 =  "The untold stories of Darth Vader";

		Font currentFont3 = biggestFont == null? bigFont : biggestFont;
		float fontSize3 = currentFont.getSize2D();
		bigFont = currentFont3.deriveFont(fontSize3 + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm3 = g2d.getFontMetrics(bigFont);
		int strWidth3 = fm3.stringWidth(gameTitleStr3);
		if(strWidth3 > LevelState.SCREEN_WIDTH - 10){
			bigFont = currentFont3;
			biggestFont = currentFont3;
			fm3 = g2d.getFontMetrics(currentFont3);
			strWidth2 = fm3.stringWidth(gameTitleStr3);
		}
		g2d.setFont(originalFont);
		g2d.setPaint(Color.RED);
		int ascent3 = fm2.getAscent();
		int strX3 = LevelState.SCREEN_WIDTH/3;
		int strY3 = (LevelState.SCREEN_HEIGHT + 3*ascent3)/2 - ascent3;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(gameTitleStr3, strX3, strY3);
		
		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Press <Space> to Start the Level";
		strWidth = fm.stringWidth(newGameStr);
		strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		strY = (LevelState.SCREEN_HEIGHT + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.white);
		g2d.drawString(newGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String itemGameStr = "Press <I> for Item Menu.";
		strWidth = fm.stringWidth(itemGameStr);
		strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(itemGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String shopGameStr = "Press <S> for Shop Menu.";
		strWidth = fm.stringWidth(shopGameStr);
		strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(shopGameStr, strX, strY);
		
		fm = g2d.getFontMetrics();
		String muteGameStr = "Press <M> to mute music.";
		strWidth = fm.stringWidth(muteGameStr);
		strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(muteGameStr, strX, strY);
		
		fm = g2d.getFontMetrics();
		String skipGameStr = "Press <N> to skip level. (for testing purposes)";
		strWidth = fm.stringWidth(skipGameStr);
		strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(skipGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String exitGameStr = "Press <Esc> to Exit the Game.";
		strWidth = fm.stringWidth(exitGameStr);
		strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(exitGameStr, strX, strY);
	}

	/**
	 * Draws the initial "Get Ready!" message.
	 */
	protected void drawGetReady() {
		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		String readyStr = "Get Ready for Level " + levelState.getLevel(); 
		g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();
		int strWidth = fm.stringWidth(readyStr);
		int strX = (LevelState.SCREEN_WIDTH - strWidth)/2;
		int strY = (LevelState.SCREEN_HEIGHT + ascent)/2;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(readyStr, strX, strY);
	}
	protected void drawLevel1Intro() {
		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();		
		g2d.setFont(originalFont);
		String firstLineStr = "The Empire is once again under attack by enemy Rebels";
		int strX = levelState.SCREEN_WIDTH/15;
		int strY = levelState.SCREEN_HEIGHT/4 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(firstLineStr, strX, strY);

		fm = g2d.getFontMetrics();
		String secondLineStr = "Vader must  intervene in this battle to avoid losing control of this planet to the enemy";
		strY = strY + 16;
		g2d.drawString(secondLineStr, strX, strY);

		fm = g2d.getFontMetrics();
		String thirdLineStr = "REBEL X-WINGs INCOMING!!!";
		strY = strY + 16;
		g2d.drawString(thirdLineStr, strX, strY);
	}
	
	protected void drawLevel2Intro() {
		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();		
		g2d.setFont(originalFont);
		String firstLineStr = "More Rebel X-Wings incoming";
		int strX = levelState.SCREEN_WIDTH/3;
		int strY = levelState.SCREEN_HEIGHT/4 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(firstLineStr, strX, strY);
		
	}
	protected void drawLevel3Intro() {
		
		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();		
		g2d.setFont(originalFont);
		String firstLineStr = "'I will take them all out myself ' -Vader";
		int strX = levelState.SCREEN_WIDTH/3;
		int strY = levelState.SCREEN_HEIGHT/4 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(firstLineStr, strX, strY);
		
	}
	protected void drawLevel4Intro() {
		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();		
		g2d.setFont(originalFont);
		String firstLineStr = "The Rebels have sent some troopers down";
		int strX = levelState.SCREEN_WIDTH/3;
		int strY = levelState.SCREEN_HEIGHT/4 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(firstLineStr, strX, strY);
		
		fm = g2d.getFontMetrics();
		String thirdLineStr = "Make them suf fer !";
		strY = strY + 16;
		g2d.drawString(thirdLineStr, strX, strY);
		
	}
	protected void drawLevel5Intro() {
		LevelState levelState = getLevelState();
		Graphics2D g2d = levelState.getGraphics2D();

		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();		
		g2d.setFont(originalFont);
		String firstLineStr = "They have sent their strongest reinforcement to the battlefield";
		int strX = levelState.SCREEN_WIDTH/5;
		int strY = levelState.SCREEN_HEIGHT/4 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(firstLineStr, strX, strY);

		fm = g2d.getFontMetrics();
		String secondLineStr = "And more X-Wing fighters!!";
		strY = strY + 16;
		g2d.drawString(secondLineStr, strX, strY);

		fm = g2d.getFontMetrics();
		String thirdLineStr = "Who is that...?";
		strY = strY + 16;
		g2d.drawString(thirdLineStr, strX, strY);
	}



	public void stateTransition(InputHandler ih, LevelState levelState){
		GameStatus status = getLevelState().getGameStatus();
		switch (levelState.getCurrentState()) {
		case LevelState.START_STATE:
			levelState.setCurrentState(LevelState.INITIAL_SCREEN);
			break;

		case LevelState.INITIAL_SCREEN:
			levelState.doInitialScreen();
			handleKeysDuringInitialScreen(ih, levelState);
			break;

		case LevelState.GETTING_READY:
			levelState.doGettingReady();
			levelState.setCurrentState(LevelState.PLAYING);
			break;

		case LevelState.PLAYING:
			levelState.doPlaying();
			handleKeysDuringPlay(ih, levelState);
			if(status.getLivesLeft() == 0) {
				levelState.setCurrentState(LevelState.GAME_OVER_SCREEN);
			}
			if(levelState.isLevelWon()) {
				levelState.setCurrentState(LevelState.LEVEL_WON);
			}
			break;

		case LevelState.NEW_MEGAMAN:
			// TODO Verify that this state is activated when MegaMan dies
			break;

		case LevelState.GAME_OVER_SCREEN:
			levelState.doGameOverScreen();
			levelState.setCurrentState(LevelState.GAME_OVER);
			break;

		case LevelState.GAME_OVER:
			levelState.doGameOver();
			break;

		case LevelState.LEVEL_WON:
			levelState.doLevelWon();
			break;
		}
	}


	public void handleKeysDuringInitialScreen(InputHandler ih, LevelState levelState) {
		if (ih.isMPressed()) {
			MegaManMain.audioClip.stop();
		}
		else {
			MegaManMain.audioClip.start();
		}
		
		if(ih.isSpacePressed()) {
			ih.reset();
			levelState.setCurrentState(LevelState.GETTING_READY);	
		}
		if(ih.isSPressed()) {
			ih.reset();
			JOptionPane.showMessageDialog( null, 
					"Item:                Price\r\n"+
							"\r\n"+
							"Extra Life:      1500\r\n"+ 
							"Power Shot:  1000\r\n"+
					"\r\n");
			
		}
		if(ih.isIPressed()) {
			ih.reset();
			JOptionPane.showMessageDialog( null, 
					"Power Up:     Explanation\r\n"+
							"\r\n"+
							"Extra Life:      Gives an extra life (One Extra Life per second)\r\n"+ 
							"                           (Press E to buy, limit of one life per second.)\r\n" +
							"Power Shot:  Activates the Power Shot which kills the asteroid in one hit\r\n"+
					"                           (Press Q to buy, afterwards press Q to fire.)\r\n");
			
		}
//		ih.reset();
		

	}

	public void handleKeysDuringPlay(InputHandler ih, LevelState levelState) {
		
		//stops music
		if(ih.isMPressed()){
			if(MegaManMain.audioClip.isRunning())
				MegaManMain.audioClip.stop();
			else
				MegaManMain.audioClip.start();
			
			ih.reset();
		}
			
		GameStatus status = getLevelState().getGameStatus();

		// fire bullet if space is pressed
		if(ih.isSpacePressed()){
			// fire only up to 5 bullets per second
			stack=0;
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastBulletTime) > 1000/5){
				lastBulletTime = currentTime;
				getLevelState().fireBullet();
			}
		}

		if(ih.isEPressed()){
			if(status.getXWingDestroyed()>= 1500){
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastExchangeTime > 1000)){
					lastExchangeTime = currentTime;
					status.setXWingsDestroyed(status.getXWingDestroyed() - 1500);
					status.setLivesLeft(status.getLivesLeft() + 1);
				}
			}
		}

		if(ih.isQPressed()){
			if(stack==0 && status.getXWingDestroyed()>= 0){
				stack++;
				status.setXWingsDestroyed(status.getXWingDestroyed()-0);
			}
			else if(stack>= 1){
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastBigBulletTime) > 1000){
					lastBigBulletTime = currentTime;
					getLevelState().fireBigBullet();
				}

			}
		}

		if(ih.isShiftPressed()){
			getLevelState().speedUpMegaMan();
		}

		if(ih.isUpPressed()){
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastBigBulletTime) > 570){
				lastBigBulletTime = currentTime;
				for(int i=0; i<6; i++){
					getLevelState().moveMegaManUp();
				}
			}
		}

		if(ih.isDownPressed()){
			getLevelState().moveMegaManDown();
		}

		if(ih.isLeftPressed()){
			getLevelState().moveMegaManLeft();
		}

		if(ih.isRightPressed()){
			getLevelState().moveMegaManRight();
		}
	}
	

	public static void delay(long millis) {
		try{
			Thread.sleep(millis);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
