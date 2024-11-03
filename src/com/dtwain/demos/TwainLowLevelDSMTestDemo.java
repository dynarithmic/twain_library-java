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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.javatuples.Triplet;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainAppInfo;
import com.dynarithmic.twain.highlevel.TwainCharacteristics;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.lowlevel.TW_IDENTITY;
import com.dynarithmic.twain.lowlevel.TwainLowLevel;
import com.dynarithmic.twain.lowlevel.TwainTriplet;

public class TwainLowLevelDSMTestDemo
{
    private Map<Triplet<Long, Integer, Integer>, TwainLowLevel> sMap = null;
    void run() throws Exception
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion(getClass().getSimpleName());

        // Get the map of the triplet information
        sMap = TwainTriplet.getTripletMap();

        // create a new twain session
        TwainSession twSession = new TwainSession();

        // Set the JNI to use and the DSM to use (LEGACY)
        TwainCharacteristics tc = twSession.getTwainCharacteristics();

        // Get the app info object and set the product name to our application name
        TwainAppInfo appInfo = tc.getAppInfo();
        appInfo.setProductName("DSM Tester").
                setManufacturer("Dynarithmic Software").
                setProductFamily("Java/JNI Layer");

        // Now start the session
        twSession.start();

        // Print out the TW_IDENTITY for the session that has been started.
        System.out.println("The path of the Twain DSM in use is: " + twSession.getDSMPath());
        TW_IDENTITY appID = twSession.getSessionId();
        System.out.println("The Twain APP Session: " + appID.asString());
        System.out.println();

        // Print out all triplet and object required for call to DTWAIN_CallDSMProc for the "Object" (last) parameter
        // The object type information comes from the JNI layer.
        sMap.forEach((k, v) ->
        {
            try
            {
                String dgName = TwainTriplet.getDGName(k.getValue0().intValue());
                String datName = TwainTriplet.getDATName(k.getValue1().intValue());
                String msgName = TwainTriplet.getMSGName(k.getValue2().intValue());
                Object testObject = TwainTriplet.createObjectFromTriplet(k.getValue0().longValue(), k.getValue1().intValue(), k.getValue2().intValue());
                Object obj = twSession.getAPIHandle().DTWAIN_CreateObjectFromTriplet(k.getValue0().longValue(), k.getValue1().intValue(), k.getValue2().intValue());
                System.out.println("Twain Triplet:" + k + "  [" + dgName +", " + datName + ", " + msgName + "]  " + "requires creation of this object type: " + obj.getClass().getName());
                if ( obj.getClass() != testObject.getClass())
                {
                    System.out.println("The object that was created in Java: " + testObject.getClass().getName() +
                            " does not match the required object " + obj.getClass().getName());
                }
            }
            catch (DTwainJavaAPIException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exc)
            {
                System.out.println(exc.getMessage());
            }
        });
        twSession.stop();
    }

    public static void main(String[] args)
    {
        TwainLowLevelDSMTestDemo theTest = new TwainLowLevelDSMTestDemo();
        try
        {
            theTest.run();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
