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

public class TW_EXTIMAGEINFO extends TwainLowLevel
{
    private final TW_UINT32   NumInfos = new TW_UINT32();
    private TW_INFO[]  Info = new TW_INFO[0];

    public TW_EXTIMAGEINFO() {}

    public TW_UINT32 getNumInfos()
    {
        return NumInfos;
    }

    public TW_INFO[] getInfo()
    {
        return Info;
    }

    public TW_INFO getOneInfo(int i)
    {
        return Info[i];
    }


    public TW_EXTIMAGEINFO setNumInfos(TW_UINT32 numInfos)
    {
        return setNumInfos(numInfos.getValue());
    }

    public TW_EXTIMAGEINFO setInfo(TW_INFO[] info)
    {
        Info = info;
        return this;
    }

    public TW_EXTIMAGEINFO setOneInfo(TW_INFO theInfo, int i )
    {
        Info[i] = theInfo;
        return this;
    }

    public TW_EXTIMAGEINFO setNumInfos(long numInfos)
    {
        UnsignedUtils.checkUnsigned32(numInfos);
        if ( numInfos != Info.length )
        {
            TW_INFO[] tempInfo = new TW_INFO[(int)numInfos];
            for (int i = 0; i < numInfos; ++i )
                tempInfo[i] = new TW_INFO();
            int minInfos = (int) Math.min(numInfos, Info.length);
            if (minInfos >= 0) System.arraycopy(Info, 0, tempInfo, 0, minInfos);
            Info = tempInfo;
        }
        NumInfos.setValue(numInfos);
        return this;
    }
}
