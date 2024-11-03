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
package com.dynarithmic.twain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dynarithmic.twain.DTwainConstants.JNIVersion;
import com.dynarithmic.twain.exceptions.DTwainIncompatibleJNIException;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainSession;

public class DTwainGlobalOptions
{
    static private int jniVersion = Is64BitArchitecture()?JNIVersion.JNI_64U:JNIVersion.JNI_32U;
    static public int defaultJNIVersion = jniVersion;
    static private boolean is64Bit = Is64BitArchitecture();
    
    static List<Integer> JNI64List = new ArrayList<Integer>() 
    { 
        {add(DTwainConstants.JNIVersion.JNI_64);  
         add(DTwainConstants.JNIVersion.JNI_64U);  
         add(DTwainConstants.JNIVersion.JNI_64D);  
         add(DTwainConstants.JNIVersion.JNI_64UD);  
         }
    };

    static List<Integer> JNI32List = new ArrayList<Integer>() 
    { 
        {add(DTwainConstants.JNIVersion.JNI_32);  
         add(DTwainConstants.JNIVersion.JNI_32U);  
         add(DTwainConstants.JNIVersion.JNI_32D);  
         add(DTwainConstants.JNIVersion.JNI_32UD);  
         }
    };

    public static int getJNIVersion() { return jniVersion; }
    public static String getJNIVersionAsString()
    {
        String s;
        try
        {
            s = TwainSession.getJNIVersionAsString(jniVersion);
            if ( s == null || s.isEmpty() )
                return "";
            return s;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static void setJNIVersion(int version) throws DTwainIncompatibleJNIException, IllegalArgumentException, IllegalAccessException
    {
        String s;
        s = TwainSession.getJNIVersionAsString(version);
        if ( s == null || s.isEmpty() )
            jniVersion = defaultJNIVersion;
        else
        if ( is64Bit )    
        {
            if ( JNI64List.contains(version))
                jniVersion = version;
            else
                throw new DTwainIncompatibleJNIException();
        }
        else
        {
            if ( JNI32List.contains(version))
                jniVersion = version;
            else
                throw new DTwainIncompatibleJNIException();
        }
    }

    public static void setJNIVersion(String version) throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException
    {
        int versionToSet = TwainSession.getJNIVersionAsInt(version);
        setJNIVersion(versionToSet);
    }
    
    public static boolean Is64BitArchitecture() 
    {
        String arch = System.getProperty("os.arch");
        if (arch.contains("64")) 
            return true;
        return false;
    }
}
