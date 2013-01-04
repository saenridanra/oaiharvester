package api;

/**
 * This interface
 * defines a simple observer
 * which is used to monitor the process
 * of the harvester.
 * 
 * @author Andreas Rain
 *
 */
public interface IHarvesterObserver {
	
	public void finishedOperation();
	
	public void analayzingRecords(int count);
	
	public void headerIncome(int count);
	
	public void headerIncomeFinished();
	
	public void dataIncome(int count);
	
	public void dataIncomeFinished();
	
	public void headerSize(int size);
	
	public void dataSize(int size);
	
}
