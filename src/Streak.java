import javafx.scene.paint.Color;

/**
 * A particle that will be drawn as a line going from the position of the particle
 * to its origin position.
 * @author Jonah Chin
 * @version 1.0
 */
public class Streak extends Particle {

	private double[] origin;

	/**
	 * The Streak constructor.
	 * @param initialPosition The initial position of the particle as an array of x and y positions in metres.
	 * @param initialVelocity The initial velocity of the particle as an array of vx and vy components in m/sec.
	 * @param creationTime The absolute time of creation of the particle in seconds.
	 * @param lifetime The lifetime of the particle in seconds.
	 * @param mass The mass of the particle in kg.
	 * @param radius The radius of the particle in metres.
	 * @param colour The colour of the particle.
	 */
	public Streak(double[] initialPosition, double[] initialVelocity,
			double creationTime, double lifetime, double mass, double radius, Color colour) {
		super(initialPosition, initialVelocity, creationTime, lifetime, mass, radius, colour);
		origin = initialPosition.clone();
	} // end full Constructor

	/**
	 * This constructor allows the creation of a template Streak object.
	 * @param position The initial position of the particle as an array of x and y positions in metres.
	 * @param lifetime The lifetime of the particle in seconds.
	 * @param mass The mass of the particle in kg.
	 * @param radius The radius of the particle in metres.
	 * @param colour The colour of the particle.
	 */
	public Streak(double[] position, double lifetime, double mass, double radius, Color colour) {
		super(lifetime, mass, radius, colour);
		origin = position.clone();
	} // end Constructor

	/**
	 * An accessor for the origin position of the Streak.
	 * @return An array of (x, y) with values in metres.
	 */
	public double[] getOrigin() { return origin.clone(); }

	/**
	 * A mutator for the origin position of a Streak.
	 * @param position An array of (x, y) with values in metres.
	 */
	public void setOrigin(double[] position) {
		origin = position.clone();
	} // end setOrigin

	/**
	 * Returns a full copy of the Streak object.
	 */
	public Streak clone() {
		Streak retStreak = new Streak(origin, getVelocity(), getCreationTime(), getLifetime(),
				getMass(), getRadius(), getColour());
		retStreak.setPosition(getPosition());
		return retStreak;
	} // end clone

} // end Streak
