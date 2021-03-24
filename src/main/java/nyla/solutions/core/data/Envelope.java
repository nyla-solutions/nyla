package nyla.solutions.core.data;

import java.io.Serializable;
import java.util.Map;

/**
 * This object encapsulates a request and response.
 * @author Gregory Green
 * @param <PayloadType>  the payload type
 *
 */
public class Envelope<PayloadType extends Object> implements Serializable
{
	/**
	 * Default constructor
	 */
	public Envelope()
	{}
	// --------------------------------------------------------
	/**
	 * Default constructor
	 * @param payload the payload
	 */
	public Envelope(PayloadType payload)
	{
		this.payload = payload;
	}
	// --------------------------------------------------------�
	/**
	 * Create with header and payload
	 * @param header the envelope header
	 * @param payload the payload
	 */
	public Envelope(Map<Object, Object> header, PayloadType payload)
	{
		this.header = header;
		this.payload = payload;
	}// --------------------------------------------------------



	/**
	 * 
	 */
	private static final long serialVersionUID = 543197843593634035L;
	/**
	 * @return the header
	 */
	public final Map<Object, Object> getHeader()
	{
		return header;
	}// --------------------------------------------
	/**
	 * @param header the header to set
	 */
	public final void setHeader(Map<Object, Object> header)
	{
		this.header = header;
	}// --------------------------------------------
	
	/**
	 * @return the pay load
	 */
	public final  PayloadType getPayload()
	{
		return payload;
	}
	/**
	 * @param payload the pay load to set
	 */
	public final void setPayload(PayloadType payload)
	{
		this.payload = payload;
	}// ------------------------------------------------'	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Envelope [header=" + header + ", payload=" + payload + "]";
	}



	private Map<Object,Object> header = null;
	private PayloadType payload  = null;
}
