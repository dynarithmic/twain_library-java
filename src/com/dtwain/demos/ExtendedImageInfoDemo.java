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

import com.dynarithmic.twain.DTwainGlobalOptions;
import com.dynarithmic.twain.DTwainConstants.*;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.*;
import com.dynarithmic.twain.highlevel.ExtendedImageInfo.*;
import com.dynarithmic.twain.highlevel.ExtendedImageInfo.BarcodeInfo.BarcodeSingleInfo;
import com.dynarithmic.twain.highlevel.ExtendedImageInfo.LineDetectionInfo.LineDetectionSingleInfo;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.*;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;

public class ExtendedImageInfoDemo
{
    // We will use this callback to get the ExtendedImageInfo that was found
    // by the TWAIN device.  The device must 
    // 1) support Extended Image Information to get any information and
    // 2) Support the barcode specific information from the extended image information
    public class ExtendedImageInfoCallback extends TwainCallback
    {
        @Override
        // Note that we must query the extended information during
        // onTransferDone().  Attempting to retrieve the
        // extended information at any other time during the acquisition 
        // process will more than likely yield no results, as the 
        // DTWAIN code will check for the TWAIN source being in 
        // TWAIN state 7 (transferred an image).
        public int onTransferDone(TwainSource sourceHandle)
        {
            try 
            {
                // The barcode info is part of the extended image information
                sourceHandle.getCapabilityInterface().initializeExtendedImageInfo();
                
                // Get the extended information now
                ExtendedImageInfo info = sourceHandle.getExtendedImageInfo();

                // Get the number of extended image info supported
                int [] supportedInfos = info.getSupportedExtendedImageInfo(); 
                
                // List the supported infos
                System.out.println("Extended Image Information:");
                System.out.println("Number of items: " + supportedInfos.length);
                System.out.println("Items:");
                int curItem = 1;
                for (int supported : supportedInfos)
                {
                    System.out.println("Item #" + curItem + ": " + ExtendedImageInfo.getTypeName(sourceHandle.getTwainSession(), supported));
                    ++curItem;
                }

                System.out.println();
                // Get the barcode information from the extended image info
                BarcodeInfo barInfo = info.getBarcodeInfo();
                
                // Get the number of barcodes detected
                long barcodeCount = barInfo.getCount();
                System.out.println("Barcode count = " + barcodeCount);
                
                // Print out barcode information for each barcode found
                for (long i = 0; i < barcodeCount; ++i)
                {
                    BarcodeSingleInfo singleInfo = barInfo.getSingleInfo((int)i);
                    System.out.println("Barcode #" + (i + 1) + " (x,y) position = {" + 
                            singleInfo.getXCoordinate() + ", " + singleInfo.getYCoordinate() + "}");
                    System.out.println("Barcode #" + (i + 1) + " text = " + singleInfo.getText());
                    System.out.println("Barcode #" + (i + 1) + " type = " + singleInfo.getTypeName(sourceHandle.getTwainSession())); 
                    System.out.println("Barcode #" + (i + 1) + " type value = " + singleInfo.getType()); 
                }

                // Get the patchcode information
                PatchcodeDetectionInfo pcInfo = info.getPatchcodeDetectionInfo();
                System.out.println();
                System.out.println("patch code information: " + pcInfo.getPatchcode());
                
                // Get the pagesource information
                PageSourceInfo pageSource = info.getPageSourceInfo();
                System.out.println("Camera information: " + pageSource.getCamera());
                System.out.println("Book name: " + pageSource.getBookname());
                System.out.println("Page number: " + pageSource.getPageNumber());
                System.out.println("Document number: " + pageSource.getDocumentNumber());
                System.out.println("Frame number: " + pageSource.getFrameNumber());
                System.out.println("Chapter number: " + pageSource.getChapterNumber());
                System.out.println("PageSide: " + pageSource.getPageSideName(sourceHandle.getTwainSession()));
                TwainFrameDouble frame = pageSource.getFrame();
                System.out.println("Frame: (" + frame.getLeft() + "," +
                                   frame.getTop() + "," +
                                   frame.getRight() + "," +
                                   frame.getBottom() + ")");
                
                // Get the skew status information
                SkewDetectionInfo skewStatus = info.getSkewDetectionInfo();
                System.out.println();
                System.out.println("Skew status: " + skewStatus.getDeskewStatus());
                
                // Shaded area detection info
                ShadedAreaDetectionInfo shadedInfo = info.getShadedAreaDetectionInfo();
                System.out.println("Shaded area count: " + shadedInfo.getCount());
                
                // Test the horizontal line detection
                LineDetectionInfo horz =  info.getHorizontalLineDetectionInfo();
                if ( horz.getCount() > 0)
                {
                    LineDetectionSingleInfo singleInfoH = horz.getSingleInfo(0);
                    System.out.println("Horizontal Line Info:");
                    System.out.println("count: " + horz.getCount());
                    System.out.println("x-coord: " + singleInfoH.getXCoordinate());
                    System.out.println("y-coord: " + singleInfoH.getYCoordinate());
                    System.out.println("thickness: " + singleInfoH.getThickness());
                    System.out.println("length: " + singleInfoH.getLength());
                }
                
                LineDetectionInfo vert =  info.getVerticalLineDetectionInfo();
                if ( vert.getCount() > 0)
                {
                    LineDetectionSingleInfo singleInfoV = vert.getSingleInfo(0);
                    System.out.println("Vertical Line Info:");
                    System.out.println("x-coord: " + singleInfoV.getXCoordinate());
                    System.out.println("y-coord: " + singleInfoV.getYCoordinate());
                    System.out.println("thickness: " + singleInfoV.getThickness());
                    System.out.println("length: " + singleInfoV.getLength());
                }
                
                // Test the image segmentation info
                ImageSegmentationInfo iInfo = info.getImageSegmentationInfo();
                System.out.println();
                System.out.println("Image Segmentation Info:");
                System.out.println("ICCProfile: " + iInfo.getICCProfile());
                System.out.println("Last Segment: " + iInfo.isLastSegment());
                System.out.println("Segment Number: " + iInfo.getSegmentNumber());
                
                // Test the endorsed text
                EndorsedTextInfo endorsedInfo = info.getEndorsedTextInfo();
                System.out.println();
                System.out.println("Endorsed Text Info:");
                System.out.println("Text: " + endorsedInfo.getText());

                // Test the version 2.0 info
                ExtendedImageInfo20 info20 = info.getExtendedImageInfo20();
                System.out.println();
                System.out.println("Image Info 20 data:");
                System.out.println("MagType: " + info20.getMagType());

                // Test the version 2.1 info
                ExtendedImageInfo21 info21 = info.getExtendedImageInfo21();
                System.out.println();
                System.out.println("Image Info 21 data:");
                System.out.println("File system source: " + info21.getFileSystemSource());
                System.out.println("Mag data length: " + info21.getMagDataLength());
                System.out.println("Page Side: " + info21.getPageSide());
                System.out.println("Mag data: " + new String(info21.getMagData()));
                System.out.println("Image Merged: " + info21.getImageMerged());

                // Test the version 2.2 info
                ExtendedImageInfo22 info22 = info.getExtendedImageInfo22();
                System.out.println();
                System.out.println("Image Info 22 data:");
                System.out.println("Paper count: " + info22.getPaperCount());
                
                // Test the version 2.3 info
                ExtendedImageInfo23 info23 = info.getExtendedImageInfo23();
                System.out.println();
                System.out.println("Image Info 23 data:");
                System.out.println("Printer text: " + info23.getPrinterText());
                
                // Test the version 2.4 info
                ExtendedImageInfo24 info24 = info.getExtendedImageInfo24();
                System.out.println();
                System.out.println("Image Info 24 data:");
                System.out.println("TwainDirect data: " + info24.getTwainDirectMetaData());
                
                // Test the version 2.5 info
                ExtendedImageInfo25 info25 = info.getExtendedImageInfo25();
                System.out.println();
                System.out.println("Image Info 25 data:");
                System.out.println("IALevelA: " + info25.getIAFieldValueA());
                System.out.println("IALevel: " + info25.getIALevel());
                System.out.println("Printer: " + info25.getPrinter());
                System.out.println("Num Codes: " + info25.getNumBarCodes());
                System.out.println("Code: " + info25.getBarCode(6));
                
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
            twainSession.registerCallback(ts, new ExtendedImageInfoCallback());

            // See if the device supports barcod/es
            CapabilityInterface ci = ts.getCapabilityInterface();
            if ( !ci.isBarcodeDetectionEnabledSupported() )
                System.out.println("The device named \"" + ts.getInfo().getProductName() + "\" does not support barcodes");
            else
                System.out.println("The device named \"" + ts.getInfo().getProductName() + "\" supports barcodes");
                
            // Enable the bar code detection
            BarcodeDetectionOptions bcOptions = ts.getAcquireCharacteristics().getBarcodeDetectionOptions();
            bcOptions.enable(true);
            
            // see if patch code is supported
            if ( !ci.isPatchcodeDetectionEnabledSupported())
                System.out.println("The device named \"" + ts.getInfo().getProductName() + "\" does not support patchcodes");
            else
                System.out.println("The device named \"" + ts.getInfo().getProductName() + "\" supports patchcodes");

            // Enable the patch code detection
            PatchcodeDetectionOptions pcOptions = ts.getAcquireCharacteristics().getPatchcodeDetectionOptions();
            pcOptions.enable(true);

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
        ExtendedImageInfoDemo s = new ExtendedImageInfoDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}