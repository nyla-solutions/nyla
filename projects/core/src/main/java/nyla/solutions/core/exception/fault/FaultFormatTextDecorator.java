package nyla.solutions.core.exception.fault;

import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.patterns.decorator.TextDecorator;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;

import java.util.Map;


/**
 * TextDecorator that converts the target fault into a formatted text.
 * @author Gregory Green
 *
 */
public class FaultFormatTextDecorator implements TextDecorator<Fault>
{
	/**
	 * Default construtor
	 */
	public FaultFormatTextDecorator(){}
	
	/**
	 * 
	 * @param target the fault format
	 * @param templateName the template name
	 */
	public FaultFormatTextDecorator(Fault target, String templateName)
	{
		this.target = target;
		this.templateName = templateName;
	}//------------------------------------------------
	
	/**
	 * Converts the target fault into a formatted text.
	 * @see nyla.solutions.core.data.Textable#getText()
	 */
	@Override
	public String getText()
	{
		if(this.target == null)
			return null;
		
			try
			{
				
				//Check if load of template needed
				if((this.template == null || this.template.length() == 0) && this.templateName != null)
				{
					try
					{
						this.template = Text.format().loadTemplate(templateName);
					}
					catch(Exception e)
					{
						throw new SetupException("Cannot load template:"+templateName,e);
					}
				}
				
				Map<Object,Object> faultMap = JavaBean.toMap(this.target);
				
				
				if(this.argumentTextDecorator != null)
				{
					this.argumentTextDecorator.setTarget(this.target.getArgument());
					
					faultMap.put(this.argumentKeyName, this.argumentTextDecorator.getText());
				}
				
				return Text.format().formatTemplate(this.template, faultMap);
			}
			catch (FormatException e)
			{
				throw new FormatFaultException(this.template, e);
			}

		
	}// --------------------------------------------------------


	@Override
	public void setTarget(Fault target)
	{
		this.target = target;
		
	}// --------------------------------------------------------

	@Override
	public Fault getTarget()
	{
		return this.target;
	}// --------------------------------------------------------
	
	
	
	/**
	 * @return the argumentTextDecorator
	 */
	public TextDecorator<Object> getArgumentTextDecorator()
	{
		return argumentTextDecorator;
	}

	/**
	 * @param argumentTextDecorator the argumentTextDecorator to set
	 */
	public void setArgumentTextDecorator(TextDecorator<Object> argumentTextDecorator)
	{
		this.argumentTextDecorator = argumentTextDecorator;
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

	/**
	 * @return the argumentKeyName
	 */
	public String getArgumentKeyName()
	{
		return argumentKeyName;
	}

	/**
	 * @param argumentKeyName the argumentKeyName to set
	 */
	public void setArgumentKeyName(String argumentKeyName)
	{
		this.argumentKeyName = argumentKeyName;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName()
	{
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName)
	{
		this.templateName = Config.interpret(templateName);
	}
	

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((argumentKeyName == null) ? 0 : argumentKeyName.hashCode());
		result = prime
				* result
				+ ((argumentTextDecorator == null) ? 0 : argumentTextDecorator
						.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		result = prime * result
				+ ((templateName == null) ? 0 : templateName.hashCode());
		return result;
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FaultFormatTextDecorator other = (FaultFormatTextDecorator) obj;
		if (argumentKeyName == null)
		{
			if (other.argumentKeyName != null)
				return false;
		}
		else if (!argumentKeyName.equals(other.argumentKeyName))
			return false;
		if (argumentTextDecorator == null)
		{
			if (other.argumentTextDecorator != null)
				return false;
		}
		else if (!argumentTextDecorator.equals(other.argumentTextDecorator))
			return false;
		if (target == null)
		{
			if (other.target != null)
				return false;
		}
		else if (!target.equals(other.target))
			return false;
		if (template == null)
		{
			if (other.template != null)
				return false;
		}
		else if (!template.equals(other.template))
			return false;
		if (templateName == null)
		{
			if (other.templateName != null)
				return false;
		}
		else if (!templateName.equals(other.templateName))
			return false;
		return true;
	}


	private Fault target;
	private TextDecorator<Object> argumentTextDecorator;
	private String template;	
	private String templateName;
	private String argumentKeyName = "argument";
}
