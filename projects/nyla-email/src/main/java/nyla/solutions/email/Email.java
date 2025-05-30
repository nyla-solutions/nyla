package nyla.solutions.email;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import nyla.solutions.core.data.Data;
import nyla.solutions.core.exception.CommunicationException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SecurityException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.logging.Log;
import nyla.solutions.core.patterns.Connectable;
import nyla.solutions.core.patterns.Disposable;
import nyla.solutions.core.patterns.conversion.TextToEmailsConverter;
import nyla.solutions.core.util.Config;
//import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;
import nyla.solutions.email.data.EmailMessage;

//import javax.activation.*;

/**
 * <pre>
 * <b>Email </b> is a client interface for sending emails.
 * This class is used for most application email management.
 * 
 * 
 * 
 * Java Mail Properties
 * 
 * Secure email properties:
mail.from=website@morningstarccc.org
mail.from.password={cryption}-35 -57 79 -68 -2
mail.host=smtp.1and1.com
mail.auth.required=true
mail.smtp.port=25
mail.smtp.ssl.enable=true

 * 
 * <b>mail.store.protocol</b> Protocol for retrieving email.
 * 
 * 
 * Example:
 * 
 * mail.store.protocol=imap
 * The bundled JavaMail library is IMAP.
 * 
 * <b>mail.transport.protocol</b> Protocol for sending email.
 * 
 * Example:
 * 
 * mail.transport.protocol=smtp
 * 
 * The bundled JavaMail library has supports for SMTP.
 * 
 * 
 * 
 * <b>mail.host</b> * The name of the mail host machine.
 * 
 * Example:
 * 
 * mail.host=mailserver mail.smtp.host= mail.smtp.port= Local machine.
 * 
 * 
 * 
 * <b>mail.user</b> Name of the default user for retrieving email.
 * 
 * Example:
 * 
 * 
 * 
 * mail.user=postmaster
 * 
 * Value of the user.name Java system property.
 * 
 * 
 * 
 * <b>mail.protocol.host</b> Mail host for a specific protocol.
 * 
 * For example, you can set mail.SMTP.host and mail.IMAP.host to
 * 
 * different machine names.
 * 
 * 
 * 
 * Examples:
 * 
 * mail.smtp.host=mail.mydom.com mail.imap.host=localhost
 * 
 * Value of the mail.host property.
 * 
 * 
 * 
 * <b>mail.protocol.user</b>
 * 
 * Protocol-specific default user name for logging into a mailer server.
 * 
 * Examples:
 * 
 * mail.smtp.user=weblogicmail.imap.user=appuser
 * 
 * Value of the mail.user property.
 * 
 * 
 * 
 * <b>mail.from</b> The default return address.
 * 
 * Examples:
 * 
 * mail.from=master@mydom.com
 * 
 * username@host
 * 
 * mail.smtp.auth.required=true
 * 
 * <b>mail.debug</b>
 * 
 * Set to True to enable JavaMail debug output.
 *  #==========================================================
 *          #Email Settings mail.debug=true mail.transport.protocol=smtp
 *          mail.from=XXXX mail.smtp.host=host mail.smtp.auth=false
 *          mail.smtp.user=XXX mail.password=
 * 
 * </pre>
 * 
 * @version 1.0
 *          
 */
public class Email implements  Disposable, SendMail, Connectable
{	

