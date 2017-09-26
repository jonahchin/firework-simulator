package org.fireworksimulator.controller;



import java.util.ArrayList;

import org.fireworksimulator.model.BurningParticle;
import org.fireworksimulator.model.Emitter;
import org.fireworksimulator.model.Environment;
import org.fireworksimulator.model.MobileEmitter;
import org.fireworksimulator.model.Particle;
import org.fireworksimulator.model.Streak;
import org.fireworksimulator.util.EmitterException;
import org.fireworksimulator.util.EnvironmentException;

import javafx.scene.paint.Color;

/**
 * This class manages the simulation.  It launches 8 stars of various colours.
 * The class also manages all the other particle effects: the sparks emitted by the star, the
 * launch sparks and the delay charge sparks.  Many constants are available to alter
 * the way the simulation works.  These can be altered more easily when the simulation
 * can be viewed.
 * @author Jonah Chin
 * @version 2.0
 */
public class ParticleManager {

	// Simulation parameters:
	// For the Star
	private static final double STAR_VELOCITY = 		22.0;	// m/sec
	private static final double STAR_ANGLE_VARIATION = 	2.0;	// degrees
	private static final double STAR_DELAY_TIME = 		2.8;	// seconds
	private static final int 	NUM_STARS = 			8;		// Total number to launch
	private static final Color[] STAR_COLOURS =
		{Color.AQUAMARINE, Color.DARKKHAKI, Color.ORANGE, Color.RED, Color.YELLOW, Color.WHITE, Color.CYAN, Color.MAGENTA};
	private static final double STAR_MASS = 			0.008;	// kg
	private static final double STAR_BURN_RATE = 		0.003;	// kg/sec
	private static final double STAR_DENSITY = 			1900.0;	// kg/m^3

	// For all sparks
	private static final Color 	SPARK_COLOUR = 			Color.ORANGE;
	private static final double SPARK_RADIUS = 			0.0015;	// metre
	private static final double SPARK_MASS = 			2.0E-6;	// kg

	// For sparks emitted by the Star
	private static final double STAR_SPARK_VELOCITY = 			3.0;	// m/sec
	private static final double STAR_SPARK_ANGLE_VARIATION = 	180.0;	// degrees
	private static final double STAR_SPARK_LIFETIME = 			0.20;	// seconds
	private static final int 	NUM_LAUNCH_STAR_SPARKS = 		20;

	// For launch streaks
	private static final double STREAK_VELOCITY = 		20.0;	// m/sec
	private static final double STREAK_ANGLE_VARIATION = 3.0;	// degrees
	private static final double STREAK_RADIUS = 		0.0005;	// metre
	private static final double STREAK_LIFETIME = 		0.15;	// seconds
	private static final int 	NUM_LAUNCH_STREAKS = 	20;

	// For delay charge sparks
	private static final double DELAY_SPARK_VELOCITY = 			2.2;	// m/sec
	private static final double DELAY_SPARK_ANGLE_VARIATION = 	90.0;	// degrees
	private static final double DELAY_SPARK_LIFETIME = 			0.60;	// seconds
	private static final int 	NUM_LAUNCH_DELAY_SPARKS = 		5;

	// This ArrayList will hold all the generated particles.
	private ArrayList<Particle> fireworks = new ArrayList<>();
	private Environment env;

	// Tracking variables
	private int countStars = 0;
	private double starLaunchTime;
	private double lastTime;
	private boolean launchFlag = false;

	// Various template and Emitter variables
	private BurningParticle theStar;
	private BurningParticle starTemplate;
	private Particle starSparkTemplate;
	private Particle delaySparkTemplate;
	private Streak streakTemplate;
	private MobileEmitter starSparkEmitter;
	private Emitter launchTube;
	private Emitter delaySparkEmitter;
	private Emitter streakEmitter;

	/**
	 * The ParticleManager constructor.  Creates various templates and emitters.
	 * @param windVelocity The wind velocity in m/sec.
	 * @param launchAngle The launch angle of the Roman candle in degrees off the vertical.
	 * @throws EnvironmentException If the wind velocity is not between -20 and 20 m/sec.
	 * @throws EmitterException If the launch angle is not between -15 and 15 degrees.
	 */
	public ParticleManager(double windVelocity, double launchAngle) throws EnvironmentException, EmitterException {
		env = new Environment(windVelocity);
		double la = Math.PI * launchAngle / 180.0;	// radians
		double[] position = new double[2];
		position[0] = Math.sin(la);
		position[1] = Math.cos(la);
		double launchTubeLifetime = STAR_DELAY_TIME * NUM_STARS;
		starTemplate = new BurningParticle(STAR_MASS, STAR_COLOURS[0], STAR_BURN_RATE, STAR_DENSITY);
		starSparkTemplate = new Particle(STAR_SPARK_LIFETIME, SPARK_MASS, SPARK_RADIUS, STAR_COLOURS[0]);
		delaySparkTemplate = new Particle(DELAY_SPARK_LIFETIME, SPARK_MASS, SPARK_RADIUS, SPARK_COLOUR);
		streakTemplate = new Streak(position, STREAK_LIFETIME, SPARK_MASS, STREAK_RADIUS, SPARK_COLOUR);
		launchTube = new Emitter(position, 0, launchTubeLifetime, STAR_VELOCITY,
				launchAngle, STAR_ANGLE_VARIATION, 1, starTemplate);
		delaySparkEmitter = new Emitter(position, 0, launchTubeLifetime, DELAY_SPARK_VELOCITY,
				launchAngle, DELAY_SPARK_ANGLE_VARIATION, NUM_LAUNCH_DELAY_SPARKS, delaySparkTemplate);
		streakEmitter = new Emitter(position, 0, launchTubeLifetime, STREAK_VELOCITY,
				launchAngle, STREAK_ANGLE_VARIATION, NUM_LAUNCH_STREAKS, streakTemplate);
		lastTime = 0;
	} // end Constructor.

