/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

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
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;

public class DuplexScannerDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "";

    public void run() throws Exception
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion(getClass().getSimpleName());
        
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = EnhancedSourceSelector.selectSource(twainSession);
        if ( ts.isOpened() )
        {
            // See if this device can scan duplex
            boolean duplexSupported = ts.getCapabilityInterface().isDuplexSupported();
            if ( !duplexSupported )
            {
                System.out.println("Duplex is not supported for the device \"" + ts.getInfo().getProductName() + "\"");
                return;
            }
            System.out.println("Duplex is supported for the device \"" + ts.getInfo().getProductName() + "\"");

            // enable duplex mode
            AcquireCharacteristics ac = ts.getAcquireCharacteristics();
            ac.getPaperHandlingOptions().enableDuplex(true);

            // Set the file acquire options. The file will be multipage TIFF-LZW format.
            // All pages acquired in a single acquisition will be saved to a multipage TIFF file.
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setType(FileType.TIFFLZWMULTI).
               setName(outDir + "testMultiPageLZW.tif");

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
        DuplexScannerDemo s = new DuplexScannerDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}