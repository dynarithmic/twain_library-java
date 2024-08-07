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
import com.dynarithmic.twain.highlevel.EnhancedSourceSelector;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class TwainCallbackDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "";
    public class DemoTwainCallback extends TwainCallback
    {
        private void printInfo(TwainSource sourceHandle, String message)
        {
            System.out.println(message + sourceHandle.getInfo().getProductName());
        }
        
        @Override
        public int onTransferDone(TwainSource sourceHandle)
        {
            printInfo(sourceHandle, " Transfer done ");
            return 1;
        }

        @Override
        public int onTransferReady(TwainSource sourceHandle)
        {
            printInfo(sourceHandle, " Transfer ready ");
            return 1;
        }

        @Override
        public int onTransferCancelled(TwainSource sourceHandle)
        {
            printInfo(sourceHandle, " Transfer cancelled ");
            return 1;
        }

        @Override
        public int onUIOpened(TwainSource sourceHandle)
        {
            printInfo(sourceHandle, " UI is opened ");
            return 1;
        }

        @Override
        public int onUIClosing(TwainSource sourceHandle)
        {
            printInfo(sourceHandle, " UI is closing ");
            return 1;
        }

        @Override
        public int onUIClosed(TwainSource sourceHandle)
        {
            printInfo(sourceHandle, " UI is closed ");
            return 1;
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
            // TwainCallback
            twainSession.registerCallback(ts, new DemoTwainCallback());
            
            // Set the file acquire options. By default, the file will be in BMP format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setName(outDir + "test3.bmp");

            ts.getAcquireCharacteristics().getPaperHandlingOptions().enableFeeder(true);

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
        TwainCallbackDemo s = new TwainCallbackDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}