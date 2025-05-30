package nyla.solutions.spring.batch;

import java.util.List;

import org.springframework.batch.core.listener.ItemListenerSupport;

import nyla.solutions.commas.Command;
import nyla.solutions.core.exception.fault.Fault;
import nyla.solutions.core.exception.fault.FaultService;

/**
 * The Spring Batch framework provides a flexible way to handle errors in a step. 
 * Batch errors will be caught by an implementation of the Spring Item listener 
 * (ErrorListener will implement the ItemListenerSupport Java interface). 
 * It will catch errors on read, process and on write operations. 
 * The detailed exceptions will be emailed to a support email distribution list.
 * 
 * @author Gregory Green
 * @param <O> Output Type
 * @param <I> Input Type
 *
 */
public class SpringBatchErrorListener<O, I> extends ItemListenerSupport<I, O>
{

	/**
	 * @see org.springframework.batch.core.listener.ItemListenerSupport#onProcessError(java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void onProcessError(I item, Exception e)
	{
		this.processErrorCmd.execute(this.faultService.raise(e, item));
	}// --------------------------------------------------------

	/**
	 * @see org.springframework.batch.core.listener.ItemListenerSupport#onReadError(java.lang.Exception)
	 */
	@Override
	public void onReadError(Exception ex)
	{
		this.readErrorCmd.execute(this.faultService.raise(ex));
	}

	/**
	 * @see org.springframework.batch.core.listener.ItemListenerSupport#onWriteError(java.lang.Exception, java.util.List)
	 */
	@Override
	public void onWriteError(Exception ex, List<? extends O> item)
	{
		this.writeErrorCmd.execute(this.faultService.raise(ex,item));
	}

	/**
	 * @return the readErrorCmd
	 */
	public Command<O, Fault> getReadErrorCmd()
	{
		return readErrorCmd;
	}

	/**
	 * @param readErrorCmd the readErrorCmd to set
	 */
	public void setReadErrorCmd(Command<O, Fault> readErrorCmd)
	{
		this.readErrorCmd = readErrorCmd;
	}

	/**
	 * @return the processErrorCmd
	 */
	public Command<?, Fault> getProcessErrorCmd()
	{
		return processErrorCmd;
	}

	/**
	 * @param processErrorCmd the processErrorCmd to set
	 */
	public void setProcessErrorCmd(Command<O, Fault> processErrorCmd)
	{
		this.processErrorCmd = processErrorCmd;
	}

	/**
	 * @return the writeErrorCmd
	 */
	public Command<O, Fault> getWriteErrorCmd()
	{
		return writeErrorCmd;
	}

	/**
	 * @param writeErrorCmd the writeErrorCmd to set
	 */
	public void setWriteErrorCmd(Command<O, Fault> writeErrorCmd)
	{
		this.writeErrorCmd = writeErrorCmd;
	}

	/**
	 * @return the faultService
	 */
	public FaultService getFaultService()
	{
		return faultService;
	}

	/**
	 * @param faultService the faultService to set
	 */
	public void setFaultService(FaultService faultService)
	{
		this.faultService = faultService;
	}

	private Command<O, Fault>  readErrorCmd;
	private Command<O, Fault> processErrorCmd;
	private Command<O, Fault>  writeErrorCmd;
	private FaultService faultService;
	
}
