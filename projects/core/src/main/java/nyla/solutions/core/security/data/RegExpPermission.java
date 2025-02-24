package nyla.solutions.core.security.data;

import java.io.Serializable;

/**
 * Permission based on matching  based on regular expressions against the permission toString
 * @author Gregory Green
 *
 */
public class RegExpPermission implements Permission, Serializable
{
	private final String regExp;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2130916801947682213L;

	public RegExpPermission(String regExp)
	{
		if (regExp == null || regExp.length() == 0)
			throw new IllegalArgumentException("regExp is required");
		this.regExp = regExp;
	}
	
	@Override
	public boolean isAuthorized(Permission permission)
	{
		if(permission == null)
			return false;
		
		String text = permission.getText();
		if(text == null)
			return false;
		
		return text.matches(regExp);
	}//------------------------------------------------

	@Override
	public String getText()
	{
		return regExp;
	}

}
