package nyla.solutions.dao.jdo;

import java.util.List;
import java.util.Map;





/**

 * <pre>

 * 

 *  

 *   

 *    QueryBuilder provides a set of functions to

 *    

 *   

 *  

 * </pre>

 * 

 * @author Gregory Green

 * @version 1.0

 */

public interface JDOQueryBuilder 

{

   /**

    * Result list of the configure objects for the query

    * @return the query results   
    */

   public Object getQueryResults();

   

   /**

    * PUBLIC: Function, return an expression that adds to a date based on the

    * specified datePart. This is eqivalent to the Sybase DATEADD funtion.

    * 

    * Example:

    * 

    * JDO: builder.get("date").addDate("year", 2) Java: NA SQL: DATEADD(date, 2,

    * year)

    */

   public JDOQueryBuilder addDate(java.lang.String aAttributeName,int aMonths);

   

   /**

    * PUBLIC: Return an expression that is the boolean logical combination of

    * both expressions. This is equivalent to the SQL "AND" operator and the

    * Java "&&" operator.

    * 

    * Example: JDOQueryBuilder employee ... JDO:

    * employee.get("firstName").equal("Bob")

    * .and(employee.get("lastName").equal("Smith"))

    *  

    */

   public JDOQueryBuilder and(JDOQueryBuilder aBuilder);

   

   public JDOQueryBuilder or(JDOQueryBuilder aBuilder);

   

   public JDOQueryBuilder equal(byte aArgument);

   

   public JDOQueryBuilder equal(char aArgument);

   

   public JDOQueryBuilder equal(double aArgument);

   

   public JDOQueryBuilder equal(float aArgument);

   

   public JDOQueryBuilder equal(int aArgument);

   

   public JDOQueryBuilder equal(long aArgument);

   

   public JDOQueryBuilder equal(java.lang.Object aArgument);

   

   public JDOQueryBuilder equal(JDOQueryBuilder aBuilder);

   

   public JDOQueryBuilder equal(short aArgument);

   

   public JDOQueryBuilder equal(boolean aArgument);

   

   public JDOQueryBuilder equalOuterJoin(java.lang.Object aArgument);

   

   public JDOQueryBuilder equalOuterJoin(JDOQueryBuilder aArgument);

   

   public JDOQueryBuilder equalsIgnoreCase(java.lang.String aArgument);

   

   public JDOQueryBuilder equalsIgnoreCase(JDOQueryBuilder aJDOQueryBuilder);

   

   public JDOQueryBuilder greaterThan(byte aArgument);

   

   public JDOQueryBuilder greaterThan(char aArgument);

   

   public JDOQueryBuilder greaterThan(double aArgument);

   

   public JDOQueryBuilder greaterThan(float aArgument);

   

   public JDOQueryBuilder greaterThan(int aArgument);

   

   public JDOQueryBuilder greaterThan(long aArgument);

   

   public JDOQueryBuilder greaterThan(java.lang.Object aArgument);

   

   /**

    * PUBLIC: Function, return an expression that adds to a date based on the

    * specified datePart. This is eqivalent to the Sybase DATEADD funtion.

    * Example: JDO: builder.get("date").addDate("year", 2) Java: NA SQL:

    * DATEADD(date, 2, year)

    */

   public JDOQueryBuilder addDate(String aDatePart,Object numberToAdd);

   

   /**

    * PUBLIC: Function, to add months to a date.

    */

   public JDOQueryBuilder addMonths(int aMonths);

   

   /**

    * Function, to add months to a date.

    */

   public JDOQueryBuilder addMonths(Object aMonths);

   

   /**

    * PUBLIC: Return an expression representing traversal of a 1:many or many:many

    * relationship. This allows you to query whether any of the "many" side of the

    * relationship satisfies the remaining criteria.

    * 

    * builder = builder.anyOf("managedEmployees").get("firstName").equal("Bob");

    * SQL SELECT DISTINCT ... WHERE (t2.MGR_ID (+) = t1.ID) AND (t2.F_NAME = 'Bob')

    *  

    */

