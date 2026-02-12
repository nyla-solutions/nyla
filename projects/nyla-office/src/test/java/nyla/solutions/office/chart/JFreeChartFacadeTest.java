package nyla.solutions.office.chart;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import org.junit.jupiter.api.Test;

public class JFreeChartFacadeTest
{



   @Test
   public void testGetBytes()
   throws Exception
   {
	   Chart chart =  new JFreeChartFacade();
	chart.setLegend(legend);
	
	chart.setCategoryLabel(categoryLabel);
	chart.setHeight(height);
	chart.setWidth(width);
	chart.setTitle(title);
	String label = "JVM";
	
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	
	cal.add(Calendar.DATE, -5);
	
	chart.plotValue(2,label, cal.getTime());
	cal.add(Calendar.DATE, +1);
	
	
	chart.plotValue(5, label, cal.getTime());
	cal.add(Calendar.DATE, +1);
	
	chart.plotValue(6,label, cal.getTime());
	
	cal.add(Calendar.DATE, +1);
	chart.plotValue(7,label, cal.getTime());

	cal.add(Calendar.DATE, +1);
	chart.plotValue(1,label, cal.getTime());

	chart.setTypeName(Chart.PNG_TYPE_NAME);
	
	
	Color[] colors = { Color.RED};
	
	chart.setSeriesColors(Arrays.asList(colors));
	chart.setBackgroundColor(Color.WHITE);
	chart.setValueLabel("GB(s)");
	
	chart.setGraphType(Chart.AREA_GRAPH_TYPE);

	IO.writer().writeFile(new File(filePath), chart.getBytes());
		
	Debugger.println("Write to "+filePath);
   }
   private String title = "Memory Usage";
   private boolean legend = true;
   private String filePath = Config.getTempDir()+"test.png";
   private String name = "name";
   private int height = 700;
   private int width =  600;
   private String categoryLabel = "Dates";


}
