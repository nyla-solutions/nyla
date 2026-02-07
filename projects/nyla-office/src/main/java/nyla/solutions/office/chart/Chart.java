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
    * PIE_3D_GRAPH_TYPE = "pie3d"
    */
   public static final String PIE_3D_GRAPH_TYPE = "pie3d";
   /**
    * Default graph type BAR_GRAPH_TYPE = "bar"
    */
   public static final String BAR_GRAPH_TYPE = "bar";

   /**
    * LINE_GRAPH_TYPE = "line"
    */
   public static final String LINE_GRAPH_TYPE = "line";

   

   /**
    * PIE_GRAPH_TYPE = "pie"
    */
   public static final String PIE_GRAPH_TYPE = "pie";

   /**
    * AREA_GRAPH_TYPE = "area"
    */
   public static final String AREA_GRAPH_TYPE = "area";
   
   /**
    * GIF_TYPE_NAME = "gif"
    */
   public static final String GIF_TYPE_NAME = "gif";
   /**
    * PNG_TYPE_NAME = "png"
    */
   public static final String PNG_TYPE_NAME = "png";

   /**
    * JPG_TYPE_NAME = "jpg"
    */
   public static final String JPG_TYPE_NAME = "jpg";

   /**
    * 
    * @param value the value to plot
    * @param xAxisValue the row label
    * @param labelCategories the column label key
    */
   public void plotValue(double value, Comparable<?> labelCategories, Comparable<?> xAxisValue);

   /**
    * 
    * @param value the value to plot
    * @param xAxisValue the row label
    * @param labelCategories the column label key
    */
   public void plotValue(Double value, Comparable<?> labelCategories, Comparable<?> xAxisValue);

   /**
    * 
    * @return the get chart title
    */
   public String getTitle();

   /**
    * 
    * @param title set the chart title
    */
   public void setTitle(String title);

   /**
    * @return the height
    */
   public int getHeight();

   /**
    * @param height the height to set
    */
   public void setHeight(int height);

   /**
    * @param categoryLabel the categoryLabel to set
    */
   public void setCategoryLabel(String categoryLabel);

   /**
    * @return the categoryLabel
    */
   public String getCategoryLabel();

   /**
    * @param valueLabel the valueLabel to set
    */
   public void setValueLabel(String valueLabel);

   /**
    * @return the valueLabel
    */
   public String getValueLabel();

   /**
    * Expect bars types (line|line3d|bar|pie)
    * 
    * @param graphType the graphType to set
    */
   public void setGraphType(String graphType);

   /**
    * @return the graphType
    */
   public String getGraphType();

   /**
    * @return the width
    */
   public int getWidth();

   /**
    * @param width the width to set
    */
   public void setWidth(int width);

   /**
    * @return the backgroundColor
    */
   public Color getBackgroundColor();

   /**
    * @param backgroundColor the backgroundColor to set
    */
   public void setBackgroundColor(Color backgroundColor);

   /**
    * @return the byteArrayBufferSize
    */
   public int getByteArrayBufferSize();

   /**
    * @param byteArrayBufferSize the byteArrayBufferSize to set
    */
   public void setByteArrayBufferSize(int byteArrayBufferSize);
   
   /**
    * Indicates whether a legend should be displayed
    * @return the legend boolean
    */
   public boolean isLegend();
   
   /**
    * Set whether a legend should be displayed
    * @param legend set the legend
    */
   public void setLegend(boolean legend);
   
	/**
	 * @return the seriesColors
	 */
   public List<Color> getSeriesColors();
   
   
   /**
	 * @param seriesColors the seriesColors to set
	 */
	public void setSeriesColors(List<Color> seriesColors);
	
   
}
