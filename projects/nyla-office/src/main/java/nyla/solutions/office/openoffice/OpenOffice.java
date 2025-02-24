package nyla.solutions.office.openoffice;

/*
 * import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeConnectionProtocol;
import org.artofsolving.jodconverter.office.OfficeManager;
import nyla.solutions.global.exception.RequiredException;
import nyla.solutions.global.util.Config;
*/

/**
 * Required settings
 * 

MUST run in 32 bit JVM!!!!!

java -d32 



Add the following to the library path

export LD_LIBRARY_PATH=/devtools/docMgmt/OpenOffice_SDK/runtime/boost/OpenOffice_SDK/macosx/lib:$LD_LIBRARY_PATH


Add the following to the classpath

export  C22=/Applications/OpenOffice.app/Contents/MacOS

Other  Environment Settings

OFFICE_PROGRAM_PATH=/Applications/OpenOffice.app/Contents/MacOS
OLDPWD=/devtools/docMgmt/OpenOffice_SDK
OO_SDK_BOOST_HOME=
OO_SDK_CAT_HOME=/bin
OO_SDK_CPP_HOME=/usr/bin
OO_SDK_HOME=/devtools/docMgmt/OpenOffice_SDK
OO_SDK_JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents
OO_SDK_MAKE_HOME=/usr/bin
OO_SDK_NAME=OpenOffice_SDK
OO_SDK_OFFICE_BIN_DIR=/Applications/OpenOffice.app/Contents/MacOS
OO_SDK_OFFICE_JAVA_DIR=/Applications/OpenOffice.app/Contents/MacOS/classes
OO_SDK_OFFICE_LIB_DIR=/Applications/OpenOffice.app/Contents/MacOS
OO_SDK_OUT=/devtools/docMgmt/OpenOffice_SDK/runtime/boost/OpenOffice_SDK
OO_SDK_SED_HOME=/usr/bin
OO_SDK_ZIP_HOME=/usr/bin
UNO_PATH=/Applications/OpenOffice.app/Contents/MacOS

**********************
PATH
/devtools/docMgmt/OpenOffice_SDK/bin:
/devtools/docMgmt/OpenOffice_SDK/runtime/boost/OpenOffice_SDK/MACOSXexample.out/bin:
/Applications/OpenOffice.app/Contents/MacOS
**********************
Manual Setting

If you do not want to use the script to set your working environment, you must set the appropriate environment variables yourself. The script sets the following environment variables:

Common Environment Variables

OO_SDK_NAME	See description above.
OO_SDK_OFFICE_BIN_DIR	The path to the program directory in the office installation (e.g. $OFFICE_HOME/program).
OO_SDK_OFFICE_LIB_DIR	The path to the office program directory in the office installtion (e.g. $OFFICE_HOME/program).
OO_SDK_OFFICE_JAVA_DIR	The path to the classes direcgtory in the office program directory where Java JARs are located (e.g. $OFFICE_HOME/program/classes).
CLASSPATH	 =$OO_SDK_OFFICE_JAVA_DIR/juh.jar; $OO_SDK_OFFICE_JAVA_DIR/jurt.jar; $OO_SDK_OFFICE_JAVA_DIR/ridl.jar; $OO_SDK_OFFICE_JAVA_DIR/unoloader.jar; [$OO_SDK_OFFICE_JAVA_DIR/unoil.jar;] $CLASSPATH
The classpath will be set or extended to the necessary jar files of the specified office installation.

OFFICE_PROGRAM_PATH	=$OFFICE_HOME/program
This variable is used to find, for example, the office type library and the UNO package deployment tool.

UNO_PATH	=$OFFICE_PROGRAM_PATH
This variable is used to ensure that the new C++ UNO bootstrap mechanism uses the configured office installation of the SDK. Normally the bootstrap mechanism finds the default office installation for the user on the system. This variable is optional but is set from the scripts to ensure a homogeneous environment. Especially useful during development where you might have more than one office installation installed.

Environment Variables for UNIX

Solaris

LD_LIBRARY_PATH	=$OO_SDK_OFFICE_LIB_DIR: $OO_SDK_HOME/(solsparc|solintel)/lib: $LD_LIBRARY_PATH
The LD_LIBRARY_PATH will be set or will be extended by the office library path, the platform dependent lib directory for several additional libraries.

PATH	=$OO_SDK_HOME/(solsparc|solintel)/bin:$OO_SDK_MAKE_HOME: $OO_SDK_ZIP_HOME: [$OO_SDK_CPP_HOME:] [$OO_SDK_JAVA_HOME/bin:] $OO_SDK_OFFICE_BIN_DIR: $PATH
The PATH variable will be extended by the paths for the SDK development tools, the compiler, the JDK, GNU make, the zip tool and the OO_SDK_OFFICE_BIN_DIR, where the compiler or the JDK are optional.

$OO_SDK_HOME/(solsparc|solintel)/lib, which are needed for linking.
Linux

LD_LIBRARY_PATH	=$OO_SDK_OFFICE_LIB_DIR: $SDK_HOME/linux/lib: $LD_LIBRARY_PATH
The LD_LIBRARY_PATH will be set or will be extended by the office library path, the platform dependent lib directory for some additional libraries.

PATH	=$OO_SDK_HOME/linux/bin:$OO_SDK_MAKE_HOME: $OO_SDK_ZIP_HOME: [$OO_SDK_CPP_HOME:] [$OO_SDK_JAVA_HOME/bin:] $OO_SDK_OFFICE_BIN_DIR: $PATH
The PATH variable will be extended by the paths for the SDK development tools, the compiler, the JDK, GNU make, the zip tool and the OO_SDK_OFFICE_BIN_DIR, where the compiler and the JDK are optional.

Based on OFFICE_HOME, the script additionally creates symbolic links to the public dynamic libraries of the office UNO runtime in $OO_SDK_HOME/linux/lib, which are needed for linking.
Environment Variables for Windows

PATH	=%OO_SDK_HOME%\windows\bin; %OO_SDK_MAKE_HOME%; %OO_SDK_ZIP_HOME%; [%OO_SDK_CLI_HOME%;] [%OO_SDK_CPP_HOME%;] [%OO_SDK_JAVA_HOME%\bin;] %OO_SDK_OFFICE_BIN_DIR%; %PATH%
The PATH variable will be extended by the paths for the SDK development tools, the compiler, the JDK, GNU make, the zip tool and the OO_SDK_OFFICE_BIN_DIR.

LIB	=%OO_SDK_HOME%\windows\lib; %LIB%
The LIB variable will be extended by the path to the import libraries that are necessary for Windows.

In addition to setting these environment variables, the script calls the "vcvar32.bat" batch file which is provided by the Microsoft Developer Studio to set the necessary environment variables for the compiler.


 * @author Gregory Green
 *
 */
