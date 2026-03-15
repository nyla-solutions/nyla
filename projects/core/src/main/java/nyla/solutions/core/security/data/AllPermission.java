package nyla.solutions.core.security.data;

import java.io.Serial;

/**
 * Represents a super permission for security ACLs.
 * @author Gregory Green
 *
 */
public class AllPermission implements Permission
{
    public static final String TEXT = "ALL";

    @Serial
    private static final long serialVersionUID = -3695892114225982362L;


    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 13;
		int result = 1;
		result = prime * result + ((TEXT == null) ? 0 : TEXT.hashCode());
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
		if (TEXT == null)
		{
			if (other.TEXT != null)
				return false;
		}
		else if (!TEXT.equals(other.TEXT))
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
		return TEXT;
	}

}
