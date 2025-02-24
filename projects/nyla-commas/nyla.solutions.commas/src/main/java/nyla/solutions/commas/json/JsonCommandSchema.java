package nyla.solutions.commas.json;

import java.io.Serializable;

import nyla.solutions.commas.CatalogClassInfo;
import nyla.solutions.commas.CommandFacts;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.reflection.ClassSchema;

/**
 * Includes the a JSON sample input/output along with the CommandFacts
 * @author Gregory Green
 *
 */
public class JsonCommandSchema implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5038710981586345153L;
	
	/**
	 * 
	 * @param facts the command facts
	 */
	public JsonCommandSchema(CommandFacts facts)
	{
		if(facts == null)
			throw new RequiredException("facts");
		
		try
		{
			this.commandFacts = facts;
						
			CatalogClassInfo argCatalogClass = facts.getArgumentClassInfo();
			
			//process input
			ClassSchema argClassSchema = argCatalogClass.getClassSchema();
			
			JsonExampleVistor argVisitor = new JsonExampleVistor();
			
			argClassSchema.accept(argVisitor);
			
			this.jsonInput = argVisitor.getJsonStringBuilder().toString();
			
			//process output
			CatalogClassInfo returnCatalogClassInfo = facts.getReturnClassInfo();
			
			if(returnCatalogClassInfo != null)
			{
				ClassSchema returnClassSchema = returnCatalogClassInfo.getClassSchema();
				
				if(returnClassSchema != null)
				{
					
					//create JSON for return
					JsonExampleVistor returnVisitor = new JsonExampleVistor();
					
					returnVisitor.visitClass(returnClassSchema);
					
					this.jsonOutput = returnVisitor.getJsonStringBuilder().toString();
				}
				else
					this.jsonOutput = null;
			}
			else
				this.jsonOutput = null;
			
		}		
		catch (Exception e)
		{
			throw new SystemException(e);
		}
	}// --------------------------------------------------------
	
	/**
	 * @return the jsonOutput
	 */
	public String getJsonOutput()
	{
		return jsonOutput;
	}
	/**
	 * @return the jsonInput
	 */
	public String getJsonInput()
	{
		return jsonInput;
	}
	/**
	 * @return the commandFacts
	 */
	public CommandFacts getCommandFacts()
	{
		return commandFacts;
	}

	//private static JSON JSON = JSON.newInstance();
	private final String jsonOutput;
	private final String jsonInput;
	private final CommandFacts commandFacts;
}
