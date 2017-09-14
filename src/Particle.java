import javafx.scene.paint.Color;

/**
 * A base class for all particles.
 * @author Jonah Chin
 * @version 2.0
 */
public class Particle extends Firework implements ODESystem {

	private static final double DRAG_COEFF = 0.4;		// unitless
	private static final int SYSTEM_SIZE = 2;

	private double[] velocity = new double[2];	// metre/sec
	private double radius;						// metre
	private double mass;						// kg
	private double wind;						// m/sec
	private Color colour;

	/**
	 * The Particle constructor.  Used by the clone method.
	 * @param initialPosition The initial position of the particle as an array of x and y positions in metres.
	 * @param initialVelocity The initial velocity of the particle as an array of vx and vy components in m/sec.
	 * @param creationTime The absolute time of creation of the particle in seconds.
	 * @param lifetime The lifetime of the particle in seconds.
	 * @param mass The mass of the particle in kg.
	 * @param radius The radius of the particle in metres.
	 * @param colour The colour of the particle.
	 */
	public Particle(double[] initialPosition, double[] initialVelocity,
			double creationTime, double lifetime, double mass, double radius, Color colour) {
		super(initialPosition, creationTime, lifetime);
		velocity = initialVelocity.clone();
		this.mass = mass;
		this.radius = radius;
		this.colour = colour;
	} // end full constructor

	/**
	 * A constructor used to create Particle templates.
	 * @param lifetime The lifetime of the particle in seconds.
	 * @param mass The mass of the particle in kg.
	 * @param radius The radius of the particle in metres.
	 * @param colour The colour of the particle.
	 */
	public Particle(double lifetime, double mass, double radius, Color colour) {
		super(new double[2], 0, lifetime);
		velocity = new double[2];
		this.mass = mass;
		this.radius = radius;
		this.colour = colour;
	} // end constructor

	/**
	 * An accessor for the particle colour.
	 * @return The colour of the particle.
	 */
	public Color getColour() { return colour; }

	/**
	 * An accessor for the velocity data.
	 * @return An array for the (vx, vy) velocity data in m/sec.
	 */
	public double[] getVelocity() { return velocity.clone(); }

	/**
	 * An accessor the mass of the particle.
	 * @return The mass in kg.
	 */
	public double getMass() { return mass; }

	/**
	 * An accessor the radius of the particle.
	 * @return The radius in metres.
	 */
	public double getRadius() { return radius; }

	/**
	 * A mutator for the radius of a particle.
	 * @param radius The radius in metres.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	} // end setRadius

	/**
	 * A mutator for the mass of a non-Newtonian particle.
	 * @param mass The mass in kg.
	 */
	public void setMass(double mass) {
		this.mass = mass;
	} // end setMass

	/**
	 * A mutator for the velocity of the particle.
	 * @param vel An array containing (vx, vy) in m/sec.
	 */
	public void setVelocity(double[] vel) {
		velocity = vel.clone();
	} // end setVelocity

	/**
	 * A mutator for the colour of the particle.
	 * @param colour The colour as an instance of the javafx.scene.paing.color class.
	 */
	public void setColour(Color colour) {
		this.colour = colour;
	} // end setColour

	public int getSystemSize() { return SYSTEM_SIZE; }

	// Returns the velocity magnitude in m/sec, given the two
	// velocity components.
	private double getVelocityMag(double vx, double vy) {
		return Math.sqrt(vx * vx + vy * vy);
	} // end getVelocityMag

	// Calculates the magnitude of the drag force on the star, given time in
	// seconds and the two velocity components in m/sec.
	private double getDragForce(double vx, double vy) {
		double velocityMag = getVelocityMag(vx, vy);
		double area = Math.PI * radius * radius;
		return Environment.DENSITY_AIR * velocityMag * velocityMag * area * DRAG_COEFF / 2;
	} // end getDragForce

	// This method returns the value of the fx function, given the
	// two velocity components in m/sec.
	// The meaning of fx is described in the assignment statement.
	private double xDE(double vX, double vY) {
		// Use apparent x velocity to calculate drag.
		double vxa = vX - wind;
		double velocityMag = getVelocityMag(vxa, vY);
		double dragForce = getDragForce(vxa, vY);
		return -dragForce * vxa / (mass * velocityMag);
	} // end xDE

	// This method returns the value of the fy function, given the
	// two velocity components in m/sec.
	// The meaning of fy is described in the assignment statement.
	private double yDE(double vX, double vY) {
		// Use apparent x velocity to calculate drag.
		double vxa = vX - wind;
		double velocityMag = getVelocityMag(vxa, vY);
		double dragForce = getDragForce(vxa, vY);
		return -Environment.G - dragForce * vY / (mass * velocityMag);
	} // end yDE

	public double[] getFunction(double time, double[] values) {
		double[] functionVal = new double[SYSTEM_SIZE];
		double vX = values[0];
		double vY = values[1];
		functionVal[0] = xDE(vX, vY);
		functionVal[1] = yDE(vX, vY);
		return functionVal;
	} // end getFunction

	/**
	 * A mutator that updates the current position of the particle.
	 * @param time The absolute time in seconds.
	 * @param deltaTime The time interval in seconds.
	 * @param env An instance of the current Environment object is needed to supply the
	 * wind velocity, which is used to calculate the apparent velocity.
	 */
	public void updatePosition(double time, double deltaTime, Environment env) {
		time = time - getCreationTime();
		wind = env.getWindVelocity();
		double[] newValues = RungeKuttaSolver.getNextPoint(this, time, deltaTime);
		velocity[0] = newValues[0];
		velocity[1] = newValues[1];
		double xVelocity = newValues[0];
		double yVelocity = newValues[1];
		double[] positions = getPosition();
		double xPos = positions[0] + xVelocity * deltaTime;
		double yPos = positions[1] + yVelocity * deltaTime;
		newValues[0] = xPos;
		newValues[1] = yPos;
		setPosition(newValues);
	} // end updatePosition

	/**
	 * Returns a full clone of the current Particle.
	 */
	public Particle clone() {
		Particle newParticle = new Particle(getPosition(), velocity,
				getCreationTime(), getLifetime(), mass, radius, colour);
		return newParticle;
	} // end clone

} // end Particle
