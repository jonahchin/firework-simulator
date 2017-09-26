package org.fireworksimulator.model;




import javafx.scene.paint.Color;

/**
 * An object that describes a particle whose mass is burning away.
 * The object requires the burn rate in kg/sec, the density in kg/m^3 and the starting
 * mass in kg.
 *
 * @author Jonah Chin
 * @version 1.0
 */
public class BurningParticle extends Particle {

	private double burnRate;
	private double density;
	private double startingMass;

	/**
	 * The constructor for the BurningParticle class.  Used only by the clone()
	 * method.  It is assumed that this particle has a spherical shape.
	 * @param initialPosition An array containing the x and y positions in metres.
	 * @param intialVelocity An array containing the initial velocities in m/sec.
	 * @param creationTime The time of creation of the particle in seconds.
	 * @param lifetime The lifetime of the particle in seconds.
	 * @param mass The starting mass of the particle in kg.
	 * @param radius The starting radius of the particle in metres.
	 * @param colour The colour of the particle.
	 * @param burnRate The burn rate, assumed constant, in kg/sec.
	 * @param density The density of the particle in kg/m^3.
	 */
	public BurningParticle(double[] initialPosition, double[] intialVelocity,
			double creationTime, double lifetime, double mass, double radius, Color colour,
			double burnRate, double density) {
		super(initialPosition, intialVelocity, creationTime, lifetime, mass, radius, colour);
		startingMass = mass;
		this.burnRate = burnRate;
		this.density = density;
	} // end full parameter Constructor

	/**
	 * A constructor used when creating a template object for this particle.
	 * @param mass The starting mass of the particle in kg.
	 * @param colour The colour of the particle.
	 * @param burnRate The burn rate, assumed constant, in kg/sec.
	 * @param density The density of the particle in kg/m^3.
	 */
	public BurningParticle(double mass, Color colour, double burnRate, double density) {
		this(new double[2], new double[2], 0, mass / burnRate, mass,
				Math.pow(3 * (mass / density) / (4 * Math.PI), 1.0 / 3.0), colour, burnRate, density);
	} // end Constructor

	/**
	 * A mutator that updates the current position of the particle.  It also updates
	 * the mass and radius of the particle since these are changing as the particle
	 * burns.
	 * @param time The current time in seconds.
	 * @param deltaTime The time interval in seconds.
	 * @param env An instance of the current Environment object is needed to supply the
	 * wind velocity, which is used to calculate the apparent velocity.
	 */
	public void updatePosition(double time, double deltaTime, Environment env) {
		double mass = startingMass - (time - getCreationTime()) * burnRate;
		double volume = mass / density;
		double radius = Math.pow(3 * volume / (4 * Math.PI), 1.0 / 3.0);
		setMass(mass);
		setRadius(radius);
		super.updatePosition(time, deltaTime, env);
	} // end updatePosition

	/**
	 * Returns a fully defined clone of the current particle.
	 */
	public BurningParticle clone() {
		BurningParticle newParticle = new BurningParticle(getPosition(), getVelocity(),
				getCreationTime(), getLifetime(), getMass(), getRadius(), getColour(),
				burnRate, density);
		return newParticle;
	} // end clone

} // end BurningParticle
