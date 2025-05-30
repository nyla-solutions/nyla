package nyla.solutions.core.patterns.search;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public interface SearchEngine
{

	/**
	 * Create a search index for a URL
	 * @param url the url to index
	 * @throws IOException
	 */
	public abstract void index(URL url) throws IOException;	

	/**
	 * Indexes a file or directory
	 * 
	 * @param file  file or a folder we wish to add to the
	 *            index
	 * @throws java.io.IOException
	 */
	public abstract void index(File file) throws IOException;
	

	/**
	 * 
	 * @param query the query used by the engine
	 * @param field
	 * @return return array  ScoreDoc[]
	 */
	public abstract Object[] search(String query, String field);
	
	
	/**
	 * 
	 * @param query the query
	 * @param <T> the type
	 * @return return array  ScoreDoc[]
	 */
	public abstract <T> T[] search(String query);
	

}