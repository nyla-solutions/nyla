package nyla.solutions.core.security.data;

/**
 * @author Gregory Green
 *
 */
public class AllPermission implements Permission
{

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 13;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("static-access")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllPermission other = (AllPermission) obj;
		if (text == null)
		{
			if (other.text != null)
				return false;
		}
		else if (!text.equals(other.text))
			return false;
		return true;
	}
	

	@Override
	public boolean isAuthorized(Permission permission)
	{
		return true;
	}
	
	
	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3695892114225982362L;


	private static final String text = "ALL";
	

}