	/**
	 * Default constructor
	 */
	public Email()
	{
	} // ----------------------------------------------------------
	/**
	 * Connect to the email server
	 */
	public void connect()
	{
		if(this.isConnected())
			return;
		try
		{
			if (this.useJNDI)
			{

				initFromJNDI();

			}
			else
			{

				init();
			}
			
		}
		catch (SetupException e)
		{

			throw e;
		}
		catch (Exception e)
		{

			throw new SetupException(e);
		}
		
	}//------------------------------------------------
	/**
	 * @return if the mail session is connected
	 */
	@Override
	public  boolean isConnected()
	{
		return this.mailSession != null;
	}//------------------------------------------------
	/**
	 * Close the transport connection
	 * @see nyla.solutions.core.patterns.Disposable#dispose()
	 */
	public void dispose()
	{
		try
		{

			if (this.mailTransport != null)
				this.mailTransport.close();
			
			this.mailSession = null;

		}
		catch (Exception e)
		{
			this.logger.warn(e);
		}
		
		try
		{
			if(this.store != null)
				this.store.close();
			
			this.store = null;
		}
		catch(Exception e)
		{
			this.logger.warn(e);
		}
	}// --------------------------------------------
	/**
	 * 
	 * @param aMap the map variables
	 * @throws javax.mail.MessagingException unknown error
	 * @throws IOException  unknown error
	 * @throws Exception unknown error
	 */
	public void sendMail(Map<Object, Object> aMap)
	throws javax.mail.MessagingException, IOException, Exception
	{

		String toMail = (String) aMap.get(EmailTags.TO);

		if(toMail == null || toMail.length() == 0)
			toMail = this.toMail;

		String templateName = (String) aMap.get(EmailTags.TEMPLATE_NAME);

		this.sendMail(toMail, templateName, aMap, Locale.getDefault());

	}// --------------------------------------------

	/**
	 * ending out E-mails through SMTP E-mail server. From the system
	 * @param to E-mail TO: field in Internet E-mail address format. If there
	 *            are more than one E-mail addresses
	 * @param subject E-mail subject line.
	 * @param messageBody  E-mail body.
	 */
	public void sendMail(String to, String subject,
			String messageBody)
	{
		if(this.mailFromUser == null || this.mailFromUser.length() == 0)
					throw new IllegalArgumentException("Config property "+EmailTags.MAIL_FROM_ADDRESS_PROP+" or setting property mailFromUser is required");

		sendMail(to, this.mailFromUser,
				subject, messageBody);

	}// --------------------------------------------

	/**
	 * Sending out E-mails through SMTP E-mail server.
	 * 
	 * @param aTo E-mail TO: field in Internet E-mail address format. If there
	 *            are more than one E-mail addresses, separate them by
	 *            SysConst.EMAIL_DELIMITER_IND.
	 * @param aFrom from address
	 * @param aSubject E-mail subject line.
	 * @param aMessageBody E-mail body.
	 */

	public  void sendMail(String aTo, String aFrom,
			String aSubject, String aMessageBody)
	{

		sendMail(aTo, aFrom, aSubject, aMessageBody, (File) null);
	}// --------------------------------------------
	
	/**
	 * Sending out E-mails through SMTP E-mail server.
	 * @param aTo the email to
	 * @param aSubject the email subject
	 * @param aMessageBody the message body
	 * @param aFile the file to send
	 * @throws Exception unknown error occurs
	 */

	public  void sendMail(String aTo, String aSubject,
			String aMessageBody, File aFile) throws Exception
	{
		sendMail(aTo, this.mailFromUser,
				aSubject, aMessageBody, aFile);
	}// --------------------------------------------
	/**
	 * 
	 * @param to the list bcc address
	 * @param from from user
	 * @param subject the subject
	 * @param messageBody the message body
	 */
	public  void sendMailBcc(String to, String from,
	String subject, String messageBody)
	{
		sendMail(to,Message.RecipientType.BCC, from,
		subject, messageBody, null);
	}//------------------------------------------------
	public  void sendMail(String to, String from,
	String subject, String aMessageBody, File aFile)
	{
		sendMail(to,Message.RecipientType.TO, from,
		subject, aMessageBody, aFile);
	}//------------------------------------------------
	/**
	 * Sending out E-mails through SMTP E-mail server.
	 * 
	 * @param to E-mail TO: field in internesendMailt E-mail address format. If
	 *            there are more than one E-mail addresses, separate them by
	 *            SysConst.EMAIL_DELIMITER_IND.
	 * @param from from address
	 * @param file the file to send
	 * @param subject E-mail subject line.
	 * @param messageBody E-mail body.
	 */

