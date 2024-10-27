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

import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.ExtendedImageInfo;
import com.dynarithmic.twain.highlevel.ExtendedImageInfo.BarcodeInfo;
import com.dynarithmic.twain.highlevel.ExtendedImageInfo.BarcodeInfo.BarcodeSingleInfo;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.BarcodeDetectionOptions;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;

public class BarcodeDemo
{
	// We will use this callback to get the barcode information that was found
	// by the TWAIN device.  The device must 
	// 1) support Extended Image Information to get any information and
	// 2) Support the barcode specific information from the extended image information
    public class BarcodeCallback extends TwainCallback
    {
        @Override
        public int onTransferDone(TwainSource sourceHandle)
        {
            try 
            {
            	// The barcode info is part of the extended image information
                sourceHandle.getCapabilityInterface().initializeExtendedImageInfo();
                
                // Get the extended information now
                ExtendedImageInfo info = sourceHandle.getExtendedImageInfo();
                
                // Get the barcode information from the extended image info
                BarcodeInfo barInfo = info.getBarcodeInfo();
                
                // Get the number of barcodes detected
                long barcodeCount = barInfo.getCount();
                System.out.println("Barcode count = " + barcodeCount);
                
                // Print out barcode information for each barcode found
                for (long i = 0; i < barcodeCount; ++i)
                {
                	BarcodeSingleInfo singleInfo = barInfo.getSingleInfo((int)i);
                	System.out.println("Barcode #" + (i + 1) + " (x,y) position = (" + 
                			singleInfo.getXCoordinate() + ", " + singleInfo.getYCoordinate() + ")");
                	System.out.println("Barcode #" + (i + 1) + " text = " + singleInfo.getText());
                	System.out.println("Barcode #" + (i + 1) + " type = " + singleInfo.getTypeName(sourceHandle.getTwainSession())); 
                	System.out.println("Barcode #" + (i + 1) + " type value = " + singleInfo.getType()); 
                }
            } catch (DTwainJavaAPIException e) {
                System.out.println("Could not retrieve extended image information");
                System.out.println(e);
            }
            return 1;
        }
    }
	
    // Simple acquire to a file
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
            // callback to get detailed barcode information
            twainSession.registerCallback(ts, new BarcodeCallback());

            // See if the device supports barcodes
            CapabilityInterface ci = ts.getCapabilityInterface();
            if ( !ci.isBarcodeDetectionEnabledSupported() )
            {
                twainSession.stop();
                System.out.println("The device named \"" + ts.getInfo().getProductName() + "\" does not support barcodes");
                return;
            }

            // Enable the bar code detection
            BarcodeDetectionOptions bcOptions = ts.getAcquireCharacteristics().getBarcodeDetectionOptions();
            bcOptions.enable(true);

            // Set the acquire options. We want to acquire using Native mode.
            AcquireCharacteristics ac = ts.getAcquireCharacteristics();
            ac.getGeneralOptions().setAcquireType(AcquireType.NATIVE);

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