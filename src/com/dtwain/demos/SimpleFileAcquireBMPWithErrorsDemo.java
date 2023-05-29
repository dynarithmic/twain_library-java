package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class SimpleFileAcquireBMPWithErrorsDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "";

    // This callback reports any errors if the image file could not be created
    public class FileCreationCallback extends TwainCallback
    {
        @Override
        public int onFileSaveError(TwainSource sourceHandle)
        {
            try
            {
                TwainSession session = getTwainSession();
                String sMsg = " Transfer failed.  Error: " + session.getErrorString(session.getLastError());
                System.out.println(sMsg);
            }
            catch (DTwainJavaAPIException e)
            {
                System.out.println("Unknown error when getting the error code");
            }
            return 1;
        }

        @Override
        public int onFileSaveOk(TwainSource sourceHandle)
        {
            String sMsg = " Transfer successful: " + sourceHandle.getAcquireCharacteristics().getFileTransferOptions().getName();
            System.out.println(sMsg);
            return 1;
        }
    }

    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();

        // TwainCallback that monitors image file creation errors
        twainSession.registerCallback(ts, new FileCreationCallback());
        if ( ts.isOpened() )
        {
            // Set the file acquire options. By default, the file will be in BMP format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setName(outDir + "test_" + getClass().getName() + ".bmp").getTransferFlags().autoCreateDirectory(true);


            // Start the acquisition
            AcquireReturnInfo retInfo = ts.acquire();
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
        SimpleFileAcquireBMPWithErrorsDemo s = new SimpleFileAcquireBMPWithErrorsDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}