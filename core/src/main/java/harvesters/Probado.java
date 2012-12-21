package harvesters;

import java.util.ArrayList;
import java.util.List;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.IdentifiersList;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;
import se.kb.oai.pmh.ResumptionToken;
import api.IHarvester;
import api.IHarvesterObserver;

public class Probado implements IHarvester {
	
	private OaiPmhServer server;
	private IHarvesterObserver observer;
	
	public Probado(){
		server = new OaiPmhServer("http://core.probado.igd.fraunhofer.de/OaiPmh.svc/");
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
			
			list.addAll(tmpList.asList());
			
			if(observer != null){
				observer.dataIncome(list.size());
			}
			
			/**
			 * Repeating the process aslong as there is a token.
			 */
			while(token != null){
				tmpList = server.listRecords(token);
				if(observer != null){
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
			
			list.addAll(tmpList.asList());
			
			if(observer != null){
				observer.dataIncome(list.size());
			}
			
			/**
			 * Repeating the process aslong as there is a token.
			 */
			while(token != null){
				tmpList = server.listIdentifiers(token);
				if(observer != null){
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
	public void registerObserver(IHarvesterObserver observer) {
		this.observer = observer;
	}

}
