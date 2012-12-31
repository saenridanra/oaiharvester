package harvesters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.xpath.DefaultXPath;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.IdentifiersList;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;
import se.kb.oai.pmh.ResumptionToken;
import se.kb.xml.XPathWrapper;
import api.IHarvester;
import api.IHarvesterObserver;

public class Probado implements IHarvester {

	private OaiPmhServer server;
	private IHarvesterObserver observer;

	private List<Record> records;
	private List<Header> identifiers;
	
    public static final String OAI_NS_PREFIX = "oai";
    public static final String OAI_NS_URI = "http://www.openarchives.org/OAI/2.0/";
    
    private static final String RESUMPTION_TOKEN_XPATH = "*/oai:resumptionToken";

	public Probado() {
		server = new OaiPmhServer(
				"http://core.probado.igd.fraunhofer.de/OaiPmh.svc/");
	}

	public String getMetadataValue(String elem, Record record){
		return ((Element) record.getMetadata().elements(elem).get(0)).getText();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Record getSingleRecord(String identifier) {
		Record r = null;

		try {
			r = server.getRecord(identifier, "dc");
		} catch (OAIException e) {
		}
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Record> listAllRecords() {
		List<Record> list = new ArrayList<Record>();

		try {
			ResumptionToken token;
			RecordsList tmpList = server.listRecords("dc");
			token = tmpList.getResumptionToken();
			
			Element root = tmpList.getResponse().getRootElement();
	        
	        XPathWrapper xpath = new XPathWrapper(root);
	        xpath.addNamespace(OAI_NS_PREFIX, OAI_NS_URI);
	        Element e = xpath.selectSingleElement(RESUMPTION_TOKEN_XPATH);
	        
			observer.dataSize(Integer.valueOf(e.attributeValue("completeListSize"))
					.intValue());

			list.addAll(tmpList.asList());

			if (observer != null) {
				observer.dataIncome(list.size());
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
			IdentifiersList tmpList = server.listIdentifiers("dc");
			token = tmpList.getResumptionToken();
			
			Element root = tmpList.getResponse().getRootElement();
	        
	        XPathWrapper xpath = new XPathWrapper(root);
	        xpath.addNamespace(OAI_NS_PREFIX, OAI_NS_URI);
	        Element e = xpath.selectSingleElement(RESUMPTION_TOKEN_XPATH);
	        
			observer.headerSize(Integer.valueOf(e.attributeValue("completeListSize"))
					.intValue());

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
				identifiers = listIdentifiers();
				System.out.println("First step finished");
				records = listAllRecords();
				System.out.println("Finished harvesting");
				return null;
			}
		};

		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		threadPool.submit(callable);
	}

	public List<Record> getRecords() {
		return records;
	}

	public List<Header> getIdentifiers() {
		return identifiers;
	}

}
