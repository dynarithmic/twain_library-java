/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

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

import com.dynarithmic.twain.DTwainConstants.BlankPageDiscardOption;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.highlevel.EnhancedSourceSelector;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.BlankPageHandlingOptions;

public class DiscardBlankPagesDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "";

    // This callback and members functions are invoked whenever a blank page is detected
    // from the scanner.
    public class BlankPageCallback extends TwainCallback
    {
        @Override
        public int onBlankPageDetectedOriginalImage(TwainSource sourceHandle)
        {
            System.out.println("Detected blank page from device");

            // If you want to keep this page, return BlankPageHandlingOptions.KEEPPAGE
            return BlankPageHandlingOptions.DISCARDPAGE;
        }

        public int onBlankPageDetectedAdjustedImage(TwainSource sourceHandle)
        {
            System.out.println("Detected blank page after adjusting image");

            // If you want to keep this page, return BlankPageHandlingOptions.KEEPPAGE
            return BlankPageHandlingOptions.DISCARDPAGE;
        }
    }

    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = EnhancedSourceSelector.selectSource(twainSession);

        if ( ts.isOpened() )
        {
            // Activate the call back
            twainSession.registerCallback(ts, new BlankPageCallback());

            // Set the file acquire options. By default, the file will be in TIFF-LZW format
            AcquireCharacteristics ac = ts.getAcquireCharacteristics();
            ac.getFileTransferOptions().
               setType(FileType.TIFFLZWMULTI).
               setName(outDir + "testTIFBlanks.tif");

            // Set the blank page handling options
            ac.getBlankPageHandlingOptions().enable(true).setDiscardOption(BlankPageDiscardOption.AUTODISCARD_ANY);

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
        DiscardBlankPagesDemo demo = new DiscardBlankPagesDemo();
        try
        {
            demo.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}