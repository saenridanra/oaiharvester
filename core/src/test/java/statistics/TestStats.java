package statistics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import core.statistics.Histogram;
import core.statistics.PieChart;

public class TestStats {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "D:\\img\\";
		
		// licenses
		Map<String, Integer> licenses = new HashMap<String, Integer>();
		licenses.put("NO_ACCESS", 1697);
		licenses.put("FULL", 1544);

		PieChart pie = new PieChart("Licenses overview", licenses);
		File fileLicensesPie = new File(path + "licenses.png");
		pie.generateImage(300, 250, fileLicensesPie);

		// dates
		Map<String, Integer> dates = new HashMap<String, Integer>();
		dates.put("from 22.02.2011 00:00:00 until 22.02.2011 00:00:00 : Created", 2);
		dates.put("from 01.01.2008 00:00:00 until 01.01.2008 00:00:00 : Created", 2963);
		dates.put("from 01.01.2007 00:00:00 until 01.01.2007 00:00:00 : Created", 196);
		dates.put("from 02.03.2010 00:00:00 until 02.03.2010 00:00:00 : Created", 70);
		dates.put("from 01.01.2010 00:00:00 until 01.01.2010 00:00:00 : Created", 10);

		Histogram histoDates = new Histogram("\"Documents added on\" overview", dates, 0);
        File histoDatesFile = new File(path + "dates.png");
        histoDates.generateImage(500, 500, histoDatesFile);
        
        // formats
        Map<String, Integer> formats = new HashMap<String, Integer>();
        formats.put("", 26);
        formats.put("3dmf", 88);
        formats.put("obj , max , wrl , c4d , 3ds , mb , lwo , fbx", 70);
        formats.put("max", 56);
        formats.put("max , 3ds", 2);
        formats.put("c4d , 3ds", 1);
        formats.put("skp", 916);
        formats.put("gsm", 521);
        formats.put("obj , 3ds", 1);
        formats.put("stl", 3);
        formats.put("obj, skp", 1);
        formats.put("c4d", 4);
        formats.put("3ds", 1348);
        formats.put("3dm", 1);
        formats.put("wrl", 199);
        formats.put("3ds, dwg", 1);
        formats.put("obj", 3);
        
		Histogram histoFormats = new Histogram("Documents format overview", formats, 1);
        File histoFormatsFile = new File(path + "formats.png");
        histoFormats.generateImage(500, 500, histoFormatsFile);
	}
}
