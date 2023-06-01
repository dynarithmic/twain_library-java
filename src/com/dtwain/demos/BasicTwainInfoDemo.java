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

import java.util.List;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSourceInfo;

public class BasicTwainInfoDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Get info concerning this TWAIN session
        System.out.println("DTWAIN Short Version Info: " + twainSession.getShortVersionName());
        System.out.println("DTWAIN Long Version Info: " + twainSession.getLongVersionName());
        System.out.println("DTWAIN Library Path: " + twainSession.getDTwainPath());
        System.out.println("TWAIN DSM Path in use: " + twainSession.getDSMPath());
        System.out.println("TWAIN Version and Copyright: " + twainSession.getVersionCopyright());
        
        // Get information on the installed TWAIN sources
        System.out.println();
        System.out.println("Available TWAIN Sources:");
        List<TwainSourceInfo> sInfo = twainSession.getAllSourceInfo();
        for ( TwainSourceInfo oneInfo : sInfo )
            System.out.println("   Product Name: " + oneInfo.getProductName());

        // Get a JSON describing the session and all available TWAIN sources
        String details = twainSession.getSessionDetails(2, true);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Here are the details of the TWAIN session:");
        System.out.println(details);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------");
        
        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            String sourceName = ts.getInfo().getProductName();
            String sourceDetails = twainSession.getSourceDetails(sourceName,  2, true);
            System.out.println("Here are the details of the selected TWAIN Source: \"" + sourceName);
            System.out.println(sourceDetails);
        }
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        BasicTwainInfoDemo info = new BasicTwainInfoDemo();
        try
        {
            info.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}