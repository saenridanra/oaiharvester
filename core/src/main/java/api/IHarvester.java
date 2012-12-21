package api;

import java.util.List;

import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.Record;

/**
 * This interface generally
 * defines which functionality
 * is provided by a Harvester.
 * 
 * @author Andreas Rain
 *
 */
public interface IHarvester {
	
	/**
	 * You can register an observer which is notified on changes.
	 * @param observer
	 */
	public void registerObserver(IHarvesterObserver observer);
	
	/**
	 * This method returns a single record
	 * according to its passed identifier.
	 * @param identifier - The identifier of which record to retrieve.
	 * @return
	 */
	public Record getSingleRecord(String identifier);
	
	/**
	 * This method returns all the records
	 * the provider has in it's database.
	 * @return
	 */
	public List<Record> listAllRecords();
	
	/**
	 * A method that let's you list all the identifiers.
	 */
	public List<Header> listIdentifiers();
	
}
