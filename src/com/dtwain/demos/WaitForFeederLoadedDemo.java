package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PaperHandlingOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PaperHandlingOptions.FeederMode;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;

public class WaitForFeederLoadedDemo {

    public void run()  
    {
        try 
        {
            TwainSession session = new TwainSession();
            TwainSource source = session.selectSource();
            if ( source.isOpened())
            {
                CapabilityInterface ci = source.getCapabilityInterface();
                if (!ci.isPaperDetectableSupported())
                {
                    session.stop();
                    return;
                }
                AcquireCharacteristics ac = source.getAcquireCharacteristics();
                PaperHandlingOptions paperHandling = ac.getPaperHandlingOptions();
                
                // We will wait for 30 seconds for the feeder to be fed paper.  After 30 seconds
                // if no paper has been detected in the feeder, the flatbed will be used instead of the feeder.  
                //
                // If the device did not have a feeder, or if the automatic feeder could not detect if 
                // paper is loaded, then we would have just used the flatbed or the feeder, without waiting 30 seconds.
                //
                // To have an infinite wait time, use PaperHandlingOptions.waitinfinite in the call to
                // set_feederwait().
                paperHandling.enableFeeder(true).setFeederWaitTime(30).setFeederMode(FeederMode.FEEDER_FLATBED);
                
                // set the characteristics to acquire to a file.
                // Set to a TIFF-LZW file
                FileTransferOptions ftOpts = ac.getFileTransferOptions();
                ftOpts.setName("tif_from_wrapper.tif").setType(FileType.TIFFLZWMULTI);
                
                // We will only acquire 2 pages
                ac.getGeneralOptions().setMaxPageCount(2);
                
                source.acquire();
            }
        } 
        catch (DTwainJavaAPIException e) 
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        WaitForFeederLoadedDemo demo = new WaitForFeederLoadedDemo();
        demo.run();
    }

}
