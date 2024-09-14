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

import java.util.Arrays;
import java.util.List;

import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.highlevel.EnhancedSourceSelector;
import com.dynarithmic.twain.highlevel.ImageHandler;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.BarcodeDetectionOptions;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.GetCapOperation;

public class BarcodeDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = EnhancedSourceSelector.selectSource(twainSession);
        if ( ts.isOpened() )
        {
            // See if the device supports barcodes
            CapabilityInterface ci = ts.getCapabilityInterface();
            if ( !ci.isBarcodeDetectionEnabledSupported() )
            {
                twainSession.stop();
                System.out.println("The device named \"" + ts.getInfo().getProductName() + "\" does not support barcodes");
                return;
            }

            GetCapOperation getAll = ci.get();
            ci.setBarcodeDetectionEnabled(Arrays.asList(true), ci.set());
            List<Integer> bcOptionsList = ci.getBarcodeSearchMode(getAll);
            for (int i = 0; i < bcOptionsList.size(); ++i)
                System.out.println(bcOptionsList.get(i));
            BarcodeDetectionOptions bcOptions = ts.getAcquireCharacteristics().getBarcodeDetectionOptions();
            bcOptions.enable(true);

            // Set the acquire options. We want to acquire using Native mode.
            AcquireCharacteristics ac = ts.getAcquireCharacteristics();
            ac.getGeneralOptions().setAcquireType(AcquireType.NATIVE);

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
        BarcodeDemo s = new BarcodeDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}