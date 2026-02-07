
package nyla.solutions.office.chart;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.Fileable;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.settings.Settings;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.function.Function;


/**
 * Executable that saves the chart to a file
 * @author Gregory Green
 *
 */
public class FileableChartExecutable implements Function<Settings,Integer>, ChartDecorator, Fileable
{
	private final String rootPath = Config.settings().getProperty(getClass(),"rootPath");
	private Chart chart = null;

	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 50256333546723804L;
	
	/**
	 * Save the chart to a file
	 * @param environment the env
	 * @return the return exit code
	 */
	public Integer apply(Settings environment)
	{
		checkChart();
		
		toFile();
		
		return 1;
	}
	/**
	 * @return the chart
	 */
	public Chart getChart()
	{
		return chart;
	}
	/**
	 * @return the bytes
	 */
	public byte[] getBytes()
	{
		checkChart();
		
		return chart.getBytes();
	}
	
	
	
	/**
	 * @return the series of colors
	 */
	public List<Color> getSeriesColors()
	{
		return chart.getSeriesColors();
	}
	/**
	 * @param seriesColors the series colors
	 * @see nyla.solutions.office.chart.Chart#setSeriesColors(java.util.List)
	 */
	public void setSeriesColors(List<Color> seriesColors)
	{
		chart.setSeriesColors(seriesColors);
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		checkChart();
		
		return chart.getName();
	}
	/**
	 * @return the type name
	 */
	public String getTypeName()
	{
		checkChart();
		
		return chart.getTypeName();
	}
	/**
	 * @param value the value
	 * @param rowKey the row key
	 * @param label the label
	 */
	public void plotValue(double value, Comparable<?> rowKey, Comparable<?> label)
	{
		checkChart();
		
		chart.plotValue(value, rowKey, label);
	}
	/**
	 * @param value
	 * @param rowKey
	 * @param label
	 */
	public void plotValue(Double value, Comparable<?> rowKey, Comparable<?> label)
	{
		checkChart();
		
		chart.plotValue(value, rowKey, label);
	}
	/**
	 * @param name the name
	 */
	public void setName(String name)
	{
		checkChart();
		
		chart.setTitle(name);
	}
	/**
	 * @param typeName the type name
	 */
	public void setTypeName(String typeName)
	{
		checkChart();
		chart.setTypeName(typeName);
	}
	public File getFile()
	{
		checkChart();
		
		return toFile();
	}
	/**
	 * <chart-name>.<chart-type>
	 * @return this.chart.getName()+"."+this.chart.getTypeName()
	 */
	public String getFileName()
	{
		checkChart();
		
		return this.chart.getName()+"."+this.chart.getTypeName();
	}
	/**
	 * @param chart the chart to set
	 */
	public void setChart(Chart chart)
	{
		if(chart == null)
			throw new RequiredException("chart in FileableChartExecutable");
		
		this.chart = chart;
	}
	/**
	 * Assert that the chart is set
	 */
	private void checkChart()
	{
		if(chart == null)
			throw new RequiredException("Chart not set in "+this.getClass().getName());
	}
	private File toFile()
	{
		
		try
		{
			File file = new File(this.rootPath+"/"+this.getFileName());
			
			IO.writer().writeFile(file, chart.getBytes());
			
			return file;
			
		} catch (IOException e)
		{
			throw new SystemException(this.rootPath+"/"+this.getFileName()+" "+Debugger.stackTrace(e));
		}
	}


	/**
	 * @return category label
	 */
	public String getCategoryLabel()
	{
		return chart.getCategoryLabel();
	}
	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return chart.getHeight();
	}
	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return chart.getTitle();
	}
	/**
	 * @return the value label
	 */
	public String getValueLabel()
	{
		return chart.getValueLabel();
	}
	/**
	 * @param categoryLabel the category label
	 */
	public void setCategoryLabel(String categoryLabel)
	{
		chart.setCategoryLabel(categoryLabel);
	}
	/**
	 * @param height the height
	 */
	public void setHeight(int height)
	{
		chart.setHeight(height);
	}
	/**
	 * @param title the title
	 */
	public void setTitle(String title)
	{
		chart.setTitle(title);
	}
	/**
	 * @param valueLabel the value label
	 */
	public void setValueLabel(String valueLabel)
	{
		chart.setValueLabel(valueLabel);
	}


	/**
	 * @return back color
	 * @see nyla.solutions.office.chart.Chart#getBackgroundColor()
	 */
	public Color getBackgroundColor()
	{
	   return chart.getBackgroundColor();
	}
	/**
	 * @return the byte array buffer size
	 * @see nyla.solutions.office.chart.Chart#getByteArrayBufferSize()
	 */
	public int getByteArrayBufferSize()
	{
	   return chart.getByteArrayBufferSize();
	}
	/**
	 * @return graph type
	 */
	public String getGraphType()
	{
	   return chart.getGraphType();
	}
	/**
	 * @return if is using a legend
	 */
	public boolean isLegend()
	{
	   return chart.isLegend();
	}
	/**
	 * @return the width
	 */
	public int getWidth()
	{
	   return chart.getWidth();
	}
	/**
	 * @param backgroundColor
	 * @see nyla.solutions.office.chart.Chart#setBackgroundColor(java.awt.Color)
	 */
	public void setBackgroundColor(Color backgroundColor)
	{
	   chart.setBackgroundColor(backgroundColor);
	}
	/**
	 * @param byteArrayBufferSize
	 * @see nyla.solutions.office.chart.Chart#setByteArrayBufferSize(int)
	 */
	public void setByteArrayBufferSize(int byteArrayBufferSize)
	{
	   chart.setByteArrayBufferSize(byteArrayBufferSize);
	}
	/**
	 * @param graphType
	 * @see nyla.solutions.office.chart.Chart#setGraphType(java.lang.String)
	 */
	public void setGraphType(String graphType)
	{
	   chart.setGraphType(graphType);
	}
	/**
	 * @param legend
	 * @see nyla.solutions.office.chart.Chart#setLegend(boolean)
	 */
	public void setLegend(boolean legend)
	{
	   chart.setLegend(legend);
	}
	/**
	 * @param width
	 * @see nyla.solutions.office.chart.Chart#setWidth(int)
	 */
	public void setWidth(int width)
	{
	   chart.setWidth(width);
	}




}
