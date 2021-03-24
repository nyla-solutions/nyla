package nyla.solutions.core.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MEssage Digest wrapper object
 * @author Gregory Green
 *
 */
public class MD
{
	/**
	 * 
	 * @param obj the object to create the check sum
	 * @return the numeric checksum for an object
	 */
	public static BigInteger checksum(Object obj) 
	{
	    if (obj == null) {
	      return BigInteger.ZERO;   
	    }
	    
	    Class<?> clz = obj.getClass();
	    
	    if(!(Serializable.class.isAssignableFrom(clz)))
 	    {
		    String tostring = obj.toString();
		    
		    if(tostring.contains(clz.getName()) && tostring.contains("@"))
		    {
		 	      	//use hash code
		 	    	return BigInteger.valueOf(obj.hashCode());
		    }
		    else
		    {
		    	//try to use toString
		    	obj = tostring;
		    }
		 }

	    try(
	    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    		ObjectOutputStream oos = new ObjectOutputStream(baos);)
	    {
		    oos.writeObject(obj);

		    MessageDigest m = MessageDigest.getInstance("MD5");
		    m.update(baos.toByteArray());

		    return new BigInteger(1, m.digest());	    	
	    }
	    catch(IOException e)
	    {
	    	throw new RuntimeException("Unable to write to object output stream ERROR:"+e.getMessage(),e);
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	    	throw new RuntimeException("Unable to get message digest MD5 ERROR:"+e.getMessage());
	    }
	    
	}
	

}
