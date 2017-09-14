/**
 * A class used to solve any size system of ordinary differential equations.
 * @author Jonah Chin
 * @version 1.1
 */
public class RungeKuttaSolver {

	/**
	 * Estimates the next point generated by the ODE system supplied to the constructor.
	 * Invokes the methods from the ode object that were specified by the ODESystem interface.
	 * @param ode The class, implementing ODESystem, that contains the system of differential equations
	 * to be solved.
	 * @param time The current time.  It is assumed that the point for this time is known and
	 * can be obtained from the ode object.
	 * @param deltaTime The time interval in seconds.
	 * @return Returns an array sized to the system size containing the estimates
	 * of the dependent variables at time + deltaTime.
	 */
	public static double[] getNextPoint(ODESystem ode, double time, double deltaTime) {
		int systemSize = ode.getSystemSize();
		double[] q1 = new double[systemSize];
		double[] q2 = new double[systemSize];
		double[] q3 = new double[systemSize];
		double[] q4 = new double[systemSize];
		double[] newVals = new double[systemSize];
		double[] intermediateVals = new double[systemSize];
		double halfTime = time + deltaTime / 2;
		double fullTime = time + deltaTime;
		int valNum;
		double[] values = ode.getVelocity();
		q1 = ode.getFunction(time, values);
		for (valNum = 0; valNum < systemSize; valNum++)
			intermediateVals[valNum] = values[valNum] + deltaTime * q1[valNum] / 2;
		q2 = ode.getFunction(halfTime, intermediateVals);
		for (valNum = 0; valNum < systemSize; valNum++)
			intermediateVals[valNum] = values[valNum] + deltaTime * q2[valNum] / 2;
		q3 = ode.getFunction(halfTime, intermediateVals);
		for (valNum = 0; valNum < systemSize; valNum++)
			intermediateVals[valNum] = values[valNum] + deltaTime * q3[valNum];
		q4 = ode.getFunction(fullTime, intermediateVals);
		for (valNum = 0; valNum < systemSize; valNum++)
			newVals[valNum] = values[valNum] + deltaTime * (q1[valNum] + 2 * q2[valNum] +
					2 * q3[valNum] + q4[valNum]) / 6;
		return newVals;
	} // end getNextPoint

} // end RungeKuttaSolver class
