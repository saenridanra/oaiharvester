package core.harvesters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;

import core.api.IHarvester;
import core.api.IHarvesterObserver;
import core.statistics.Histogram;
import core.statistics.PieChart;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.IdentifiersList;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;
import se.kb.oai.pmh.ResumptionToken;
import se.kb.xml.XPathWrapper;

public class Harvester implements IHarvester {

	private OaiPmhServer server;
	private IHarvesterObserver observer;

	private List<Record> records;
	private List<Header> identifiers;

	public static final String OAI_NS_PREFIX = "oai";
	public static final String OAI_NS_URI = "http://www.openarchives.org/OAI/2.0/";

	private static final String RESUMPTION_TOKEN_XPATH = "*/oai:resumptionToken";

	private String url, savePath, metadataFormat;

	int documentCount;

	Map<String, Integer> dates;
	Map<String, Integer> licenses;
	Map<String, Integer> formatExtensions;

	Document statisticsXml, statisticsHtml;

	public Harvester(String url, String savePath) throws IOException {
		if (!new File(savePath).isDirectory())
			throw new IOException("The path does not seem to be a directory.");
		this.url = url;
		this.savePath = savePath;

		dates = new HashMap<String, Integer>();
		licenses = new HashMap<String, Integer>();
		formatExtensions = new HashMap<String, Integer>();

		server = new OaiPmhServer(url);
	}

	public String getMetadataValue(String elem, Record record) {
		return ((Element) record.getMetadata().elements(elem).get(0)).getText();
	}
	
	public Element getElement(String elem, Record record) {
		return ((Element) record.getMetadata().elements(elem).get(0));
	}

