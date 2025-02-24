# NYLA Office

[NYLA Office](https://github.com/nyla-solutions/nyla/tree/master/nyla.solutions.office) provides utilities for creating Excel, PDF and open office documents.


## Excel
Excel provides a set of functions to handle Excel files.
 
###Use Cases
 
 	String filePath = "src/resources/excel/input.xls";
	Excel excel = Excel.getExcel(filePath);
	ExcelSheet sheet = excel.retrieveSheet(sheetName);
	Assert.assertTrue(sheet.getColumnCount() >  1);
	Assert.assertTrue(sheet.getRowCount() >  1);
	
	for (String[] row : sheet.getRows())
	{
	   Debugger.println("row:"+Arrays.asList(currentRow));
	}
	
	
## CSV
CSV is a object to create a CSV file.
 
###Use Case
 
	File file = new File("src/test/resources/csv/output.csv");
	CSV.writeHeader(file, "header1","header2","header3");
	CSV.appendFile(file, 11,12,13);
			
	Object[] row2 = {21,22,23};
	CSV.appendFile(file, row2);

##JFreeChartFacade

 JFreeChartFacade is a wrapper to the popular JFreeChart API(s).
 
###Simple Pie Char

	Chart chart =  new JFreeChartFacade();
	chart.setCategoryLabel(categoryLabel);
	chart.setHeight(height);
	chart.setName(name);
	chart.plotValue(100.0, "Group A (66%)", "Usage");
	chart.plotValue(50.0, "Group B  (34%)", "Usage");
	chart.setTypeName(Chart.PNG_TYPE_NAME);
	
	chart.setGraphType(Chart.PIE_GRAPH_TYPE);
	
	
	IO.writeFile(new File(filePath), chart.getBytes());
	
###JVM memory area graph
	
	//The Y axis is the memory size in GB
	// X is days of week (Mon-Fri)
	Chart chart =  new JFreeChartFacade();
	chart.setLegend(legend);
	
	chart.setCategoryLabel(categoryLabel);
	chart.setHeight(height);
	chart.setName(name);
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

	
	IO.writeFile(new File(filePath), chart.getBytes());
		
	Debugger.println("Write to "+filePath);
	
## FOP PDF 

FOP is a wrapper for Apache FOP API to generate PDF document using XSL-FO.
 
###Use Case
 
	String fo = IO.readClassPath("pdf/example.fop");
			
	File file = new File("src/test/resources/pdf/test.pdf");
	file.delete();
			
	FOP.writePDF(fo, file);
	Assert.assertTrue(file.exists());
	
 
###Example FO
  
	  <?xml version="1.0" encoding="ISO-8859-1"?>
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<fo:layout-master-set>
	  <fo:simple-page-master master-name="A4">
	    <fo:region-body />
	  </fo:simple-page-master>
	</fo:layout-master-set>
	
	<fo:page-sequence master-reference="A4">
	  <fo:flow flow-name="xsl-region-body">
	    <fo:block>Hello World</fo:block>
	  </fo:flow>
	</fo:page-sequence>
	</fo:root>


###Add images FO example 

	   <fo:block>
	    <fo:external-graphic src="../graphics/xml_feather_transparent.gif"/>
	  </fo:block>
  
  
Also see [http://w3schools.sinsixx.com/xslfo/default.asp.htm](http://w3schools.sinsixx.com/xslfo/default.asp.htm)