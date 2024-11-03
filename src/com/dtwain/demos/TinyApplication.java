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
import com.dynarithmic.twain.highlevel.*;

public class TinyApplication
{
    public static void main(String[] args)
    {
        try
        {
            // Open a session
            TwainSession session = new TwainSession();

            // Select a TWAIN Source
            TwainSource source = session.selectSource();
            if ( source.isOpened() )
                // acquire to a BMP file to the current working directory, with name "temp.bmp"
                // To set the name, see the SimpleFileAcquireBMPDemo.java demo
                source.acquire();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
