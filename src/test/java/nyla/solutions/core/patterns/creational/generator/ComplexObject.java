package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.security.user.data.UserProfile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Gregory Green
 */
public class ComplexObject
{
    private Currency currency;
    private UserProfile userProfile;
    private SimpleObject simpleObject;

    private BigDecimal bigDecimal;
    private BigInteger bigInteger;

    private LocalDateTime localDateTime;
    private Calendar calendar;
    private Collection<String> collection;
    private List<BigInteger> list;
    private Set<Integer> set;
    private Map<String,String> map;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<BigInteger> getList()
    {
        return list;
    }

    public void setList(List<BigInteger> list)
    {
        this.list = list;
    }

    public Set<Integer> getSet()
    {
        return set;
    }

    public void setSet(Set<Integer> set)
    {
        this.set = set;
    }

    public Map<String, String> getMap()
    {
        return map;
    }

    public void setMap(Map<String, String> map)
    {
        this.map = map;
    }

    public Collection<String> getCollection()
    {
        return collection;
    }

    public void setCollection(Collection<String> collection)
    {
        this.collection = collection;
    }

    public BigDecimal getBigDecimal()
    {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal)
    {
        this.bigDecimal = bigDecimal;
    }

    public BigInteger getBigInteger()
    {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger)
    {
        this.bigInteger = bigInteger;
    }

    public LocalDateTime getLocalDateTime()
    {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime)
    {
        this.localDateTime = localDateTime;
    }

    public Calendar getCalendar()
    {
        return calendar;
    }

    public void setCalendar(Calendar calendar)
    {
        this.calendar = calendar;
    }

    public UserProfile getUserProfile()
    {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile)
    {
        this.userProfile = userProfile;
    }

    public SimpleObject getSimpleObject()
    {
        return simpleObject;
    }

    public void setSimpleObject(SimpleObject simpleObject)
    {
        this.simpleObject = simpleObject;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ComplexObject{");
        sb.append("userProfile=").append(userProfile);
        sb.append(", simpleObject=").append(simpleObject);
        sb.append(", bigDecimal=").append(bigDecimal);
        sb.append(", bigInteger=").append(bigInteger);
        sb.append(", localDateTime=").append(localDateTime);
        sb.append(", calendar=").append(calendar);
        sb.append('}');
        return sb.toString();
    }
}
