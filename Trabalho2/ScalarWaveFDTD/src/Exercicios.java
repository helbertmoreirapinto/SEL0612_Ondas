
public class Exercicios {
	public static void fig_2_1(double S, int n) {
		double N_trans_S = UtilScalarWave.N_transition(S);
		double N[] = vector_range(1, 10, n);
		double at[] = new double[n + 1];
		double Vp[] = new double[n + 1];
		for (int i = 0; i <= n; i++) {
			double tau = UtilScalarWave.f_tau(N[i], S);
			double k = (N[i] <= N_trans_S) ? Math.PI : Math.acos(tau);
			if (N[i] <= N_trans_S)
				at[i] = (-1) * Math.log((-1) * (tau + Math.sqrt(Math.pow(tau, 2) - 1)));
			Vp[i] = (2 * Math.PI) / (N[i] * k);
		}

		DataGraphic[] data = new DataGraphic[2];
		data[0] = new DataGraphic(at, "Attenuation Constant");
		data[1] = new DataGraphic(Vp, "Phase Velocity");

		Graphics.plot("Fig2.1", false, "nome x", "nome y", data);
	}

	public static void fig_2_2(double S, int n) {
		double N[] = vector_range(3, 80, n);
		double Vp[] = new double[n + 1];
		for (int i = 0; i <= n; i++) {
			double tau = UtilScalarWave.f_tau(N[i], S);
			double k = Math.acos(tau);
			Vp[i] = (2 * Math.PI) / (N[i] * k);
			Vp[i] = 100 * (1 - Vp[i]);
		}

		DataGraphic[] data = new DataGraphic[1];
		data[0] = new DataGraphic(Vp, "Phase Velocity Error");

		Graphics.plot("Fig2.2", true, "nome x", "nome y", data);
	}

	public static void fig_2_3() {
		DataGraphic[] data = new DataGraphic[2];
		double S0 = 0.5;
		int I = 200;
		int N = (int) (I / S0);
		double S[] = init_vector(I - 1, S0);
		double[][] u = UtilScalarWave.FDTD(S, I, N, 0);
		data[0] = new DataGraphic(u[(int) (190 / S0)], "S = 0.5");

		S0 = 1;
		S = init_vector(I - 1, S0);
		u = UtilScalarWave.FDTD(S, I, N, 0);
		data[1] = new DataGraphic(u[(int) (190 / S0)], "S = 1");

		Graphics.plot("Fig2.3", false, "nome x", "nome y", data);

	}

	public static void fig_2_5() {
		int I = 200;
		int N = 400;
		double S[] = new double[I];
		for (int i = 0; i < I; i++)
			S[i] = (i < 140) ? 1 : .25;
		double u[][] = UtilScalarWave.FDTD(S, I, N, 0);
		DataGraphic[] data = new DataGraphic[1];
		data[0] = new DataGraphic(u[250], "Wavefunction");

		Graphics.plot("Fig2.5", false, "nome x", "nome y", data);
	}

	public static void fig_2_6() {
		int I = 220;
		int N = 400;
		double S[] = init_vector(I, 1.0005);
		double u[][] = UtilScalarWave.FDTD(S, I, N, 1);
		double v[][] = new double[3][20];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 20; j++)
				v[i][j] = u[200 + 10 * i][j];

		DataGraphic[] data = new DataGraphic[3], data2 = new DataGraphic[3];
		data[0] = new DataGraphic(u[200], "n = 200");
		data2[0] = new DataGraphic(v[0], "n = 200");
		data[1] = new DataGraphic(u[210], "n = 210");
		data2[1] = new DataGraphic(v[1], "n = 210");
		data[2] = new DataGraphic(u[220], "n = 220");
		data2[2] = new DataGraphic(v[2], "n = 220");
		Graphics.plot("Fig2.6a", false, "nome x", "nome y", data);
		Graphics.plot("Fig2.6b", false, "nome x", "nome y", data2);
	}

	public static void fig_2_7() {
		int I = 220;
		int N = 300;
		double S[] = init_vector(I, 1);
		S[89] = 1.075;
		double u[][] = UtilScalarWave.FDTD(S, I, N, 2);
		double v[][] = new double[2][40];
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 40; j++)
				v[i][j] = u[190 + 10 * i][70 + j];
		DataGraphic[] data = new DataGraphic[2], data2 = new DataGraphic[2];
		data[0] = new DataGraphic(u[190], "n = 190");
		data2[0] = new DataGraphic(v[0], "n = 200");
		data[1] = new DataGraphic(u[200], "n = 200");
		data2[1] = new DataGraphic(v[1], "n = 200");
		Graphics.plot("Fig2.7a", false, "nome x", "nome y", data);
		Graphics.plot("Fig2.7b", false, "nome x", "nome y", data2);
	}

	private static double[] vector_range(int begin, int end, int div) {
		int range = end - begin;
		double vector[] = new double[div + 1];
		for (int i = 0; i <= div; i++) {
			vector[i] = begin + range * ((double) i / div);
		}
		return vector;
	}

	private static double[] init_vector(int size, double init_value) {
		double vector[] = new double[size];
		for (int i = 0; i < size; i++)
			vector[i] = init_value;
		return vector;
	}
}
