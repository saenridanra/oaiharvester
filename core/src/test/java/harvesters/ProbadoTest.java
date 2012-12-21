package harvesters;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import se.kb.oai.pmh.Record;
import api.IHarvester;
import api.IHarvesterObserver;

public class ProbadoTest implements IHarvesterObserver{
	
	private IHarvester harvester;
	
	@BeforeClass
	public void setUp(){
		harvester = new Probado();
		harvester.registerObserver(this);
	}
	
	@Test
	public void listRecordsTest(){
		List<Record> list = harvester.listAllRecords();
		
		for(Record r : list){
			try {
				System.out.println(r.getMetadataAsString());
			} catch (IOException e) {
				System.out.println("Error casting metadata!");
				e.printStackTrace();
			}
		}
	}

	public void dataIncome(int count) {
		System.out.println("Dataincome: " + count);
		
	}

	public void dataIncomeFinished() {
		System.out.println("Dataincome finished");
		
	}
}
