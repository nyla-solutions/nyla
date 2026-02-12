package nyla.solutions.office.chart;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;



/**
 * <pre>
 * 
 * JFreeChartFacade is a wrapper to the popular JFreeChart API(s).
 * 
<strong>Simple Pie Char</strong>
	Chart chart =  new JFreeChartFacade();
	chart.setCategoryLabel(categoryLabel);
	chart.setHeight(height);
	chart.setName(name);
	chart.plotValue(100.0, "Group A (66%)", "Usage");
	chart.plotValue(50.0, "Group B  (34%)", "Usage");
	chart.setTypeName(Chart.PNG_TYPE_NAME);
	
	chart.setGraphType(Chart.PIE_GRAPH_TYPE);
	
	
	IO.writeFile(new File(filePath), chart.getBytes());
	
<strong>JVM memory area graph</strong>
	
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
	
	</pre>
	
 * @author Gregory Green
 *
 */
public class JFreeChartFacade implements Chart
{



	private Color backgroundColor = Color.white;
	private List<Color> seriesColors = null;
	private String graphType = null;
	private String name = Config.settings().getProperty(this.getClass(),"name","Chart");
	private String typeName = Config.settings().getProperty(this.getClass(),"typeName",Chart.JPG_TYPE_NAME);
	private PlotOrientation orientation = PlotOrientation.VERTICAL;
	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	private String valueLabel = Config.settings().getProperty(this.getClass().getName()+".valueLabel","Values");
	private String categoryLabel = Config.settings().getProperty(this.getClass().getName()+".categoryLabel","Categories");
	private String title = Config.settings().getProperty(this.getClass().getName()+".title","Summary");
	private boolean tooltips = Config.settings().getPropertyBoolean(this.getClass().getName()+".tooltips",false);
	private boolean legend = Config.settings().getPropertyBoolean(this.getClass().getName()+".legend",false);
	private boolean urls = Config.settings().getPropertyBoolean(this.getClass().getName()+".urls",false);
	private int byteArrayBufferSize =  Config.settings().getPropertyInteger(this.getClass().getName()+".byteArrayBufferSize",1024);
	private int width =  Config.settings().getPropertyInteger(this.getClass().getName()+".width",550);
	private int height =  Config.settings().getPropertyInteger(this.getClass().getName()+".height",550);
	private boolean detectLegend = Config.settings().getPropertyBoolean(this.getClass().getName()+".detectLegend",true);
   
	/**
	 * serial Version UID
	 */
	@Serial
    private static final long serialVersionUID = -6910401210511262949L;
	/**
	 * 
	 * @return JPEG version of chart
	 */
	public byte[] getBytes()
	{
		try
		{
			JFreeChart chart = createChart();
			
			
			ByteArrayOutputStream chartOut =  new ByteArrayOutputStream(this.byteArrayBufferSize);

            ChartUtils.writeChartAsJPEG(chartOut, chart, width, height);
            //ChartUtils.writeChartAsJPEG(chartOut, chart, width, height);
			
			return chartOut.toByteArray();
		} 
		catch (IOException e)
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
		
	}


	/**
	   * Creates and returns a bar chart showing the breakdown
	   * of visits by folder. The data is arbitrary.
	   */
	  private  JFreeChart createChart()
	  {	   
	   
	    JFreeChart jfreeChart;
	    
	    if(LINE_GRAPH_TYPE.equalsIgnoreCase(graphType))
	     {
	    	jfreeChart = ChartFactory.createLineChart( title, categoryLabel, valueLabel, dataset, orientation, legend, tooltips, urls );	    	
	    	
	    	
	     }
	    else if(PIE_GRAPH_TYPE.equalsIgnoreCase(graphType))
	    {
		jfreeChart = ChartFactory.createPieChart(title, toPieDataset(), legend,tooltips,urls);
	    }
	    else if(AREA_GRAPH_TYPE.equalsIgnoreCase(graphType))
	    {
		jfreeChart = ChartFactory.createAreaChart(title, categoryLabel, valueLabel, dataset, orientation, detectLegend, tooltips, urls);
	    }
        else
	    {
		 //default
	    	jfreeChart = ChartFactory.createBarChart( title, categoryLabel, valueLabel, dataset, orientation, legend, tooltips, urls );
	    }


	    
	    paintPlotSeries(jfreeChart);
	    
	    jfreeChart.setBackgroundPaint(backgroundColor );
	    
	    //BarRenderer renderer = (BarRenderer)plot.getRenderer();
	    //renderer.setDrawBarOutline(false);
	    
	    //renderer.setSeriesPaint(0, new GradientPaint( 0.0f, 0.0f, Color.green, 0.0f, 0.0f, Color.lightGray ));
	    return jfreeChart;
	  }
	  /**
	   * Render the colors for various series data.
	   * This method using the colors provided in the seriesColor arrayList
	   * @param chart the chart to paint
	   */
	  protected void paintPlotSeries(JFreeChart chart)
	  {
		  if(this.seriesColors == null || this.seriesColors.isEmpty())
			  return; //nothing to paint (use default values)
		  
		  
		  CategoryPlot plot = chart.getCategoryPlot();
		  CategoryItemRenderer  renderer = plot.getRenderer();
		  
		  Color color;
		  
		  for (int i = 0; i < seriesColors.size(); i++) 
		  {
			color = (Color)seriesColors.get(i);
			renderer.setSeriesPaint(i, color);
		  }
	  }
	  /**
	   * Plot a value on the chart
	   * @param value the value to plot
	   * @param xAxisValue the column name
	   * @param labelCategory the row key data
	   */
	  public void plotValue(double value, Comparable<?> labelCategory, Comparable<?> xAxisValue)
	  {
		  dataset.addValue(value, labelCategory, xAxisValue);
	  }
	  /**
	   * Plot a value on the chart
	   * @param value the value to plot
	   * @param labelCategory the row key
	   * @param xAxisValue the label
	   */
	  public void plotValue(Double value, Comparable<?> labelCategory, Comparable<?> xAxisValue)
	  {
		  if(value == null)
			  return; //ignore plot (no data)
		  
		  if(detectLegend && labelCategory != null && labelCategory.toString().length() > 0 &&
				  xAxisValue != null && xAxisValue.toString().length() > 0)
		  {
			this.legend = true;		  
		  }
			  
		  dataset.addValue(value.doubleValue(), labelCategory, xAxisValue);
	  }
	  
