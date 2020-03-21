package nyla.solutions.core.patterns.creational.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Green
 */
public class ClassWithNoSetter
{
    private final String test;
    private String test2;
    protected List<String> acidAny;
    private String test3;

    public ClassWithNoSetter(String test)
    {
        this.test = test;
    }

    public String getTest2()
    {
        return test2;
    }

    public String getTest()
    {
        return test;
    }

    public void setTest3(String test3)
    {
        this.test3 = test3;
    }

    public List<String> getAcidAny() {
        if (acidAny == null) {
            acidAny = new ArrayList<String>();
        }
        return this.acidAny;
    }
}
