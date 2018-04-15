package rbadia.voidspace.sounds;

import java.applet.Applet;
import java.applet.AudioClip;

import rbadia.voidspace.main.Level1State;

/**
 * Manages and plays the game's sounds.
 */
public class SoundManager {
	private static final boolean SOUND_ON = true;

	private AudioClip shipExplosionSound = Applet.newAudioClip(Level1State.class.getResource(
			"/rbadia/voidspace/sounds/shipExplosion.wav"));
	private AudioClip ForceSound = Applet.newAudioClip(Level1State.class.getResource(
			"/rbadia/voidspace/sounds/ForceSound.wav"));
	private AudioClip screamSound = Applet.newAudioClip(Level1State.class.getResource(
			"/rbadia/voidspace/sounds/WilhelmScream.wav"));  

	/**
	 * Plays sound for bullets fired by the ship.
	 */
	public void playBulletSound(){
		if(SOUND_ON){
			new Thread(new Runnable(){
				public void run() {
					ForceSound.play();
				}
			}).start();
		}
	}

	/**
	 * Plays sound for ship explosions.
	 */
	public void playShipExplosionSound(){
		if(SOUND_ON){
			new Thread(new Runnable(){
				public void run() {
					shipExplosionSound.play();
				}
			}).start();
		}
	}

	/**
	 * Plays sound for asteroid explosions.
	 */
	public void playAsteroidExplosionSound(){
		// play sound for asteroid explosions
		if(SOUND_ON){
			new Thread(new Runnable(){
				public void run() {
					shipExplosionSound.play();
				}
			}).start();	
		}
	}
	public void playScreamSound(){
		// play sound for scream
		if(SOUND_ON){
			new Thread(new Runnable(){
				public void run() {
					screamSound.play();
				}
			}).start();	
		}
	}

}
