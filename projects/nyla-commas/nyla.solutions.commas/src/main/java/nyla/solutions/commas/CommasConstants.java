package nyla.solutions.commas;

import nyla.solutions.core.util.Config;

/**
 * @author Gregory Green
 *
 */
public interface CommasConstants
{
	/**
	 * ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME = "isAdvisedSkipMethodInvoke"
	 * 
	 * CommandAttribute advisedSkipMethodInvoke = new CommandAttribute(
				CommasProxyCommand.ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME, 
				Boolean.class.getName(), Boolean.TRUE.toString());
	 */
	public static final String ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME = "isAdvisedSkipMethodInvoke";
	
	/**
	 * Used to override ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME
	 *  ALWAYS_EXECUTE_METHOD_HEADER = "alwayExecuteMethod
	 */
	public final static String ALWAYS_EXECUTE_METHOD_HEADER = "alwayExecuteMethod";
	
	/**
	 * nameSeparator = Config.getProperty(CommasServiceFactory.class,"nameSeparator",".")
	 */
	public final static String nameSeparator = Config.getProperty("nyla.sreaderolutions.global.patterns.command.commas.CommasServiceFactory.nameSeparator",".");
	
	/**
	 * DEFAULT_PACKAGE_ROOT = "solutions.gedi.commas"
	 */
	public static final String DEFAULT_PACKAGE_ROOT = "nyla.solutions.global.patterns.command.commas";
	
	/**
	 * DEFAULT_BRIDGE_FUNCTION_NAME = "commasBridgeFunction"
	 */
	public static final String DEFAULT_COMMAS_FUNCTION_NAME = "commasFunction";
	
	public static final String COMMAND_NAME_HEADER = "cmd";
	
	/**
	 * VERSION_HEADER = "version"
	 */
	public static final String VERSION_HEADER = "version";

	/**
	 * ROOT_SERVICE_NAME = ""
	 */
	public static final String ROOT_SERVICE_NAME = "";
	/**
	 * CONTENT_TYPE_HEADER = "ContentType"
	 */
	public static final String CONTENT_TYPE_HEADER = "ContentType";
	

}
