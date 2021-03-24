package nyla.solutions.core.ds;


import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.security.PrivilegedAction;
import java.util.Hashtable;



public class JndiAction<T> implements PrivilegedAction<T>
{

    public JndiAction(Hashtable<?,?> env)
    {
    	if(env == null)
    		this.env = null;
    	else
    		this.env = (Hashtable<?,?>)env.clone();
    }

    @SuppressWarnings("unchecked")
	public T run()
    {

        javax.naming.directory.DirContext result = null;

        try

        {

            result = new InitialDirContext(env);

        }

        catch(NamingException ex)

        {

            ex.printStackTrace();

        }

        return (T)result;

    }



    private Hashtable<?,?> env;

}

