package nyla.solutions.core.util;

import java.io.Serializable;
import java.util.*;


/**
 * <pre>
 * 
 * 
 * <b>BeanComparator </b> is a generic Bean property java.util.Comparator implementation. 
 * It compares specified property beans using reflection. This object is internally used
 * by the Organizer.sortByJavaBeanProperty(String,Collection) method.
 * 
 * <b>Example code</b>
 * 
	<span style="color:blue">//The constructor accepts a JavaBean property name</span>
	BeanComparator beanComparator = new BeanComparator("firstName");
		
	<span style="color:blue">//The following are two sample user profile beans</span>
	UserProfile josiah = new UserProfile();
	josiah.setFirstName("Josiah");
		
	UserProfile nyla = new UserProfile();
	nyla.setFirstName("Nyla");
		 
	<span style="color:blue">//Reflection is used to compare the properties of the beans</span>
	Assert.assertTrue(beanComparator.compare(josiah, nyla) &lt; 0);
		 
	<span style="color:blue">//The following shows how the BeanComparator.sort method can be used
	//This method can be used to sort an given collection based on the JavaBean properties 
	 //of objects in the collection</span>
	ArrayList<UserProfile> unSorted = new ArrayList<UserProfile>();
	unSorted.add(0, nyla);
	unSorted.add(1, josiah);
		 
	<span style="color:blue">//Setting the descending will determine the sort order</span>
	beanComparator.setDescending(true);
	beanComparator.sort(unSorted);

	Assert.assertTrue(unSorted.get(0) == nyla);
		 
	<span style="color:blue">//Changing the descending flag changes the output of the sort method</span>
	beanComparator.setDescending(false);
	beanComparator.sort(unSorted);
	Assert.assertTrue(unSorted.get(0) == josiah);
 * 
 * </pre>
 * 
 * @author Gregory Green
 * 
 * @version 1.0
 * 
 */
public class BeanComparator implements Comparator<Object>, Serializable
{
	private boolean descending;
	private String propertyName;
	static final long serialVersionUID = (long) BeanComparator.class.getName()
			.hashCode();

	public BeanComparator(String aPropertyName, boolean aDescending) {
		this.descending = false;
		this.propertyName = "";
		this.setPropertyName(aPropertyName);
		this.descending = aDescending;
	}

	public boolean isDescending() {
		return this.descending;
	}

	public final void setDescending(boolean descending) {
		this.descending = descending;
	}

	public BeanComparator(String aPropertyName) {
		this(aPropertyName, false);
	}

	public int compare(Object aBean1, Object aBean2) {
		int results= internalCompare(aBean1, aBean2);
		
		if(descending)
			return results * -1;
		else
			return results;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int internalCompare(Object aBean1, Object aBean2)
	{
		if(aBean1 ==  aBean2)
			return 0;
		
		if (this.propertyName != null && this.propertyName.length() != 0) {
			try {
				Comparable e = (Comparable) JavaBean.getProperty(aBean1,
						this.propertyName);
				Comparable comparable2 = (Comparable) JavaBean.getProperty(
						aBean2, this.propertyName);
				
				if(e == comparable2)
					return 0;
				else if(e !=null && comparable2 != null)
					return e.compareTo(comparable2);
				else if(e != null && comparable2 == null)
					return 1;
				else
					return -1;
				
			} catch (Exception arg4) {
				throw new RuntimeException(Debugger.stackTrace(arg4));
			}
		} else {
			Debugger.printWarn(this,
					"Bean property not set, the list will not be sorted");
			return 0;
		}
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName(String aProperyName) {
		this.propertyName = aProperyName;
	}

	public List<?> sort(Collection<?> aCollection) {
		Object list = null;
		if (aCollection instanceof List) {
			list = (List<?>) aCollection;
		} else {
			list = new ArrayList<Object>(aCollection);
		}

		Collections.sort((List<?>) list, this);

		return (List<?>) list;
	}
}

