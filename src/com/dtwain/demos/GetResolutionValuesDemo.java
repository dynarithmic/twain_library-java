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

import com.dynarithmic.twain.exceptions.DTwainRuntimeException;
import com.dynarithmic.twain.highlevel.*;
import com.dynarithmic.twain.highlevel.capabilityinterface.*;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.GetCapOperation;
import com.dynarithmic.twain.lowlevel.TwainConstants.MSG;

public class GetResolutionValuesDemo
{
    public void run()
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion(getClass().getSimpleName());

        TwainSource twainSource = null;
        try
        {
            TwainSession twSession = new TwainSession();
            twainSource = twSession.selectSource();

            if ( twainSource.isOpened() )
            {
                String[] unit = {"Dots per Inch", "Dots per centimeter", "Picas", "Points", "TWIPS", "Pixels"};

                // Get the capability interface.  The CapabilityInterface is where all capabilities
                // are set, retrieved, and queried for the selected device.
                CapabilityInterface ci = twainSource.getCapabilityInterface();


                // Get an operation that will perform a MSG_GET
                GetCapOperation capOp = ci.get();

                // turn on range expansion, just in case capability values returned are stored in a range
                capOp.enableExpandRange(true); // If this is false, only the range's description will be displayed

                // Test if X-RESOULUTION is supported
                if ( ci.isXResolutionSupported() )
                {
                    // Now get the values
                    List<Double> xres = ci.getXResolution(capOp);

                    // X-resolution supported, let's see what the available values are for this device
                    boolean validRange = TwainRangeUtils.isValidRange(xres);
                    
                    // Write out whether the values are range descriptors, or all the actual values.
                    System.out.println( "The X resolution values are as follows:" + (validRange?" (as range)":""));

                    // Now get the device's unit setting.  This must be supported by all devices, however we
                    // will check it for support using exception handling instead of explicitly calling
                    // a function and testing the return value (as we did with the X-Resolution).
                    List<Integer> units = null;
                    int currentUnit = 0;
                    try
                    {
                        // We want to get the current value
                        capOp.setOperation(MSG.MSG_GETCURRENT);

                        // Get the current unit of Measure
                        units = ci.getUnits(capOp);
                        if ( !units.isEmpty())
                            currentUnit = units.get(0);  // This will always be the first value
                    }
                    catch (DTwainRuntimeException capFail)
                    {
                        System.out.println(capFail.getMessage() );
                    }

                    // Output each value for the x-resolution
                    for ( int i = 0; i < xres.size(); ++i )
                        System.out.println( xres.get(i) + " " + unit[currentUnit]);

                    // Now test setting the resolution to first value.
                    List<Double> setValues = new ArrayList<>();
                    setValues.add(xres.get(0));

                    // This will set the values using the default "set" operation (MSG_SET)
                    ci.setXResolution(setValues, ci.set());

                    // Check if this worked
                    if ( ci.getLastCapError().getErrorCode() != 0 )
                        System.out.println("Setting resolution failed for this value: " + xres.get(0));
                    else
                        System.out.println("Setting resolution successful for this value: " + xres.get(0));

                    // end the TWAIN session
                    twSession.stop();
                }
            }
        }
        catch (Exception e1) {
            System.out.println(e1.getMessage());
        }
    }

    public static void main(String[] args)
    {
        GetResolutionValuesDemo gValues = new GetResolutionValuesDemo();
        gValues.run();
    }
}