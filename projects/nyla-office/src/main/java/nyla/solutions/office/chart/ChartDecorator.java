package nyla.solutions.office.chart;

/**
 * This class wraps a chart object
 * (see Decorator Design Pattern) 
 * @author Gregory Green
 *
 */
public interface ChartDecorator extends Chart
{
	/**
	 * 
	 * @return the chart 
	 */
	public Chart getChart();
	
	/**
	 * 
	 * @param chart the char
	 */
	public void setChart(Chart chart);
}
