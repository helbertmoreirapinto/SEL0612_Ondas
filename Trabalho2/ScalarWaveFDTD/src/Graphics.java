import java.awt.Color;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graphics {

	public static void plot(String name_plot, boolean log_scale, String axisX_name, String axisY_name,
			DataGraphic[] data) {
		int k = 0;
		XYPlot plot = new XYPlot();

		for (DataGraphic dg : data) {
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries serie = new XYSeries(dg.name);

			for (int i = 0; i < dg.data.length; i++)
				serie.add(i, dg.data[i]);
			dataset.addSeries(serie);
			plot.setDataset(k, dataset);
			XYSplineRenderer splinerenderer = new XYSplineRenderer();
			splinerenderer.setSeriesFillPaint(k,
					Color.getHSBColor((float) Math.random(), (float) Math.random(), (float) Math.random()));
			plot.setRangeAxis(0, (log_scale) ? new LogAxis(axisY_name) : new NumberAxis(axisY_name));
			plot.setDomainAxis(new NumberAxis(axisX_name));
			plot.setRenderer(k, (k == 0) ? new XYSplineRenderer() : splinerenderer);
			if (dg.scale > 0)
				plot.mapDatasetToRangeAxis(0, dg.scale);
			k++;
		}

		String filepath = String.format("simulations/%s.png", name_plot);
		try (OutputStream outstream = new FileOutputStream(filepath)) {
			JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			chart.setBackgroundPaint(Color.WHITE);
			ChartUtilities.writeChartAsPNG(outstream, chart, 1024, 768);
		} catch (Exception e) {
			System.err.println("Erro: " + e.getMessage());
		}
	}
}
