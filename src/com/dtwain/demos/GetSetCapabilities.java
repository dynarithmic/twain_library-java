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

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainFrameDouble;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.SetCapOperation;
import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.ICAP_SUPPORTEDSIZES;

public class GetSetCapabilities
{

    public void run() throws DTwainJavaAPIException
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            System.out.println("The opened source is: " + ts.getInfo().getProductName());

            // Get the interface to the capabilities of the device.
            // Note that the capability interface actually will "talk"
            // to the device, getting and setting the capabilities.  This differs
            // from the acquire characteristics, which only logically sets values
            // without talking to the actual device.
            CapabilityInterface ci = ts.getCapabilityInterface();

            // Get the paper sizes using the provided supportedsizes() function
            List<Integer> allPaperSizes = ci.getSupportedSizes(ci.get());

            // Get all the paper sizes, but use the ICAP_SUPPORTEDSIZES directly.
            // Note that we can use the general get_cap_values function if we want
            // to provide the capability to set as an explicit argument instead of
            // calling the specific capability setting function.
            List<Object> allPaperSizes2 = ci.getCapValues( TwainConstants.CAPS.ICAP_SUPPORTEDSIZES, ci.get());

            boolean allTheSame = true;
            if ( allPaperSizes.size() == allPaperSizes2.size())
            {
                int current = 0;
                for (Integer i : allPaperSizes)
                {
                    if ( i != ((Integer) allPaperSizes2.get(current)).intValue())
                    {
                        allTheSame = false;
                        break;
                    }
                    ++current;
                }
            }
            else
                allTheSame = false;
            if ( allTheSame )
                System.out.println("Paper Sizes are the same when issuing call to get all the paper sizes.");

            int current = 1;
            for (Integer i : allPaperSizes)
            {
                System.out.println("Page size " + current + ": " + ICAP_SUPPORTEDSIZES.values()[i]);
                ++current;
            }

            // Set the current size to the first value found
            // Note that to set capabilities, the first argument is always a List of the
            // of values, and not a single value.
            SetCapOperation setOp = ci.set();
            ci.setSupportedSizes( Arrays.asList(allPaperSizes.get(0)), setOp );

            // See if the current value actually has been set
            List<Integer> currentSize = ci.getSupportedSizes(ci.getCurrent());

            if (currentSize.get(0).intValue() == allPaperSizes.get(0).intValue())
                System.out.println("Set the capability ok");
            else
                System.out.println("Did not set the capability");

            // Now for strings.  Test if the CAP_AUTHOR capability is supported
            if (ci.isAuthorSupported())
            {
                // Get the author
                List<String> author = ci.getAuthor(ci.get());
                if ( !author.isEmpty() )
                    System.out.println("The author is \"" + author.get(0) + "\"");
            }

            // Now for the odd one, frames.  Test the ICAP_FRAMES capability
            if (ci.isFramesSupported())
            {
                // Get the frames.  Returned will be a List of TwainFrameDouble
                List<TwainFrameDouble> allFrames = ci.getFrames(ci.get());
                if (!allFrames.isEmpty())
                {
                    // We only care about the first frame
                    TwainFrameDouble frame = allFrames.get(0);

                    // Write out the frame
                    System.out.println("\nLeft: " + frame.getLeft() +  "\nTop: " + frame.getTop()
                                    +  "\nRight: " + frame.getRight() + "\nBottom: " + frame.getBottom());

                    // Now set the frame
                    frame.setRight(4.5).setBottom(8);
                    System.out.println("The frame we will try to set");
                    System.out.println("Left: " + frame.getLeft());
                    System.out.println("Top: " + frame.getTop());
                    System.out.println("Right: " + frame.getRight());
                    System.out.println("Bottom: " + frame.getBottom());

                    // call TWAIN to set the frame on the device
                    ci.setFrames(allFrames, ci.set());

                    // See if the frame was set by getting the current new frame
                    List<TwainFrameDouble> newFrames = ci.getFrames(ci.getCurrent());
                    if (newFrames.isEmpty())
                        System.out.println("Getting current frame was not successful");
                    else
                    {
                        // Check if the current frame is the same value as the one we set
                        if (newFrames.get(0).equals(frame))
                            System.out.println("The frame was set successfully");
                        else
                            System.out.println("The frame was not set successfully");

                        // Write out the frame
                        TwainFrameDouble testFrame = newFrames.get(0);

                        System.out.println("\nLeft: " + testFrame.getLeft() +  "\nTop: " + testFrame.getTop()
                        +  "\nRight: " + testFrame.getRight() + "\nBottom: " + testFrame.getBottom());
                    }
                }
            }
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

    public static void main(String[] args)
    {
        try
        {
            GetSetCapabilities demo = new GetSetCapabilities();
            demo.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
