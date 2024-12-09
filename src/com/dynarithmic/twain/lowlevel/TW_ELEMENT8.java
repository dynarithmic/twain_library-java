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

public class TW_ELEMENT8 extends TwainLowLevel
{
    private TW_UINT8 Index = new TW_UINT8();
    private TW_UINT8 Channel1 = new TW_UINT8();
    private TW_UINT8 Channel2 = new TW_UINT8();
    private TW_UINT8 Channel3 = new TW_UINT8();

    public TW_ELEMENT8() {}

    public TW_UINT8 getIndex()
    {
        return Index;
    }

    public TW_UINT8 getChannel1()
    {
        return Channel1;
    }

    public TW_UINT8 getChannel2()
    {
        return Channel2;
    }

    public TW_UINT8 getChannel3()
    {
        return Channel3;
    }

    public TW_ELEMENT8 setIndex(TW_UINT8 index)
    {
        Index = index;
        return this;
    }

    public TW_ELEMENT8 setChannel1(TW_UINT8 channel1)
    {
        Channel1 = channel1;
        return this;
    }

    public TW_ELEMENT8 setChannel2(TW_UINT8 channel2)
    {
        Channel2 = channel2;
        return this;
    }

    public TW_ELEMENT8 setChannel3(TW_UINT8 channel3)
    {
        Channel3 = channel3;
        return this;
    }

    public TW_ELEMENT8 setIndex(int index)
    {
        Index.setValue(index);
        return this;
    }

    public TW_ELEMENT8 setChannel1(int channel1)
    {
        Channel1.setValue(channel1);
        return this;
    }

    public TW_ELEMENT8 setChannel2(int channel2)
    {
        Channel2.setValue(channel2);
        return this;
    }

    public TW_ELEMENT8 setChannel3(int channel3)
    {
        Channel3.setValue(channel3);
        return this;
    }
}
