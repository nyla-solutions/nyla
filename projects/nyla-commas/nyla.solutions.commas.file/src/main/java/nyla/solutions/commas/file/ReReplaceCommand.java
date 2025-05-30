package nyla.solutions.commas.file;

import java.io.File;
import java.util.Map;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.io.SynchronizedIO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;




public class ReReplaceCommand implements FileCommand<Void>
{
		/**
		 * Use a regular expression replacing matches in the source content to an output location
		 */
		public Void execute(File file)
		{	
			try
			{	
				//read source full text
				String fileContent = IO.readFile(file.getAbsolutePath());			
				
				//replace content
				Debugger.println(this,"replacing RE["+regExp+"]");				
				
				String runtimeReplacement = this.replacement;
				
				if(this.formatting)
				{
					runtimeReplacement = formatReplacement(file);
				}
				
				Debugger.println(this,"pre runtimeReplacement="+runtimeReplacement);
				
				//runtimeReplacement = runtimeReplacement.replace(" ", "");
				fileContent = Text.replaceForRegExprWith(fileContent, regExp, runtimeReplacement);
								
				//fileContent = Text.replaceForRegExprWith(fileContent, regExp, "Hello world");
				
				Debugger.println(this,"post fileContent="+runtimeReplacement);
				
				//Debugger.println(this,"replacement="+replacement);
				
				//save text to output file
				SynchronizedIO.getInstance().writeFile(file.getAbsolutePath(), fileContent);
				
				
				return null;
			}
			catch (Exception e)
			{
				throw new SystemException(e);
			}
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
		 * @return the formatting
		 */
		public boolean isFormatting()
		{
			return formatting;
		}//---------------------------------------------
		/**
		 * @param formatting the formatting to set
		 */
		public void setFormatting(boolean formatting)
		{
			this.formatting = formatting;
		}

		private String formatReplacement(File file)
		throws Exception
		{
			Map<Object,Object> map = Config.getProperties();
			
			if(map == null)
				return null;
			
			if(this.bindMap != null)
				map.putAll(this.bindMap);
			
			map.putAll(JavaBean.toMap(file));
			
			map.put(contentKey, IO.readFile(file.getAbsolutePath()));
			
			return Text.format(this.replacement, map);
		}//---------------------------------------------
		
		/**
		 * @return the bindMap
		 */
		public Map<Object,Object>  getBindMap()
		{
			return bindMap;
		}
		/**
		 * @param bindMap the bindMap to set
		 */
		public void setBindMap(Map<Object,Object>  bindMap)
		{
			this.bindMap = bindMap;
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


		private String contentKey = Config.getProperty(getClass(),"formatting","content");
		private Map<Object,Object> bindMap = null;
		private boolean formatting= Config.getPropertyBoolean(getClass(),"formatting",false).booleanValue();
		//private String outputRootPath = Config.getProperty(getClass(),"outputRootPath","./output");
		private String regExp = "";
		private String replacement = "";

}
