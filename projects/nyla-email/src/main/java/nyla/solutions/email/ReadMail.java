package nyla.solutions.email;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Text;
import nyla.solutions.email.data.EmailMessage;

public class ReadMail implements Closeable
{
	public ReadMail(Store store)
	{
		this(store,"Inbox");
	}//------------------------------------------------
	/**
	 * Constructor
	 * @param store the read mail
	 * @param folderName the folder name
	 */
	public ReadMail(Store store, String folderName)
	{
		if(store == null)
					throw new IllegalArgumentException("store is required");
		
		if(folderName == null || folderName.trim().length() == 0)
					throw new IllegalArgumentException("folderName is required");
		try
		{
			inbox = store.getFolder(folderName);
			
			if(inbox == null)
				throw new NullPointerException(folderName+" does not exists");
		}
		catch (MessagingException e)
		{
			throw new ConnectionException(e.getMessage(),e);
		}
		catch(RuntimeException e)
		{
			if(e.getMessage().contains("Not connected"))
			{
				throw new ConnectionException("Not connected, try the properties mail.auth.required,"+
					" mail.from "+
					" mail.from.password "+
					" mail.imap.host "+
					" mail.stmp.host "
					);
			}
			throw e;
			
		}
		
	}//------------------------------------------------
	/**
	 * 
	 * @param count the number of records to read
	 * @param startIndex the starting message index
	 * @param pattern the Complex regular expression to match
	 * @param subjectOnly flag to determine if only the subject will be checked
	 * @return matching email message
	 * @throws MessagingException when a mail server communication error occurs
	 * @throws IOException when IO error occurs
	 */
	public Collection<EmailMessage> readMatches(int count, int startIndex, String pattern, boolean subjectOnly) 
	throws MessagingException, IOException
	{
		if(!inbox.isOpen())
		{
			inbox.open(Folder.READ_ONLY);
		}

		if(startIndex< 1)
			throw new IllegalArgumentException("startIndex must be greater than 0");
		
		if(count < 1)
			throw new IllegalArgumentException("Count must be greater than 0");
			
		/* Get the messages which is unread in the Inbox */
		int totalCount = inbox.getMessageCount();
		if(startIndex >  totalCount)
			return null;
		
		if(startIndex+ count-1 > totalCount)
		{
			count = totalCount - startIndex;
		}
		
		Message[] msgs = inbox.getMessages(startIndex,startIndex+ count-1);
		
		if(msgs == null || msgs.length == 0)
			return null;
		
			ArrayList<EmailMessage> results = Arrays.asList(msgs).stream().filter(msg -> {
							try
							{
								if(msg == null)
									return false;
								
								if(Text.matches(msg.getSubject(),pattern))
									return false;
								
								if(subjectOnly)
								{	
									return true;
								}

								//do not filter/remove the matching patterns
								return !Text.matches(getContent(msg),pattern);
								
							}
							catch (IOException e)
							{
								throw new nyla.solutions.core.exception.CommunicationException(e.getMessage());
							}
							catch (MessagingException e)
							{
								throw new nyla.solutions.core.exception.CommunicationException(e.getMessage());
							}
						}
					).map(message -> {
						try
						{
							return toEmailMessage(message);
						}
						catch (MessagingException | IOException e)
						{
							throw new RuntimeException(e);
						}
					}  ).collect(Collectors.toCollection(ArrayList::new));
			
			if(results.isEmpty())
				return null;
			
			return results;
		
	}//------------------------------------------------

	/* Print the envelope(FromAddress,ReceivedDate,Subject) */
	private EmailMessage toEmailMessage(Message message) 
	throws MessagingException, IOException 
	{
		EmailMessage emailMessage = new EmailMessage();
		// FROM
		Address[] froms = message.getFrom();
		
		if (froms != null)
		{
			for (int j = 0; j < froms.length; j++)
			{
				emailMessage.addFrom(froms[j].toString());
			}
		}
		// TO
		
		Address[] tos = message.getRecipients(Message.RecipientType.TO);

		if (tos != null)
		{
			for (int j = 0; j < tos.length; j++)
			{
				emailMessage.addTo(tos[j].toString());
			}
		}
		
		emailMessage.setSubject(message.getSubject());
		emailMessage.setRecievedDate(message.getReceivedDate());
		emailMessage.setContent(getContent(message));
		
		System.out.println("emailMessage : " + emailMessage);
		
		return emailMessage;
		//getContent(message);
	}//------------------------------------------------

	private String getContent(Message msg) 
	throws MessagingException, IOException
	{
		Object obj = msg.getContent();
		
		if(obj == null)
			return null;
		
		if(!Multipart.class.isAssignableFrom(obj.getClass()))
			return obj.toString();
		
		Multipart mp = (Multipart) msg.getContent();
		int count = mp.getCount(); 
			
			StringBuilder results = new StringBuilder();
			
			for (int i = 0; i < count; i++)
			{
				results.append(readTextPart(mp.getBodyPart(i)));
			}

			return results.toString();
	}//------------------------------------------------

	private String readTextPart(Part p) 
	throws IOException, MessagingException
	{
		// Dump input stream ..
		InputStream is = p.getInputStream();
		// If "is" is not already buffered, wrap a BufferedInputStream
		// around it.
		if (!(is instanceof BufferedInputStream))
		{
			is = new BufferedInputStream(is);
		}
		return IO.readText(is, true);
	}//------------------------------------------------

	@Override
	public synchronized void close() throws IOException
	{
		if(isClosed)
			return;
		try
		{
			inbox.close(false);
		}
		catch (MessagingException e)
		{
			throw new nyla.solutions.core.exception.CommunicationException(e.getMessage(),e);
		}
		
		this.isClosed =  true;
		
	}//------------------------------------------------
	private final Folder inbox;
	private boolean isClosed = false;
}
