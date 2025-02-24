package nyla.solutions.core.ds;

import nyla.solutions.core.util.Debugger;

import javax.naming.NamingException;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;


public class JndiSocketFactory extends SSLSocketFactory
{

	public static void setClassLoader(ClassLoader newLoader)
	{

		myClassLoader = newLoader;

	}

	private static ClassLoader getClassLoader()
	{

		if (myClassLoader == null)

			myClassLoader = ClassLoader.getSystemClassLoader();

		return myClassLoader;

	}

	public static void setDebugOn()
	{

		System.setProperty("javax.net.debug", "ssl handshake verbose");

	}

	public synchronized static void init(String caKeystoreFile, String clientKeystoreFile,
			char caPassphrase[], char clientPassphrase[],
			String caKeystoreType, String clientKeystoreType)

	throws NamingException, Exception

	{

		if (default_factory != null)

			return;

		SSLContext sslctx;

		KeyManagerFactory clientKeyManagerFactory;

		checkFileSanity(caKeystoreFile, clientKeystoreFile, clientPassphrase);

		if (caKeystoreFile == null)

			caKeystoreFile = clientKeystoreFile;

		String protocol = System.getProperty("sslversion", "TLS");

		if (!"TLS".equals(protocol))

			System.out.println("SECURITY: Using non-standard ssl version: '"
					+ protocol + "'");

		sslctx = SSLContext.getInstance(protocol);

		clientKeyManagerFactory = null;

		// if(clientPassphrase == null || clientPassphrase.length <= 0)

		// break MISSING_BLOCK_LABEL_392;

		// if(!"KSE".equals(clientKeystoreType))

		// break MISSING_BLOCK_LABEL_340;

		Class<?> c;

		Constructor<?> constructor;

		int size;

		byte password[];

		int i;

		Object pkcs12Parser;

		Method getSunKeyStore;

		// int i;

		try

		{

			c = getClassLoader().loadClass(
					"com.ca.commons.security.openssl.ParsePkcs12");

			if (c == null)

			{

				Debugger.printError(JndiSocketFactory.class,
						"PKI internal error");

				return;

			}

		}

		catch (Exception e)

		{

			Debugger.printError(JndiSocketFactory.class,
					"unable to load pkcs12 parser (not in class path?)");

			return;

		}

		constructor = c.getConstructor(new Class[]
		{

		java.lang.String.class, byte[].class

		});

		size = clientPassphrase.length;

		password = new byte[size];

		for (i = 0; i < size; i++)

			password[i] = (byte) clientPassphrase[i];

		pkcs12Parser = constructor.newInstance(new Object[]
		{

		clientKeystoreFile, password

		});

		getSunKeyStore = c.getMethod("getSunKeyStore", new Class[]
		{

		java.lang.String.class

		});

		clientKeystore = (KeyStore) getSunKeyStore.invoke(pkcs12Parser,
				new Object[]
				{

				"MyFriend"

				});

		for (i = 0; i < size; i++)

			password[i] = 0;

		// break MISSING_BLOCK_LABEL_376;

		if (clientKeystoreType == null)

			clientKeystoreType = "JKS";

		clientKeystore = KeyStore.getInstance(clientKeystoreType);

		if (clientKeystoreFile != null)
	   {
			FileInputStream stream = null;
			
			try
			{
				stream = new FileInputStream(clientKeystoreFile);
				
				clientKeystore.load(stream,
						clientPassphrase);
			}
			finally
			{
				if(stream != null)
					stream.close();
			}


	   }
			
		clientKeyManagerFactory = KeyManagerFactory.getInstance("SunX509");

		clientKeyManagerFactory.init(clientKeystore, clientPassphrase);

		javax.net.ssl.KeyManager keyManagers[] = null;

		if (clientKeyManagerFactory != null)

			keyManagers = clientKeyManagerFactory.getKeyManagers();

		if (caKeystoreType == null)

			caKeystoreType = "JKS";

		KeyStore caKeystore = KeyStore.getInstance(caKeystoreType);

		if (caKeystoreFile != null)
		{
			InputStream is = null;
			
			try
			{
				is = new FileInputStream(caKeystoreFile);
			
				caKeystore.load(is, caPassphrase);
			}
			finally
			{
				if(is != null)
					is.close();
			}
			
		}
		TrustManagerFactory caTrustManagerFactory = TrustManagerFactory
				.getInstance("SunX509");

		caTrustManagerFactory.init(caKeystore);

		javax.net.ssl.TrustManager trustManagers[] = caTrustManagerFactory
				.getTrustManagers();

		sslctx.init(keyManagers, trustManagers, null);

		synchronized (JndiSocketFactory.class)

		{

			factory = sslctx.getSocketFactory();

			default_factory = new JndiSocketFactory();

		}

		// break MISSING_BLOCK_LABEL_580;

		/*
		 * 
		 * GeneralSecurityException e; e; NamingException ne = new
		 * 
		 * NamingException("security error: unable to initialise
		 * 
		 * JndiSocketFactory"); ne.initCause(e); throw ne; IOException e; e;
		 * 
		 * NamingException ne = new NamingException("file access error: unable
		 * 
		 * to initialise JndiSocketFactory"); ne.initCause(e); throw ne;
		 */

	}

