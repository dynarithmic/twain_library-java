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

import com.dynarithmic.twain.highlevel.TwainAppInfo;
import com.dynarithmic.twain.highlevel.TwainCharacteristics;
import com.dynarithmic.twain.highlevel.TwainSession;

public class SetApplicationInfoDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Create the TWAIN characteristics that allows a TwainSession to
        // be created, but not started.
        TwainCharacteristics tc = new TwainCharacteristics();

        // Before we start a TWAIN session, let's set the application information.
        // Setting the application info allows the TWAIN Data Source Manager to
        // use these names when logging, displaying Source UI's, etc.
        TwainAppInfo appInfo = tc.getAppInfo();
        appInfo.setManufacturer(this.getClass().getName());
        appInfo.setProductName(this.getClass().getName());

        // Create a TWAIN session that uses our characteristics
        TwainSession twainSession = new TwainSession(tc);

        // Now the underlying TWAIN system will use our application information for
        // things like logging.

        // Close down the TWAIN Session
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        SetApplicationInfoDemo s = new SetApplicationInfoDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}