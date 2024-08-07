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

import java.util.ArrayList;
import java.util.List;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.GetCapOperation;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.SetCapOperation;
import com.dynarithmic.twain.lowlevel.TwainConstants.ICAP_PIXELTYPE;

public class GetSetColorTypeDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // Output to console the color types and bit depths supported by
            // the selected device
            CapabilityInterface ci = ts.getCapabilityInterface();

            // Get an operation that will perform a MSG_GET
            GetCapOperation capOp = ci.get();

            // Now get all the color (pixel) types supported
            List<Integer> allColorTypes = ci.getPixelType(capOp);

            // Loop through all the types, and get the bit depths supported.
            // To do this, we need to set the pixel type before getting the bit
            // depths associated with the type.
            SetCapOperation setOp = ci.set();
            List<Integer> setList = new ArrayList<>();
            setList.add(1);
            System.out.println("Pixel type and bit depth info for device \"" + ts.getInfo().getProductName() + "\":");

            ICAP_PIXELTYPE [] ptValues = ICAP_PIXELTYPE.values();
            for (int colorType : allColorTypes)
            {
                System.out.println();

                // Let's be tricky and convert the integer to the ICAP_PIXELTYPE enum name
                // Note that ICAP_PIXELTYPE is the Twain capability we're actually utilizing
                System.out.println("Color Type " + ptValues[colorType].name() + " has the following supported bit depths:");
                setList.set(0, colorType);
                ci.setPixelType(setList, setOp);
                if (ci.getLastError() == DTwainConstants.ErrorCode.ERROR_NONE.value())
                {
                    // Get the bit depth values associated with the selected color type
                    List<Integer> bitDepthList = ci.getBitDepth(capOp);
                    int current = 1;
                    for (int bitDepth : bitDepthList)
                    {
                        System.out.println("BitDepth " + current + ": " + bitDepth);
                        ++current;
                    }
                }
            }
        }
        else
        {
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
        GetSetColorTypeDemo s = new GetSetColorTypeDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}