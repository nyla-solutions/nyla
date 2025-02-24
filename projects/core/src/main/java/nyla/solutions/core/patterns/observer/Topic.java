package nyla.solutions.core.patterns.observer;

import nyla.solutions.core.data.Copier;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.Debugger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * <b>Topic</b> is a implementation of subject
 *
 * @param <T> the class type
 * @author Gregory Green
 */
public class Topic<T> implements Subject<T>, Copier
{
    private final String name;
    private Map<String, SubjectObserver<T>> observerMap = new HashMap<String, SubjectObserver<T>>();


    /**
     * Constructor for Topic initializes internal
     *
     * @param name subject name
     */
    public Topic(String name)
    {
        this.name = name;
    }

    public void add(SubjectObserver<T> subjectObserver)
    {
        if (subjectObserver == null)
            throw new RequiredException("subjectObserver in Topic.add");

        String id = subjectObserver.getId();

        if (id == null || id.length() == 0)
            throw new IllegalArgumentException("Subject observer id required");

        //get previous
        SubjectObserver<?> prev = (SubjectObserver<?>) observerMap.get(subjectObserver.getId());

        //add
        observerMap.put(subjectObserver.getId(), subjectObserver);

    }// --------------------------------------------

    /**
     * @see nyla.solutions.core.patterns.observer.Subject#notify(java.lang.Object)
     */
    public void notify(T object)
    {
        //loop thru observers
        SubjectObserver<T> subjectObserver = null;
        for (Iterator<SubjectObserver<T>> i = observerMap.values().iterator(); i.hasNext(); ) {
            subjectObserver = (SubjectObserver<T>) i.next();
            synchronized (subjectObserver) {
                subjectObserver.update(this.getName(), object);
            }
        }
    }// --------------------------------------------


    public void remove(SubjectObserver<T> subjectObserver)
    {
        if (subjectObserver == null || subjectObserver.getId() == null || subjectObserver.getId().length() == 0)
            return;//do nothing

        //remove
        observerMap.remove(subjectObserver.getId());
    }// --------------------------------------------

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }// --------------------------------------------


    @SuppressWarnings("unchecked")
    public int compareTo(Object object)
    {
        if (!(object instanceof Subject))
            return -1;

        Subject<T> subject = (Subject<T>) object;

        return this.name.compareTo(subject.getName());
    }// --------------------------------------------

    @SuppressWarnings("unchecked")
    public void copy(Copier from)
    {
        if (!(from instanceof Topic)) {
            return;
        }

        Topic<T> topic = (Topic<T>) from;
        this.observerMap = topic.observerMap;
    }// --------------------------------------------

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((observerMap == null) ? 0 : observerMap.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Topic<T> other = (Topic<T>) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (observerMap == null) {
            if (other.observerMap != null)
                return false;
        } else if (!observerMap.equals(other.observerMap))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Topic{");
        sb.append("name='").append(name).append('\'');
        sb.append(", observerMap=").append(observerMap);
        sb.append('}');
        return sb.toString();
    }

    protected Map<String, SubjectObserver<T>> getObserverMap()
    {
        return observerMap;
    }
}