	/**
	 * Launches a single star at the supplied absolute time and adds one set of streaks.
	 * @param time The absolute time in seconds.  The first star will be launched at time=0.
	 * @throws EmitterException If the mobile emitter cannot be created.
	 */
	public void start(double time) throws EmitterException {
		theStar = (BurningParticle)launchTube.launch(time).get(0);
		starLaunchTime = time;
		theStar.setColour(STAR_COLOURS[countStars]);
		fireworks.add(theStar);
		starSparkTemplate.setColour(theStar.getColour());
		starSparkEmitter = new MobileEmitter(STAR_SPARK_VELOCITY, 0, STAR_SPARK_ANGLE_VARIATION,
				NUM_LAUNCH_STAR_SPARKS, starSparkTemplate, theStar);
		countStars++;
		fireworks.addAll(streakEmitter.launch(time));
		launchFlag = true;
	} // end start method

	/**
	 * This method updates the simulation.  "Dead" fireworks are removed and positions
	 * updated.
	 * @param time The absolute time in seconds. The simulation was started at time = 0;
	 */
	private void update(double time) {
		double deltaTime = time - lastTime;
		lastTime = time;
		int index = 0;
		// Clean out dead fireworks
		do {
			if (!fireworks.get(index).isAlive(time))
				fireworks.remove(index);
			else
				index++;
		} while (fireworks.size() > 0 && index < fireworks.size());
		// Update positions
		for (Particle fire : fireworks)
			fire.updatePosition(time, deltaTime, env);
		// Keep adding delay charge sparks until delay time is past.
		if (time - starLaunchTime < STAR_DELAY_TIME)
			fireworks.addAll(delaySparkEmitter.launch(time));
		else if (countStars < NUM_STARS)
			try {
				start(time);					// Launch another Star
			} catch (EmitterException e1) {}	// Won't get here
		else
			return;		// Simulation is over
		// Add star sparks as long as the Star exists
		if (theStar.isAlive(time))
			fireworks.addAll(starSparkEmitter.launch(time));
	} // end update

	/**
	 * An accessor for the collection of particles.
	 * @param time The absolute time in seconds. The simulation started at time = 0.
	 * @return The collection of particles in an ArrayList.
	 */
	public ArrayList<Particle> getFireworks(double time) {
		update(time);
		ArrayList<Particle> copy = new ArrayList<>(fireworks.size());
		for (Particle firework : fireworks)
			copy.add(firework.clone());
		return copy;
	} // end getFireworks

	/**
	 * Returns a flag used to indicate when a Star has been launched. Intended for use
	 * with resetLaunchFlag() which resets the flag back to false.
	 * @return A flag that is true if a Star has just been launched.
	 */
	public boolean getLaunchFlag() { return launchFlag; }

	/**
	 * Resets a flag that is used to track the launch of a Star once the launch has
	 * been detected.
	 */
	public void resetLaunchFlag() { launchFlag = false; }

	/**
	 * A mutator for the wind velocity that allows it to be changed while the simulation
	 * is running
	 * @param wind in km/hour
	 * @throws EnvironmentException If the velocity is illegal.
	 */
	public void setWindVelocity(double wind) throws EnvironmentException {
		env.setWindVelocity(wind);
	} // end setWindVelocity

	/**
	 * A mutator for the launch angle that allows it to be changed while the
	 * simulation is running.
	 * @param firingAngle The angle in degrees.
	 * @throws EmitterException If the angle is not legal.
	 */
	public void setLaunchAngle(double firingAngle) throws EmitterException {
		launchTube.setLaunchAngle(firingAngle);
		streakEmitter.setLaunchAngle(firingAngle);
	} // end setLaunchAngle

	/**
	 * Allows the position of the launch tube end to be moved during the simulation.
	 * @param position An array of (x, y) positions in metres.
	 */
	public void setTipPosition(double[] position) {
		launchTube.setPosition(position);
		delaySparkEmitter.setPosition(position);
		streakTemplate.setOrigin(position);
		streakEmitter.setPosition(position);
	} // end setTipPosition

} // end ParticleManager class
