package nyla.solutions.office.chart;

import java.awt.Color;
import java.util.List;

import nyla.solutions.core.data.Binary;
import nyla.solutions.core.data.Nameable;
import nyla.solutions.core.data.Type;


/**
 * Generic representation of a chart generation framework
 * 
 * @author Gregory Green
 * 
 */
public interface Chart extends Binary, Type, Nameable
{

   /**
    * Default graph type BAR_GRAPH_TYPE = "bar"
    */
   String BAR_GRAPH_TYPE = "bar";

   /**
    * LINE_GRAPH_TYPE = "line"
    */
   String LINE_GRAPH_TYPE = "line";

   

   /**
    * PIE_GRAPH_TYPE = "pie"
    */
   String PIE_GRAPH_TYPE = "pie";

   /**
    * AREA_GRAPH_TYPE = "area"
    */
   String AREA_GRAPH_TYPE = "area";
   
   /**
    * GIF_TYPE_NAME = "gif"
    */
   String GIF_TYPE_NAME = "gif";
   /**
    * PNG_TYPE_NAME = "png"
    */
   String PNG_TYPE_NAME = "png";

   /**
    * JPG_TYPE_NAME = "jpg"
    */
   String JPG_TYPE_NAME = "jpg";

   /**
    * 
    * @param value the value to plot
    * @param xAxisValue the row label
    * @param labelCategories the column label key
    */
   void plotValue(double value, Comparable<?> labelCategories, Comparable<?> xAxisValue);

   /**
    * 
    * @param value the value to plot
    * @param xAxisValue the row label
    * @param labelCategories the column label key
    */
   void plotValue(Double value, Comparable<?> labelCategories, Comparable<?> xAxisValue);

   /**
    * 
    * @return the get chart title
    */
   String getTitle();

   /**
    * 
    * @param title set the chart title
    */
   void setTitle(String title);

   /**
    * @return the height
    */
   int getHeight();

   /**
    * @param height the height to set
    */
   void setHeight(int height);

   /**
    * @param categoryLabel the categoryLabel to set
    */
   void setCategoryLabel(String categoryLabel);

   /**
    * @return the categoryLabel
    */
   String getCategoryLabel();

   /**
    * @param valueLabel the valueLabel to set
    */
   void setValueLabel(String valueLabel);

   /**
    * @return the valueLabel
    */
   String getValueLabel();

   /**
    * Expect bars types (line|line3d|bar|pie)
    * 
    * @param graphType the graphType to set
    */
   void setGraphType(String graphType);

   /**
    * @return the graphType
    */
   String getGraphType();

   /**
    * @return the width
    */
   int getWidth();

   /**
    * @param width the width to set
    */
   void setWidth(int width);

   /**
    * @return the backgroundColor
    */
   Color getBackgroundColor();

   /**
    * @param backgroundColor the backgroundColor to set
    */
   void setBackgroundColor(Color backgroundColor);

   /**
    * @return the byteArrayBufferSize
    */
   int getByteArrayBufferSize();

   /**
    * @param byteArrayBufferSize the byteArrayBufferSize to set
    */
   void setByteArrayBufferSize(int byteArrayBufferSize);
   
   /**
    * Indicates whether a legend should be displayed
    * @return the legend boolean
    */
   boolean isLegend();
   
   /**
    * Set whether a legend should be displayed
    * @param legend set the legend
    */
   void setLegend(boolean legend);
   
	/**
	 * @return the seriesColors
	 */
   List<Color> getSeriesColors();
   
   
   /**
	 * @param seriesColors the seriesColors to set
	 */
	void setSeriesColors(List<Color> seriesColors);
	
   
}
