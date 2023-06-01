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
package com.dynarithmic.twain.lowlevel;

public class TW_STRING extends TwainLowLevel
{
    protected char[] data = null;
    protected int actualsize = 0;
    public TW_STRING()
    {
        data = new char[0];
    }

    public TW_STRING(int allocSize, int actual) throws IllegalArgumentException
    {
        if ( actual > allocSize )
            throw new IllegalArgumentException();
        data = new char [allocSize];
        actualsize = actual;
    }

    protected void setSize(int allocSize, int actual) throws IllegalArgumentException
    {
        if ( actual > allocSize )
            throw new IllegalArgumentException();
        data = new char [allocSize];
        actualsize = actual;
    }

    public String getValue()
    {
        if ( data != null )
        {
            String s = new String(data);
            int nullIndex = s.indexOf(0);
            if ( nullIndex != -1 )
                return s.substring(0, nullIndex);
            return s;
        }
        return "";
    }

    public TW_STRING setValue(String s)
    {
        if ( data != null)
        {
            int minToCopy = Math.min(actualsize,  s.length());
            for (int i = 0; i < minToCopy; ++i)
                data[i] = s.charAt(i);
            data[minToCopy] = '\0';
        }
        return this;
    }

    public char[] getData()
    {
        return data;
    }

    public int getSize()
    {
        return actualsize;
    }
}
