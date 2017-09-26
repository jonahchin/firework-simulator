


import java.util.ArrayList;

/**
 * The base class for Emitter objects.
 * @author Jonah Chin
 * @version 2.0
 */
public class Emitter extends Firework {

	private double launchAngle = 0;				// radians
	private double launchAngleVariation = 0;	// radians
	private double exitVelocity;				// m/sec
	private int numToLaunch;
	private Particle launchType;

	/**
	 * The constructor for an Emitter object.
	 * @param position An array containing the initial x and y position in metres.
	 * @param creationTime The time when the emitter was created in seconds.
	 * @param lifetime The lifetime of the emitter in seconds.
	 * @param exitVelocity The launch velocity of the sparks from the emitter.
	 * @param firingAngle The launch angle of the emitter, from the vertical in degrees.
	 * @param variation The random variation range for the launch angle in degrees.
	 * @param numToLaunch The number of Particle objects to launch at a time, must be &gt;= 1.
	 * @param launchType An instance of the Particle to be launched. Acts as a template.
	 * @throws EmitterException If the two angles are not legal. The firing angle must lie
	 * between -180 and 180 degrees, and the variation angle between 0 and 180 degrees, inclusive.
	 */
	public Emitter (double[] position, double creationTime, double lifetime,
			double exitVelocity, double firingAngle, double variation, int numToLaunch,
			Particle launchType) throws EmitterException {
		super(position, creationTime, lifetime);
		this.exitVelocity = exitVelocity;
		if (firingAngle < -180 || firingAngle > 180)
			throw new EmitterException("Firing angle out of range: " + firingAngle);
		else
			launchAngle = firingAngle * Math.PI / 180.0;
		if (variation < 0 || variation > 180)
			throw new EmitterException("Firing angle variation out of range: " + variation);
		else
			launchAngleVariation = variation * Math.PI / 180.0;
		if (numToLaunch < 1)
			throw new EmitterException("Must launch one or mor Particles.");
		else
			this.numToLaunch = numToLaunch;
		this.launchType = launchType;
	} // end constructor

	// Calculates and returns an angle randomly generated between (firing angle - variation)
	// and (firing angle + variation) in radians.
	private double getRandomLaunchAngle() {
		return launchAngle + launchAngleVariation * 2 * (Math.random() - 0.5);
	} // end getRandomLaunchAngle

	// Adds some variation to the exit velocity.  Returns a value in m/sec.
	private double getRandomExitVelocity() {
		return exitVelocity - 0.1 * exitVelocity * (Math.random() - 0.5);
	} // end getRandomExitVelocity

	/**
	 * Allows the launch angle to be mutable.
	 * @param firingAngle The new angle in degrees away from the vertical.
	 * @throws EmitterException Thrown if the angle is less than -15 or greater than
	 * 15 degrees.
	 */
	public void setLaunchAngle(double firingAngle) throws EmitterException {
		if (firingAngle < -15 || firingAngle > 15)
			throw new EmitterException("Launch angle out of range.");
		launchAngle = firingAngle * Math.PI / 180.0;;
	} // end setLaunchAngle

	/**
	 * Launches particles at the supplied time.  Assumes this emitter is stationary.
	 * New particles are cloned from the template and then modified.
	 * @param time Time in seconds
	 * @return A collection of launched particles.
	 */
	public ArrayList<Particle> launch(double time) {
		double angle;
		double[] position = getPosition();
		double[] velocity = new double[2];
		double variableExitVelocity;
		ArrayList<Particle> particles = new ArrayList<>(numToLaunch);
		Particle toLaunch;
		for (int i = 0; i < numToLaunch; i++) {
			angle = getRandomLaunchAngle();
			variableExitVelocity = getRandomExitVelocity();
			velocity[0] = variableExitVelocity * Math.sin(angle);
			velocity[1] = variableExitVelocity * Math.cos(angle);
			toLaunch = launchType.clone();
			toLaunch.setPosition(position);
			toLaunch.setVelocity(velocity);
			toLaunch.setCreationTime(time);
			particles.add(toLaunch);
		}
		return particles;
	} // end launch

} // end Emitter class
