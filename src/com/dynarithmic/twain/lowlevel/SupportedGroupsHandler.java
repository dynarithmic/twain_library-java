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

import com.dynarithmic.twain.lowlevel.TwainConstants.DF;

public class SupportedGroupsHandler
{
    private static final TwainConstantMapper<DF> s_map = new TwainConstantMapper<>(TwainConstants.DF.class);
    private SupportedGroupsHandler()
    {}

    public static String asString(int val)
    {
        StringBuilder sOut = new StringBuilder();
        long numBits = 32;
        boolean foundGroup = false;
        for (int i = 0; i < numBits; ++i)
        {
            int curGroup = 1 << i;
            if ( (val & curGroup) != 0)
            {
                if ( foundGroup )
                    sOut.append(",");
                try
                {
                    sOut.append(" ").append(s_map.toString(curGroup));
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    sOut.append(" Unknown(").append(curGroup).append(")");
                }
                foundGroup = true;
            }
        }
        return sOut.toString();
    }
}
