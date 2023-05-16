package com.dtwain.demos;

import java.io.ByteArrayOutputStream;

import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.BufferedStripInfo;
import com.dynarithmic.twain.highlevel.BufferedTransferInfo;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class BufferedAcquisitionAdvancedDemo
{
    public class BufferedDemoCallback extends TwainCallback
    {
        TwainSession twainSession = null;
        TwainSource twainSource = null;
        int currentStrip = 0;
        long totalImageSize = 0;
        final ByteArrayOutputStream m_compressedStream = new ByteArrayOutputStream();

        public BufferedDemoCallback(TwainSession ts, TwainSource tSource)
        {
            twainSession = ts;
            twainSource = tSource;
        }

        public void appendBytesToCompressedStream(byte [] b)
        {
            for (int i = 0; i < b.length; ++i)
                m_compressedStream.write(b[i]);
        }

        public ByteArrayOutputStream getStream()
        {
            return m_compressedStream;
        }

        @Override
        public int onTransferReady(long sourceHandle)
        {
            System.out.println("Ready to transfer...");
            totalImageSize = 0;
            currentStrip = 0;
            return 1;
        }

        @Override
        public int onTransferStripReady(long sourceHandle)
        {
            System.out.println("Ready to transfer strip...");
            return 1;
        }

        @Override
        public int onTransferStripDone(long sourceHandle)
        {
            BufferedStripInfo stripInfo = null;
            try
            {
                stripInfo = twainSource.getBufferedStripInfo();
            }
            catch (DTwainJavaAPIException e)
            {
                e.printStackTrace();
            }
            if (stripInfo != null)
            {
                // Add this strip's data to our stream
                ++currentStrip;
                System.out.println("Strip " + currentStrip + "   Transferred strip.  Strip size:  " + stripInfo.getBytesWritten());
                totalImageSize += stripInfo.getBytesWritten();
                appendBytesToCompressedStream(stripInfo.getBufferedStripData());
            }
            return 1;
        }

        @Override
        public int onTransferDone(long sourceHandle)
        {
            System.out.println("Transfer done.  Total bytes: " + totalImageSize);
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
        if ( ts.isOpened() )
        {
            // Twain Callback
            BufferedDemoCallback iCallback = new BufferedDemoCallback(twainSession, ts);
            iCallback.activate();

            // Set the acquire options. We want to acquire using Buffered mode.
            ts.getAcquireCharacteristics().getGeneralOptions().setAcquireType(AcquireType.BUFFERED);

            // Set the information we would like to use for the buffered transfer
            BufferedTransferInfo bufInfo = ts.getBufferedTransferInfo();
            bufInfo.setHandleStrips(true).  // We will handle the strips
            setBufferSize(bufInfo.getMaximumBufferSize()); // Set the buffer size to the maximum

            // Start the acquisition
            AcquireReturnInfo retInfo = ts.acquire();
            if ( retInfo.getReturnCode() == ErrorCode.ERROR_NONE )
            {
                System.out.println("Acquisition Successful");
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
        BufferedAcquisitionAdvancedDemo s = new BufferedAcquisitionAdvancedDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}