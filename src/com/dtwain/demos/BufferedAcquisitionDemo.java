package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.highlevel.ImageHandler;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class BufferedAcquisitionDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // Set the acquire options. We want to acquire using Buffered mode.
            ts.getAcquireCharacteristics().getGeneralOptions().setAcquireType(AcquireType.BUFFERED);

            // For this example, we won't define our own buffered memory strip
            // See BufferedAcquisitionAdvancedDemo.java to show defining your own memory strip

            // Start the acquisition
            AcquireReturnInfo retInfo = ts.acquire();
            if ( retInfo.getReturnCode() == ErrorCode.ERROR_NONE )
            {
                System.out.println("Acquisition Successful");

                // Now get the image data from the acquisition.  The ImageHandler class
                // does this.
                ImageHandler iHandler = retInfo.getImageHandler();

                // Get the number of times user hit the "scan pages" indicator on the TWAIN device's
                // user interface. Note that this is the typical way most TWAIN devices with an
                // interface work.
                long count = iHandler.getNumAcquisitions();
                System.out.println("You probably hit the scan button " + count + " times.");
                if ( count == 0 )
                    twainSession.stop(); // Just stop the session and return

                // Now for each time a scan was done, get the number of pages.
                // Note that devices with a document feeder allows you to scan multiple pages
                // a multiple number of times before closing the user interface.
                for (int i = 0; i < count; ++i)
                {
                    // Get the number of images from scan attempt i
                    long numImages = iHandler.getNumImages(i);
                    System.out.println("For scan number " + (i+1) + ", you scanned " + numImages + " pages");

                    // Now get the image data.
                    // This for loop only shows how to get the image data.
                    // After the loop, we will send iHandler's information to a
                    // dialog that displays the image.
                    for (int j = 0; j < numImages; ++j)
                    {
                        // imageData is the actual raw bytes of the image.
                        // For TWAIN devices, by default this will always be
                        // a Windows BMP.  For now, we won't do anything inside
                        // the loop.
                        byte [] imageData = iHandler.getImageData(i, j);
                        // Now you can take the imageData and give it to your favorite
                        // Image handling code...
                        System.out.println("The image obtained contains " + imageData.length + " bytes of data");
                    }
                }
            }
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
        BufferedAcquisitionDemo s = new BufferedAcquisitionDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}