	private static void checkFileSanity(String caKeystoreFile,

	String clientKeystoreFile, char clientPassphrase[])

	throws NamingException
	{

		if (clientKeystoreFile == null && caKeystoreFile == null)

			throw new NamingException(

			"SSL Initialisation error: No valid keystore files available.");

		if (caKeystoreFile != null && !(new File(caKeystoreFile)).exists())

			throw new NamingException("SSL Initialisation error: file '"

			+ caKeystoreFile + "' does not exist.");

		if (clientKeystoreFile != null && clientPassphrase != null

		&& !(new File(clientKeystoreFile)).exists())

			throw new NamingException("SSL Initialisation error: file '"

			+ clientKeystoreFile + "' does not exist.");

		else

			return;

	}

	public JndiSocketFactory()
	{

	}

	public static SocketFactory getDefault()
	{

		synchronized (JndiSocketFactory.class)
		{

			if (default_factory == null)

				default_factory = new JndiSocketFactory();

		}

		return default_factory;

	}

	public static KeyStore getClientKeyStore()
	{

		return clientKeystore;

	}

	public Socket createSocket(String host, int port) throws IOException,

	UnknownHostException
	{

		return factory.createSocket(host, port);

	}

	public Socket createSocket(InetAddress host, int port) throws IOException,

	UnknownHostException
	{

		return factory.createSocket(host, port);

	}

	public Socket createSocket(InetAddress host, int port,

	InetAddress client_host, int client_port) throws IOException,

	UnknownHostException
	{

		return factory.createSocket(host, port, client_host, client_port);

	}

	public Socket createSocket(String host, int port, InetAddress client_host,

	int client_port) throws IOException, UnknownHostException
	{

		return factory.createSocket(host, port, client_host, client_port);

	}

	public Socket createSocket(Socket socket, String host, int port,

	boolean autoclose) throws IOException, UnknownHostException
	{

		return factory.createSocket(socket, host, port, autoclose);

	}

	public String[] getDefaultCipherSuites()
	{

		return factory.getDefaultCipherSuites();

	}

	public String[] getSupportedCipherSuites()
	{

		return factory.getSupportedCipherSuites();

	}

	private static SSLSocketFactory factory = null;

	private static JndiSocketFactory default_factory = null;

	private static KeyStore clientKeystore;

	// private static final String DEFAULT_KEYSTORE_TYPE = "JKS";

	// private static final String PKI_INTERNAL_TYPE = "KSE";

	private static ClassLoader myClassLoader = null;

}