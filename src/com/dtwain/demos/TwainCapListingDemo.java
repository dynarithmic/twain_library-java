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

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.EnhancedSourceSelector;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;

public class TwainCapListingDemo
{
    private TwainSource twainSource = null;

    private void listCaps()
    {
       System.out.println("Source selected: " + twainSource.getInfo().getProductName());

       // get all the supported capabilities of the device
       CapabilityInterface theCaps = twainSource.getCapabilityInterface();

       // for each one, output the name of the capability along with the value (in parentheses)
       List<Integer> allCaps = theCaps.getCaps();
       for (int cap : allCaps)
           System.out.println(CapabilityInterface.getNameFromCap(cap) + " (" + cap + ")");
    }

    public void run() throws DTwainJavaAPIException
    {
        TwainSession twSession = new TwainSession();
        // Select a TWAIN device and crDTwainJavaAPISourcenSource helper object using the returned
        // TWAIN Source (which is a long type), and the interface we initialized above.
        twainSource = EnhancedSourceSelector.selectSource(twSession);

        if ( twainSource.isOpened())
        {
            // list the caps
            listCaps();
        }
        twSession.stop();
    }

    public static void main(String[] args) throws DTwainJavaAPIException
    {
        TwainCapListingDemo simpleProg = new TwainCapListingDemo();
        simpleProg.run();

        // must be called, since TWAIN dialog is a Swing component
        // and AWT thread was started
        System.exit(0);
    }
}