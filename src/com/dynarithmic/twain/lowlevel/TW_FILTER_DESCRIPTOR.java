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
package com.dynarithmic.twain.lowlevel;

public class TW_FILTER_DESCRIPTOR extends TwainLowLevel
{
    private TW_UINT32 Size = new TW_UINT32();
    private TW_UINT32 HueStart = new TW_UINT32();
    private TW_UINT32 HueEnd = new TW_UINT32();
    private TW_UINT32 SaturationStart = new TW_UINT32();
    private TW_UINT32 SaturationEnd = new TW_UINT32();
    private TW_UINT32 ValueStart = new TW_UINT32();
    private TW_UINT32 ValueEnd = new TW_UINT32();
    private TW_UINT32 Replacement = new TW_UINT32();

    public TW_FILTER_DESCRIPTOR()
    {
    }

    public TW_UINT32 getSize()
    {
        return Size;
    }

    public TW_UINT32 getHueStart()
    {
        return HueStart;
    }

    public TW_UINT32 getHueEnd()
    {
        return HueEnd;
    }

    public TW_UINT32 getSaturationStart()
    {
        return SaturationStart;
    }

    public TW_UINT32 getSaturationEnd()
    {
        return SaturationEnd;
    }

    public TW_UINT32 getValueStart()
    {
        return ValueStart;
    }

    public TW_UINT32 getValueEnd()
    {
        return ValueEnd;
    }

    public TW_UINT32 getReplacement()
    {
        return Replacement;
    }

    public TW_FILTER_DESCRIPTOR setSize(TW_UINT32 size)
    {
        Size = size;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setHueStart(TW_UINT32 hueStart)
    {
        HueStart = hueStart;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setHueEnd(TW_UINT32 hueEnd)
    {
        HueEnd = hueEnd;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setSaturationStart(TW_UINT32 saturationStart)
    {
        SaturationStart = saturationStart;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setSaturationEnd(TW_UINT32 saturationEnd)
    {
        SaturationEnd = saturationEnd;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setValueStart(TW_UINT32 valueStart)
    {
        ValueStart = valueStart;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setValueEnd(TW_UINT32 valueEnd)
    {
        ValueEnd = valueEnd;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setReplacement(TW_UINT32 replacement)
    {
        Replacement = replacement;
        return this;
    }

    public TW_FILTER_DESCRIPTOR setSize(long size)
    {
        Size.setValue(size);
        return this;
    }

    public TW_FILTER_DESCRIPTOR setHueStart(long hueStart)
    {
        HueStart.setValue(hueStart);
        return this;
    }

    public TW_FILTER_DESCRIPTOR setHueEnd(long hueEnd)
    {
        HueEnd.setValue(hueEnd);
        return this;
    }

    public TW_FILTER_DESCRIPTOR setSaturationStart(long saturationStart)
    {
        SaturationStart.setValue(saturationStart);
        return this;
    }

    public TW_FILTER_DESCRIPTOR setSaturationEnd(long saturationEnd)
    {
        SaturationEnd.setValue(saturationEnd);
        return this;
    }

    public TW_FILTER_DESCRIPTOR setValueStart(long valueStart)
    {
        ValueStart.setValue(valueStart);
        return this;
    }

    public TW_FILTER_DESCRIPTOR setValueEnd(long valueEnd)
    {
        ValueEnd.setValue(valueEnd);
        return this;
    }

    public TW_FILTER_DESCRIPTOR setReplacement(long replacement)
    {
        Replacement.setValue(replacement);
        return this;
    }
}
