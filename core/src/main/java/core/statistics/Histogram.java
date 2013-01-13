package core.statistics;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Histogram extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFreeChart chart;

	public Histogram(String title, Map<String, Integer> map, int type) {
		// new dataset
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// column keys (since we have only one it does not make any sense to give it a name)
		String category = "";
		
		// fill dataset with values
		String key = null;
		
		// type = 0 -> dates else -> formats
		if (type == 0) {
			String keyArray[] = null;
			for (Entry<String, Integer> e : map.entrySet()) {
				keyArray = getMonthYear(e.getKey());
				key = keyArray[0] + " " + keyArray[1] + ": " + e.getValue() + " document/s";
				dataset.addValue(e.getValue(), key, category);
			}
		}
		else {
			for (Entry<String, Integer> e : map.entrySet()) {
				if (e.getKey().equals("")) 
					key = "unknown" + ": " + e.getValue() + " document/s";
				else 
					key = e.getKey() + ": " + e.getValue() + " document/s";
				
				dataset.addValue(e.getValue(), key, category);
			}			
		}

		// create chart
		String xAxis;
		String yAxis = "Number of documents";
		
		if (type == 0) 
			xAxis = "Document added on";
		else 
			xAxis = "Document format";
		
		chart = ChartFactory.createBarChart(title, // chart title
				xAxis, // x axis label
				yAxis, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, false 
				);
	}

	public void generateImage(int width, int height, File file) {
		// output to png
		try {
			ChartUtilities.saveChartAsPNG(file, chart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] getMonthYear(String str) {
		String[] strArray = new String[2];

		Pattern regex = Pattern.compile("(from [0-9]{2,}\\.(.+?)\\.(.+?) )");
		String subjectString = str;
		Matcher regexMatcher = regex.matcher(subjectString);
		if (regexMatcher.find()) {
			strArray[0] = getMonthName(regexMatcher.group(2));
			strArray[1] = regexMatcher.group(3);
		}

		return strArray;
	}

	private static String getMonthName(String str) {
		int month = Integer.parseInt(str);
		String monthString = null;

		switch (month) {
		case 1:
			monthString = "January";
			break;
		case 2:
			monthString = "February";
			break;
		case 3:
			monthString = "March";
			break;
		case 4:
			monthString = "April";
			break;
		case 5:
			monthString = "May";
			break;
		case 6:
			monthString = "June";
			break;
		case 7:
			monthString = "July";
			break;
		case 8:
			monthString = "August";
			break;
		case 9:
			monthString = "September";
			break;
		case 10:
			monthString = "October";
			break;
		case 11:
			monthString = "November";
			break;
		case 12:
			monthString = "December";
			break;
		}

		return monthString;
	}
}