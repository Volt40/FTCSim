package ftcsim.util;

/**
 * A final class with matrix operations.
 */
public final class Matrix {
	
	/**
	 * Computes the dot product.
	 * @param A Matrix A.
	 * @param B Matrix B.
	 * @return A dot B.
	 */
	public static double[][] dot(double[][] A, double[][] B) {
		double[][] X = new double[A.length][B[0].length];
		for (int i = 0; i < X.length; i++)
			for (int j = 0; j < X[0].length; j++)
				for (int k = 0; k < A[0].length; k++)
					X[i][j] += A[i][k] * B[k][j];
		return X;
	}
	
	/**
	 * Transposes the matrix.
	 * @param A Matrix A.
	 * @return A transposed.
	 */
	public static double[][] transpose(double[][] A) {
		double[][] X = new double[A[0].length][A.length];
		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A[0].length; j++)
				X[j][i] = A[i][j];
		return X;
	}
	
}
