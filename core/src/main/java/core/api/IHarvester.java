package core.api;

import java.io.IOException;
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
	 * @return returns the records
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
	
	/**
	 * Perform this method to harvest the records from the server
	 * and keep the data in the harvester.
	 */
	public void harvestAndHold();
	
	/**
	 * Use this method to get a specific metadatavalue from the server.
	 * 
	 * @param xpath
	 * @param record
	 * @return returns the value as a string
	 */
	public String getMetadataValue(String xpath, Record record);
	
	/**
	 * 
	 * @return returns the held list of records
	 */
	public List<Record> getRecords();

	/**
	 * 
	 * @return returns the held list of identifiers
	 */
	public List<Header> getIdentifiers();
	
	public void save() throws IOException;
	
	public String getUrl();
	public String getSavePath();
	
}
