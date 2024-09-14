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

public class TW_PALETTE8 extends TwainLowLevel
{
    private TW_UINT16 NumColors = new TW_UINT16();
    private TW_UINT16 PaletteType = new TW_UINT16();
    private TW_ELEMENT8[] Colors = new TW_ELEMENT8[256];

    public TW_PALETTE8()
    {
        for (int i = 0; i < 256; ++i)
            Colors[i] = new TW_ELEMENT8();
    }

    public TW_UINT16 getNumColors()
    {
        return NumColors;
    }

    public TW_UINT16 getPaletteType()
    {
        return PaletteType;
    }

    public TW_ELEMENT8[] getColors()
    {
        return Colors;
    }

    public TW_ELEMENT8 getColorValue(int which)
    {
        return Colors[which];
    }

    public TW_PALETTE8 setNumColors(TW_UINT16 numColors)
    {
        NumColors = numColors;
        return this;
    }

    public TW_PALETTE8 setPaletteType(TW_UINT16 paletteType)
    {
        PaletteType = paletteType;
        return this;
    }

    public TW_PALETTE8 setColors(TW_ELEMENT8[] colors)
    {
        Colors = colors;
        return this;
    }

    public TW_PALETTE8 setPaletteType(int paletteType)
    {
        PaletteType.setValue(paletteType);
        return this;
    }

    public TW_PALETTE8 setNumColors(int numColors)
    {
        NumColors.setValue(numColors);
        return this;
    }

    public TW_PALETTE8 setColorValue(TW_ELEMENT8 val, int which)
    {
        Colors[which] = val;
        return this;
    }

}
