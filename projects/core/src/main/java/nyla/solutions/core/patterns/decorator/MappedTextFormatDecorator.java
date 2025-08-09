package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Mapped;
import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Text;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static nyla.solutions.core.util.Config.settings;


/**
 * This object using a FreeMarker. The place-holder values are obtained from
 * text decorators. The Decorator can be set by ServiceFactory object.
 * 
 * @author Gregory Green
 *
 */
public class MappedTextFormatDecorator implements Mapped<String,Textable>, Textable
{
    private String templateUrl = settings().getProperty(MappedTextFormatDecorator.class.getName()+".templateUrl","");
    private Map<String,Textable> map = new Hashtable<String,Textable>();
    private String template = settings().getProperty(MappedTextFormatDecorator.class.getName()+".template","");

    public MappedTextFormatDecorator()
    {}

    public MappedTextFormatDecorator(Map<String, Textable> map, String template)
    {
        this.map = map;
        this.template = template;
    }

    /**
    * 
    *
    * @see nyla.solutions.core.data.Mapped#getMap()
    */
   public Map<String,Textable> getMap()
   {      
      return map;
   }
    /**
    * 
    *asset all values are Textable
    * @see nyla.solutions.core.data.Mapped#setMap(java.util.Map)
    */
   @Override
   public void setMap(Map<String,Textable> map)
   {
      if(map == null)
         throw new RequiredException("map in MappedTextFormatDecorator.setMap");
      
      //asset all values are Textable
      
      //set map
      this.map = map;

   }//--------------------------------------------
    /**
    * 
    * @return the freemarker template
    * @throws IOException
    */
    protected String getTemplate()
   throws IOException
   {
	   if(this.template != null && this.template.length() > 0)
		   return template;
	   
	    if(templateUrl == null || templateUrl.length() == 0)         
	          throw new RequiredException("templateUrl in MappedTextFormatDecorator.getText");
	    
	    
	    return IO.readURL(templateUrl);
   }//---------------------------------------------
    /**
    * Convert get text output from each Textable in map.
    * Return the format output using Text.format.
    * 
    * Note the bind template is retrieved from
    * the URL provided in templateUrl.
    *
    * @see nyla.solutions.core.data.Textable#getText()
    */
   public String getText()
   {
      
     //convert textable to map of text
	   Object key = null;
      try 
      {
    	  //read bindTemplate
         String bindTemplate = getTemplate();
         Map<Object,String> textMap = new Hashtable<Object,String>();
    
         for(Map.Entry<String, Textable> entry: map.entrySet())
         {
            key = entry.getKey();

            if(!bindTemplate.contains(String.valueOf(key)))
                continue; //skip

            try
			{
				//convert to text
				textMap.put(key,(entry.getValue()).getText());
			}
			catch (Exception e)
			{
				throw new SystemException("Unable to build text for key:"+key+" error:"+e.getMessage(),e);
			}         
         }
         
         String formattedOutput = Text.format(bindTemplate, textMap);
         return formattedOutput;
         
      } 
      catch (RuntimeException e) 
      {
         throw e;
      }
      catch (Exception e) 
      {
         throw new SetupException(e.getMessage(),e);
      }
   }//--------------------------------------------   
    /**
    * @return the templateUrl
    */
   public String getTemplateUrl()
   {
      return templateUrl;
   }//--------------------------------------------

   /**
    * @param templateUrl the templateUrl to set
    */
   public void setTemplateUrl(String templateUrl)
   {
      if (templateUrl == null || templateUrl.length() == 0)
         throw new IllegalArgumentException(
         "templateUrl required in setTemplateUrl");

      this.templateUrl = templateUrl;
   }//--------------------------------------------

   /**
	 * @param template the template to set
	 */
	public void setTemplate(String template)
	{
		this.template = template;
	}
}
