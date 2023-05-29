package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class MultiPageTIFFCompressionDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "";
    public class TIFFCallback extends TwainCallback
    {
        TwainSession twainSession = null;
        TwainSource twainSource = null;
        int page;
        public TIFFCallback() 
        {
            page = 1;
        }

        // Called when device is ready to acquire the image (but hasn't yet done so)
        @Override
        public int onTransferReady(TwainSource sourceHandle)
        {
            // If the page is an odd number, use LZW compression, 
            // else use Group 4 FAX compression
            try
            {
                if ( page % 2 == 1 )
                    sourceHandle.setTiffCompressType(FileType.TIFFLZW);
                else
                    twainSource.setTiffCompressType(FileType.TIFFG4);
                ++page;
            }
            catch (DTwainJavaAPIException e)
            {
                System.out.println(e);
            }
            return 1;
        }
    }

    // Acquire to a multipage TIFF.  The first page will be compressed
    // depending on the page's bit-per-pixel
    //
    // This will demonstrate the ability to create multipage TIFF files,
    // where each page can have a different compression type.
    // For example, if the page will be 1 bpp, a TIFFG3 or TIFFG4 may
    // be preferred, and for color/grayscale, a TIFF-LZW
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // TwainCallback

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            twainSession.registerCallback(ts, new TIFFCallback());
            
            // Set the file acquire options. By default, the file will be in TIFF-LZW format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setType(FileType.TIFFLZWMULTI). // We start out with multi-page TIFFLZW
               setName(outDir + "testTIFDifferentCompressions.tif");

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
        MultiPageTIFFCompressionDemo s = new MultiPageTIFFCompressionDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}