@Deprecated
public class OpenOffice 
//implements Closeable
{
//	
//	private OpenOffice(String officeHome, long taskExecutionTimeout)
//	{
//		officeManager = new DefaultOfficeManagerConfiguration()
//		.setOfficeHome(officeHome)
//	      .setConnectionProtocol(OfficeConnectionProtocol.PIPE)
//	      .setPipeNames("office1", "office2")
//	      .setTaskExecutionTimeout(taskExecutionTimeout) //30000L
//	      .buildOfficeManager();		
//		
//		officeManager.start();
//		
//		 converter = new OfficeDocumentConverter(officeManager);
//		 
//		 
//	}// --------------------------------------------------------
//    public void convert(File inputFile, File outputFile)
//    {
//    	
//       
//        converter.convert(inputFile, outputFile);
//    }// --------------------------------------------------------
//    /**
//     * 
//     * @return get open instance of the OpenOFfice
//     */
//    public static synchronized OpenOffice open()
//    {
//    	if(instance == null)
//    	{
//    		if(officeHome == null || officeHome.length() == 0)
//    			throw new RequiredException(OpenOffice.class.getName()+".setOfficeHome(path) example:/Applications/OpenOffice.app/Contents");
//    			
//    		instance = new OpenOffice(officeHome,timeout);  		
//    	}
//    	
//    	return instance;
//    }// --------------------------------------------------------
//    
//    @Override
//    public synchronized void close() throws IOException
//    {
//    	if(this.officeManager != null)
//    	{
//    		this.officeManager.stop();
//    		
//    		this.officeManager = null;
//    		
//    		instance = null;
//    	}
//    	
//    }// --------------------------------------------------------
//
//    /**
//	 * @return the officeHome
//	 */
//	public static String getOfficeHome()
//	{
//		return officeHome;
//	}
//	/**
//	 * @param officeHome the officeHome to set
//	 */
//	public static void setOfficeHome(String officeHome)
//	{
//		OpenOffice.officeHome = officeHome;
//	}
//	/**
//	 * @return the timeout
//	 */
//	public static long getTimeout()
//	{
//		return timeout;
//	}
//	/**
//	 * @param timeout the timeout to set
//	 */
//	public static void setTimeout(long timeout)
//	{
//		OpenOffice.timeout = timeout;
//	}
//
//	private final OfficeDocumentConverter converter;
//	private  OfficeManager officeManager;
//    private static OpenOffice instance = null;
//    private static String officeHome = null;
//    private static long timeout = Config.getPropertyLong(OpenOffice.class,"timeout",300000L); //300,000 milliseconds = 5 minutes
//    

}