	  /**
	 * @return the orientation
	 */
	public PlotOrientation getOrientation()
	{
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(PlotOrientation orientation)
	{
		this.orientation = orientation;
	}
	/**
	 * Note: Default orientation is vertical 
	 * @param orientation the display orientation (HORIZONTAL or VERTICAL).
	 * 
	 *
	 */
	public void setOrientation(String orientation)
	{
		if("HORIZONTAL".equalsIgnoreCase(orientation))
		{
			this.setOrientation(PlotOrientation.HORIZONTAL);
		}
		else
		{
			this.setOrientation(PlotOrientation.VERTICAL);
		}		
	}

	/**
	 * @return the dataset
	 */
	public DefaultCategoryDataset getDataset()
	{
		return dataset;
	}
	

	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(DefaultCategoryDataset dataset)
	{
		this.dataset = dataset;
	}

	/**
	 * @return the valueLabel
	 */
	public String getValueLabel()
	{
		return valueLabel;
	}

	/**
	 * @param valueLabel the valueLabel to set
	 */
	public void setValueLabel(String valueLabel)
	{
		this.valueLabel = valueLabel;
	}

	/**
	 * @return the categoryLabel
	 */
	public String getCategoryLabel()
	{
		return categoryLabel;
	}

	/**
	 * @param categoryLabel the categoryLabel to set
	 */
	public void setCategoryLabel(String categoryLabel)
	{
		this.categoryLabel = categoryLabel;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the tooltips
	 */
	public boolean isTooltips()
	{
		return tooltips;
	}

	/**
	 * @param tooltips the tooltips to set
	 */
	public void setTooltips(boolean tooltips)
	{
		this.tooltips = tooltips;
	}

	/**
	 * @return the legend
	 */
	public boolean isLegend()
	{
		return legend;
	}

	/**
	 * @param legend the legend to set
	 */
	public void setLegend(boolean legend)
	{
		this.legend = legend;
	}

	/**
	 * @return the urls
	 */
	public boolean isUrls()
	{
		return urls;
	}

	/**
	 * @param urls the urls to set
	 */
	public void setUrls(boolean urls)
	{
		this.urls = urls;
	}

	/**
	 * @return the byteArrayBufferSize
	 */
	public int getByteArrayBufferSize()
	{
		return byteArrayBufferSize;
	}

	/**
	 * @param byteArrayBufferSize the byteArrayBufferSize to set
	 */
	public void setByteArrayBufferSize(int byteArrayBufferSize)
	{
		this.byteArrayBufferSize = byteArrayBufferSize;
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName()
	{
		return typeName;
	}

	/**
	 * Examples (png, jpg, gif)
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}


	
	/**
	 * @return the graphType
	 */
	public String getGraphType()
	{
		return graphType;
	}

	/**
	 * Expect bars types (line|bar|pie)
	 * @param graphType the graphType to set
	 */
	public void setGraphType(String graphType)
	{
	   if(graphType != null && 
	     !LINE_GRAPH_TYPE.equals(graphType) &&
	     !PIE_GRAPH_TYPE.equals(graphType) &&
	     !AREA_GRAPH_TYPE.equals(graphType) &&
	     !BAR_GRAPH_TYPE.equals(graphType))
	   {
		throw new IllegalArgumentException("Expect bars types (line|bar|pie)");
	   }
	   
	    this.graphType = graphType;
	}

	/**
	 * @return the detectLegend
	 */
	public boolean isDetectLegend()
	{
		return detectLegend;
	}

	/**
	 * @param detectLegend the detectLegend to set
	 */
	public void setDetectLegend(boolean detectLegend)
	{
		this.detectLegend = detectLegend;
	}
	/**
	 * @return the seriesColors
	 */
	public List<Color> getSeriesColors()
	{
		return seriesColors;
	}

	/**
	 * @param seriesColors the seriesColors to set
	 */
	public void setSeriesColors(List<Color> seriesColors)
	{
		this.seriesColors = seriesColors;
	}
	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	private DefaultPieDataset toPieDataset()
	{
	   int columnCount = dataset.getColumnCount();	
	   if(columnCount == 0)
		throw new RequiredException("Plot at least one value with a label/column");
	   
	   
	   int rowCount = dataset.getRowCount();
	   
	   DefaultPieDataset pieDataset = new DefaultPieDataset();
	   Comparable<?> key;
	   Number value;
	   
	   //use row key and row value for first column
	   for (int row = 0; row < rowCount; row++)
	   {
		key = this.dataset.getRowKey(row);
		
		value = this.dataset.getValue(row, 0); //get value for first column
		pieDataset.setValue(key, value);		
	   }
	
	   return pieDataset;
	}

}
