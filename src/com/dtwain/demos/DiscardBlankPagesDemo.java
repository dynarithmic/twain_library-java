package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.BlankPageDiscardOption;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.BlankPageHandlingOptions;

public class DiscardBlankPagesDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";

    // This callback and members functions are invoked whenever a blank page is detected
    // from the scanner.
    public class BlankPageCallback extends TwainCallback
    {
        TwainSession twainSession = null;
        TwainSource twainSource = null;

        public BlankPageCallback(TwainSession session, TwainSource source)
        {
            twainSession = session;
            twainSource = source;
        }

        @Override
        public int onBlankPageDetectedOriginalImage(long sourceHandle)
        {
            System.out.println("Detected blank page from device");

            // If you want to keep this page, return BlankPageHandlingOptions.KEEPPAGE
            return BlankPageHandlingOptions.DISCARDPAGE;
        }

        public int onBlankPageDetectedAdjustedImage(long sourceHandle)
        {
            System.out.println("Detected blank page after adjusting image");

            // If you want to keep this page, return BlankPageHandlingOptions.KEEPPAGE
            return BlankPageHandlingOptions.DISCARDPAGE;
        }
    }

    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();

        if ( ts.isOpened() )
        {
            // Activate the call back
            BlankPageCallback iCallback = new BlankPageCallback(twainSession, ts);
            iCallback.activate();

            // Set the file acquire options. By default, the file will be in TIFF-LZW format
            AcquireCharacteristics ac = ts.getAcquireCharacteristics();
            ac.getFileTransferOptions().
               setType(FileType.TIFFLZWMULTI).
               setName(outDir + "testTIFBlanks.tif");

            // Set the blank page handling options
            ac.getBlankPageHandlingOptions().enable(true).setDiscardOption(BlankPageDiscardOption.AUTODISCARD_ANY);

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
        DiscardBlankPagesDemo demo = new DiscardBlankPagesDemo();
        try
        {
            demo.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}