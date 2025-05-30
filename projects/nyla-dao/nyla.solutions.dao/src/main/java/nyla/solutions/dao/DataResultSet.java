package nyla.solutions.dao;

import java.io.Serializable;

import java.util.*;

import nyla.solutions.core.data.DataRow;

/**
 * 
 * <pre>
 *  Represents a data set
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class DataResultSet implements Serializable
{

   DataResultSet()
   {
      super();     
   }// --------------------------------------------
   public Collection<String> getColumnNames()
   {
      return columns;
   }// --------------------------------------------


   void addColumn(String aColumnName, int aPosition)
   {
      columns.ensureCapacity(aPosition);
      columns.add(aPosition, aColumnName);
   }// --------------------------------------------

   /**
    * 
    * @param aRow the data row to add
    */
   void addRow(DataRow aRow)
   {
      dataRows.add(aRow);
   }// --------------------------------------------
   /**
    * 
    * @return this.columns.size()
    */
   public int getColumnCount()
   {
      return this.columns.size();
   }// --------------------------------------------
   /**
    * 
    * @return collection of DataRows
    */
   public Collection<DataRow> getRows()
   {
      return this.dataRows;   
   }// --------------------------------------------
  

   /**
    * Return the number of rows in the result set
    * @return dataRows.size()
    */
   public int size()
   {
      if(this.dataRows == null)
         return 0;
      else
         return this.dataRows.size();
   }// --------------------------------------------

   /**
    * 
    * @see java.util.ArrayList#isEmpty()
    */
   public boolean isEmpty()
   {
      return dataRows.isEmpty();
   }// --------------------------------------------
   public String toString()
   {
      return dataRows.toString();
      
   }//----------------------------------------
   static final long serialVersionUID = 1;
   
   private ArrayList<DataRow> dataRows = new ArrayList<DataRow>();

   private ArrayList<String> columns = new ArrayList<String>();

}
