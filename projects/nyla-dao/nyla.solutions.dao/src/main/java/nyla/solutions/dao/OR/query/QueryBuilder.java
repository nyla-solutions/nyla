package nyla.solutions.dao.OR.query;

import java.util.List;





/**
 * <pre>
 *   
 *    QueryBuilder provides an interface to query data.
 *  
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */

public interface QueryBuilder 

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

   public QueryBuilder addDate(java.lang.String aAttributeName,int aMonths);

   

   /**

    * PUBLIC: Return an expression that is the boolean logical combination of

    * both expressions. This is equivalent to the SQL "AND" operator and the

    * Java "&&" operator.

    * 

    * Example: QueryBuilder employee ... JDO:

    * employee.get("firstName").equal("Bob")

    * .and(employee.get("lastName").equal("Smith"))

    *  

    */

   public QueryBuilder and(QueryBuilder aBuilder);

   

   public QueryBuilder or(QueryBuilder aBuilder);

   

   public QueryBuilder equal(byte aArgument);

   

   public QueryBuilder equal(char aArgument);

   

   public QueryBuilder equal(double aArgument);

   

   public QueryBuilder equal(float aArgument);

   

   public QueryBuilder equal(int aArgument);

   

   public QueryBuilder equal(long aArgument);

   

   public QueryBuilder equal(java.lang.Object aArgument);

   

   public QueryBuilder equal(QueryBuilder aBuilder);

   

   public QueryBuilder equal(short aArgument);

   

   public QueryBuilder equal(boolean aArgument);

   

   public QueryBuilder equalOuterJoin(java.lang.Object aArgument);

   

   public QueryBuilder equalOuterJoin(QueryBuilder aArgument);

   

   public QueryBuilder equalsIgnoreCase(java.lang.String aArgument);

   

   public QueryBuilder equalsIgnoreCase(QueryBuilder aQueryBuilder);

   

   public QueryBuilder greaterThan(byte aArgument);

   

   public QueryBuilder greaterThan(char aArgument);

   

   public QueryBuilder greaterThan(double aArgument);

   

   public QueryBuilder greaterThan(float aArgument);

   

   public QueryBuilder greaterThan(int aArgument);

   

   public QueryBuilder greaterThan(long aArgument);

   

   public QueryBuilder greaterThan(java.lang.Object aArgument);

   

   /**

    * PUBLIC: Function, return an expression that adds to a date based on the

    * specified datePart. This is eqivalent to the Sybase DATEADD funtion.

    * Example: JDO: builder.get("date").addDate("year", 2) Java: NA SQL:

    * DATEADD(date, 2, year)

    */

   public QueryBuilder addDate(String aDatePart,Object numberToAdd);

   

   /**

    * PUBLIC: Function, to add months to a date.

    */

   public QueryBuilder addMonths(int aMonths);

   

   /**

    * Function, to add months to a date.

    */

   public QueryBuilder addMonths(Object aMonths);

   

   /**

    * PUBLIC: Return an expression representing traversal of a 1:many or many:many

    * relationship. This allows you to query whether any of the "many" side of the

    * relationship satisfies the remaining criteria.

    * 

    * builder = builder.anyOf("managedEmployees").get("firstName").equal("Bob");

    * SQL SELECT DISTINCT ... WHERE (t2.MGR_ID (+) = t1.ID) AND (t2.F_NAME = 'Bob')

    *  

    */

   public QueryBuilder anyOf(String aArgument);

   

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

   public QueryBuilder anyOfAllowingNone(String aArgument);

  

       

       /**

        * This can only be used within an ordering expression.

        * 

        * It will order the result ascending. Example:

        * 

        * expBuilder.get("address").get("city").ascending()

        */

       public QueryBuilder ascending();

       

       

       /**

        * Function, returns the single character strings ascii value.

        */

       public QueryBuilder asciiValue();   

       

       /**

        * Function, This represents the aggregate function Average. Can be used only

        * within Report Queries

        */

       public QueryBuilder average();

       

       

       /**

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(byte aArgument1,byte aArgument2);

       

       

       /**

        * employee.get("character").between('A','C')

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(char aArgument1,char aArgument2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(double aArgument1,double aArgument2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(float aArgument1,float aArgument2);

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(int aArgument1,int aArgument2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(long aArgument1,long aArgument2);

       

       

       /**

        * employee.get("letter").between("A","C")

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(Object aArgument1,Object aArgument2);

       

       

       /**

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(QueryBuilder  aBuilder1,QueryBuilder aBuilder2);

       

       

       /**

        * employee.get("age").between(19,50)

        * 

        * @param aArgument1

        *           first value

        * @param aArgument2

        *           second value

        * @return expression to test whether a column is between the given values

        */

       public QueryBuilder between(short  aArgument1,short aArgument2);

       

       /**

        * @param aArgument

        *           the object to concate

        * @return the concatenation of the two string values.

        */

       public QueryBuilder concat(Object aArgument);

       

       

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

       public QueryBuilder containsAllKeyWords(String aSpaceSeperatedKeyWords);   

       

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

       public QueryBuilder containsAnyKeyWords(String aSpaceSeperatedKeyWords);   

       

       /**

        * PUBLIC: Return an expression that compares if the receivers value contains

        * the substring.

        * 

        * Example: QueryBuilder employee = ...

        * employee.get("firstName").containsSubstring("Bob")

        * 

        * SQL: F_NAME LIKE '%BOB%'

        * 

        * @param aArgument

        *           the string

        * @return

        */

       public QueryBuilder containsSubstring(String aArgument);

       

       /**

        * PUBLIC: Return an expression that compares if the receivers value contains

        * the substring.

        * 

        * Example: QueryBuilder employee = ...

        * employee.get("firstName").containsSubstring("Bob")

        * 

        * SQL: F_NAME LIKE '%BOB%'

        * 

        * @param aArgument

        *           the string

        * @return

        */

       public QueryBuilder containsSubstring(QueryBuilder aBuilder);

       

       public QueryBuilder containsSubstringIgnoringCase(String aArgument);  

       

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

       public QueryBuilder containsSubstringIgnoringCase(QueryBuilder aBuilder);

       

       public QueryBuilder convertToUseOuterJoin();

       

       

       /**

        * This represents the aggregate function Average. Can be used only within

        * Report Queries.

        * 

        * @return

        */

       public QueryBuilder count();

       

       /**

        * 

        * @return database system time

        */

       public QueryBuilder currentDate();

       

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

       public QueryBuilder dateDifference(String aDatePart,java.util.Date aDate);

       

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

       public QueryBuilder dateDifference(String  aDatePart,QueryBuilder aBuilder);

       

       

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

       public QueryBuilder decode(java.util.Map<?,?> aMap,String aDefautValue);

       

       /**

        * PUBLIC: This can only be used within an ordering expression. It will order

        * the result descending.

        * 

        * Example: expBuilder.get("address").get("city").descending()

        * 

        * @return

        */

       public QueryBuilder descending();

        

        /**

         * PUBLIC: Function, This represents the distinct option inside an aggregate

         * function. Can be used only within Report Queries

         * 

         * @return

         */

        public QueryBuilder distinct();

         

         /**

          * PUBLIC: Return an expression that wraps the attribute or query key name. This

          * method is used to construct user-defined queries containing joins.

          * 

          * Example: builder.get("address").get("city").equal("Ottawa");

          * 

          * @param aAttributName

          * @return

          */

         public QueryBuilder getColumn(String aAttributName);

         

         public QueryBuilder getAllowingNull(String aAttributeName);

         

         public QueryBuilder greaterThan(short aArgument);

         

         public QueryBuilder greaterThan(boolean aArgument);

         

         public QueryBuilder greaterThanEqual(byte aArgument);

         

         public QueryBuilder greaterThanEqual(char aArgument);

         

         public QueryBuilder greaterThanEqual(double aArgument);

         

         public QueryBuilder greaterThanEqual(float aArgument);

         

         

         public QueryBuilder greaterThanEqual(int aArgument);

         

         

         public QueryBuilder greaterThanEqual(long aArgument);

         

         

         public QueryBuilder greaterThanEqual(Object aArgument);

         

         

         public QueryBuilder greaterThanEqual(QueryBuilder aArgument);

         

         

         public QueryBuilder greaterThanEqual(short aArgument);

         

         

         public QueryBuilder greaterThanEqual(boolean aArgument);

         

         

         public QueryBuilder in(byte[] aArgument);

         

         public QueryBuilder in(char[] aArgument);

         

         

         public QueryBuilder in(double[] aArgument);

         

         

         public QueryBuilder in(float[] aArgument);

         

         

         public QueryBuilder in(int[] aArgument);

         

         

         public QueryBuilder in(long[] aArgument);

         

         

         public QueryBuilder in(Object[] aArgument);

         

         

         public QueryBuilder in(short[] aArgument);

         

         

         public QueryBuilder in(boolean[] aArgument);

         

         

         public QueryBuilder in(List<?> aArgument);

         

         

         public QueryBuilder in(QueryBuilder aArgument);

         

         

         /**

          * ADVANCED: Return a user defined function accepting all of the arguments. The

          * function is assumed to be a normal prefix function like, CONCAT(base, value1,

          * value2, value3, ...);.

          * 

          * @param aFunction

          * @param aArguments

          * @return

          */public QueryBuilder getFunctionWithArguments(String aFunction,Object [] aArguments);

          

          /**

           * Return a user defined function accepting the argument. The function is

           * assumed to be a normal prefix function and will print like, CONCAT(base,

           * argument);.

           * 

           * @param aFunction

           * @param aArgument

           * @return

           */

          public QueryBuilder getFunction(String  aFunction,Object aArgument);

          

          

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

          public QueryBuilder getFunction(String aFunction);

          

          public QueryBuilder isNull();

          

          public QueryBuilder lastDay();

          

          public QueryBuilder leftTrim();

          

          /**

           * 

           * @return returns the size of the string.

           */

          public QueryBuilder length();

          

          public QueryBuilder lessThan(byte aArgument);

          

          public QueryBuilder lessThan(char aArgument);

          

          public QueryBuilder lessThan(double aArgument);

          

          public QueryBuilder lessThan(float aArgument);

          

          public QueryBuilder lessThan(int aArgument);

          

          public QueryBuilder lessThan(long aArgument);

          

          

          public QueryBuilder lessThan(Object aArgument);

          

          

          public QueryBuilder lessThan(QueryBuilder aBuilder);

          

          

          public QueryBuilder lessThan(short aArgument);

          

          

          public QueryBuilder lessThan(boolean aArgument);

          

          

          public QueryBuilder lessThanEqual(byte aArgument);

          

          

          public QueryBuilder lessThanEqual(char aArgument);

          

          

          public QueryBuilder lessThanEqual(double aArgument);

          

          

          public QueryBuilder lessThanEqual(float aArgument);

          

          

          public QueryBuilder lessThanEqual(int aArgument);

          

          

          public QueryBuilder lessThanEqual(long aArgument);

          

          

          public QueryBuilder lessThanEqual(Object aArgument);

          

          

          public QueryBuilder lessThanEqual(QueryBuilder aArgument);

          

          

          public QueryBuilder lessThanEqual(short aArgument);

          

          

          public QueryBuilder lessThanEqual(boolean aArgument);

          

          

          public QueryBuilder like(String aArgument);

          

          

          public QueryBuilder like(String  aArgument1,String aArgument2);

          

          

          public QueryBuilder like(QueryBuilder aBuilder);

          

          

          public QueryBuilder likeIgnoreCase(String aArgument);

          

          

          public QueryBuilder likeIgnoreCase(QueryBuilder aBuilder);

          

          

          public QueryBuilder maximum();

          

          /**
           * This represents the aggregate function Minimum. Can be used only within Report Queries. 
           * @return
           */
          public QueryBuilder minimum();

          

          

          public QueryBuilder not();

          

          

          public QueryBuilder notBetween(byte aArgument1,byte aArgument2);

          

          

          public QueryBuilder notBetween(char  aArgument1,char aArgument2);

          

          public QueryBuilder notBetween(double  aArgument1,double aArgument2);

          

          

          public QueryBuilder notBetween(float  aArgument1,float aArgument2);

          

          

          public QueryBuilder notBetween(int aArgument1,int aArgument2);

          

          

          public QueryBuilder notBetween(long  aArgument1,long aArgument2);

          

          

          public QueryBuilder notBetween(Object  aArgument1,Object aArgument2);

          

          

          public QueryBuilder notBetween(short  aArgument1,short aArgument2);

          

          

          public QueryBuilder notBetween(QueryBuilder  aBuilder1,QueryBuilder aBuilder2);

          

          

          public QueryBuilder notEqual(byte aArgument);

          

          

          public QueryBuilder notEqual(char aArgument);

          

          

          public QueryBuilder notEqual(double aArgument);

          

          

          public QueryBuilder notEqual(float aArgument);

          

          

          public QueryBuilder notEqual(int aArgument);

          

          

          public QueryBuilder notEqual(long aArgument);

          

          

          public QueryBuilder notEqual(Object aArgument);

          

          

          public QueryBuilder notEqual(QueryBuilder aBuilder);

          

          

          public QueryBuilder notEqual(short aArgument);

          

          

          public QueryBuilder notEqual(boolean aArgument);

          

          

          public QueryBuilder notIn(byte[] aArgument);

          

          

          public QueryBuilder notIn(char[] aArgument);

          

          

          public QueryBuilder notIn(double[] aArgument);

          

          

          public QueryBuilder notIn(float[] aArgument);

          

          

          public QueryBuilder notIn(int[] aArgument);

          

          

          public QueryBuilder notIn(long[] aArgument);

          

          

          public QueryBuilder notIn(Object[] aArgument);

          

          

          public QueryBuilder notIn(short[] aArgument);

          

          

          public QueryBuilder notIn(boolean[] aArgument);

          

          

          public QueryBuilder notIn(QueryBuilder aBuilder);

          

          

          public QueryBuilder notLike(String aArgument);

          

          

          public QueryBuilder notLike(QueryBuilder aBuilder);

          

          

          public QueryBuilder notNull();

          

          

          public QueryBuilder reverse();

          

          

          public QueryBuilder rightTrim();

          

          

          public QueryBuilder substring(int  aArgument1,int aArgument2);

          

          

          public QueryBuilder sum();

          

          

          public QueryBuilder toCharacter();

          

          

          public QueryBuilder toDate();

          

          

          public QueryBuilder toLowerCase();

          

          

          public QueryBuilder toNumber();

          

          

          public QueryBuilder toUpperCase();

          

          

          public QueryBuilder toUppercaseCasedWords();

          

          

          public QueryBuilder trim();

          public QueryBuilder appendSQL(String aSQL);      
          
          public QueryBuilder prefixSQL(String aSQL);
 }