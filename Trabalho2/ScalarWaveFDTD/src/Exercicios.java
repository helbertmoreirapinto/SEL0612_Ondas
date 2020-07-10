import java.awt.Color;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
		data[0] = new DataGraphic(at, "Attenuation Constant", 2);
		data[1] = new DataGraphic(Vp, "Phase Velocity", 2);
		teste(at, Vp);
		// Graphics.plot("Fig2.1", false, "nome x", "nome ay", data);
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
		data[0] = new DataGraphic(Vp, "Phase Velocity Error", -1);

		Graphics.plot("Fig2.2", true, "N lambda", "Phase Velocity Error", data);
	}

	public static void fig_2_4() {
		DataGraphic[] data = new DataGraphic[2];
		double S0 = 0.5;
		int I = 200;
		int N = (int) (I / S0);
		double S[] = init_vector(I - 1, S0);
		double[][] u = UtilScalarWave.FDTD(S, I, N, 0);
		data[0] = new DataGraphic(u[(int) (190 / S0)], "S = 0.5", -1);

		S0 = 1;
		S = init_vector(I - 1, S0);
		u = UtilScalarWave.FDTD(S, I, N, 0);
		data[1] = new DataGraphic(u[(int) (190 / S0)], "S = 1", -1);

		Graphics.plot("Fig2.4", false, "Grid i coordinate", "Wavefunction", data);

	}

	public static void fig_2_5() {
		int I = 200;
		int N = 400;
		double S[] = new double[I];
		for (int i = 0; i < I; i++)
			S[i] = (i < 140) ? 1 : .25;
		double u[][] = UtilScalarWave.FDTD(S, I, N, 0);
		DataGraphic[] data = new DataGraphic[1];
		data[0] = new DataGraphic(u[250], "Wavefunction", -1);

		Graphics.plot("Fig2.5", false, "Grid i coordinate", "Wavefunction", data);
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
		data[0] = new DataGraphic(u[200], "n = 200", -1);
		data2[0] = new DataGraphic(v[0], "n = 200", -1);
		data[1] = new DataGraphic(u[210], "n = 210", -1);
		data2[1] = new DataGraphic(v[1], "n = 210", -1);
		data[2] = new DataGraphic(u[220], "n = 220", -1);
		data2[2] = new DataGraphic(v[2], "n = 220", -1);
		Graphics.plot("Fig2.6a", false, "Grid i coordinate", "Wavefunction", data);
		Graphics.plot("Fig2.6b", false, "Grid i coordinate", "Wavefunction", data2);
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
		data[0] = new DataGraphic(u[190], "n = 190", -1);
		data2[0] = new DataGraphic(v[0], "n = 190", -1);
		data[1] = new DataGraphic(u[200], "n = 200", -1);
		data2[1] = new DataGraphic(v[1], "n = 200", -1);
		Graphics.plot("Fig2.7a", false, "Grid i coordinate", "Wavefunction", data);
		Graphics.plot("Fig2.7b", false, "Grid i coordinate", "Wavefunction", data2);
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

	private static void teste(double[] v1, double[] v2) {
		XYSeries series1 = new XYSeries("Attenuation Constant");
		XYSeries series2 = new XYSeries("Phase Velocity");
		int i = 0;
		for (double v : v1) {
			double x = 1 + (9 * (double) i / 100);
			i++;
			series1.add(x, v);
		}
		i = 0;
		for (double v : v2) {
			double x = 1 + (9 * (double) i / 100);
			i++;
			series2.add(x, v);
		}

		// create the datasets
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset1.addSeries(series1);
		dataset2.addSeries(series2);

		// construct the plot
		XYPlot plot = new XYPlot();
		plot.setDataset(0, dataset1);
		plot.setDataset(1, dataset2);

		// customize the plot with renderers and axis
		plot.setRenderer(0, new XYSplineRenderer());// use default fill paint for first series
		XYSplineRenderer splinerenderer = new XYSplineRenderer();
		splinerenderer.setSeriesFillPaint(0, Color.BLUE);
		plot.setRenderer(1, splinerenderer);
		NumberAxis n1 = new NumberAxis("Attenuation Constant");
		n1.setRange(0, 6);
		NumberAxis n2 = new NumberAxis("Phase Velocity");
		n2.setRange(0, 2);
		plot.setRangeAxis(0, n1);
		plot.setRangeAxis(1, n2);
		NumberAxis n = new NumberAxis();
		n.setRange(1,10);
		plot.setDomainAxis(n);

		// Map the data to the appropriate axis
		plot.mapDatasetToRangeAxis(0, 0);
		plot.mapDatasetToRangeAxis(1, 1);

		// generate the chart
		String filepath = String.format("teste.png");
		try (OutputStream outstream = new FileOutputStream(filepath)) {
			JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			chart.setBackgroundPaint(Color.WHITE);
			ChartUtilities.writeChartAsPNG(outstream, chart, 1024, 768);
		} catch (Exception e) {
			System.err.println("Erro: " + e.getMessage());
		}
	}
}
