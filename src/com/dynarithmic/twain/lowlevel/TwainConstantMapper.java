/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

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
package com.dynarithmic.twain.lowlevel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;

public class TwainConstantMapper<T>
{
    protected final Map<Integer, String> intToStringMap = new TreeMap<>();
    protected final Map<String, Integer> stringToIntMap = new TreeMap<>();
    private final Class<T> constantClass;
    private boolean isInitialized = false;

    public static class StringModifier
    {
        private StringModifier() {}
        public static final int MAKEUPPER = 1;
        public static final int TRIMLEFT = 2;
        public static final int TRIMRIGHT = 4;
        public static final int TRIMALL = TRIMLEFT | TRIMRIGHT;
        public static final int APPLYALL = MAKEUPPER | TRIMALL;
    }

    public TwainConstantMapper(Class<T> theClass)
    {
        constantClass = theClass;
        try
        {
            initialize();
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
        	System.err.println("Cannot map TWAIN constant to string name");
        	System.err.println("Possible exceptions may be thrown by application if accessing Twain constants");
        }
    }

    public void initialize() throws IllegalArgumentException, IllegalAccessException
    {
        if (!isInitialized)
        {

            Field[] declaredFields = FieldUtils.getAllFields(constantClass);
            for (Field field : declaredFields)
            {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                {
                    intToStringMap.put((Integer) field.get(null), field.getName());
                    stringToIntMap.put(field.getName(), (Integer) field.get(null));
                }
            }
        }
        isInitialized = true;
    }

    public int getSize() { return intToStringMap.size(); }
    
    public List<Integer> getInts() 
    { 
        
        List<Integer> ret = new ArrayList<>();
        intToStringMap.forEach((key, value) ->
        {        
            ret.add(key);
        });
        return ret;
    }

    public List<String> getStrings() 
    { 
        
        List<String> ret = new ArrayList<>();
        intToStringMap.forEach((key, value) ->
        {        
            ret.add(value);
        });
        return ret;
    }
    
    public String toString(int nName) throws IllegalArgumentException, IllegalAccessException
    {
        if (!isInitialized)
            initialize();
        String s = intToStringMap.get(nName);
        return s == null ? "" : s;
    }

    public int toInt(String sName, int modifications) throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException
    {
        if (!isInitialized)
            initialize();
        String origName = sName;
        if ((modifications & StringModifier.TRIMRIGHT) == StringModifier.TRIMRIGHT)
            sName = StringUtils.stripEnd(sName, null);
        if ((modifications & StringModifier.TRIMLEFT) == StringModifier.TRIMLEFT)
            sName = StringUtils.stripStart(sName, null);
        if ((modifications & StringModifier.MAKEUPPER) == StringModifier.MAKEUPPER)
            sName = StringUtils.upperCase(sName);
        if ( sName == null )
            throw new DTwainJavaAPIException("The string name \"" + origName + "\" using modifications " + modifications +
                                            " is not mapped to an integer");
        try
        {
            return stringToIntMap.get(sName);
        }
        catch(Exception e)
        {
            throw new DTwainJavaAPIException("The string name \"" + origName + "\" using modifications " + modifications +
                    " is not mapped to an integer\nAdditional Information:\n" + e);
        }
    }

    public int toInt(String sName) throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException
    {
        return toInt(sName, StringModifier.APPLYALL);
    }
}