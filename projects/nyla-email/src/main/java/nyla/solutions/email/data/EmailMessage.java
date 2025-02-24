package nyla.solutions.email.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * 
 * @author Gregory Green
 *
 */
public class EmailMessage
{
	
	/**
	 * @return the cc
	 */
	public Collection<String> getCc()
	{
		return new ArrayList<String>(cc);
	}//------------------------------------------------
	/**
	 * @return the bcc
	 */
	public Collection<String> getBcc()
	{
		return new ArrayList<String>(bcc);
	}

	/**
	 * @return the to
	 */
	public Collection<String> getTo()
	{
		return new ArrayList<String>(to);
	}
	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}
	public void addTo(String to)
	{
		this.to.add(to);
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	/**
	 * @return the body
	 */
	public String getBody()
	{
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body)
	{
		this.body = body;
	}//------------------------------------------------
	public void addFrom(String form)
	{
		this.from.add(form);
	}
	
	/**
	 * @return the from
	 */
	public HashSet<String> getFrom()
	{
		return from;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EmailMessage [cc=").append(cc).append(", bcc=").append(bcc).append(", to=").append(to)
				.append(", from=").append(from).append(", recievedDate=").append(recievedDate).append(", content=")
				.append(content).append(", subject=").append(subject).append(", body=").append(body).append("]");
		return builder.toString();
	}
	/**
	 * @return the recievedDate
	 */
	public Date getRecievedDate()
	{
		if(recievedDate == null)
			return null;
		
		return new Date(recievedDate.getTime());
	}//------------------------------------------------
	/**
	 * @param recievedDate the recievedDate to set
	 */
	public void setRecievedDate(Date recievedDate)
	{
		if(recievedDate == null)
			return;
		this.recievedDate = new Date(recievedDate.getTime());
	}
	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}


	private HashSet<String> cc = new HashSet<String>();
	private HashSet<String> bcc= new HashSet<String>();
	private HashSet<String> to = new HashSet<String>();
	private HashSet<String> from = new HashSet<String>();
	private Date recievedDate;
	private String content = null;
	private String subject;
	private String body;

}
