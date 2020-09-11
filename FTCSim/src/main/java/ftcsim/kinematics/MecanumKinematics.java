package ftcsim.kinematics;

import ftcsim.util.Matrix;

public class MecanumKinematics implements Kinematics {
	
	// Forward transformation matrix.
	private double[][] T;
	
	/**
	 * Creates a kinematic model of a mecanum drive with the given parameters.
	 * @param l1 Horizontal distance from the center to the wheels.
	 * @param l2 Vertical distance from the center to the wheels.
	 * @param r Radius of the wheels.
	 */
	public MecanumKinematics(double l1, double l2, double r) {
		T = new double[][] {
			{r / 4, r / 4, r / 4, r / 4},
			{r / 4, -r / 4, -r / 4, r / 4},
			{(-r / 4) / (l1 + l2), (r / 4) / (l1 + l2), (-r / 4) / (l1 + l2), (r / 4) / (l1 + l2)}
		};
	}
	
	/**
	 * Solves the kinematic equation. 
	 * @param w Rotation of the wheels in the form {w1, w2, w3, w4}.
	 * @return Overall displacement in the form {x, y, r).
	 */
	public double[] solve(double[] w) {
		return Matrix.transpose(Matrix.dot(T, Matrix.transpose(new double[][] {w})))[0];
	}
	
}
