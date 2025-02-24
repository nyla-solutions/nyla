package nyla.solutions.dao.OR;

import java.util.Collection;



import nyla.solutions.dao.OR.query.QueryBuilder;
import nyla.solutions.core.exception.NoDataFoundException;

/**
 * <pre>
 * ORDAO provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface ORDAO
{
   /**
    * Select objects based a query builder
    * @param aQueryBuilder the query builder
    * @return the results from the criteria
    * @throws NoDataFoundException
    */
   public Collection<?> select(QueryBuilder aQueryBuilder)
   throws NoDataFoundException;

}
