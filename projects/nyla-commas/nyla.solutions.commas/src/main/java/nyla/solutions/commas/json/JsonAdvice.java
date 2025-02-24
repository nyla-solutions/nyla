package nyla.solutions.commas.json;

import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommandFacts;
import nyla.solutions.commas.annotations.Aspect;
import nyla.solutions.commas.aop.Advice;

@Aspect(name=JsonAdvice.JSON_ADVICE_NAME)
public class JsonAdvice implements Advice
{
	public static final String JSON_ADVICE_NAME  = "jsonAdvice";
	
	public Command<?,?> getBeforeCommand()
	{
		return new JsonBeforeCommand();
	}

	public Command<?,?> getAfterCommand()
	{
		return new JsonAfterCommand();
	}

	public CommandFacts getFacts()
	{
		return facts;
	}

	public void setFacts(CommandFacts facts)
	{
		this.facts = facts;
	}

	private CommandFacts facts;
}
