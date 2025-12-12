package nyla.solutions.core.util.organizer;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.logging.Log;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.JavaBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static nyla.solutions.core.util.Config.settings;

/**
 * @author Gregory Green
 */
public class FilterOrganizer {


    private final static Log logger = Debugger.getLog(FilterOrganizer.class);

    /**
     * @param list        the list to filter
     * @param propertyName the property name to base the filters
     * @param aValue       the value to compare
     * @return the filtered list
     */
    public static Collection<Object> filterByJavaBeanProperty(
            List<Object> list, String propertyName, Comparable<Object> aValue)
    {

        try {
            if (list == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<>(list.size());

            Object bean;
            Object beanPropertyValue;
            for (Object object : list) {
                bean = object;
                beanPropertyValue = JavaBean.getProperty(bean, propertyName);
                if (aValue.compareTo(beanPropertyValue) == 0) {
                    // only add equal this bean
                    filteredList.add(bean);
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * @param list            the list to filter
     * @param propertyName    the property name to filter
     * @param startComparable the formatted start date
     * @param endComparable   the formatted end date
     * @return the filter results
     */
    public static Collection<Object> filterByJavaBeanDateProperty(List<Object> list,
                                                                  String propertyName,
                                                                  Comparable<Object> startComparable,
                                                                  Comparable<Object> endComparable)
    {

        logger.debug("In Organizer filtering: " + propertyName
                + " for date value between : " + startComparable + " and " + endComparable);
        try {
            if (list == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<>(list.size());

            Object bean;
            Object beanPropertyValue;
            for (Iterator<Object> i = list.iterator(); i.hasNext(); ) {
                try {
                    bean = i.next();
                    beanPropertyValue = JavaBean.getProperty(bean,
                            propertyName);

                    DateFormat format = new SimpleDateFormat(
                            settings().getProperty("document.date.format"));
                    Date propDate = format.parse(beanPropertyValue.toString());
                    Date aDate = format.parse(startComparable.toString());
                    Date bDate = format.parse(endComparable.toString());

                    if (propDate.after(aDate) && propDate.before(bDate)) {
                        filteredList.add(bean);
                    }
                }
                catch (Exception e) {
                    logger.debug("error occurred : " + e);
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static Collection<Object> filterByJavaBeanPageProperty(ArrayList<Object> aList,
                                                                  String aPropertyName, int fromIndex, int toIndex)
    {

        logger.debug("In Organizer filtering: " + aPropertyName);
        try {
            if (aList == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<>(aList.size());

            Object bean;
            Object beanPropertyValue;
            for (Iterator<Object> i = aList.iterator(); i.hasNext(); ) {
                try {
                    bean = i.next();
                    beanPropertyValue = JavaBean.getProperty(bean,
                            aPropertyName);
                    int beanPropIntVal = Integer.parseInt(
                            beanPropertyValue.toString());

                    if ((fromIndex <= beanPropIntVal)
                            && (beanPropIntVal <= toIndex)) {
                        filteredList.add(bean);
                    }
                }
                catch (Exception e) {
                    logger.debug("error occurred : " + e);
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }


}