	private  void sendMail(String to,Message.RecipientType recipientType, String from,
	String subject, String messageBody, File file)
	{
		
		if(to == null || to.length() == 0)
					throw new IllegalArgumentException("to is required");
		
		if(from == null || from.length() == 0)
					throw new IllegalArgumentException("from is required");
		this.connect();
		
		try
		{
			MimeMessage mailMessage = new MimeMessage(this.mailSession);

			mailMessage.setFrom(new InternetAddress(from));

			if (messageBody == null)
			{
				messageBody = "";

			}

			//Message.RecipientType.TO
			mailMessage.setRecipients(recipientType,
					this.getAllEmailAddress(to));

			subject = subject == null ? this.defaultSubject : subject;
			mailMessage.setSubject(subject);

			// attach file
			if (file != null)
			{
				this.attach(mailMessage, file, messageBody);
			}
			else
			{
				mailMessage.setContent(messageBody, this.contentType);
			}

			sendMessage(mailMessage);
		}
		catch (SystemException e)
		{
			throw new CommunicationException(e);
		}
		catch (Exception e)
		{
			throw new CommunicationException(e);
		}
	}// --------------------------------------------

	/**
	 * 
	 * @param aMailMessage
	 * @throws MessagingException
	 */
	private void sendMessage(MimeMessage aMailMessage)
	{
		boolean authNeeed = this.isAuthenicationRequired();
		Debugger.println(this, "authNeeed=" + authNeeed);

		try
		{
			if (authNeeed)
			{
				// -------------------------------------------
				// set properties
//				if(!this.isConnected())
//				{
//					Properties props = System.getProperties();
//					props.put("mail.smtp.auth", String.valueOf(authNeeed));
//
//					// try to connect and send it
//					aMailMessage.saveChanges();					
//				}
//				
//				Transport tp = this.mailSession.getTransport("smtp");
//
//				tp.connect(this.smtpHost,
//						this.mailFromUser,
//						String.valueOf(this.mailFromPassword));
//				// send the thing off
//				Transport.send(aMailMessage, aMailMessage.getAllRecipients());
//				tp.close();
				
				mailTransport.sendMessage(aMailMessage, aMailMessage.getAllRecipients());
				// -------------------------------------------
				// aMailMessage.saveChanges();
				// this.the_mailTransport.sendMessage(aMailMessage,
				// aMailMessage.getAllRecipients());

			}
			else
			{

				Transport.send(aMailMessage);

			}
		}
		catch (Exception e)
		{
			String message = e.getMessage();
			
			if(message.contains("530 Authentication"))
			{
				throw new SecurityException("Security issue, try setting properties "
						+EmailTags.MAIL_AUTHENICATION_REQUIRED_PROP+", "+EmailTags.MAIL_FROM_ADDRESS_PROP+" and "+EmailTags.MAIL_FROM_PASSWORD_PROP+" error:"+message);
			}
			
			
			throw new CommunicationException("Unable to connect to server to send message. Try setting mail.host or mail.<protocol>.host (ex: mail.smtp.host). "+message,e);
		}
	}// --------------------------------------------

	/**
	 * 
	 * Check to see if there are more than one E-mail addresses separated
	 * 
	 * by EMAIL_DELIMITER_IND and put them into array, so SMTP
	 * 
	 * E-mail server can send them out in one single call.
	 * 
	 * 
	 * 
	 * @param ToEmailAddress E-mail TO: field in internet E-mail address format.
	 *            If there are more than one E-mail addresses, separate them by
	 *            SysConst.EMAIL_DELIMITER_IND.
	 * 
	 * 
	 * 
	 * @return Array of E-mail addresses.
	 */

	private InternetAddress[] getAllEmailAddress(String toEmailAdddress)
	throws AddressException
	{

		if (toEmailAdddress == null)

			throw new IllegalArgumentException(
					"aToEmailAddress required in Email");

		StringTokenizer tokens = new StringTokenizer(toEmailAdddress,
				EmailTags.EMAIL_DELIMITER_IND);

		if (tokens.countTokens() <= 0)
		{
			throw (new IllegalArgumentException(
					"No TO E-mail addresses passed in."));
		}

		InternetAddress[] emailAddresses = new InternetAddress[tokens
				.countTokens()];

		int i = 0;

		while (tokens.hasMoreTokens())
		{
			emailAddresses[i] = new InternetAddress(tokens.nextToken());
			i++;
		}
		
		return (emailAddresses);

	}// --------------------------------------------

