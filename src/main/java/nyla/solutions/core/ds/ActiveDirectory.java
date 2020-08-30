package nyla.solutions.core.ds;

import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.security.auth.x500.X500Principal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 * A simple Active Directory client.
 * 
 */
public class ActiveDirectory
{
	public ActiveDirectory()
	{
		
		fullUrl = serverUrl + "/" + rootDn;
		fullUrl = getValidURL(fullUrl);
		
	}// -----------------------------------------------
	
	public Principal authenticate(String username, char password[]) 
    throws NamingException
	{
		DirContext ctx = null;
		try
		{
			String userDN = toDN(username);
			ctx = initContext(userDN, password);

			return new X500Principal(userDN);
		}
		finally
		{
			if(ctx != null)
				try{ ctx.close(); } catch(Exception e){e.printStackTrace();}
		}
	}// -----------------------------------------------
	private DirContext initContext(String userDN, char password[])
	throws NamingException
	{
		Hashtable<String,Object> env = new Hashtable<String,Object>();
		
		
		setupBasicProperties(env);
		setupSimpleSecurityProperties(env, userDN, password);

		return new InitialDirContext(env);
	}// -----------------------------------------------
	public String[] getRoles(String username, char[] password) throws NamingException
	{
		String[] rolesArray = null;
		DirContext ctx = null;
		try
		{
		
			String userDN = toDN(username);
			ctx = initContext(userDN, password);
			
			SearchControls sc = new SearchControls();
			String[] attributeFilter =
			{ "cn", "mail", "memberOf" };
			sc.setReturningAttributes(attributeFilter);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String filter = "(&(sAMAccountName=" + username + "))";

			NamingEnumeration<SearchResult> results = ctx.search("", filter, sc);

			ArrayList<String> roles = new ArrayList<String>();
			while (results.hasMore())
			{
				SearchResult sr = results.next();
				Attributes attrs = sr.getAttributes();
				//Attribute attr = attrs.get("cn");
				//attr = attrs.get("mail");

				Attribute attr2 = attrs.get("memberOf");
				NamingEnumeration<?> ne = attr2.getAll();
				while (ne.hasMore())
				{
					roles.add(ne.next().toString());
					
				}
				rolesArray = new String[roles.size()];
				roles.toArray(rolesArray);
				
				
				
			}
		}
		finally
		{
			try
			{
				if (ctx != null)
					ctx.close();
			}
			catch (Exception ex)
			{
				Debugger.printError(ex);
			}
		}
		return rolesArray;
	}// -----------------------------------------------
	private String toDN(String username)
	{
		String userDN = new StringBuilder(userDnPrefix).append(username).append(this.userDnSuffix).toString();
		return userDN;
	}

	/**
	 * Returns valid LDAP URL for the specified URL.
	 * 
	 * @param url
	 *            the URL to convert to the valid URL
	 */
	private String getValidURL(String url)
	{
		if (url != null && url.length() > 0)
		{
			// XXX really important that this one happens first!!
			return url.replaceAll("[%]", "%25")
				.replaceAll(" ", "%20")
				.replaceAll("[<]", "%3c")
				.replaceAll("[>]", "%3e")
				.replaceAll("[\"]", "%3f")
			.replaceAll("[#]", "%23")
			.replaceAll("[{]", "%7b")
			.replaceAll("[}]", "%7d")
			.replaceAll("[|]", "%7c")
			.replaceAll("[\\\\]", "%5c") // double check this
													// one :-)
			.replaceAll("[\\^]", "%5e")
			.replaceAll("[~]", "%7e")
			.replaceAll("[\\[]", "%5b")
			.replaceAll("[\\]]", "%5d")
			.replaceAll("[']", "%27")
			.replaceAll("[?]", "%3f");
		}

		return url;

	}

	/**
	 * Sets the environment properties needed for a simple username + password
	 * authenticated jndi connection.
	 * 
	 * @param env
	 *            The LDAP security environment
	 * @param userDn
	 *            The user distinguished name
	 * @param pwd
	 *            The user password
	 */
	private static void setupSimpleSecurityProperties(Hashtable<String,Object> env,
			String userDn, char[] pwd)
	{
		// 'simple' = username + password
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		// add the full user dn
		env.put(Context.SECURITY_PRINCIPAL, userDn);

		// set password
		env.put(Context.SECURITY_CREDENTIALS, new String(pwd));
	}// -----------------------------------------------

	/**
	 * Sets basic LDAP connection properties in env.
	 * 
	 * @param env
	 *            The LDAP security environment
	 * @throws NamingException
	 *             Thrown if the passed in values are invalid
	 */
	private Hashtable<?,?> setupBasicProperties(Hashtable<String,Object> env)
			throws NamingException
	{
		return setupBasicProperties(env,this.fullUrl);
	}// -----------------------------------------------
	private Hashtable<String,Object> setupBasicProperties(Hashtable<String,Object> env, String providerUrl)
			throws NamingException
	{
		// set the tracing level
		if (tracing)
			env.put("com.sun.jndi.ldap.trace.ber", System.err);

		// always use ldap v3
		env.put("java.naming.ldap.version", "3");

		// use jndi provider
		if (env.get(Context.INITIAL_CONTEXT_FACTORY) == null)
			env.put(Context.INITIAL_CONTEXT_FACTORY, DEFAULT_CTX);

		// usually what we want
		env.put("java.naming.ldap.deleteRDN", "false");

		// follow, ignore, throw
		env.put(Context.REFERRAL, referralType);

		// to handle non-standard binary attributes
		env.put("java.naming.ldap.attributes.binary",
				"photo jpegphoto jpegPhoto");

		// finding, searching, etc.
		env.put("java.naming.ldap.derefAliases", aliasType);

		// no authentication (may be modified by other code)
		env.put(Context.SECURITY_AUTHENTICATION, "none");

		// the ldap url to connect to; e.g. "ldap://foo.com:389"
		env.put(Context.PROVIDER_URL, providerUrl);

		return env;
	}

	   
	private String fullUrl = "";
	private String userDnPrefix = Config.getProperty(ActiveDirectory.class,"userDnPrefix","");
	private String userDnSuffix = Config.getProperty(ActiveDirectory.class,"userDnSuffix","");
	private String referralType =  Config.getProperty(ActiveDirectory.class,"referralType","follow");
	private String aliasType = Config.getProperty(ActiveDirectory.class,"aliasType","searching");
	private String rootDn =  Config.getProperty(LDAP.ROOT_DN_PROP); //"DC=medco,DC=com";
	private boolean tracing =  Config.getPropertyBoolean(ActiveDirectory.class,"tracing",false);
	private String serverUrl =  Config.getProperty(LDAP.SERVER_URL_PROP); //"ldap://medco.com:389";
	private static final String DEFAULT_CTX = "com.sun.jndi.ldap.LdapCtxFactory";

}
