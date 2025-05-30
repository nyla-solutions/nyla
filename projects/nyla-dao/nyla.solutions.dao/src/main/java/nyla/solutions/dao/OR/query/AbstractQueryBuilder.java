package nyla.solutions.dao.OR.query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * <pre>
 * AbstractQueryBuilder is a data access object interface
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class AbstractQueryBuilder implements QueryBuilder
{

   public QueryBuilder addDate(String aAttributeName, int aMonths)
   {
      
      return null;
   }

   public QueryBuilder addDate(String aDatePart, Object numberToAdd)
   {
      
      return null;
   }

   public QueryBuilder addMonths(int aMonths)
   {
      
      return null;
   }

   public QueryBuilder addMonths(Object aMonths)
   {
      
      return null;
   }

   public QueryBuilder and(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder anyOf(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder anyOfAllowingNone(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder appendSQL(String aSQL)
   {
      
      return null;
   }

   public QueryBuilder ascending()
   {
      
      return null;
   }

   public QueryBuilder asciiValue()
   {
      
      return null;
   }

   public QueryBuilder average()
   {
      
      return null;
   }

   public QueryBuilder between(byte aArgument1, byte aArgument2)
   {
      
      return null;
   }

   public QueryBuilder between(char aArgument1, char aArgument2)
   {
      
      return null;
   }

   public QueryBuilder between(double aArgument1, double aArgument2)
   {
      
      return null;
   }

   public QueryBuilder between(float aArgument1, float aArgument2)
   {
      
      return null;
   }

   public QueryBuilder between(int aArgument1, int aArgument2)
   {
      
      return null;
   }

   public QueryBuilder between(long aArgument1, long aArgument2)
   {
      
      return null;
   }

   public QueryBuilder between(Object aArgument1, Object aArgument2)
   {
      
      return null;
   }

   public QueryBuilder between(QueryBuilder aBuilder1, QueryBuilder aBuilder2)
   {
      
      return null;
   }

   public QueryBuilder between(short aArgument1, short aArgument2)
   {
      
      return null;
   }

   public QueryBuilder concat(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder containsAllKeyWords(String aSpaceSeperatedKeyWords)
   {
      
      return null;
   }

   public QueryBuilder containsAnyKeyWords(String aSpaceSeperatedKeyWords)
   {
      
      return null;
   }

   public QueryBuilder containsSubstring(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder containsSubstring(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder containsSubstringIgnoringCase(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder containsSubstringIgnoringCase(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder convertToUseOuterJoin()
   {
      
      return null;
   }

   public QueryBuilder count()
   {
      
      return null;
   }

   public QueryBuilder currentDate()
   {
      
      return null;
   }

   public QueryBuilder dateDifference(String aDatePart, Date aDate)
   {
      
      return null;
   }

   public QueryBuilder dateDifference(String aDatePart, QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder decode(Map<?,?> aMap, String aDefautValue)
   {
      
      return null;
   }

   public QueryBuilder descending()
   {
      
      return null;
   }

   public QueryBuilder distinct()
   {
      
      return null;
   }

   public QueryBuilder equal(byte aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(char aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(double aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(float aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(int aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(long aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder equal(short aArgument)
   {
      
      return null;
   }

   public QueryBuilder equal(boolean aArgument)
   {
      
      return null;
   }

   public QueryBuilder equalOuterJoin(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder equalOuterJoin(QueryBuilder aArgument)
   {
      
      return null;
   }

   public QueryBuilder equalsIgnoreCase(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder equalsIgnoreCase(QueryBuilder aQueryBuilder)
   {
      
      return null;
   }

   public QueryBuilder getAllowingNull(String aAttributeName)
   {
      
      return null;
   }

   public QueryBuilder getColumn(String aAttributName)
   {
      
      return null;
   }

   public QueryBuilder getFunction(String aFunction, Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder getFunction(String aFunction)
   {
      
      return null;
   }

   public QueryBuilder getFunctionWithArguments(String aFunction,
                                                Object[] aArguments)
   {
      
      return null;
   }

   public Object getQueryResults()
   {
      
      return null;
   }

   public QueryBuilder greaterThan(byte aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(char aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(double aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(float aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(int aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(long aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(short aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThan(boolean aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(byte aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(char aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(double aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(float aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(int aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(long aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(QueryBuilder aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(short aArgument)
   {
      
      return null;
   }

   public QueryBuilder greaterThanEqual(boolean aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(byte[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(char[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(double[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(float[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(int[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(long[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(Object[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(short[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(boolean[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(List<?> aArgument)
   {
      
      return null;
   }

   public QueryBuilder in(QueryBuilder aArgument)
   {
      
      return null;
   }

   public QueryBuilder isNull()
   {
      
      return null;
   }

   public QueryBuilder lastDay()
   {
      
      return null;
   }

   public QueryBuilder leftTrim()
   {
      
      return null;
   }

   public QueryBuilder length()
   {
      
      return null;
   }

   public QueryBuilder lessThan(byte aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(char aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(double aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(float aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(int aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(long aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder lessThan(short aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThan(boolean aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(byte aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(char aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(double aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(float aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(int aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(long aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(QueryBuilder aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(short aArgument)
   {
      
      return null;
   }

   public QueryBuilder lessThanEqual(boolean aArgument)
   {
      
      return null;
   }

   public QueryBuilder like(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder like(String aArgument1, String aArgument2)
   {
      
      return null;
   }

   public QueryBuilder like(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder likeIgnoreCase(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder likeIgnoreCase(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder maximum()
   {
      
      return null;
   }

   public QueryBuilder minimum()
   {
      
      return null;
   }

   public QueryBuilder not()
   {
      
      return null;
   }

   public QueryBuilder notBetween(byte aArgument1, byte aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(char aArgument1, char aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(double aArgument1, double aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(float aArgument1, float aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(int aArgument1, int aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(long aArgument1, long aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(Object aArgument1, Object aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(short aArgument1, short aArgument2)
   {
      
      return null;
   }

   public QueryBuilder notBetween(QueryBuilder aBuilder1, QueryBuilder aBuilder2)
   {
      
      return null;
   }

   public QueryBuilder notEqual(byte aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(char aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(double aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(float aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(int aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(long aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(Object aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder notEqual(short aArgument)
   {
      
      return null;
   }

   public QueryBuilder notEqual(boolean aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(byte[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(char[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(double[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(float[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(int[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(long[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(Object[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(short[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(boolean[] aArgument)
   {
      
      return null;
   }

   public QueryBuilder notIn(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder notLike(String aArgument)
   {
      
      return null;
   }

   public QueryBuilder notLike(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder notNull()
   {
      
      return null;
   }

   public QueryBuilder or(QueryBuilder aBuilder)
   {
      
      return null;
   }

   public QueryBuilder prefixSQL(String aSQL)
   {
      
      return null;
   }

   public QueryBuilder reverse()
   {
      
      return null;
   }

   public QueryBuilder rightTrim()
   {
      
      return null;
   }

   public QueryBuilder substring(int aArgument1, int aArgument2)
   {
      
      return null;
   }

   public QueryBuilder sum()
   {
      
      return null;
   }

   public QueryBuilder toCharacter()
   {
      
      return null;
   }

   public QueryBuilder toDate()
   {
      
      return null;
   }

   public QueryBuilder toLowerCase()
   {
      
      return null;
   }

   public QueryBuilder toNumber()
   {
      
      return null;
   }

   public QueryBuilder toUpperCase()
   {
      
      return null;
   }

   public QueryBuilder toUppercaseCasedWords()
   {
      
      return null;
   }

   public QueryBuilder trim()
   {
      
      return null;
   }

}
