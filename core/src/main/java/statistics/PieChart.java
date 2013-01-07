package statistics;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFreeChart chart;

	public PieChart(String chartTitle, Map<String, Integer> map) {
		// new dataset
		DefaultPieDataset dataset = new DefaultPieDataset();

		// fill dataset with values
		for (Entry<String, Integer> e : map.entrySet()) {
			dataset.setValue(e.getKey(), e.getValue());
		}

		// create chart
		chart = ChartFactory.createPieChart(chartTitle, // chart title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setNoDataMessage("No data available");
		plot.setLabelGap(0.02);
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}: {1} document/s"));
	}

	public void generateImage(int width, int height, File file) {
		// output to png
		try {
			ChartUtilities.saveChartAsPNG(file, chart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
