package org.fireworksimulator.model;



/**
 * A base class for all Emitter and Particle objects.  This class holds position and
 * lifetime data.
 * @author Jonah Chin
 * @version 2.0
 */
public class Firework {

	private double[] position = new double[2];	// metres
	private double creationTime;				// sec
	private double lifetime;					// sec

	/**
	 * The Firework constructor.
	 * @param position The x and y positions in metres
	 * @param creationTime The time of the creation of the object in seconds.
	 * @param lifetime The lifetime of the object in seconds.
	 */
	public Firework(double[] position, double creationTime, double lifetime) {
		this.position = position.clone();
		this.creationTime = creationTime;
		this.lifetime = lifetime;
	} // end Constructor

	/**
	 * An accessor for position data.
	 * @return An cloned array for the (x, y) position in metres.
	 */
	public double[] getPosition() { return position.clone(); }

	/**
	 * An accessor for the creation time of a Firework.
	 * @return The creation time in seconds.
	 */
	public double getCreationTime() { return creationTime; }

	/**
	 * An accessor for the lifetime of a Firework.
	 * @return The lifetime in seconds.
	 */
	public double getLifetime() { return lifetime; }

	/**
	 * Returns true if the Firework is still within its lifetime, false otherwise.
	 * @param time The absolute time in seconds.
	 * @return true if the Firework is still alive, false otherwise
	 */
	public boolean isAlive (double time) {
		return time - creationTime <= lifetime;
	} // end isAlive

	/**
	 * A mutator for the position data.
	 * @param position An array containing the (x, y) position data in metres.
	 */
	public void setPosition(double[] position) {
		this.position = position.clone();
	} // end setPosition

	/**
	 * A mutator for the creation time of the Firework.
	 * @param time The time the Firework was created in seconds.
	 */
	public void setCreationTime(double time) {
		creationTime = time;
	} // end setCreationTime

	/**
	 * Returns a string representation of the position.
	 * @return A string containing the position in metres.
	 */
	public String toString() {
		double posX = position[0];
		double posY = position[1];
		return String.format("%5.2f, %5.2f", posX, posY);
	} // end toString

} // end Firework
