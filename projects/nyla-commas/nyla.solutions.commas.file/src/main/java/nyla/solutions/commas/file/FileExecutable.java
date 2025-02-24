package nyla.solutions.commas.file;

import java.io.File;

import nyla.solutions.commas.Executable;
import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;


	/**
	 * File executable implementation for modifying file content	
	 * @author Gregory Green
	 *
	 */
	public class FileExecutable implements Executable
	{
		/**
		 * Use a regular expression replacing matches in the source content to an mutation output location
		 */
		public void process(File file)
		{	
			if(file.isDirectory())
			{
				processFolder(file);
			}
			else
			{
				//handles 
				processDocument(file);
			}
						
		}//---------------------------------------------
		/**
		 * Calls process  for each file
		 * @param directory
		 */
		private void processFolder(File directory)
		{
			File[] children = IO.listFiles(directory, this.listFilter);
			
			Debugger.printInfo(this,"processing folder="+directory.getAbsolutePath());
			
			if(children == null)
				return;
			
			for(int i = 0; i < children.length;i++)
			{
				process(children[i]);
			}
		}//---------------------------------------------
		/**
		 * Use a regular expression replacing matches in the source content to an output location
		 */
		protected void processDocument(File file)
		{	
			if(this.fileCommand == null)
				throw new RequiredException("this.fileCommand in FileExecutable");
			
			Debugger.println(this,"processing file="+file.getAbsolutePath());
			
			fileCommand.execute(file);
		}// --------------------------------------------	
		/**
		 * @return the nameRegExpFilter
		 */
		public String getNameRegExpFilter()
		{
			return nameRegExpFilter;
		}// --------------------------------------------
		/**
		 * @param nameRegExpFilter the nameRegExpFilter to set
		 */
		public void setNameRegExpFilter(String nameRegExpFilter)
		{
			this.nameRegExpFilter = nameRegExpFilter;
		}// --------------------------------------------		
		/**
		 * @return the regExp
		 */
		public String getRegExp()
		{
			return regExp;
		}// --------------------------------------------
		/**
		 * @param regExp the regExp to set
		 */
		public void setRegExp(String regExp)
		{
			this.regExp = regExp;
		}// --------------------------------------------
		
		/**
		 * @return the outputRootPath
		 */
		public String getOutputRootPath()
		{
			return outputRootPath;
		}// --------------------------------------------
		/**
		 * @param outputRootPath the outputRootPath to set
		 */
		public void setOutputRootPath(String outputRootPath)
		{
			this.outputRootPath = outputRootPath;
		}// --------------------------------------------
		/**
		 * @return the replacement
		 */
		public String getReplacement()
		{
			return replacement;
		}// --------------------------------------------
		/**
		 * @param replacement the replacement to set
		 */
		public void setReplacement(String replacement)
		{
			this.replacement = replacement;
		}// --------------------------------------------
		/**
		 * 
		 */
		public Integer execute(Environment env)
		{
			Debugger.printInfo(this,"Configured execute directory path:"+executeDirectoryPath);
			
			process(new File(executeDirectoryPath));
			
			Debugger.printInfo(this,"Processing DONE");
			
			return 0;
			
		}//---------------------------------------------
		
		/**
		 * @return the listFilter
		 */
		public String getListFilter()
		{
			return listFilter;
		}//---------------------------------------------
		/**
		 * @param listFilter the listFilter to set
		 */
		public void setListFilter(String listFilter)
		{
			this.listFilter = listFilter;
		}//---------------------------------------------
		/**
		 * @return the fileCommand
		 */
		public FileCommand<Object> getFileCommand()
		{
			return fileCommand;
		}//---------------------------------------------
		/**
		 * @param fileCommand the fileCommand to set
		 */
		public void setFileCommand(FileCommand<Object> fileCommand)
		{
			this.fileCommand = fileCommand;
		}//---------------------------------------------
		/**
		 * @return the executeDirectoryPath
		 */
		public String getExecuteDirectoryPath()
		{
			return executeDirectoryPath;
		}
		/**
		 * @param executeDirectoryPath the executeDirectoryPath to set
		 */
		public void setExecuteDirectoryPath(String executeDirectoryPath)
		{
			this.executeDirectoryPath = executeDirectoryPath;
		}



		private String executeDirectoryPath  =  Config.getProperty(FileExecutable.class,"executeDirectoryPath");
		private FileCommand<Object> fileCommand = null;
		private String listFilter = Config.getProperty(FileExecutable.class,"listFilter","*.*");
		private String outputRootPath = Config.getProperty(FileExecutable.class,"outputRootPath","./output");
		private String nameRegExpFilter = Config.getProperty(FileExecutable.class,"fileNameRegExpFilter",".*");
		private String regExp = "";
		private String replacement = "";

	}