	/**
	 * {@inheritDoc}
	 */
	public Record getSingleRecord(String identifier) {
		Record r = null;

		try {
			r = server.getRecord(identifier, metadataFormat);
		} catch (OAIException e) {
		}
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Record> listAllRecords() {
		List<Record> list = new ArrayList<Record>();

		int c = 0;
		try {
			ResumptionToken token;
			RecordsList tmpList = server.listRecords(metadataFormat);
			token = tmpList.getResumptionToken();

			Element root = tmpList.getResponse().getRootElement();

			XPathWrapper xpath = new XPathWrapper(root);
			xpath.addNamespace(OAI_NS_PREFIX, OAI_NS_URI);
			Element e = xpath.selectSingleElement(RESUMPTION_TOKEN_XPATH);

			try{
				documentCount = Integer.valueOf(
						e.attributeValue("completeListSize")).intValue();
				observer.dataSize(documentCount);
			}
			catch(Exception ee){
				observer.dataSize(0);
			}

			list.addAll(tmpList.asList());

			if (observer != null) {
				observer.dataIncome(list.size());
				
				c += list.size();
			}

			/**
			 * Repeating the process aslong as there is a token.
			 */
			while (token != null) {
				tmpList = server.listRecords(token);
				if (observer != null) {
					observer.dataIncome(tmpList.asList().size());
				}
				list.addAll(tmpList.asList());
				token = tmpList.getResumptionToken();
			}

		} catch (OAIException e) {
			e.printStackTrace();
		}
		
		if(metadataFormat.equals("p3dm")){
			observer.dataSize(c);
		}

		observer.dataIncomeFinished();

		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Header> listIdentifiers() {
		List<Header> list = new ArrayList<Header>();

		try {
			ResumptionToken token;
			IdentifiersList tmpList = server.listIdentifiers(metadataFormat);
			token = tmpList.getResumptionToken();
			Element root = tmpList.getResponse().getRootElement();

			XPathWrapper xpath = new XPathWrapper(root);
			xpath.addNamespace(OAI_NS_PREFIX, OAI_NS_URI);
			Element e = xpath.selectSingleElement(RESUMPTION_TOKEN_XPATH);

			try{
				observer.headerSize(Integer.valueOf(
						e.attributeValue("completeListSize")).intValue());
			}
			catch(Exception ee){
				observer.headerSize(0);
			}

			list.addAll(tmpList.asList());

			if (observer != null) {
				observer.headerIncome(list.size());
			}

			/**
			 * Repeating the process aslong as there is a token.
			 */
			while (token != null) {
				tmpList = server.listIdentifiers(token);
				if (observer != null) {
					observer.headerIncome(tmpList.asList().size());
				}
				list.addAll(tmpList.asList());
				token = tmpList.getResumptionToken();
			}

		} catch (OAIException e) {
			e.printStackTrace();
		}

		observer.headerIncomeFinished();

		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerObserver(IHarvesterObserver observer) {
		this.observer = observer;
	}

	public void harvestAndHold() {
		System.out.println("Setting up harvester thread.");
		Callable<Void> callable = new Callable<Void>() {
			public Void call() throws Exception {
				System.out.println("Callable called");
				//identifiers = listIdentifiers();
				System.out.println("First step finished");
				records = listAllRecords();
				System.out.println("Finished harvesting");
				createStatistics();
				observer.finishedOperation();
				return null;
			}
		};

		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		threadPool.submit(callable);
	}

	private void createStatistics() {
		
		if(metadataFormat.equals("dc")){
			int i = 0;
			for (Record record : records) {
				observer.analayzingRecords(i);
				
				String license = getMetadataValue("rights", record);
				String formatExtension = getMetadataValue("format", record);
				String date = getMetadataValue("date", record);

				if (licenses.containsKey(license)) {
					int newValue = licenses.get(license) + 1;
					licenses.remove(license);
					licenses.put(license, newValue);
				} else {
					licenses.put(license, 1);
				}

				if (formatExtensions.containsKey(formatExtension)) {
					int newValue = formatExtensions.get(formatExtension) + 1;
					formatExtensions.remove(formatExtension);
					formatExtensions.put(formatExtension, newValue);
				} else {
					formatExtensions.put(formatExtension, 1);
				}

				if (dates.containsKey(date)) {
					int newValue = dates.get(date) + 1;
					dates.remove(date);
					dates.put(date, newValue);
				} else {
					dates.put(date, 1);
				}
				
				i++;
			}
		}
		else if(metadataFormat.equals("p3dm")){
			int i = 0;
			for (Record record : records) {
				observer.analayzingRecords(i);
				
				String license = getElement("LICENSE", record).attributeValue("NAME");
				String formatExtension = getElement("MODELFILES", record).element("MODELFILE").elementText("EXTENSION");
				String date = getElement("DATES", record).element("DATEAVAILABLE").getText();

				if (licenses.containsKey(license)) {
					int newValue = licenses.get(license) + 1;
					licenses.remove(license);
					licenses.put(license, newValue);
				} else {
					licenses.put(license, 1);
				}

				if (formatExtensions.containsKey(formatExtension)) {
					int newValue = formatExtensions.get(formatExtension) + 1;
					formatExtensions.remove(formatExtension);
					formatExtensions.put(formatExtension, newValue);
				} else {
					formatExtensions.put(formatExtension, 1);
				}

				if (dates.containsKey(date)) {
					int newValue = dates.get(date) + 1;
					dates.remove(date);
					dates.put(date, newValue);
				} else {
					dates.put(date, 1);
				}
				
				i++;
			}
		}
		else{
			return;
		}

		statisticsXml = DocumentHelper.createDocument();
		Element root = statisticsXml.addElement("StatisticalData");
		root.addAttribute("harvesterUrl", url);
		root.addElement("DocumentCount").setText(String.valueOf(documentCount));

		Element licensesElement = root.addElement("Licenses");
		for (Entry<String, Integer> e : this.licenses.entrySet()) {
			Element elem = licensesElement.addElement("License");
			elem.addAttribute("id", e.getKey());
			elem.setText(e.getValue().toString());
		}
		
		PieChart pie = new PieChart("Licenses overview", licenses);
		File fileLicensesPie = new File(new StringBuilder().append(savePath)
				.append(File.separator).append("licenses.png").toString());
		pie.generateImage(300, 250, fileLicensesPie);

		Element datesElement = root.addElement("Dates");
		for (Entry<String, Integer> e : this.dates.entrySet()) {
			Element elem = datesElement.addElement("Date");
			elem.addAttribute("id", e.getKey());
			elem.setText(e.getValue().toString());
		}
		
		Histogram histoDates = new Histogram("\"Documents added on\" overview", dates, 0);
        File histoDatesFile = new File(new StringBuilder().append(savePath)
				.append(File.separator).append("dates.png").toString());
        histoDates.generateImage(500, 500, histoDatesFile);

		Element formatsElement = root.addElement("Formats");
		for (Entry<String, Integer> e : this.formatExtensions.entrySet()) {
			Element elem = formatsElement.addElement("Format");
			elem.addAttribute("id", e.getKey());
			elem.setText(e.getValue().toString());
		}

		Histogram histoFormats = new Histogram("Documents format overview", formatExtensions, 1);
        File histoFormatsFile = new File(new StringBuilder().append(savePath)
				.append(File.separator).append("formats.png").toString());
        histoFormats.generateImage(500, 500, histoFormatsFile);
        
		// load the transformer using JAXP
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = factory.newTransformer(new StreamSource(
					"src/main/resources/statistics.xsl"));
		} catch (TransformerConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// now lets style the given document
		DocumentSource source = new DocumentSource(statisticsXml);
		DocumentResult result = new DocumentResult();
		try {
			transformer.transform(source, result);
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// return the transformed document
		statisticsHtml = result.getDocument();
	}

	public List<Record> getRecords() {
		return records;
	}

	public List<Header> getIdentifiers() {
		return identifiers;
	}

	public void save() throws IOException {
		if (records != null && records.size() > 0) {

			File file;
			FileOutputStream os;
			for (int i = 0; i < records.size(); i += 10) {
				file = new File(new StringBuilder().append(savePath)
						.append(File.separator).append("records" + i + "to" + (i+10) + ".xml")
						.toString());
				if (!file.exists())
					file.createNewFile();

				os = new FileOutputStream(file);
				os.write(records.get(i).getResponse().asXML().getBytes());

				os.flush();
				os.close();
			}

			file = new File(new StringBuilder().append(savePath)
					.append(File.separator).append("statistics.xml").toString());
			if (!file.exists())
				file.createNewFile();

			os = new FileOutputStream(file);

			os.write(statisticsXml.asXML().getBytes());

			os.flush();
			os.close();
			
			file = new File(new StringBuilder().append(savePath)
					.append(File.separator).append("statistics.html").toString());
			if (!file.exists())
				file.createNewFile();

			os = new FileOutputStream(file);

			os.write(statisticsHtml.asXML().getBytes());

			os.flush();
			os.close();
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getMetadataFormat() {
		return metadataFormat;
	}

	public void setMetadataFormat(String metadataFormat) {
		this.metadataFormat = metadataFormat;
	}

}
