package com.dtwain.demos;
import java.util.logging.Logger;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.SessionStartupMode;
import com.dynarithmic.twain.highlevel.TwainLogger;
import com.dynarithmic.twain.highlevel.TwainLoggerProc;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class LoggingDemo2
{
    private static Logger logger = Logger.getLogger(LoggingDemo2.class.getName());        
    public class JULLogger implements TwainLoggerProc
    {
        @Override
        public void Log(String sMsg) throws Exception 
        {
            logger.info(sMsg);
        }
    }
    
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";
     
    public void run() throws Exception
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion(getClass().getSimpleName());

        // Create a TWAIN Session without starting it.  We want to log 
        // what happens when TWAIN starts up
        TwainSession twainSession = new TwainSession(SessionStartupMode.NONE);
        twainSession.enableLogging(true);

        // Set up logging to java.util.logger
        TwainLogger logging = TwainSession.getLogger();
        logging.setVerbosity(TwainLogger.LoggerVerbosity.MAXIMUM).
                addLogger(new JULLogger());          // Log to the system console

        // Now start the session, which will also start the logger
        twainSession.start();
        
        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = EnhancedSourceSelector.selectSource(twainSession);
        if ( ts.isOpened() )
        {
            // Set the file acquire options. By default, the file will be in BMP format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setName(outDir + "test3.bmp");

            // Start the acquisition
            AcquireReturnInfo retInfo = ts.acquire();
            if ( retInfo.getReturnCode() == ErrorCode.ERROR_NONE )
                System.out.println("Acquisition Successful");
            else
                System.out.println("Acquisition Failed with error: " + retInfo.getReturnCode());
        }
        else
        {
            // See why no acquisition was done
            ErrorCode err = ts.getLastError();

            // The user didn't make a selection
            if (err == ErrorCode.ERROR_SOURCESELECTION_CANCELED)
                System.out.println("User closed the TWAIN dialog without selecting a data source");
            else
            // User selected, but something went wrong in opening the data source
                System.out.println("Source selection failed with error: " + err);
        }

        // Close down the TWAIN Session
        twainSession.stopLogging();
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        LoggingDemo2 s = new LoggingDemo2();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}