	/**
	 * Sends a email message to specified address
	 * @param to to email
	 * @param aFrom from email
	 * @param aSubject subject line 
	 * @param aTemplateNM the text template
	 * @param aMap the bind variables
	 * @param aLocale the local
	 * @throws javax.mail.MessagingException when unknown error occurs
	 * @throws IOException when unknown error occurs
	 * @throws Exception when unknown error occurs
	 */
	public void sendMail(String to, String aFrom, String aSubject,
			String aTemplateNM, Map<Object, Object> aMap, Locale aLocale)
	throws javax.mail.MessagingException, IOException, Exception
	{

		aMap.put(EmailTags.TO, to);
		aMap.put(EmailTags.FROM_EMAIL, aFrom);
		aMap.put(EmailTags.SUBJECT, aSubject);

		sendMail(to, aTemplateNM, aMap, aLocale);

	}// --------------------------------------------

	/**
	 * Sends a email message to specified address
	 * @param to the recipient
	 * @param templateName the bind Template (see Text object)
	 * @param map   the bind template map
	 * @param locale the locale
	 * @throws javax.mail.MessagingException unknown error occurs
	 * @throws IOException  unknown error occurs
	 * @throws Exception  unknown error occurs
	 */
	public void sendMail(String to, String templateName,
			Map<Object, Object> map, Locale locale)
	throws javax.mail.MessagingException, IOException, Exception
	{

		logger.debug("sendMail");
		
		this.connect();

		Object o = map.get(EmailTags.SUBJECT);

		// process Subject

		String SubjectStr = "System Contact.";

		if (o != null && !Data.isNull(o.toString()))
			SubjectStr = o.toString();

		o = map.get(EmailTags.CATEGORY);

		// process Subject Category

		if (o != null && !Data.isNull(o.toString()))
			SubjectStr += " -- Category --" + o;

		// Create Mail Message
		MimeMessage mailMessage = new MimeMessage(mailSession);

		if(this.mailFromUser == null || this.mailFromUser.length() == 0)
			throw new RequiredException("Config property "+EmailTags.MAIL_FROM_ADDRESS_PROP);
		
		mailMessage.setFrom(new InternetAddress(this.mailFromUser,

		(String) map.get(EmailTags.FROM_NAME)));

		// GET message body from template

		String MessageBody = Text
				.formatFromTemplate(templateName, map, locale);

		Debugger.println(MessageBody);

		InternetAddress[] addr = this.getAllEmailAddress(to);

		mailMessage.addRecipients(Message.RecipientType.TO, addr);

		mailMessage.setSubject(SubjectStr);

		mailMessage.setSentDate(new Date());

		// Process CC

		o = map.get(EmailTags.CC);

		String cc_string = null;

		if (o != null && !Data.isNull(o.toString()))
		{
			cc_string = o.toString();
		}

		if (cc_string != null)
		{

			InternetAddress[] cc_addr = this.getAllEmailAddress(cc_string);
			mailMessage.addRecipients(Message.RecipientType.CC, cc_addr);
		}

		Multipart mp = new MimeMultipart();

		MimeBodyPart mbp1 = new MimeBodyPart();

		mbp1.setContent(MessageBody, contentType);

		mp.addBodyPart(mbp1);

		// Debug
		javax.mail.Address[] addrList = mailMessage.getAllRecipients();
		for (int i = 0; i < addrList.length; i++)
		{
			logger.debug("TO:" + addrList[i]);
		}

		mailMessage.setContent(mp);

		if (isAuthenicationRequired())
		{
			logger.debug("CONNECTED=" + this.mailTransport.isConnected());
			this.mailTransport.sendMessage(mailMessage,
					mailMessage.getAllRecipients());
		}

		else
		{
			Transport.send(mailMessage);

		}

	} // ----------------------------------------------------------
	private  void initFromJNDI() throws Exception
	{

		InitialContext initialContext = this.getContext();

		this.mailSession = (Session) initialContext.lookup(jndiName);

		this.mailTransport = this.mailSession.getTransport();

	}// --------------------------------------------
	public  Collection<String> shouldRemoveEmails()
			throws javax.mail.MessagingException, IOException
	{
		int batchCount = 100;
		int startIndex = 1;
		
		String pattern = ".*failed.*||.*failure.*||.*Undelivered.*||.*could not be delivered.*||.*unsubscribe.*||(.*invalid.*${AND}.*email.*)";
		
		Collection<EmailMessage>  emailMesssages = readMatches(batchCount, startIndex, pattern);
		if(emailMesssages == null || emailMesssages.isEmpty())
			return null;
		
		HashSet<String> results = new HashSet<String>(batchCount);
		
		String subject =null, body  =null;
		
		do
		{
			
			for (EmailMessage emailMessage : emailMesssages)
			{
				//Should remove unscribe subjects
				subject = emailMessage.getSubject();
				if(subject != null)
				{
					subject = subject.toLowerCase(Locale.US);
					
					if(subject.contains("unsubscribe"))
					{
						if(emailMessage.getFrom() != null)
						{
							results.addAll(emailMessage.getFrom());
						}
					}
				}
				
				body = emailMessage.getContent();
				if(body == null)
					continue;
				
				body = body.trim();
				if(body.length() == 0)
					continue;
				
				//find all emails
				Collection<String> emailsToRemove = findEmails(body);
				if(emailsToRemove != null)
					results.addAll(emailsToRemove);
			}
			
			//iterate
			startIndex = startIndex + batchCount;
			emailMesssages = readMatches(batchCount, startIndex, pattern);
			
		} while (emailMesssages != null && !emailMesssages.isEmpty());
		
		
		if(results.isEmpty())
			return null;
		
		return results;
	}//------------------------------------------------
	/**
	 * 
	 * @param context the email message text
	 * @return new TextToEmailsConverter().convert(context);
	 */
	public Collection<String> findEmails(String context)
	{
		return new TextToEmailsConverter().convert(context);
	}//------------------------------------------------
	
