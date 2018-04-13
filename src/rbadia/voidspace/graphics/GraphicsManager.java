package rbadia.voidspace.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.main.Level3State;
import rbadia.voidspace.main.LevelState;
import rbadia.voidspace.model.XWing;
//import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Floor;
//import rbadia.voidspace.model.BulletBoss;
//import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.PowerUp;
import rbadia.voidspace.model.RebelTrooper;

/**
 * Manages and draws game graphics and images.
 */
public class GraphicsManager {
	private BufferedImage megaManImg;
	private BufferedImage megaFallRImg;
	private BufferedImage megaFireRImg;
	private BufferedImage floorImg;
	private BufferedImage platformImg;
	private BufferedImage bulletImg;
	private BufferedImage RebelsbulletImg;
	private BufferedImage bigBulletImg;
	private BufferedImage xWingLeftImg;
	private BufferedImage xWingRightImg;
	private BufferedImage xWingExplosionImg;
	private BufferedImage megaManExplosionImg;
	private BufferedImage bigAsteroidExplosionImg;
	private BufferedImage megamanLeft;
	private BufferedImage megamanRight;
	private BufferedImage megamanFireLeft;
	private BufferedImage megamanLookLeft;
	private BufferedImage rebelTrooperLookingRight;
	private BufferedImage rebelTrooperLookingLeft;
	private BufferedImage DeathStar;
	private BufferedImage BossImg;
	private BufferedImage PowerUpImg;
	private BufferedImage MenuImg;

	/**
	 * Creates a new graphics manager and loads the game images.
	 */
	public GraphicsManager(){
		// load images
		try {
			this.megaManImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/Vader.png"));
			this.megaFallRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/VaderFall.png"));
			this.megaFireRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/VaderFireRight.png"));
			this.megamanLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/VaderRunLeft.png"));
			this.megamanRight = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/VaderRunRight.png"));
			this.megamanFireLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/VaderFireLeft.png"));
			this.megamanLookLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/VaderLookLeft.png"));
			this.floorImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFloor.png"));
			this.platformImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/platform3.png"));
			this.xWingLeftImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/xWingLeft.png"));
			this.xWingRightImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/xWingRight.png"));
			this.xWingExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroidExplosion.png"));
			this.bulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/lightBlue.png"));
			this.RebelsbulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));
			this.bigBulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BlueBullet.png"));
			this.rebelTrooperLookingRight = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/RebelRight.png"));
			this.rebelTrooperLookingLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/RebelLeft.png"));
			this.DeathStar = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/background.png"));
			this.BossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/rsz_1solocrop.png"));
			this.PowerUpImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/PowerUp.png"));
			this.MenuImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/menupicture.png"));


		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"MegaMan!!! - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Draws a MegaMan image to the specified graphics canvas.
	 * @param MegaMan the ship to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */

	public void drawMegaMan (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaManImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFallR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFallRImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFireR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFireRImg, megaMan.x, megaMan.y, observer);	
	}
	public void drawMegaRunL (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megamanLeft, megaMan.x, megaMan.y, observer);	
	}
	public void drawMegaRunR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megamanRight, megaMan.x, megaMan.y, observer);	
	}
	public void drawMegaFireLeft (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megamanFireLeft, megaMan.x, megaMan.y, observer);	
	}
	public void drawMegaLookLeft (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megamanLookLeft, megaMan.x, megaMan.y, observer);	
	}

	public void drawFloor (Floor floor, Graphics2D g2d, ImageObserver observer, int i){
			g2d.drawImage(floorImg, floor.x, floor.y, observer);				
	}
	public void drawPlatform(Platform platform, Graphics2D g2d, ImageObserver observer, int i){
			g2d.drawImage(platformImg, platform.x , platform.y, observer);	
	}
	
	public void drawPlatform2 (Platform platform, Graphics2D g2d, ImageObserver observer, int i){
		g2d.drawImage(platformImg, platform.x , platform.y, observer);	
		}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBullet(Bullet bullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bulletImg, bullet.x, bullet.y, observer);
	}
	public void drawRebelsbulletImg(Bullet bullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(RebelsbulletImg, bullet.x, bullet.y, observer);
	}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bigBullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBigBullet(BigBullet bigBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigBulletImg, bigBullet.x, bigBullet.y, observer);
	}
	
	/**
	 * Draws an x-wing image to the specified graphics canvas.
	 * @param xWing the x-wing to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawXWingLeft(XWing xWing, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(xWingLeftImg, xWing.x, xWing.y, observer);
	}
	public void drawXWingRight(XWing xWing, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(xWingRightImg, xWing.x, xWing.y, observer);
	}


	/**
	 * Draws a MegaMan explosion image to the specified graphics canvas.
	 * @param megaManExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawMegaManExplosion(Rectangle megaManExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(megaManExplosionImg, megaManExplosion.x, megaManExplosion.y, observer);
	}

	/**
	 * Draws an x-wing explosion image to the specified graphics canvas.
	 * @param xWingExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawXWingExplosion(Rectangle xWingExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(xWingExplosionImg, xWingExplosion.x, xWingExplosion.y, observer);
	}

	public void drawBigAsteroidExplosion(Rectangle bigAsteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigAsteroidExplosionImg, bigAsteroidExplosion.x, bigAsteroidExplosion.y, observer);
	}
	
	public void drawRebelTrooperLookingLeft(RebelTrooper rebelTrooper, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(rebelTrooperLookingLeft, rebelTrooper.x, rebelTrooper.y, observer);
	}
	
	public void drawRebelTrooperLookingRight(RebelTrooper rebelTrooper, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(rebelTrooperLookingRight, rebelTrooper.x, rebelTrooper.y, observer);
	}
	public void drawBoss(Boss boss, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(BossImg, boss.x, boss.y, observer);
	}
	public void drawDeathStar(Graphics2D g2d) {
		g2d.drawImage(DeathStar, 0,0,LevelState.SCREEN_WIDTH,LevelState.SCREEN_HEIGHT,null);
	}
	public void drawPowerUp( PowerUp powerUp, Graphics2D g2d, Level3State level3State) {
		g2d.drawImage(PowerUpImg, powerUp.x, powerUp.y, 25, 25, null);
	}
	public void drawMenuPicture(Graphics2D g2d) {
		g2d.drawImage(MenuImg, 0,0,LevelState.SCREEN_WIDTH,LevelState.SCREEN_HEIGHT,null);
	}
}










