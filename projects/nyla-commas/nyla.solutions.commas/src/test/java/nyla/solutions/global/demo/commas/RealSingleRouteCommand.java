package nyla.solutions.global.demo.commas;

import java.util.ArrayList;
import java.util.Collection;

import nyla.solutions.commas.annotations.Attribute;
import nyla.solutions.commas.annotations.CMD;
import nyla.solutions.commas.annotations.COMMAS;
import nyla.solutions.commas.remote.partitioning.RmiAllRoutesAdvice;
import nyla.solutions.commas.remote.partitioning.RmiOneRouteAdvice;
import nyla.solutions.core.data.Criteria;
import nyla.solutions.core.security.user.data.User;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Debugger;

@COMMAS
public class RealSingleRouteCommand
{
	@CMD(advice=RmiOneRouteAdvice.ADVICE_NAME,
			targetName="molecules",
			attributes={@Attribute(name=RmiOneRouteAdvice.LOOKUP_PROP_ATTRIB_NAME,value="primaryKey")})
	public Collection<User> findUsers(Criteria criteria)
	{
		Debugger.println(this,"finderUsers("+criteria+")");
		ArrayList<User> users = new ArrayList<User>();
		
		User nyla = new UserProfile("nyla@emc.com","nyla","Nyla","Green");
		
		users.add(nyla);
		
		return users;
	}

	
	@CMD(advice=RmiAllRoutesAdvice.ADVICE_NAME)
	public Collection<User> findUsersEveryWhere(Criteria criteria)
	{
		Debugger.println(this,"finderUsers("+criteria+")");
		ArrayList<User> users = new ArrayList<User>();
		
		//-Dsolutions.global.demo.commas.RealSingleRouteCommand.findUsersEveryWhere.serverName=server1
		String serverName = System.getProperty(RealSingleRouteCommand.class.getName()+".findUsersEveryWhere.serverName");
		
		User nyla = new UserProfile("nyla@"+serverName,"nyla","Nyla","Green");
		
		users.add(nyla);
		
		return users;
	}
}
