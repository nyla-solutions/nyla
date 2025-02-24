package nyla.solutions.commas;

import java.io.Serializable;
import java.util.Arrays;

import nyla.solutions.core.patterns.transaction.Transactional;


/**
 * <pre>
 * Contains meta on how functions should be executed.
 *
 *
</pre>
 * @author Gregory Green
 *
 */
public class CommandFacts implements Serializable, Transactional
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4219290774337042571L;
	/**
	 * @return the functionName
	 */
	public String getName()
	{
		return commandName;
	}
	/**
	 * @param commandName the commandName to set
	 */
	public void setCommandName(String commandName)
	{
		this.commandName = commandName;
	}
	
	/**
	 * Note the default input name is REQUEST
	 * @return the inputName (i.e. the request container name for CICS bridge calls)
	 */
	public String getInputName()
	{
		return inputName;
	}
	/**
	 * @param inputName the inputName to set (i.e. the request container name for CICS bridge calls)
	 */
	public void setInputName(String inputName)
	{
		this.inputName = inputName;
	}
	

	/**
	 * Note the default input name is RESPONSE
	 * @return the outputName (i.e. the response container name for CICS bridge calls)
	 */
	public String getOutputName()
	{
		return outputName;
	}

	/**
	 * @param outputName the outputName to set
	 */
	public void setOutputName(String outputName)
	{
		this.outputName = outputName;
	}

	/**
	 * Get pool name to use for onServer or onServers execution types
	 * @return the poolName the pool name
	 */
	public String getPoolName()
	{
		return poolName;
	}// -----------------------------------------------
	/**
	 * Set the pool name to use for onServer or onServers execution types
	 * @param poolName the poolName to set
	 */
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}// -----------------------------------------------

