package nyla.solutions.core.util.organizer;

import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.patterns.iteration.PagingCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author Gregory Green
 */
public class FlatsOrganizer {




    /**
     * <pre>
     * Flatten collection of collections into a collection of objects
     *
     * Usage
     * Collection<Serializable> flattedCollection = null;
     *
     * switch(returnType)
     * {
     * case tree: flattedCollection = new TreeSet<Serializable>();break;
     * case set: flattedCollection = new HashSet<Serializable>(results.size());break;
     * default: flattedCollection = new HashSet<Serializable>(results.size());
     * }
     *
     * Organizer.flatten((Collection)results, flattedCollection);
     * </pre>
     *
     * @param input         the input collection
     * @param flattenOutput the collection output
     * @param <T>           to flatten output type
     */
    @SuppressWarnings("unchecked")
    public <T> void flatten(Collection<?> input,
                                   Collection<T> flattenOutput)
    {
        if (input == null || input.isEmpty() || flattenOutput == null)
            return;

        for (Object inputObj : input) {
            if (inputObj == null)
                continue;

            if (inputObj instanceof Collection)
                flatten((Collection<Object>) inputObj, flattenOutput);
            else
                flattenOutput.add((T) inputObj);

        }

    }

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that
     * @param <T> the paging the type class
     * @return to flatten collections into a single collection
     */
    public <T> Paging<T> flattenPaging(Collection<Paging<T>> collectionOfPaging)
    {
        if (collectionOfPaging == null || collectionOfPaging.isEmpty())
            return null;

        PageCriteria pageCriteria = null;
        // get first page criteria
        Paging<T> firstPaging = collectionOfPaging.iterator().next();

        if (firstPaging != null)
            pageCriteria = firstPaging.getPageCriteria();

        return flattenPaging(collectionOfPaging, pageCriteria, null, null);

    }

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that need to be flattened
     * @param sorter             optional comparable for sorting
     * @param filter             optional filter, if filter.
     * @param <T>                the type class
     * @return to flatten collections into a single collection
     */
    public <T> Paging<T> flattenPaging(Collection<Paging<T>> collectionOfPaging,
                                              Comparator<T> sorter, BooleanExpression<T> filter)
    {
        if (collectionOfPaging == null || collectionOfPaging.isEmpty())
            return null;

        PageCriteria pageCriteria = collectionOfPaging.iterator().next().getPageCriteria();
        return flattenPaging(collectionOfPaging, pageCriteria, sorter, filter);
    }

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that need to be flattened
     * @param sorter             optional comparable for sorting
     * @param filter             optional filter, if filter.
     * @param pageCriteria       the page criteria
     * @param <T>                the collection page object type
     * @return to flatten collections into a single collection
     */
    @SuppressWarnings("unchecked")
    public <T> Paging<T> flattenPaging(Collection<?> collectionOfPaging, PageCriteria pageCriteria,
                                              Comparator<T> sorter, BooleanExpression<T> filter)
    {
        if (collectionOfPaging == null || collectionOfPaging.isEmpty())
            return null;

        Paging<T> pagingResults = null;

        if (sorter != null) {
            // Create tree set based paging
            TreeSet<T> treeSet = new TreeSet<>(sorter);
            pagingResults = new PagingCollection<>(treeSet, pageCriteria);
        }

        // Add all to an aggregated collection
        Paging<T> paging;
        for (Object item : collectionOfPaging) {
            if (item instanceof Paging) {
                paging = (Paging<T>) item;

                if (pagingResults != null)
                    addAll(pagingResults, paging, filter);
                else {
                    pagingResults = paging;
                }
            } else if (item != null) {
                // initialize paging results if needed
                if (pagingResults == null) {
                    if (sorter != null) {
                        // Create tree set based paging
                        TreeSet<T> treeSet = new TreeSet<>(sorter);
                        pagingResults = new PagingCollection<>(treeSet, pageCriteria);
                    } else
                        pagingResults = new PagingCollection<>(new ArrayList<>(), pageCriteria);

                }

                pagingResults.add((T) item);
            }
        }

        return pagingResults;
    }


    /**
     * Add all collections
     *
     * @param <T>           the type class
     * @param paging        the paging output
     * @param pagingResults the results to add to
     * @param filter        remove object where filter.getBoolean() == true
     */
    public <T> void addAll(Collection<T> pagingResults, Collection<T> paging, BooleanExpression<T> filter)
    {
        if (pagingResults == null || paging == null)
            return;

        if (filter != null) {
            for (T obj : paging) {
                if (filter.apply(obj))
                    pagingResults.add(obj);
            }
        } else {
            // add independent of a filter
            pagingResults.addAll(paging);
        }
    }
}
