package nyla.solutions.global.patterns.command.remote;

import java.io.Serializable;
import java.net.URI;
import java.rmi.RemoteException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommasServiceFactory;
import nyla.solutions.commas.remote.RemoteCommand;
import nyla.solutions.commas.remote.partitioning.PartitionCommasRemoteRegistry;
import nyla.solutions.commas.remote.partitioning.RemoteCommasServer;
import nyla.solutions.core.data.Criteria;
import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.net.rmi.RMI;
import nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry;
import nyla.solutions.core.patterns.loadbalancer.PropertiesLoadBalanceRegistry;
import nyla.solutions.core.patterns.servicefactory.ConfigServiceFactory;
import nyla.solutions.core.patterns.servicefactory.ServiceFactory;
import nyla.solutions.core.security.user.data.User;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.global.demo.commas.RealSingleRouteCommand;

@Ignore
public class RemoteCommasTest
{
	//@BeforeClass
	public static void init()
	throws Exception
	{
		Thread t = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					//start RMI registry
					String host = "localhost";
					int port = 27001;
					String name = "commasRegistry";
					
					RMI.startRmiRegistry(port);
					
					PartitionCommasRemoteRegistry.startRegistry(host, port, name);
					
					RemoteCommasServer.startServer(host, port, "demo", name, null);
				}
				catch (RemoteException e)
				{
					e.printStackTrace();
				}
			}
		});
	
	t.start();
	
	Thread.sleep(1000*5);
		
	}// --------------------------------------------------------
	@Before
	public  void setUp()
	throws Exception
	{
		
		  rmi = new RMI("localhost",27001);
		  factory = ConfigServiceFactory.getConfigServiceFactoryInstance();
	}// --------------------------------------------------------
	
	@Test
	public void testLookup()
	throws Exception
	{
		
		
		RemoteCommand<Serializable,Envelope<UserProfile>> command = rmi.lookup("demo");
		
		Assert.assertNotNull(command);
		
		String rmiUrl = "rmi://localhost:27001/demo";
		command = RMI.lookup(new URI(rmiUrl));
		
		Assert.assertNotNull(command);
	}// --------------------------------------------------------
	@Test
	public void testSingleRoute()
	{	
		
		
		PropertiesLoadBalanceRegistry registry = factory.create(LoadBalanceRegistry.class);
		
		String filePath = registry.getPropertyFilePath();
		
		Assert.assertTrue(filePath != null && filePath.length() > 0);
		
		Assert.assertEquals("./runtime/tmp/loadbalance.properties", filePath);
		
		String name = RealSingleRouteCommand.class.getName()+".findUsers";
		Command<Collection<User>,Criteria> cmd = CommasServiceFactory.getCommasServiceFactory().createCommand(name);
		
		for(int i=0; i <2; i++)
		{
			Criteria input = new Criteria(i);
			Collection<User> users = cmd.execute(input);		
			Assert.assertNotNull(users);
		
		}
			
		
	}// --------------------------------------------------------
	
	@Test
	public void testAllRoutes() throws Exception
	{
		String name = RealSingleRouteCommand.class.getName()+".findUsersEveryWhere";
		Command<Collection<User>,Criteria> cmd = CommasServiceFactory.getCommasServiceFactory().createCommand(name);
		
		
		for(int i=0; i <1; i++)
		{
			Criteria input = new Criteria(i);
			Collection<User> users = cmd.execute(input);		
			Assert.assertNotNull(users);
			
			for (User user : users)
			{
				Assert.assertNotNull(user);
			}
		}
	}// --------------------------------------------------------
	private RMI rmi;
	private ServiceFactory factory;
	

}
