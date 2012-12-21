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
	
	public void dataIncome(int count);
	
	public void dataIncomeFinished();
	
}
