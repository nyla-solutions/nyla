package nyla.solutions.core.security.user.data;


import nyla.solutions.core.security.data.SecurityCredential;

import java.io.Serializable;
import java.security.Principal;



/**
 * 
 * <pre>
 * User is a value object interface representation of the User data 
 * and associated entities.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface User extends Comparable<Object>, Cloneable, 
Principal, SecurityCredential, Serializable
{  
    /**
     * 
     * @return the user's first name
     */
    public String getFirstName();

    /**
     * 
     * @return the user's last name
     */
    public String getLastName();
    
    /**
     * 
     * @return the user's email name
     */    
    public String getEmail();
    
    
    /**
     * 
     * @return the user's title Mr, Mrs., etc.
     */
    public String getTitle();
}