	/**
	 * 
	 * @param count number of records to read
	 * @param startIndex start index 1 or higher
	 * @param pattern the search pattern
	 * @return collection of the message
	 * @throws javax.mail.MessagingException a message exception occurs
	 * @throws IOException IO exception occurs
	 */
	public  Collection<EmailMessage> readMatches(int count,  int startIndex, String pattern)
	throws javax.mail.MessagingException, IOException
	{

		logger.debug("readMail");
		
		this.connect();
		
		try(ReadMail reader = new ReadMail(this.store))
		{
			return reader.readMatches(count, startIndex, pattern,false);
		}
		catch(javax.mail.MessagingException e)
		{
			Debugger.printError(e);
			throw e;
		}
		catch(IOException e)
		{
			Debugger.printError(e);
			throw e;
		}
	}//------------------------------------------------
	/**
	 * Initialize the email object
	 * 
	 */
	private void init()
	{
			try
			{
				Properties systemProperties = System.getProperties();
				
				
				Properties mailProperties = new Properties();
			
				mailProperties.setProperty("mail.imap.host",imapHost);
				mailProperties.setProperty("mail.imap.port",imapPort);
				mailProperties.setProperty("mail.smtp.ssl.enable",String.valueOf(smtpSslEnable));
				mailProperties.setProperty("mail.smtp.host",mailHost);
				mailProperties.setProperty("mail.host",mailHost);				
				mailProperties.setProperty("mail.imap.auth.plain.disable", "false");
				mailProperties.setProperty("mail.imap.auth.ntlm.disable", "false");
				mailProperties.setProperty("mail.store.protocol", "imap");
				mailProperties.setProperty("mail.imap.starttls.enable","true");
				mailProperties.setProperty("mail.imap.starttls.required","true");
				mailProperties.setProperty("mail.imap.socketFactory.port","993");
				mailProperties.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				mailProperties.setProperty("mail.imap.auth.login.disable","false");
				mailProperties.setProperty("mail.imap.socketFactory.fallback","false");
				mailProperties.setProperty("mail.imap.debug", "true");
				mailProperties.setProperty("mail.imap.sasl.enable", "true");
				mailProperties.setProperty("mail.imap.ssl.enable","true");
				mailProperties.setProperty("mail.imap.auth.plain.disable","true");
				mailProperties.setProperty("mail.imap.auth.mechanisms","LOGIN");
				mailProperties.put("mail.auth.required", this.authenicationRequired);
				mailProperties.put("mail.smtp.port",this.mailPort);
				mailProperties.put("mail.webdav.host",webdavHost);
				mailProperties.put("mail.weddav.port",weddavPort);
				mailProperties.put("mail.debug", Config.getProperty("MAIL_DEBUG","true"));
				mailProperties.put("mail.from", this.mailFromUser);
				mailProperties.put("mail.smtp.from",this.mailFromUser);
				mailProperties.put("mail.smtp.starttls.enable", 
					Config.getProperty("MAIL_SMTP_STARTTLS_ENABLE","false"));
				mailProperties.put("mail.imap.sasl.authorizationid",mailImapSaslAuth);
				mailProperties.put("mail.smtp.auth",authNeeded);
				
				//Merge System Properties
				String key = null;
				for(Map.Entry<Object, Object> entry: systemProperties.entrySet())
				{
					key = String.valueOf(entry.getKey());
					
					if(key == null || !key.contains("mail."))
						continue;
					
					mailProperties.setProperty(key, String.valueOf(entry.getValue()));
				}
				
				//merge config properties
				Map<Object,Object> configProperties = Config.getProperties();
				if(configProperties != null  && !configProperties.isEmpty())
				{
					for (Map.Entry<Object, Object> entry : configProperties.entrySet())
					{
						key = String.valueOf(entry.getKey());
						
						if(key == null || !key.contains("mail."))
							continue;
						
						mailProperties.setProperty(key, String.valueOf(entry.getValue()));
					}
					
				}

				mailSession = Session.getDefaultInstance(mailProperties,

				new javax.mail.Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(
								getMailFromUser(),
								Text.toString(getMailFromPassword()));
					}

				});

