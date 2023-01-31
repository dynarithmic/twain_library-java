package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.highlevel.TwainLoggerCharacteristics.LoggerVerbosity;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class SimpleFileAcquireTIFFLZWDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";
    // Simple acquire to a file
    public void VerySimpleTest() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();
        twainSession.getLoggerCharacteristics().enable(true).setVerbosity(LoggerVerbosity.HIGH);

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // Set the file acquire options. By default, the file will be in TIFF-LZW format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setType(FileType.TIFFLZW).
               setName(outDir + "testTIF.tif");

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
        SimpleFileAcquireTIFFLZWDemo s = new SimpleFileAcquireTIFFLZWDemo();
        try
        {
            s.VerySimpleTest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}