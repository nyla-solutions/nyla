package nyla.solutions.core.patterns.creational.builder.mapped;

import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static nyla.solutions.core.util.Config.settings;


public class FileMappedKeyDirector<K,V> extends MappedKeyDirector<K,V>
{
	
	/**
	 * Director method to construct a document
	 * @param filePath the file path the output 
	 * @param engineer the strategy 
	 */
	public void constructDocument(String filePath, MappedKeyEngineer<K,V> engineer)
	{
		//clear cache		
		try
		{
			crawl(new File(filePath), engineer);	
		}
		finally
		{
			crawledPaths.clear();
		}		
	}// --------------------------------------------
	/**
	 * Director method to construct a document
	 * @param file the file the walk through
	 * @param engineer the mapped key engineer
	 */
	protected void crawl(File file, MappedKeyEngineer<K,V> engineer)
	{
		
		if (!file.exists())
		{
			Debugger.println(file + " does not exist.");
			return;
		}
		if (file.isDirectory())
		{
			File[] files = IO.listFiles(file, listPattern);

			for (int i = 0; i < files.length; i++)
			{
				//recursive
				if(!this.mustSkip(files[i]))
				crawl(files[i],engineer);
			}
		}
		else
		{
			try
			{
				engineer.construct(file.getPath(), this.constructMapToText(file.getPath()));
				crawledPaths.add(file.getPath());				
			}
			catch(NoDataFoundException e)
			{
				//print warning if found not 
				Debugger.printWarn(e);
			}
		}
	}// --------------------------------------------
	private boolean mustSkip(File file)
	{
		String path = file.getPath();
		
		if(Text.matches(path, ignorePathRegExp) || crawledPaths.contains(path))
		{
			Debugger.println(this, "skipped ="+path);
			return true;
		}
		return false;
	}// --------------------------------------------
	private String listPattern = settings().getProperty(this.getClass(),"listPattern","*.([xX][mM][lL]|[hH][tT][mM][lL]?|txt|TXT|xml|XML)");
	private Set<String> crawledPaths = new HashSet<String>();
	private String ignorePathRegExp = "";
	//private int quota = Integer.MAX_VALUE;
}
