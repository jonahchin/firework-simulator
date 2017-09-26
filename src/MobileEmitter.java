


import java.util.ArrayList;

/**
 * An emitter that is linked to a Particle, so that it moves with the Particle.
 * @author Jonah Chin
 * @version 1.0
 */
public class MobileEmitter extends Emitter {

	private Particle followMe;

	/**
	 * The constructor for the MobileEmitter object.
	 * @param exitVelocity The exit velocity magnitude of the particles to be emitted in m/sec.
	 * @param firingAngle The launch angle of the emitter, from the vertical in degrees.
	 * @param variation The random variation range for the launch angle in degrees.
	 * @param numToLaunch The number of Particle objects to launch at a time, must be &gt;= 1.
	 * @param launchType An instance of the Particle to be launched. Acts as a template.
	 * @param mobile Provides a link to the Particle to which this emitter is to be attached.
	 * @throws EmitterException If the two angles are not legal. The firing angle must lie
	 * between -180 and 180 degrees, and the variation angle between 0 and 180 degrees, inclusive.
	 */
	public MobileEmitter(double exitVelocity, double firingAngle,
			double variation, int numToLaunch, Particle launchType, Particle mobile) throws EmitterException {
		super(mobile.getPosition(), mobile.getCreationTime(), mobile.getLifetime(), exitVelocity, firingAngle, variation,
				numToLaunch, launchType);
		// Do not clone this one!
		followMe = mobile;
	} // end Constructor

	/**
	 * Launches particles at the supplied time.  This emitter is mobile.
	 * New particles are cloned from the template and then modified so that their starting
	 * position matches the particle to which the emitter is attached. The velocity
	 * is also modified to include the velocity of the attached particle.
	 * @param time Time in seconds
	 * @return A collection of launched particles.
	 */
	public ArrayList<Particle> launch(double time) {
		double[] velocity = followMe.getVelocity();
		double vX = velocity[0];
		double vY = velocity[1];
		setPosition(followMe.getPosition());
		ArrayList<Particle> particles = super.launch(time);
		for (Particle part : particles) {
			velocity = part.getVelocity();
			velocity[0] = velocity[0] + vX;
			velocity[1] = velocity[1] + vY;
			part.setVelocity(velocity);
		}
		return particles;
	} // end launch

} // end MobileEmitter
