package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class SimpleTwainCallbackDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";
    public class DemoTwainCallback extends TwainCallback
    {
        TwainSession twainSession = null;
        public DemoTwainCallback(TwainSession ts)
        {
            twainSession = ts;
        }

        private void printInfo(long sourceHandle, String message)
        {
            TwainSource ts;
            try
            {
                ts = new TwainSource(twainSession, sourceHandle);
                System.out.println(message + ts.getInfo().getProductName());
            }
            catch (DTwainJavaAPIException e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public int onTransferDone(long sourceHandle)
        {
            printInfo(sourceHandle, " Transfer done ");
            return 1;
        }

        @Override
        public int onTransferReady(long sourceHandle)
        {
            printInfo(sourceHandle, " Transfer ready ");
            return 1;
        }

        @Override
        public int onTransferCancelled(long sourceHandle)
        {
            printInfo(sourceHandle, " Transfer cancelled ");
            return 1;
        }

        @Override
        public int onUIOpened(long sourceHandle)
        {
            printInfo(sourceHandle, " UI is opened ");
            return 1;
        }

        @Override
        public int onUIClosing(long sourceHandle)
        {
            printInfo(sourceHandle, " UI is closing ");
            return 1;
        }

        @Override
        public int onUIClosed(long sourceHandle)
        {
            printInfo(sourceHandle, " UI is closed ");
            return 1;
        }
    }

    // Simple acquire to a file
    public void testCallback() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // TwainCallback
        DemoTwainCallback iCallback = new DemoTwainCallback(twainSession);
        iCallback.activate();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // Set the file acquire options. By default, the file will be in BMP format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setName(outDir + "test3.bmp");

            ts.getAcquireCharacteristics().getPaperHandlingOptions().enableFeeder(true);

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
        SimpleTwainCallbackDemo s = new SimpleTwainCallbackDemo();
        try
        {
            s.testCallback();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}