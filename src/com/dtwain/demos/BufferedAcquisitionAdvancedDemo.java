/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
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
        int currentStrip = 0;
        long totalImageSize = 0;
        final ByteArrayOutputStream m_compressedStream = new ByteArrayOutputStream();

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
        public int onTransferReady(TwainSource sourceHandle)
        {
            System.out.println("Ready to transfer...");
            totalImageSize = 0;
            currentStrip = 0;
            return 1;
        }

        @Override
        public int onTransferStripReady(TwainSource sourceHandle)
        {
            System.out.println("Ready to transfer strip...");
            return 1;
        }

        private void addStrip(TwainSource sourceHandle)
        {
            BufferedStripInfo stripInfo = null;
            try
            {
                stripInfo = sourceHandle.getBufferedStripInfo();
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
        }
        
        @Override
        public int onTransferStripDone(TwainSource sourceHandle)
        {
            addStrip(sourceHandle);
            return 1;
        }

        @Override
        public int onTransferDone(TwainSource sourceHandle)
        {
            addStrip(sourceHandle);
            System.out.println("Transfer done.  Total bytes: " + totalImageSize);
            return 1;
        }
    }

    public void run() throws Exception
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion(getClass().getSimpleName());
        
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // Twain Callback
            twainSession.registerCallback(ts, new BufferedDemoCallback());

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