package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PaperHandlingOptions.ManualDuplexMode;

public class ManualDuplexScanningDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";

    public class DemoTwainCallback extends TwainCallback
    {
        TwainSession twainSession = null;
        TwainSource twainSource = null;

        public DemoTwainCallback(TwainSession session, TwainSource source)
        {
            twainSession = session;
            twainSource = source;
        }

        private void printInfo(long sourceHandle, String message)
        {
            System.out.println(message + twainSource.getInfo().getProductName());
        }

        @Override
        public int onUIOpened(long sourceHandle)
        {
            printInfo(sourceHandle, " Please place front side to be scanned (if not already placed in scanner)");
            return 1;
        }

        @Override
        public int onManDupSide1Start(long sourceHandle)
        {
            printInfo(sourceHandle, " Scanning front side of the page(s)");
            return 1;
        }

        @Override
        public int onManDupSide1Done(long sourceHandle)
        {
            printInfo(sourceHandle, " Scanned front of the page(s)");
            return 1;
        }

        @Override
        public int onManDupSide2Start(long sourceHandle)
        {
            printInfo(sourceHandle, " Ready to scan back of the page(s)");
            return 1;
        }

        @Override
        public int onManDupSide2Done(long sourceHandle)
        {
            printInfo(sourceHandle, " Scanned back of the page(s)");
            return 1;
        }
    }

    // Simple acquire to a file
    public void VerySimpleTest() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // We don't care if the device has a duplexer, we can still scan duplex
            // enable manual duplex mode
            AcquireCharacteristics ac = ts.getAcquireCharacteristics();
            ac.getPaperHandlingOptions().setManualDuplexMpde(ManualDuplexMode.FACEUPBOTTOMFEED);

            // Set the file acquire options. The file will be multipage TIFF-LZW format.
            // All pages acquired in a single acquisition will be saved to a multipage TIFF file.
            ac.getFileTransferOptions().
               setType(FileType.TIFFLZWMULTI).
               setName(outDir + "testMultiPageLZW.tif");

            // Activate the call back
            DemoTwainCallback iCallback = new DemoTwainCallback(twainSession, ts);
            iCallback.activate();

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
        ManualDuplexScanningDemo s = new ManualDuplexScanningDemo();
        try
        {
            s.VerySimpleTest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}