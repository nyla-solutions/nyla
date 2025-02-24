/**
 * 
 */
package nyla.solutions.core.operations;

import nyla.solutions.core.io.IO;

import java.io.*;
import java.util.*;


/**
 * Generates run command script can contains CLASSPATH elements.
 * Unix and Windows environments are supported.
 * 
 * @author Gregory Green
 *
 */
public class GeneratorRunScript
{
   public static final String CLASSPATH_PROP = "java.class.path";
   
   public static final void main(String [] args)
   {
	   
	   if(args.length ==0)
	   {
		   throw new IllegalArgumentException("outputFile required");
	   }
	   
	   File file= new File(args[0]);
	   
		try
		{
		   FileOutputStream os = new FileOutputStream(file);
		   
		   PrintWriter writer = new PrintWriter(new OutputStreamWriter(os,IO.CHARSET));
	
		   writeScript(writer);
		   
		   
		   System.out.println("See file:"+file.getAbsolutePath());
		} 
		catch (Exception e)
		{
		   e.printStackTrace();
		  // throw new SystemException(Debugger.stackTrace(e));
		}
	
	
   }// ----------------------------------------------
   /**
    * 
    * @param ps the print stream to write output
    * @throws IOException the errors writting to the filesystem
    */
   public static final void writeScript(PrintStream ps)
   throws IOException
   {
	   PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out,IO.CHARSET));
	   
	   writeScript(writer);
	   
   }// --------------------------------------------------------
   
   public static final void writeScript(Writer writer)
   throws IOException
   {
	   String newline = IO.newline();
	   
	   boolean isWindows = System.getProperty("os.name").startsWith("Windows");
	   
	   String variablePrefix = "$C";
	   String variableSuffix = File.pathSeparator;
	   
	   if(isWindows)
	   {
		   variablePrefix = "%C";
		   variableSuffix = "%"+File.pathSeparator;
	   }
	   
	String setSyntax = isWindows ? "@set " : "export ";
	   
	String classpath = System.getProperty(CLASSPATH_PROP);
	
	String cpath = null;
	
	HashMap<String, HashSet<Object>> map = new HashMap<String, HashSet<Object>>();
	String folderPath = null;
	HashSet<Object> folderPathClassPaths = null;
	
	for(StringTokenizer tok = new StringTokenizer(classpath,File.pathSeparator);tok.hasMoreTokens();)
	{
	   cpath = tok.nextToken();
	   
	   //get path
	   folderPath = IO.parseFolderPath(cpath);
	   
	   //System.out.println("folderPath="+folderPath);
	   
	   //get list for folder path
	   folderPathClassPaths = (HashSet<Object>)map.get(folderPath);
	   
	   //create if needed
	   if(folderPathClassPaths == null)
	   {
		folderPathClassPaths = new HashSet<Object>();
	   }
	   
	   folderPathClassPaths.add(cpath);
	   
	   //put in map
	   map.put(folderPath, folderPathClassPaths);	   	   
	   
	}	
	
	//loop thru keys in map
	int cnt = 0;
	
	
	for(Map.Entry<String, HashSet<Object>> entry : map.entrySet())
	{
	   folderPath = entry.getKey();
	   
	   //get list of classpaths
	   folderPathClassPaths =  entry.getValue();
	   
	   //write variable
	   writer.write(IO.newline());
	   writer.write(setSyntax);
	   writer.write(new StringBuilder(" C").append(cnt).append("=").toString());
	   
	   
	   //loop thru paths
	   int printedCnt = 0;
	   
	   String line = null;
	   for(Iterator<Object> pathI = folderPathClassPaths.iterator();pathI.hasNext();)
	   {
		//limit number of entries per path
		if(printedCnt > limitPerPath)
		{
		   //increment
		   cnt++;
		   
		   writer.write(newline);
		   
		   writer.write(new StringBuilder(setSyntax).append(" C").append(cnt).append("=").toString());
		   
		   printedCnt = 0;
		   
		}
		
		
		
		
		
		line = pathI.next().toString();
		writer.write(line);
		
		printedCnt += line.length();
		
		writer.write(File.pathSeparator);
		
	   }
	   writer.write(newline);
	   writer.write(newline);
	   
	   writer.flush();
	   
	}
	
	//print classpath
	writer.write(setSyntax); writer.write(" CLASSPATH=");
	
	for(int i=0; i <cnt;i++)
	{
	   writer.write(variablePrefix+i);
	   writer.write(variableSuffix);
	   
	}
	writer.write(newline);
	writer.write(newline);
	
	
	writer.write("java <CLASS> <ARG>");


	writer.flush();
	
	writer.write(newline);
	
	writer.write("java  junit.textui.TestRunner <CLASS> <ARG>");
	writer.write(newline);
	
	writer.flush();
	
	
	//System.out.println("File.pathSeparator="+File.pathSeparator);
	//System.out.println("File.separator="+File.separator);
	
	
   }
   private static int limitPerPath = 100;


}
