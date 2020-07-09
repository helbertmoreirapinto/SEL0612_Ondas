
public class UtilScalarWave {
	public static double N_transition(double S) {
		double num = 2 * Math.PI * S;
		double den = Math.acos(1 - 2 * (Math.pow(S, 2)));
		return num / den;
	}

	public static double f_tau(double N, double S) {
		double x = 2 * Math.PI * S;
		double num = Math.cos(x / N) - 1;
		double den = Math.pow(S, 2);
		return 1.0 + (num / den);
	}

	public static double gaussian_pulse_0(double n, double S) {
		double arg = (-1) * Math.pow((n * S - 40), 2) / 200;
		return Math.exp(arg);
	}

	public static double gaussian_pulse_1(int n) {
		double arg = (-1) * Math.pow((n - 60), 2) / 200;
		return Math.exp(arg);
	}

	public static double gaussian_pulse_2(int n, double S) {
		double arg = (-1) * Math.pow((n - 60), 2) / 100;
		//double arg = (-1) * Math.pow((n - 50)/(S*20*S), 2);
		return Math.exp(arg);
	}

	public static double[][] FDTD(double[] S, int I, int N, int gauss_method) {
		double u[][] = new double[N][I];
		u[0][0] = (gauss_method == 0) ? gaussian_pulse_0(0, S[0]) : (gauss_method == 1) ? gaussian_pulse_1(0) : gaussian_pulse_2(0, S[0]);
		for (int i = 2; i < N; i++) {
			u[i][0] = (gauss_method == 0) ? gaussian_pulse_0(i, S[0]) : (gauss_method == 1) ? gaussian_pulse_1(i) : gaussian_pulse_2(i, S[0]);
			for (int j = 1; j < (I - 1); j++) {
				double pa = Math.pow(S[j], 2);
				double pb = u[i - 1][j + 1] - 2 * u[i - 1][j] + u[i - 1][j - 1];
				double pc = 2 * u[i - 1][j] - u[i - 2][j];
				u[i][j] = pa * pb + pc;
			}
		}
		return u;
	}
}
