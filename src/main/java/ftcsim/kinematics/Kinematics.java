package ftcsim.kinematics;

/**
 * The kinematics for a simulation.
 */
public interface Kinematics {
	
	/**
	 * Solves the kinematics equation. 
	 * @param w Rotation of the wheels.
	 * @return Overall displacement.
	 */
	public double[] solve(double[] w);
	
}
