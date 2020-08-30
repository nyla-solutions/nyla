package nyla.solutions.core.security.data;

import nyla.solutions.core.Identifiable;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * <pre>
 * LoginCredential consist of a user name and password. 
 * The login credential are passed in the HTTP header using the 
 * basic 64 encoding.
 * 
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class LoginCredential implements SecurityCredential, Principal, Serializable, Identifiable
{

   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = 1871190779208616626L;
   
   public LoginCredential()
   {
      super();
   }// --------------------------------------------
   /**
    * 
    * Constructor for LoginCredential initializes internal 
    * data settings.
    * @param loginID the user login
    * @param password the user password
    */
   public LoginCredential(String loginID, String password)
   {
      this.setLoginID(loginID);
      this.setPassword(password);
   }// --------------------------------------------
   /**
    * 
    * Constructor for LoginCredential initializes internal 
    * data settings.
    * @param loginID the user login
    * @param password the user password
    * @param domain the user belongs
    */
   public LoginCredential(String loginID, String password, String domain)
   {
      this(loginID, password);
      
      this.setDomain(domain);
   }// --------------------------------------------
   
   /**
    * 
    * Constructor for LoginCredential initializes internal 
    * data settings.
    * @param loginID the user login
    * @param password the user password
    *  * @param domain the user belongs
    */
   public LoginCredential(String loginID, char[] password, String domain)
   {
      this(loginID, password);
      
      this.setDomain(domain);
   }// --------------------------------------------
   /**
    * 
    * Constructor for LoginCredential initializes internal 
    * data settings.
    * @param loginID the user login
    * @param password the user password
    */
   public LoginCredential(String loginID, char[] password)
   {
      this.setLoginID(loginID);
      this.setPassword(password);
   }// --------------------------------------------
   /**
    * @return Returns the password.
    */
   public final char[] getPassword()
   {
	   if(password == null)
		   return null;
	   
      return password.clone();
   }// --------------------------------------------
   /**
    * @param password The password to set.
    */
   public final void setPassword(char[] password)
   {   
	   if(password == null)
		   this.password = null;
	   else
		   this.password = password.clone();
   }// --------------------------------------------
   /**
    * 
    * @param password the login user password
    */
   public final void setPassword(String password)
   {
      if (password == null)
         throw new IllegalArgumentException(
         "password required in LoginCredential.setPassword");
      
      this.password = password.toCharArray();
   }// --------------------------------------------
   /**
    * 
    *
    * @see nyla.solutions.core.security.data.SecurityCredential#getId()
    */
   public String getId()
   {
      
      return loginID;
   }// ------------------------------------------------
   /**
    * Implement a principal
    * @return the login ID
    */
   public String getName()
	{
		return loginID;
	} 
   /**
    * @return Returns the loginID.
    */
   public final String getLoginID()
   {
      return loginID;
   }// --------------------------------------------

   /**
    * @param loginID The loginID to set.
    */
   public final void setLoginID(String loginID)
   {
      if (loginID == null)
         throw new IllegalArgumentException(
         "loginID required in LoginCredential.setLoginID");
   
      this.loginID = loginID;
   }// --------------------------------------------
   /**
    * @return Returns the domain.
    */
   public final String getDomain()
   {
      return domain;
   }// --------------------------------------------
   /**
    * @param domain The domain to set.
    */
   public final void setDomain(String domain)
   {
      if (domain == null)
         domain = "";
   
      this.domain = domain;
   }// --------------------------------------------
   /**
    * @return the properties
    */
   public Map<String,Object> getProperties()
   {
      return properties;
   }
   /**
    * @param key key the properties to set
    * @param value value the properties to set
    */
   public void setProperty(String key, Object value)
   {
      this.properties.put(key, value);
   }
   /**
    * 
    * @param id the ID 
    */
   public void setId(String id)
   {
    this.setLoginID(id);
      
   }
   
   private Map<String,Object> properties = new HashMap<String,Object>();
   private String domain = "";
   private String loginID = "";
   private char[] password = null;



}
