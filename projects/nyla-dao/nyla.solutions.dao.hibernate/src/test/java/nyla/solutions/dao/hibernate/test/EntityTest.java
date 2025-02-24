package nyla.solutions.dao.hibernate.test;

import java.util.Calendar;
import java.util.Date;

public class EntityTest
{
   /**
    * @return the test1
    */
   public final int getTest1()
   {
      return test1;
   }// --------------------------------------------
   /**
    * @return the test2
    */
   public final String getTest2()
   {
      return test2;
   }// --------------------------------------------
   /**
    * @return the test3
    */
   public final Date getTest3()
   {
      return test3;
   }// --------------------------------------------
   /**
    * @return the test4
    */
   public final double getTest4()
   {
      return test4;
   }// --------------------------------------------

   /**
    * @return the test5
    */
   public final float getTest5()
   {
      return test5;
   }// --------------------------------------------

   /**
    * @return the test6
    */
   public final long getTest6()
   {
      return test6;
   }// --------------------------------------------
   /**
    * @param test1 the test1 to set
    */
   public final void setTest1(int test1)
   {
      this.test1 = test1;
   }// --------------------------------------------
   /**
    * @param test2 the test2 to set
    */
   public final void setTest2(String test2)
   {
      if (test2 == null)
         test2 = "";
   
      this.test2 = test2;
   }// --------------------------------------------

   /**
    * @param test3 the test3 to set
    */
   public final void setTest3(Date test3)
   {   
      this.test3 = test3;
   }// --------------------------------------------

   /**
    * @param test4 the test4 to set
    */
   public final void setTest4(double test4)
   {
      this.test4 = test4;
   }// --------------------------------------------

   /**
    * @param test5 the test5 to set
    */
   public final void setTest5(float test5)
   {
   
      this.test5 = test5;
   }// --------------------------------------------

   /**
    * @param test6 the test6 to set
    */
   public final void setTest6(long test6)
   {
      this.test6 = test6;
   }// --------------------------------------------
   
   private int test1 = 0;
   private String test2 = "test2";
   private Date test3 = Calendar.getInstance().getTime();
   private double test4 = 121;
   private float test5 = 21321;
   private long test6 = 32324324;
}
