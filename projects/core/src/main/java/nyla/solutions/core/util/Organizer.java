package nyla.solutions.core.util;

import nyla.solutions.core.operations.logging.Log;
import nyla.solutions.core.util.organizer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *
 *  Organizer provides a set of functions to sort, filter and find elements in container objects
 *  such as collections, arrays and maps.
 *
 *  This object has methods to sort collections of object. It can compare a particular
 *  bean property value. Note that this class uses the JavaBean object that is
 *  also located in the same util directory.
 *
 *  The following is a sample web action that uses the Organizer to sort a list of sites Java bean objects.
 *
 *   ...
 *
 *  public Collection getSitesDetailList()
 *  {
 *  //     Here SiteAction Must be Called Like SiteAction.getSiteDetails
 *  this.sitesDetailList = MockUtils.getSomeSiteDetails();
 *
 *  String sortProperty = JSF.getRequestParameter(&quot;sortProperty&quot;);
 *  String filterNameProperty = JSF.getRequestParameter(&quot;filterPropertyName&quot;);
 *  Comparable filterValueProperty =
 *  (Comparable)JSF.getRequestParameter(&quot;filterValueProperty&quot;);
 *
 *  if (!Text.isNull(sortProperty))
 *  {
 *  sitesDetailList = Organizer.sortByJavaBeanProperty(sortProperty,
 *  sitesDetailList);
 *  }
 *  else if(!Text.isNull(filterNameProperty))
 *  {
 *  sitesDetailList = Organizer.filterByJavaBeanProperty((ArrayList)sitesDetailList,filterNameProperty,filterValueProperty);
 *  }
 *
 *  return sitesDetailList;
 *  }
 *
 *
 * </pre>
 *
 * @author Gregory Green
 * @version 2.0
 */
public final class Organizer
{
    private static final Log logger = Debugger.getLog(Organizer.class);
    private static final ChangeOrganizer changeOrganizer = new ChangeOrganizer();
    private static final SearchOrganizer searchOrganizer = new SearchOrganizer();
    private static final CopyOrganizer copyOrganizer = new CopyOrganizer();
    private static final FlatsOrganizer flatsOrganizer = new FlatsOrganizer();
    private static final SortOrganizer sortOrganizer = new SortOrganizer();

    /**
     * Constructor for Organizer initializes internal
     */
    private Organizer()
    {
    }



    /**
     * @param <T>   the array of this type
     * @param array the array to select from
     * @return the first element
     */
    public static <T> T first(T[] array)
    {
        if (array == null || array.length < 1)
            return null;

        return array[0];
    }

    /**
     * Add object to a list
     *
     * @param <T> the input type
     * @param list    where objects will be added
     * @param objects the object add
     */
    public static <T> void addAll(Collection<T> list, T[] objects)
    {
        list.addAll(Arrays.asList(objects));
    }



    /**
     * All object to a given collection
     *
     * @param <T>   the type class
     * @param aFrom the collection to add from
     * @param aTo   the collection to add to.
     */
    public static <T> void addAll(Collection<T> aFrom, Collection<T> aTo)
    {
        if (aFrom == null || aTo == null)
            return; // do nothing

        T object;
        for (T t : aFrom) {
            object = t;
            if (object != null) {
                aTo.add(object);
            }
        }
    }

    /**
     * @param aCollection the collection of objects
     * @return aCollection == null || aCollection.isEmpty()
     */
    public static boolean isEmpty(Collection<?> aCollection)
    {
        return aCollection == null || aCollection.isEmpty();
    }

    public static boolean isEmpty(Object[] objects)
    {
        return objects == null || objects.length == 0;
    }




    /**
     * @param <T>   the type for the list
     * @param count the number of copies
     * @param value the value to
     * @return the collection with that number of copies
     */
    public static <T> List<T> fill(int count, T value)
    {
        if (value == null || count < 1)
            return null;

        ArrayList<T> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(value);
        }

        return list;
    }

    /**
     * Get element by index
     *
     * @param <T>  the input type
     * @param i    the index
     * @param args the input argument
     * @return the object at the index
     */
    public static <T> T at(int i, T[] args)
    {
        if (args == null || i < 0)
            return null;

        if (i >= args.length)
            return null;

        return args[i];
    }

    /**
     * Factory method to create arranger
     * @param list the list to arrange
     * @return the arranger
     * @param <T>
     */
    public static<T> Arranger arrange(T... list) {
        if(list != null &&  list.length == 1 && list[0] instanceof List singleList)
            return arrange(singleList);

        return arrange(Arrays.asList(list));
    }

    /**
     * Factory method to create arranger
     * @param list
     * @return
     * @param <T> the type class
     */
    public static<T> Arranger arrange(List<T> list) {
        return new Arranger(list);
    }

    /**
     * Factory method to get change organizer
     * @return the change organizer
     */
    public static ChangeOrganizer change() {
        return changeOrganizer;
    }

    /**
     * Factory method to get search organizer
     * @return the search organizer
     */
    public static SearchOrganizer search() {
        return searchOrganizer;
    }

    /**
     * Factory method to get copy organizer
     * @return the copy organizer
     */
    public static CopyOrganizer copies() {
        return copyOrganizer;
    }

    /**
     * Factory method to get flats organizer
     *
     * @return the flats organizer
     */
    public static FlatsOrganizer flats() {
        return flatsOrganizer;
    }

    public static SortOrganizer sort() {
        return sortOrganizer;
    }
}