   public JDOQueryBuilder anyOf(String aArgument);

   

   /**

    * ADVANCED: Return an expression representing traversal of a 1:many or

    * many:many relationship. This allows you to query whether any of the "many"

    * side of the relationship satisfies the remaining criteria. This version of

    * the anyOf operation performs an outer join. Outer joins allow the join to

    * performed even if the target of the relationship is empty. NOTE: outer joins

    * are not supported on all database and have differing symantics.

    * 

    * Example:

    * 

    * Format Equivalent

    * 

    * builder.get("id").equal("14858"); builder =

    * builder.or(builder.anyOfAllowingNone("managedEmployees")

    * .get("firstName").equal("Bob"));

    * 

    * SQL SELECT DISTINCT ... WHERE (t2.MGR_ID (+) = t1.ID) AND (t2.F_NAME = 'Bob')

    */

   public JDOQueryBuilder anyOfAllowingNone(String aArgument);

  

       

       /**

        * This can only be used within an ordering expression.

        * 

        * It will order the result ascending. Example:

        * 

        * expBuilder.get("address").get("city").ascending()

        */

       public JDOQueryBuilder ascending();

       

       

       /**

        * Function, returns the single character strings ascii value.

        */

       public JDOQueryBuilder asciiValue();   

       

       /**

        * Function, This represents the aggregate function Average. Can be used only

        * within Report Queries

        */

       public JDOQueryBuilder average();

       

       

       /**

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(byte aArgument1,byte aArgument2);

       

       

       /**

        * employee.get("character").between('A','C')

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(char aArgument1,char aArgument2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(double aArgument1,double aArgument2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(float aArgument1,float aArgument2);

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(int aArgument1,int aArgument2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(long aArgument1,long aArgument2);

       

       

       /**

        * employee.get("letter").between("A","C")

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(Object aArgument1,Object aArgument2);

       

       

       /**

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(JDOQueryBuilder  aBuilder1,JDOQueryBuilder aBuilder2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public JDOQueryBuilder between(short  aArgument1,short aArgument2);

       

       /**

        * @param aArgument

        *           the object to concate

        * @return the concatenation of the two string values.

        */

       public JDOQueryBuilder concat(Object aArgument);

       

       

       /**

        * PUBLIC: Return an expression that performs a key word search. Example:

        * 

        * JDOBuilder project = ...

        * project.get("description").containsAllKeyWords("TopLink rdbms java")

        * 

        * @param aSpaceSeperatedKeyWords

        *           Space Seperated Key Words

        * @return

        */

       public JDOQueryBuilder containsAllKeyWords(String aSpaceSeperatedKeyWords);   

       

       /**

        * PUBLIC: Return an expression that performs a key word search vor any of the

        * given words. Example:

        * 

        * JDOBuilder project = ...

        * project.get("description").containsAnyKeyWords("TopLink rdbms java")

        * 

        * @param aSpaceSeperatedKeyWords

        *           Space Seperated Key Words

        * @return

        */

       public JDOQueryBuilder containsAnyKeyWords(String aSpaceSeperatedKeyWords);   

       

       /**

        * PUBLIC: Return an expression that compares if the receivers value contains

        * the substring.

        * 

        * Example: JDOQueryBuilder employee = ...

        * employee.get("firstName").containsSubstring("Bob")

        * 

        * SQL: F_NAME LIKE '%BOB%'

        * 

        * @param aArgument

        *           the string

        * @return

        */

       public JDOQueryBuilder containsSubstring(String aArgument);

       

       /**

        * PUBLIC: Return an expression that compares if the receivers value contains

        * the substring.

        * 

        * Example: JDOQueryBuilder employee = ...

        * employee.get("firstName").containsSubstring("Bob")

        * 

        * SQL: F_NAME LIKE '%BOB%'

        * 

        * @param aArgument

        *           the string

        * @return

        */

