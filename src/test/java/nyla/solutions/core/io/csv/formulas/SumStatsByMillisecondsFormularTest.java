package nyla.solutions.core.io.csv.formulas;

import nyla.solutions.core.io.csv.CsvReader;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class SumStatsByMillisecondsFormularTest
{

	@Test
	public void testMain()
	{
		String[] args2 = {"src/test/resources/csv/timing.csv", "0", "1","1000" };
			SumStatsByMillisecondsFormular.main(args2);
			
			
	
	}
	
	
	@Test
	public void testCac()
	throws Exception
	{
		
			SumStatsByMillisecondsFormular formular = new SumStatsByMillisecondsFormular(0, 1, 1000);
			
			CsvReader reader = new CsvReader(Paths.get("src/test/resources/csv/server.csv").toFile());
			formular.calc(reader);
			

			
			System.out.println("formular:"+formular);
	}

}
