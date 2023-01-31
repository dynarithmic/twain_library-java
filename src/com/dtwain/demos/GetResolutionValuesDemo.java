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
    public void runDemo()
    {
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
        gValues.runDemo();
    }
}