       public JDOQueryBuilder containsSubstring(JDOQueryBuilder aBuilder);

       

       public JDOQueryBuilder containsSubstringIgnoringCase(String aArgument);  

       

       /**

        * Return an expression that compares if the receivers value contains the

        * substring, ignoring case.

        * 

        * Example:

        * 

        * employee.get("firstName").containsSubstringIgnoringCase("Bob") SQL:

        * TOUPPER(F_NAME) LIKE '%BOB%'

        * 

        * @param aBuilder

        *           the query builder contain the expressions

        * @return

        */

       public JDOQueryBuilder containsSubstringIgnoringCase(JDOQueryBuilder aBuilder);

       

       public JDOQueryBuilder convertToUseOuterJoin();

       

       

       /**

        * This represents the aggregate function Average. Can be used only within

        * Report Queries.

        * 

        * @return

        */

       public JDOQueryBuilder count();

       

       /**

        * 

        * @return database system time

        */

       public JDOQueryBuilder currentDate();

       

       /**

        * PUBLIC: Function, Return the difference between the queried part of a

        * date(i.e. years, days etc.) and same part of the given date. The equivalent

        * of the Sybase function DateDiff Example:

        * 

        * employee.get("date").dateDifference("year", new

        * Date(System.currentTimeMillis()))

        * 

        * SQL: DATEADD(date, 2, GETDATE)

        * 

        * @param aDatePart

        *           (year,month,date)

        * @param aDate

        * @return

        */

       public JDOQueryBuilder dateDifference(String aDatePart,java.util.Date aDate);

       

       /**

        * Function, Return the difference between the queried part of a date(i.e.

        * years, days etc.) and same part of the given date. The equivalent of the

        * Sybase function DateDiff Example:

        * 

        * @param aDatePart

        *           (year,month,date)

        * @param aBuilder

        * @return

        */

       public JDOQueryBuilder dateDifference(String  aDatePart,JDOQueryBuilder aBuilder);

       

       

       /**

        * public Expression decode(java.util.Hashtable decodeableItems,

        * java.lang.String aDefautValue)

        * 

        * PUBLIC: Function Convert values returned by the query to values given in the

        * decodeableItems hashtable. The equivalent of the Oracle DECODE function.

        * Note: This will only work on databases that support Decode with the syntax

        * below.

        * 

        * Example:

        * 

        * Hashtable decodeTable = new Hashtable();

        * 

        * decodeTable.put("Robert", "Bob"); decodeTable.put("Susan", "Sue");

        * 

        * employee.get("name").decode(decodeTable, "No-Nickname")

        * 

        * SQL: DECODE(name, "Robert", "Bob", "Susan", "Sue", "No-Nickname")

        * 

        * @param aMap

        *           java.util.Hashtable a hashtable containing the items to be decoded.

        *           Keys represent the items to match coming from the query. Values

        *           represent what a key will be changed to.

        * @param aDefautValue

        *           the default value that will be used if none of the keys in the

        *           hashtable match

        * @return

        */

       public JDOQueryBuilder decode(Map<?,?> aMap,String aDefautValue);

       

       /**

        * PUBLIC: This can only be used within an ordering expression. It will order

        * the result descending.

        * 

        * Example: expBuilder.get("address").get("city").descending()

        * 

        * @return

        */

       public JDOQueryBuilder descending();

        

        /**

         * PUBLIC: Function, This represents the distinct option inside an aggregate

         * function. Can be used only within Report Queries

         * 

         * @return

         */

        public JDOQueryBuilder distinct();

         

         /**

          * PUBLIC: Return an expression that wraps the attribute or query key name. This

          * method is used to construct user-defined queries containing joins.

          * 

          * Example: builder.get("address").get("city").equal("Ottawa");

          * 

          * @param aAttributName

          * @return

          */

         public JDOQueryBuilder getColumn(String aAttributName);

         

         public JDOQueryBuilder getAllowingNull(String aAttributeName);

         

         public JDOQueryBuilder greaterThan(short aArgument);

         

