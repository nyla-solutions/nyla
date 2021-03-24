package nyla.solutions.core.net;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Utility for network related tasks
 * @author Gregory Green
 *
 */
public final class Networking
{
	/**
	 * @param host1 a host name 
	 * @param host2 another host name 
	 * @return if two host names are equal by IP addresses or host name
	 */
	public static boolean hostEquals(String host1, String host2)
	{		
		if(host1 == null)
			return false;
		
		if(host1.equalsIgnoreCase(host2))
			return true;
		
		if(host2 == null)
			return false;
		
		try
		{
			//check ip address host 1
			InetAddress[] addresses1 = InetAddress.getAllByName(host1); 
			InetAddress address1 = null;
			
			InetAddress[] addresses2 = InetAddress.getAllByName(host2); 
			InetAddress address2 = null;
			
			if(addresses1 != null)
			{
				
				for (int i=0; i <addresses1.length;i++)
				{
					address1 = addresses1[i];
					if(host2.equalsIgnoreCase(address1.getHostAddress()))
						return true;
					else
					{
						//got thru host 2 ip addresses
						if(addresses2 != null)
						{
							for (int x=0; x <addresses2.length;x++)
							{
								address2 = addresses2[x];
								if(address1.getHostAddress().equalsIgnoreCase(address2.getHostAddress()))
									return true;
							}
						}
						
					}
				}
			}
			//host 2 ip addresses

			
			return false;
		}
		catch (UnknownHostException e)
		{
			return false;
		}

	}
}
