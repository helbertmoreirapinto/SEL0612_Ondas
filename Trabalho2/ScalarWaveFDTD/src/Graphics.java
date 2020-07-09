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

	public static void plot(String name_plot, boolean log_scale, String axisX_name, String axisY_name, DataGraphic[] data) {
		int k = 0;
		XYPlot plott = new XYPlot();
		for (DataGraphic dg : data) {
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries serie = new XYSeries(dg.name);

			for (int i = 0; i < dg.data.length; i++)
				serie.add(i, dg.data[i]);
			dataset.addSeries(serie);
			plott.setDataset(k, dataset);
			XYSplineRenderer splinerenderer = new XYSplineRenderer();
			splinerenderer.setSeriesFillPaint(k, Color.BLUE);
			plott.setRangeAxis(k, (log_scale)?new LogAxis(axisY_name):new NumberAxis(axisY_name));
			plott.setDomainAxis(new NumberAxis(axisX_name));
			plott.setRenderer(k, (k==0)?new XYSplineRenderer():splinerenderer);
		}

		String filepath = String.format("simulations/%s_%d.png", name_plot, k++);
		try (OutputStream outstream = new FileOutputStream(filepath)) {
//			LogAxis yAxis = new LogAxis("");
//			yAxis.setBase(10);
//			LogFormat format = new LogFormat(yAxis.getBase(), "", "", true);
//			yAxis.setNumberFormatOverride(format);
//			XYPlot plot = new XYPlot(new XYSeriesCollection(serie), new NumberAxis(""),
//					(log_scale) ? yAxis : new NumberAxis(""), new XYLineAndShapeRenderer(true, false));
			JFreeChart chart = new JFreeChart(name_plot, JFreeChart.DEFAULT_TITLE_FONT, plott, true);
			ChartUtilities.writeChartAsPNG(outstream, chart, 1024, 768);
		} catch (Exception e) {
			System.err.println("Erro: " + e.getMessage());
		}
	}
}
/*


//customize the plot with renderers and axis
plot.setRenderer(0, new XYSplineRenderer());//use default fill paint for first series
XYSplineRenderer splinerenderer = new XYSplineRenderer();
splinerenderer.setSeriesFillPaint(0, Color.BLUE);
plot.setRenderer(1, splinerenderer);
plot.setRangeAxis(0, new NumberAxis("Series 1"));
plot.setRangeAxis(1, new NumberAxis("Series 2"));
plot.setDomainAxis(new NumberAxis("X Axis"));

//Map the data to the appropriate axis
plot.mapDatasetToRangeAxis(0, 0);
plot.mapDatasetToRangeAxis(1, 1);   

//generate the chart

chart.setBackgroundPaint(Color.WHITE);
*/
