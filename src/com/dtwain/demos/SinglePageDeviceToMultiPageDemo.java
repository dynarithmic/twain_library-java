package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.DTwainConstants.MultipageSaveMode;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;

public class SinglePageDeviceToMultiPageDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // Get the acquire characteristics
            AcquireCharacteristics ac = ts.getAcquireCharacteristics();

            // Set the file acquire options. The file will be multipage TIFF-LZW format.
            // All pages acquired in a single acquisition will be saved to a multipage TIFF file.
            ac.getFileTransferOptions().
               setType(FileType.TIFFLZWMULTI).
               setName(outDir + "testMultiPageLZW.tif").
               // set the multipage save option to save the file when the source UI
               // is closed
               getMultipageSaveOptions().setSaveMode(MultipageSaveMode.ONUICLODE);

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

    // This demo will show saving to a multipage file using a device that can only
    // acquire a single page (for example, a flatbed scanner)
    public static void main(String [] args)
    {
        SinglePageDeviceToMultiPageDemo s = new SinglePageDeviceToMultiPageDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}