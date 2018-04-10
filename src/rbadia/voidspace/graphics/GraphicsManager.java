package rbadia.voidspace.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.model.Asteroid;
//import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
//import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Floor;
//import rbadia.voidspace.model.BulletBoss;
//import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.StormTrooper;

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
	private BufferedImage bigBulletImg;
	private BufferedImage asteroidImg;
	private BufferedImage asteroidExplosionImg;
	private BufferedImage megaManExplosionImg;
	private BufferedImage bigAsteroidExplosionImg;
	private BufferedImage megamanLeft;
	private BufferedImage megamanRight;
	private BufferedImage megamanFireLeft;
	private BufferedImage megamanLookLeft;
	private BufferedImage stormTrooperLookingRight;
	private BufferedImage stormTrooperLookingLeft;
	private BufferedImage DeathStar;

	/**
	 * Creates a new graphics manager and loads the game images.
	 */
	public GraphicsManager(){
		// load images
		try {
			this.megaManImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3.png"));
			this.megaFallRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallRight.png"));
			this.megaFireRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireRight.png"));
			this.megamanLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaRunLeft.gif"));
			this.megamanRight = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaRunRight.gif"));
			this.megamanFireLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireLeft.png"));
			this.megamanLookLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaLookLeft.png"));
			this.floorImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFloor.png"));
			this.platformImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/platform3.png"));
			this.asteroidImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroid.png"));
			this.asteroidExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroidExplosion.png"));
			this.bulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bullet.png"));
			this.bigBulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));
			this.stormTrooperLookingRight = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/stormtrooperLookingRight.png"));
			this.stormTrooperLookingLeft = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/stormtrooperLookingLeft.png"));
			this.DeathStar = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/DeathStar.png"));

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
	 * Draws an asteroid image to the specified graphics canvas.
	 * @param asteroid the asteroid to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroid(Asteroid asteroid, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidImg, asteroid.x, asteroid.y, observer);
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
	 * Draws an asteroid explosion image to the specified graphics canvas.
	 * @param asteroidExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroidExplosion(Rectangle asteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidExplosionImg, asteroidExplosion.x, asteroidExplosion.y, observer);
	}

	public void drawBigAsteroidExplosion(Rectangle bigAsteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigAsteroidExplosionImg, bigAsteroidExplosion.x, bigAsteroidExplosion.y, observer);
	}
	
	public void drawStormTrooperLookingLeft(StormTrooper stormTrooper, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(stormTrooperLookingLeft, stormTrooper.x, stormTrooper.y, observer);
	}
	
	public void drawStormTrooperLookingRight(StormTrooper stormTrooper, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(stormTrooperLookingRight, stormTrooper.x, stormTrooper.y, observer);
	}
	public void drawDeathStar(Graphics2D g2d) {
		g2d.drawImage(DeathStar, 0,0,530,480,null);
	}



}










