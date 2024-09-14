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
                    sourceHandle.setTiffCompressType(FileType.TIFFG4);
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