         public JDOQueryBuilder greaterThan(boolean aArgument);

         

         public JDOQueryBuilder greaterThanEqual(byte aArgument);

         

         public JDOQueryBuilder greaterThanEqual(char aArgument);

         

         public JDOQueryBuilder greaterThanEqual(double aArgument);

         

         public JDOQueryBuilder greaterThanEqual(float aArgument);

         

         

         public JDOQueryBuilder greaterThanEqual(int aArgument);

         

         

         public JDOQueryBuilder greaterThanEqual(long aArgument);

         

         

         public JDOQueryBuilder greaterThanEqual(Object aArgument);

         

         

         public JDOQueryBuilder greaterThanEqual(JDOQueryBuilder aArgument);

         

         

         public JDOQueryBuilder greaterThanEqual(short aArgument);

         

         

         public JDOQueryBuilder greaterThanEqual(boolean aArgument);

         

         

         public JDOQueryBuilder in(byte[] aArgument);

         

         public JDOQueryBuilder in(char[] aArgument);

         

         

         public JDOQueryBuilder in(double[] aArgument);

         

         

         public JDOQueryBuilder in(float[] aArgument);

         

         

         public JDOQueryBuilder in(int[] aArgument);

         

         

         public JDOQueryBuilder in(long[] aArgument);

         

         

         public JDOQueryBuilder in(Object[] aArgument);

         

         

         public JDOQueryBuilder in(short[] aArgument);

         

         

         public JDOQueryBuilder in(boolean[] aArgument);


         public JDOQueryBuilder in(List<?> aArgument);

         
         public JDOQueryBuilder in(JDOQueryBuilder aArgument);


         /**

          * ADVANCED: Return a user defined function accepting all of the arguments. The

          * function is assumed to be a normal prefix function like, CONCAT(base, value1,

          * value2, value3, ...);.

          * 

          * @param aFunction

          * @param aArguments

          * @return

          */public JDOQueryBuilder getFunctionWithArguments(String aFunction,Object [] aArguments);

          

          /**

           * Return a user defined function accepting the argument. The function is

           * assumed to be a normal prefix function and will print like, CONCAT(base,

           * argument);.

           * 

           * @param aFunction

           * @param aArgument

           * @return

           */

          public JDOQueryBuilder getFunction(String  aFunction,Object aArgument);

          

          

          /**

           * ADVANCED: Return a user defined function accepting the argument. The function

           * is assumed to be a normal prefix function and will print like, UPPER(base);.

           * Example:

           * 

           * builder.get("firstName");.getFunction("UPPER");

           * 

           * @param aFunction

           * @return

           */

          public JDOQueryBuilder getFunction(String aFunction);

          

          public JDOQueryBuilder isNull();

          

          public JDOQueryBuilder lastDay();

          

          public JDOQueryBuilder leftTrim();

          

          /**

           * 

           * @return returns the size of the string.

           */

          public JDOQueryBuilder length();

          

          public JDOQueryBuilder lessThan(byte aArgument);

          

          public JDOQueryBuilder lessThan(char aArgument);

          

          public JDOQueryBuilder lessThan(double aArgument);

          

          public JDOQueryBuilder lessThan(float aArgument);

          

          public JDOQueryBuilder lessThan(int aArgument);

          

          public JDOQueryBuilder lessThan(long aArgument);

          

          

          public JDOQueryBuilder lessThan(Object aArgument);

          

          

          public JDOQueryBuilder lessThan(JDOQueryBuilder aBuilder);

          

          

          public JDOQueryBuilder lessThan(short aArgument);

          

          

