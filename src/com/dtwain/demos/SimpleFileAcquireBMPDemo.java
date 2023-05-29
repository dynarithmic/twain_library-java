package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class SimpleFileAcquireBMPDemo
{
    // Change this to the output directory that fits your environment
    static public String outputDirectory = "";

    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // Set the file acquire options. By default, the file will be in BMP format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setName(outputDirectory + "test_" + getClass().getName() + ".bmp");

            // Start the acquisition
            AcquireReturnInfo retInfo = ts.acquire();

            // Note that the return code only indicates whether the acquisition processing
            // was started successfully.
            //
            // If there is an error in the actual creation of the
            // image file, please see the SimpleFileAcquireBMPWithErrorsDemo.java to see how
            // to handle the internal errors during the acquisition.
            if ( retInfo.getReturnCode() == ErrorCode.ERROR_NONE )
                System.out.println("Acquisition process started and ended successfully");
            else
                System.out.println("Acquisition process failed with error: " + retInfo.getReturnCode());
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
        SimpleFileAcquireBMPDemo s = new SimpleFileAcquireBMPDemo();

        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}