/*
	/**
	 * Set the name of the region for onRegion calls
	 * @param targetName the regionName to set
	 */
	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}// -----------------------------------------------

	

	/**
	 * Set the name of the region for onRegion calls
	 * @return the targetName
	 */
	public String getTargetName()
	{
		return targetName;
	}// -----------------------------------------------

	/**
	 * Set the transaction type of the function
	 * TransactionType.NONE - no transaction support
	 * TransactionType.READONLY- read (not write) transaction
	 * TransactionType.WRITE - read/write or read transaction data
	 */
	public void setTransactionType(TransactionType transactionType)
	{
		this.transactionType = transactionType;
	}// -----------------------------------------------
	/**
	 * Get the current transaction type
	 */
	public TransactionType getTransactionType()
	{
		return this.transactionType;
	}// -----------------------------------------------
	
	

	/**
	 * Command controller name
	 * @return the gridFunctionName
	 */
	public String getControllerLocation()
	{
		return gridFunctionName;
	}// -----------------------------------------------
	/**
	 * Set controller name
	 * @param gridFunctionName the gridFunctionName to set
	 */
	public void setControllerLocation(String gridFunctionName)
	{
		this.gridFunctionName = gridFunctionName;
	}


	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes)
	{
		this.notes = notes;
	}// -----------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commandName == null) ? 0 : commandName.hashCode());
		result = prime
				* result
				+ ((gridFunctionName == null) ? 0 : gridFunctionName.hashCode());
		result = prime * result
				+ ((inputName == null) ? 0 : inputName.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		//result = prime * result + Arrays.hashCode(onRegionFilterKeyFacts);
		result = prime * result
				+ ((outputName == null) ? 0 : outputName.hashCode());
		result = prime * result
				+ ((poolName == null) ? 0 : poolName.hashCode());
		result = prime * result
				+ ((targetName == null) ? 0 : targetName.hashCode());
		result = prime * result
				+ ((commasName == null) ? 0 : commasName.hashCode());
		result = prime * result
				+ ((transactionType == null) ? 0 : transactionType.hashCode());
		return result;
	}// -----------------------------------------------

	/* (non-Javadoc)
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
		CommandFacts other = (CommandFacts) obj;
		if (commandName == null)
		{
			if (other.commandName != null)
				return false;
		}
		else if (!commandName.equals(other.commandName))
			return false;
		if (gridFunctionName == null)
		{
			if (other.gridFunctionName != null)
				return false;
		}
		else if (!gridFunctionName.equals(other.gridFunctionName))
			return false;
		if (inputName == null)
		{
			if (other.inputName != null)
				return false;
		}
		else if (!inputName.equals(other.inputName))
			return false;
		if (notes == null)
		{
			if (other.notes != null)
				return false;
		}
		else if (!notes.equals(other.notes))
			return false;
		//if (!Arrays
		//		.equals(onRegionFilterKeyFacts, other.onRegionFilterKeyFacts))
			//return false;
		if (outputName == null)
		{
			if (other.outputName != null)
				return false;
		}
		else if (!outputName.equals(other.outputName))
			return false;
		if (poolName == null)
		{
			if (other.poolName != null)
				return false;
		}
		else if (!poolName.equals(other.poolName))
			return false;
		if (targetName == null)
		{
			if (other.targetName != null)
				return false;
		}
		else if (!targetName.equals(other.targetName))
			return false;
		if (commasName == null)
		{
			if (other.commasName != null)
				return false;
		}
		else if (!commasName.equals(other.commasName))
			return false;
		if (transactionType != other.transactionType)
			return false;
		return true;
	}// -----------------------------------------------

	/**
	 * @see java.lang.Object#toString()
	 */
	
	/**
	 * @return the serviceName
	 */
	public String getCommasName()
	{
		return commasName;
	}// -----------------------------------------------
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CommandFacts [simpleName=" + simpleName
				+ ", commandAttributes=" + Arrays.toString(commandAttributes)
				+ ", notes=" + notes + ", parameterTypeLengthCount="
				+ parameterTypeLengthCount + ", argumentClassInfo="
				+ argumentClassInfo + ", returnClassInfo=" + returnClassInfo
				+ ", commasName=" + commasName + ", inputName=" + inputName
				+ ", outputName=" + outputName + ", shortDescription="
				+ shortDescription + ", commandName=" + commandName
				+ ", regionName=" + targetName + ", poolName=" + poolName
				+ ", transactionType=" + transactionType
				+ ", gridFunctionName=" + gridFunctionName + "]";
	}
	
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setCommasName(String serviceName)
	{
		this.commasName = serviceName;
	}
	

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription()
	{
		return shortDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription)
	{
		this.shortDescription = shortDescription;
	}


	/**
	 * @return the parameterTypeLengthCount
	 */
	public int getParameterTypeLengthCount()
	{
		return parameterTypeLengthCount;
	}

	/**
	 * @param parameterTypeLengthCount the parameterTypeLengthCount to set
	 */
	public void setParameterTypeLengthCount(int parameterTypeLengthCount)
	{
		this.parameterTypeLengthCount = parameterTypeLengthCount;
	}



	/**
	 * @return the arg1ClassInfo
	 */
	public CatalogClassInfo getArgumentClassInfo()
	{
		return this.argumentClassInfo;
	}

	/**
	 * @param argumentClassInfo the parameter types to set
	 */
	public void setArgumentClassInfo(CatalogClassInfo argumentClassInfo)
	{
		this.argumentClassInfo = argumentClassInfo;
	}

	/**
	 * @return the returnClassInfo
	 */
	public CatalogClassInfo getReturnClassInfo()
	{
		return returnClassInfo;
	}

	/**
	 * @param returnClassInfo the returnClassInfo to set
	 */
	public void setReturnClassInfo(CatalogClassInfo returnClassInfo)
	{
		this.returnClassInfo = returnClassInfo;
	}


	
	/**
	 * @return the functionAttributes
	 */
	public CommandAttribute[] getCommandAttributes()
	{
		if(commandAttributes == null)
			return null;
		
		return commandAttributes.clone();
	}

	/**
	 * @param commandAttributes the commandAttributes to set
	 */
	public void setCommandAttributes(CommandAttribute[] commandAttributes)
	{
		if(commandAttributes == null)
			this.commandAttributes = null;
		else
			this.commandAttributes = commandAttributes.clone();
	}// --------------------------------------------------------
	
	/**
	 * @return the simpleName
	 */
	public String getSimpleName()
	{
		return simpleName;
	}
	/**
	 * @param simpleName the simpleName to set
	 */
	public void setSimpleName(String simpleName)
	{
		this.simpleName = simpleName;
	}

	private String simpleName;
	private CommandAttribute[] commandAttributes;
	private String notes = null;
	private int parameterTypeLengthCount;
	private CatalogClassInfo argumentClassInfo;
	private CatalogClassInfo returnClassInfo;
	private String commasName = "";
	private String inputName = "INPUT";
	private String outputName = "OUTPUT";
	private String shortDescription;
	private String commandName = null;
	private String targetName = null;
	private String poolName = "client";
	//private OnRegionFilterKeyFacts[] onRegionFilterKeyFacts =null;
	private TransactionType transactionType = TransactionType.WRITE;
	private String gridFunctionName = "commasFunctionController";

}
