package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.highlevel.TwainLoggerCharacteristics;
import com.dynarithmic.twain.highlevel.TwainLoggerCharacteristics.LoggerVerbosity;
import com.dynarithmic.twain.highlevel.TwainLoggerCharacteristics.LoggingDestination;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class SimpleLoggingDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Set up logging
        TwainLoggerCharacteristics logging = twainSession.getLoggerCharacteristics();
        logging.setVerbosity(LoggerVerbosity.MINIMAL).
                setDestination(LoggingDestination.TOCONSOLE).enable(true);

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
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
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        SimpleLoggingDemo s = new SimpleLoggingDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}