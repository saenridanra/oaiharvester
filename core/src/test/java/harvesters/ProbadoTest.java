package harvesters;

import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.IHarvester;
import api.IHarvesterObserver;

import com.google.common.io.Files;

public class ProbadoTest implements IHarvesterObserver{
	
	private IHarvester harvester;
	
	boolean finished = false;
	int size = 0;
	
	@BeforeClass
	public void setUp(){
		try {
			harvester = new Harvester("http://core.probado.igd.fraunhofer.de/oaipmh.svc/", Files.createTempDir().getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		harvester.registerObserver(this);
	}
	
	@Test
	public void listRecordsTest(){
		harvester.harvestAndHold();
		while(!finished){
			
		}
	}

	public void dataIncome(int count) {
		System.out.println("Dataincome: " + count);
		
	}

	public void dataIncomeFinished() {
		System.out.println("Dataincome finished");
		
	}

	public void headerIncome(int count) {
		System.out.println("Headerincome " + count);
	}

	public void headerIncomeFinished() {
		// TODO Auto-generated method stub
		
	}

	public void headerSize(int size) {
		// TODO Auto-generated method stub
		
	}

	public void dataSize(int size) {
		this.size = size;
		
	}

	public void finishedOperation() {
		// TODO Auto-generated method stub
		
	}

	public void analayzingRecords(int count) {
		if(count == size){
			finished = true;
		}
	}
}
