package nyla.solutions.core.patterns;

import java.io.Closeable;
import java.io.IOException;

/**
  <b>Disposable</b> is  public interface interface
  for objects to free held resource when requested
*/
public interface Disposable extends Closeable
{
	/**
	 * Default close method call close
	 */
	default @Override void close() throws IOException
	{
		dispose();
		
	}
	/**
	 * Dispose of needed resources
	 */
   void dispose();
}
