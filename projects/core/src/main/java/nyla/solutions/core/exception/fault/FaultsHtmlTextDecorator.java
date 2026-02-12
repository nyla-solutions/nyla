package nyla.solutions.core.exception.fault;

import nyla.solutions.core.exception.NotImplementedException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.decorator.TextDecorator;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * Creates and html table for the fault collection
 * @author Gregory Green
 *
 */
public class FaultsHtmlTextDecorator implements TextDecorator<Collection<Fault>>
{
	/**
	 * FAULT_HTML_ROW_TEMPLATE = "FaultsHtmlTextDecorator_ROW"
	 */
	public static final String HTML_ROW_FAULT_TEMPLATE = "FaultsHtmlTextDecorator_ROW";
	
	/**
	 * FaultsHtmlTextDecorator_TEMPLATE = "FaultsHtmlTextDecorator"
	 */
	public static final String FaultsHtmlTextDecorator_TEMPLATE = "FaultsHtmlTextDecorator";
	
	public FaultsHtmlTextDecorator(Collection<Fault> faults)
	{
		this.faults = faults;
	}

	@Override
	public String getText()
	{
		if(this.faults == null || this.faults.isEmpty())
			return null;
		
		FaultFormatTextDecorator decorator = new FaultFormatTextDecorator();
		decorator.setTemplateName(HTML_ROW_FAULT_TEMPLATE);
		
		StringBuilder faultsTableRows = new StringBuilder(); 
		for (Fault fault : faults)
		{
			if(fault == null)
				continue;
			
			decorator.setTarget(fault);
			faultsTableRows.append(decorator.getText());
		}
		
		if(faultsTableRows.length() == 0)
			return null;

			Map<Object, Object> map = new HashMap<Object,Object>();
			map.put("faultsTableRows", faultsTableRows.toString());
			
			return nyla.solutions.core.util.Text.format().formatFromTemplate(FaultsHtmlTextDecorator_TEMPLATE, map, Locale.US);

	}

	@Override
	public void setTarget(Collection<Fault> target)
	{
		throw new NotImplementedException();
	}

	@Override
	public Collection<Fault> getTarget()
	{
		return faults;
	}
	
	private final Collection<Fault> faults;
	
	//private static final String faultTableHeader = Config.getProperty("<table><thead><tr><th>Code</th><th>Module</th><th>Message</th><th>Category</th><th>Operation</th><th>Category</th>)

}
