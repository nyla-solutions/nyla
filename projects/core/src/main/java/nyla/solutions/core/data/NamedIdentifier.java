package nyla.solutions.core.data;

/**
 * @author Gregory Green
 */
public class NamedIdentifier implements Nameable,Identifier
{
    private String name;
    private String id;

    public NamedIdentifier()
    {}

    public NamedIdentifier(String name)
    {
        this.name = name;
    }

    public NamedIdentifier(String name, String id)
    {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
