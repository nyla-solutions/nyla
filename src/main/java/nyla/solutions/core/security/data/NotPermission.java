package nyla.solutions.core.security.data;

public class NotPermission implements Permission
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1057689469500576446L;


	/**
	 * @param permission
	 */
	public NotPermission(Permission permission)
	{
		if (permission == null)
			throw new IllegalArgumentException("permission is required");
		
		this.permission = permission;
	}

	
	@Override
	public boolean isAuthorized(Permission permission)
	{
		return !this.permission.isAuthorized(permission);
	}

	@Override
	public String getText()
	{
	
		return new StringBuilder("!").append(permission.getText()).toString();
	}

	private final Permission permission;
}
