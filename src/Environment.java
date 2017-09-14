/**
 * Contains all the information about the environment in which the Roman Candle is being fired.
 * This consists of the wind velocity, the air density and gravity constants.
 * @author Jonah Chin
 * @version 1.0
 */
public class Environment {

	private double windVelocity;					// m/sec
	/**
	 * Air density in kg per cubic metre at close to sea level.
	 */
	public final static double DENSITY_AIR = 1.2;	// kg/m^3
	/**
	 * The acceleration due to Earth's gravity in metres per second squared.
	 */
	public final static double G = 9.807;  			// m/s^2

	/**
	 * The constructor for the Environment object.
	 * @param wind The wind velocity in km/hour.
	 * @throws EnvironmentException If the magnitude of the wind velocity is above 20 km/hour.
	 */
	public Environment(double wind) throws EnvironmentException {
		if (wind < -20 || wind > 20)
			throw new EnvironmentException("Wind too high: " + wind);
		windVelocity = wind / 3.6;
	} // end Constructor

	/**
	 * Changes the wind velocity.  It is assumed that the wind only blows along
	 * the x axis.
	 * @param wind Wind velocity magnitude in km/hr.
	 */
	public void setWindVelocity(double wind) {
		windVelocity = wind / 3.6;
	} // end setWindVelocity

	/**
	 * The wind velocity accessor.
	 * @return The wind velocity in m/sec.
	 */
	public double getWindVelocity() {
		return windVelocity;
	} // end getWindVelocity

} // end Environment
