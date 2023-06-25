/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

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

import com.dynarithmic.twain.DTwainConstants.DSMType;
import com.dynarithmic.twain.DTwainConstants.SessionStartupMode;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;

public class ChooseTWAINVersion2DSMDemo 
{
    public void run() throws Exception
    {
        DSMType dsmToTest [] = { DSMType.LEGACY, DSMType.VERSION2 };

        TwainSession twainSession = new TwainSession(SessionStartupMode.NONE);
        
        for (int i = 0; i < 2; ++i)
        {
            // Set the TWAIN Data Source Manager to the version we
            // are using.
            // This must be done before starting a TWAIN session,
            // or if changed while a session has started, the session
            // must be restarted to reflect the change in the DSM being used.
            twainSession.setDSM(dsmToTest[i]);
        
            // Start a TWAIN session
            twainSession.start();

            if (twainSession.isStarted())
            {
                System.out.println("TWAIN DSM Path in use: " + twainSession.getDSMPath());
                TwainSource twainSource = twainSession.selectSource();
                if ( twainSource.isSelected())
                    twainSource.close();
            }
            twainSession.stop();
        }
    }
    
    public static void main(String[] args) 
    {
        ChooseTWAINVersion2DSMDemo demo = new ChooseTWAINVersion2DSMDemo();
        try
        {
            demo.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
