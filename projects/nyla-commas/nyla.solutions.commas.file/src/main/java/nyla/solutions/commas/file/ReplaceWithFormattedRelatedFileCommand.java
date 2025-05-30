package nyla.solutions.commas.file;

import java.io.File;
import java.util.Map;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.io.SynchronizedIO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;



/**
 * This class includes the text content of a related file into the given file.
 * The related file is read and formatted using the Text.format() method.
 * The input text indicated by the replacementRE in the given execute method is replaced at runtime.
 * @author Gregory Green
 *
 */
public class ReplaceWithFormattedRelatedFileCommand  implements FileCommand<Void>
{

	/**
	 * Implement the file processing
	 * @param 
	 */
	public Void execute(File file)
	{	
		if( template == null || template.length() == 0)
			throw new RequiredException("this.template");
		
		if(this.replacementRE == null)
			throw new RequiredException("this. in ReplaceWithFormattedFileCommand");
		
		//read RACI HTML
		try 
		{
			SynchronizedIO io = SynchronizedIO.getInstance();
			String includeFileContent = io.readFile(file.getAbsolutePath()+includeFileSufix);
			
			//applied formatting
			Map<Object,Object> map = Config.getProperties();
			
			if(map == null)
				return null;
			
			map.putAll(JavaBean.toMap(file));
				
			map.put(contentKey, includeFileContent);
				
			String formattedOutput = Text.format(template, map);			
			
			String fileText = IO.readFile(file);
			
			//replace text in processMapHTMLFile with formatted output
			io.writeFile(file.getAbsolutePath(),Text.replaceForRegExprWith(fileText,
					this.replacementRE,formattedOutput));
			
			return null;
		} 
		catch (Exception e) 
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
		
	}//---------------------------------------------
	
	/**
	 * @return the replacementRE
	 */
	public String getReplacementRE()
	{
		return replacementRE;
	}
	/**
	 * @param replacementRE the replacementRE to set
	 */
	public void setReplacementRE(String replacementRE)
	{
		this.replacementRE = replacementRE;
	}
	/**
	 * @return the contentKey
	 */
	public String getContentKey()
	{
		return contentKey;
	}
	/**
	 * @param contentKey the contentKey to set
	 */
	public void setContentKey(String contentKey)
	{
		this.contentKey = contentKey;
	}
	/**
	 * @return the includeFileSufix
	 */
	public String getIncludeFileSufix()
	{
		return includeFileSufix;
	}
	/**
	 * @param includeFileSufix the includeFileSufix to set
	 */
	public void setIncludeFileSufix(String includeFileSufix)
	{
		this.includeFileSufix = includeFileSufix;
	}
	/**
	 * @return the template
	 */
	public String getTemplate()
	{
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template)
	{
		this.template = template;
	}

	private String replacementRE = null;
	private String contentKey = Config.getProperty(this.getClass(),"contentKey","content");
	private String includeFileSufix = Config.getProperty(this.getClass(),"includeFileSufix");
	private String template = Config.getProperty(this.getClass(),"template","${content}");
	
}
