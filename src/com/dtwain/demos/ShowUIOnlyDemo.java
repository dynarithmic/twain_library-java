/*  
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2026 Dynarithmic Software.

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
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;

public class ShowUIOnlyDemo
{
    public void run() throws Exception
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion(getClass().getSimpleName());
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();
        // Select a TWAIN Source using the enhanced Select Source dialog 
        TwainSource ts = EnhancedSourceSelector.selectSource(twainSession);
        if ( ts.isOpened() )
        {
            // Test for the ShowUIOnly mode by querying the capabilities
            CapabilityInterface ci = ts.getCapabilityInterface();
            if ( !ci.isEnableDSUIOnlySupported() )
                System.out.println("The source " + ts.getInfo().getProductName() + " does not support UI-Only mode");
            else
            {
                // Turn on UI-only mode
                ts.getAcquireCharacteristics().getUserInterfaceOptions().showUIOnly(true);
                
                // Start the process of showing the UI
                AcquireReturnInfo retInfo = ts.acquire();
                
                if ( retInfo.getReturnCode() == ErrorCode.ERROR_NONE )
                    System.out.println("Acquisition process started and ended successfully");
                else
                    System.out.println("Acquisition process failed with error: " + retInfo.getReturnCode());
            }
        }
        // Close down the TWAIN Session
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        ShowUIOnlyDemo s = new ShowUIOnlyDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}