				mailTransport = mailSession.getTransport(mailProtocol);

				initStore();
				
				Debugger.println(this, "properties=" + mailSession.getProperties());

				if (isAuthenicationRequired())
				{

					String server = this.mailHost;
					if(server == null || server.length() == 0)
						throw new RequiredException("Config property "+EmailTags.MAIL_SERVER_PROP);
				
					
					int port = mailPort;
					String from = this.mailFromUser;
					
					if(this.mailFromUser == null || this.mailFromUser.length() == 0)
						throw new RequiredException("Config property "+EmailTags.MAIL_FROM_ADDRESS_PROP);
				
					char[] password = this.getMailFromPassword();
					if(password == null || password.length == 0)
						throw new RequiredException(EmailTags.MAIL_FROM_PASSWORD_PROP+" property");
					
					mailTransport.connect(server, port, from, new String(password));

				}
			}
			catch (NoSuchProviderException e)
			{
				throw new SetupException(e);
			}
			catch (MessagingException e)
			{
				throw new CommunicationException(e);
			}

	} // -----------------------------------------------------------
	private void initStore()
	throws MessagingException
	{

		String protocol = readProtocol;
		if(protocol.length() == 0)
		{
			protocol  = "imap";
		}
	
		this.store = mailSession.getStore(protocol);
		
		if(store == null)
			return;
		
		String mailReadHost = Config.getProperty("mail."+protocol+".host","");
		
		if(mailReadHost == null || mailReadHost.length() ==0)
			return; //do not nothing
		
		if (!isAuthenicationRequired())
			return;
		
		String user = getMailFromUser();
		if(user == null|| user.trim().length() == 0)
					throw new SetupException("user is required. Set property:"+EmailTags.MAIL_FROM_ADDRESS_PROP);
		
		char[] passwordArray = getMailFromPassword();
		
		String password = "";
		
		if(passwordArray != null && passwordArray.length > 0)
			password = new String(passwordArray);
		
		try{
			store.connect(mailReadHost, user,
					password);
		}
		catch(AuthenticationFailedException e)
		{
			if(password == null || password.trim().length() == 0)
				throw new SecurityException("Required password not provided in property:"+EmailTags.MAIL_FROM_PASSWORD_PROP);
			else
				throw new CommunicationException("Unable authenticate to host:"+mailReadHost+" user:"+user+" ERROR:"+e.getMessage(),e);
		}
		catch(MessagingException e)
		{
			throw new CommunicationException("Unable to connect to host:"+mailReadHost+" user:"+user+" ERROR:"+e.getMessage(),e);
		}
		catch(RuntimeException e)
		{
			throw new CommunicationException("Unable to connect to host:"+mailReadHost+" user:"+user+" ERROR:"+e.getMessage(),e);
		}		
		
		
	}//------------------------------------------------

	/**
	 * 
	 * @param aMessage the message to add to
	 * 
	 * @param aFile the file attachment
	 */

	private  void attach(MimeMessage aMessage, File aFile, String aMessageText)
    throws Exception
	{

		// DocumentDelegate d = new DocumentDelegate();
		Multipart mp = new MimeMultipart();

		if (aMessageText != null && aMessageText.length() > 0)
		{
			MimeBodyPart textPart = new MimeBodyPart();

			textPart.setContent(aMessageText, this.contentType);

			mp.addBodyPart(textPart);
		}

		MimeBodyPart mbp = new MimeBodyPart();

		mbp.setDataHandler(new DataHandler(new FileDataSource(aFile)));

		mbp.setFileName(aFile.getName());

		mp.addBodyPart(mbp);

		aMessage.setContent(mp);

	}// --------------------------------------------

	
	/**
	 * 
	 * @param map the map
	 * @param appendtext the text to append
	 */
	public void appendCC(Map<Object, Object> map, String appendtext)
	{
		StringBuffer cc = null;

		if (map.get(EmailTags.CC) == null)
		{

			cc = new StringBuffer();

		}
		else
		{

			cc = new StringBuffer((String) map.get(EmailTags.CC));

		}

		if (cc.length() == 0)
		{
			cc.append(appendtext);
		}
		else
		{
			cc.append(";" + appendtext);
		}

		map.put(EmailTags.CC, cc.toString());

	}// --------------------------------------------
	private InitialContext getContext() throws Exception
	{

		return new InitialContext();

	}// --------------------------------------------

	/**
	 * @return the contentType
	 */
	public  String getContentType()
	{
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public  void setContentType(String contentType)
	{
		this.contentType = contentType;
	}//------------------------------------------------

	
	/**
	 * @return the mailHost
	 */
	public String getMailHost()
	{
		return mailHost;
	}
	/**
	 * @param mailHost the smtpHost to set
	 */
	public void setMailHost(String mailHost)
	{
		this.mailHost = mailHost;
	}


	/**
	 * @return the authenicationRequired
	 */
	public boolean isAuthenicationRequired()
	{
		return authenicationRequired;
	}
	/**
	 * @param authenicationRequired the authenicationRequired to set
	 */
	public void setAuthenicationRequired(boolean authenicationRequired)
	{
		this.authenicationRequired = authenicationRequired;
	}


	/**
	 * @return the mailFromUser
	 */
	public String getMailFromUser()
	{
		return mailFromUser;
	}
	/**
	 * @param mailFromUser the mailFromUser to set
	 */
	public void setMailFromUser(String mailFromUser)
	{
		this.mailFromUser = mailFromUser;
	}
	/**
	 * @return the mailFromPassword
	 */
	public char[] getMailFromPassword()
	{
		if(mailFromPassword == null || mailFromPassword.length == 0)
			return null;
		
		return Arrays.copyOf(mailFromPassword,mailFromPassword.length);
	}//------------------------------------------------
	/**
	 * @param mailFromPassword the mailFromPassword to set
	 */
	public void setMailFromPassword(char[] mailFromPassword)
	{
		if(mailFromPassword == null || mailFromPassword.length == 0)
		{
			this.mailFromPassword = null;
		}
		else
		{
			this.mailFromPassword = Arrays.copyOf(mailFromPassword,mailFromPassword.length);	
		}
			
		
	}//------------------------------------------------
	/**
	 * @return the mailTransport
	 */
	public  Transport getMailTransport()
	{
		return mailTransport;
	}
	/**
	 * @param mailTransport the mailTransport to set
	 */
	public  void setMailTransport(Transport mailTransport)
	{
		this.mailTransport = mailTransport;
	}

	/**
	 * @return the defaultSubject
	 */
	public String getDefaultSubject()
	{
		return defaultSubject;
	}
	/**
	 * @param defaultSubject the defaultSubject to set
	 */
	public void setDefaultSubject(String defaultSubject)
	{
		this.defaultSubject = defaultSubject;
	}


	/**
	 * @return the authNeeded
	 */
	public boolean isAuthNeeded()
	{
		return authNeeded;
	}
	/**
	 * @return the mailProtocol
	 */
	public String getMailProtocol()
	{
		return mailProtocol;
	}
	/**
	 * @return the mailPort
	 */
	public int getMailPort()
	{
		return mailPort;
	}
	/**
	 * @param authNeeded the authNeeded to set
	 */
	public void setAuthNeeded(boolean authNeeded)
	{
		this.authNeeded = authNeeded;
	}
	/**
	 * @param mailProtocol the mailProtocol to set
	 */
	public void setMailProtocol(String mailProtocol)
	{
		this.mailProtocol = mailProtocol;
	}
	/**
	 * @param mailPort the mailPort to set
	 */
	public void setMailPort(int mailPort)
	{
		this.mailPort = mailPort;
	}


	/**
	 * @return the smtpSslEnable
	 */
	public boolean getSmtpSslEnable()
	{
		return smtpSslEnable;
	}
	/**
	 * @param smtpSslEnable the smtpSslEnable to set
	 */
	public void setSmtpSslEnable(boolean smtpSslEnable)
	{
		this.smtpSslEnable = smtpSslEnable;
	}


	private String defaultSubject = Config.getProperty(EmailTags.MAIL_SUBJECT_PROP,"");
	private String mailFromUser = Config.getProperty(EmailTags.MAIL_FROM_ADDRESS_PROP,"");
	private char[] mailFromPassword = Config
			.getPropertyPassword(EmailTags.MAIL_FROM_PASSWORD_PROP,"");
	private boolean authenicationRequired = Config.getPropertyBoolean(EmailTags.MAIL_AUTHENICATION_REQUIRED_PROP,
			Boolean.FALSE).booleanValue();
	private String mailHost = Config.getProperty(EmailTags.MAIL_SERVER_PROP,""); //required
	private Session mailSession = null;
	private Store store = null;
	private String contentType = Config.getProperty("MAIL_CONTENT_TYPE", "text/html");
	private Transport mailTransport = null;
	private Log logger = Debugger.getLog(getClass());
	private boolean useJNDI = Config.getPropertyBoolean(EmailTags.MAIL_SESSION_JNDI_USED, false)
	.booleanValue();
	private String toMail = Config.getProperty("mail.to","");
	private String jndiName = Config.getProperty(EmailTags.MAIL_SESSION_JNDI_NAME,"");
	private String readProtocol = Config.getProperty("MAIL_READ_PROTOCOL", "");
	private String imapHost = Config.getProperty("MAIL_IMAP_HOST", "");
	private String imapPort = Config.getProperty("MAIL_IMAP_PORT", "143");
	private boolean smtpSslEnable =  Config.getPropertyBoolean("MAIL_SMTP_SSL_ENABLE", true);
	private String webdavHost = Config.getProperty("MAIL_WEBDAV_HOST", "");

	private String weddavPort = Config.getProperty("MAIL_WEBDAV_PORT", "143");
	private String mailImapSaslAuth = Config.getProperty(EmailTags.MAIL_FROM_ADDRESS_PROP,this.mailFromUser);
	private boolean authNeeded = Config.getPropertyBoolean("MAIL_AUTH_REQUIRED", false);
	private String mailProtocol = Config.getProperty("MAIL_PROTOCOL", "smtp");
	private int mailPort = Config.getPropertyInteger("MAIL_PORT", 25).intValue();
	
}