          public JDOQueryBuilder lessThan(boolean aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(byte aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(char aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(double aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(float aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(int aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(long aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(Object aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(JDOQueryBuilder aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(short aArgument);

          

          

          public JDOQueryBuilder lessThanEqual(boolean aArgument);

          

          

          public JDOQueryBuilder like(String aArgument);

          

          

          public JDOQueryBuilder like(String  aArgument1,String aArgument2);

          

          

          public JDOQueryBuilder like(JDOQueryBuilder aBuilder);

          

          

          public JDOQueryBuilder likeIgnoreCase(String aArgument);

          

          

          public JDOQueryBuilder likeIgnoreCase(JDOQueryBuilder aBuilder);

          

          

          public JDOQueryBuilder maximum();

          

          /**
           * This represents the aggregate function Minimum. Can be used only within Report Queries. 
           * @return
           */
          public JDOQueryBuilder minimum();

          

          

          public JDOQueryBuilder not();

          

          

          public JDOQueryBuilder notBetween(byte aArgument1,byte aArgument2);

          

          

          public JDOQueryBuilder notBetween(char  aArgument1,char aArgument2);

          

          public JDOQueryBuilder notBetween(double  aArgument1,double aArgument2);

          

          

          public JDOQueryBuilder notBetween(float  aArgument1,float aArgument2);

          

          

          public JDOQueryBuilder notBetween(int aArgument1,int aArgument2);

          

          

          public JDOQueryBuilder notBetween(long  aArgument1,long aArgument2);

          

          

          public JDOQueryBuilder notBetween(Object  aArgument1,Object aArgument2);

          

          

          public JDOQueryBuilder notBetween(short  aArgument1,short aArgument2);

          

          

          public JDOQueryBuilder notBetween(JDOQueryBuilder  aBuilder1,JDOQueryBuilder aBuilder2);

          

          

          public JDOQueryBuilder notEqual(byte aArgument);

          

          

          public JDOQueryBuilder notEqual(char aArgument);

          

          

          public JDOQueryBuilder notEqual(double aArgument);

          

          

          public JDOQueryBuilder notEqual(float aArgument);

          

          

          public JDOQueryBuilder notEqual(int aArgument);

          

          

          public JDOQueryBuilder notEqual(long aArgument);

          

          

          public JDOQueryBuilder notEqual(Object aArgument);

          

          

          public JDOQueryBuilder notEqual(JDOQueryBuilder aBuilder);

          

          

          public JDOQueryBuilder notEqual(short aArgument);

          

          

          public JDOQueryBuilder notEqual(boolean aArgument);

          

          

          public JDOQueryBuilder notIn(byte[] aArgument);

          

          

          public JDOQueryBuilder notIn(char[] aArgument);

          

          

          public JDOQueryBuilder notIn(double[] aArgument);

          

          

          public JDOQueryBuilder notIn(float[] aArgument);

          

          

          public JDOQueryBuilder notIn(int[] aArgument);

          

          

          public JDOQueryBuilder notIn(long[] aArgument);

          

          

          public JDOQueryBuilder notIn(Object[] aArgument);

          

          

          public JDOQueryBuilder notIn(short[] aArgument);

          

          

          public JDOQueryBuilder notIn(boolean[] aArgument);

          

          

          public JDOQueryBuilder notIn(JDOQueryBuilder aBuilder);

          

          

          public JDOQueryBuilder notLike(String aArgument);

          

          

          public JDOQueryBuilder notLike(JDOQueryBuilder aBuilder);

          

          

          public JDOQueryBuilder notNull();

          

          

          public JDOQueryBuilder reverse();

          

          

          public JDOQueryBuilder rightTrim();

          

          

          public JDOQueryBuilder substring(int  aArgument1,int aArgument2);

          

          

          public JDOQueryBuilder sum();

          

          

          public JDOQueryBuilder toCharacter();

          

          

          public JDOQueryBuilder toDate();

          

          

          public JDOQueryBuilder toLowerCase();

          

          

          public JDOQueryBuilder toNumber();

          

          

          public JDOQueryBuilder toUpperCase();

          

          

          public JDOQueryBuilder toUppercaseCasedWords();

          

          

          public JDOQueryBuilder trim();

          public JDOQueryBuilder appendSQL(String aSQL);      
          
          public JDOQueryBuilder prefixSQL(String aSQL);
 }