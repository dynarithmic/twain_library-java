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

public class TW_JPEGCOMPRESSION extends TwainLowLevel
{
    private TW_UINT16   ColorSpace = new TW_UINT16();
    private TW_UINT32   SubSampling = new TW_UINT32();
    private TW_UINT16   NumComponents = new TW_UINT16();
    private TW_UINT16   RestartFrequency = new TW_UINT16();
    private TW_UINT16[] QuantMap = new TW_UINT16[4];
    private TW_MEMORY[] QuantTable = new TW_MEMORY[4];
    private TW_UINT16[] HuffmanMap = new TW_UINT16[4];
    private TW_MEMORY[] HuffmanDC = new TW_MEMORY[2];
    private TW_MEMORY[] HuffmanAC = new TW_MEMORY[2];

    public TW_JPEGCOMPRESSION()
    {
        for (int i = 0; i < 4; ++i)
        {
            QuantMap[i] = new TW_UINT16();
            QuantTable[i] = new TW_MEMORY();
            HuffmanMap[i] = new TW_UINT16();
        }

        for (int i = 0; i < 2; ++i)
        {
            HuffmanDC[i] = new TW_MEMORY();
            HuffmanAC[i] = new TW_MEMORY();
        }
    }

    public TW_UINT16 getQuantMapValue(int i)
    {
        if ( i < 0 || i >= QuantMap.length )
            throw new IllegalArgumentException();
        return QuantMap[i];
    }

    public TW_MEMORY getQuantTableValue(int i)
    {
        if ( i < 0 || i >= QuantTable.length )
            throw new IllegalArgumentException();
        return QuantTable[i];
    }

    public TW_UINT16 getHuffmanMapValue(int i)
    {
        if ( i < 0 || i >= HuffmanMap.length )
            throw new IllegalArgumentException();
        return HuffmanMap[i];
    }

    public TW_MEMORY getHuffmanDCValue(int i)
    {
        if ( i < 0 || i >= HuffmanDC.length )
            throw new IllegalArgumentException();
        return HuffmanDC[i];
    }

    public TW_MEMORY getHuffmanACValue(int i)
    {
        if ( i < 0 || i >= HuffmanAC.length )
            throw new IllegalArgumentException();
        return HuffmanAC[i];
    }

    public TW_UINT16 getColorSpace()
    {
        return ColorSpace;
    }

    public TW_UINT32 getSubSampling()
    {
        return SubSampling;
    }

    public TW_UINT16 getNumComponents()
    {
        return NumComponents;
    }

    public TW_UINT16 getRestartFrequency()
    {
        return RestartFrequency;
    }

    public TW_UINT16[] getQuantMap()
    {
        return QuantMap;
    }

    public TW_MEMORY[] getQuantTable()
    {
        return QuantTable;
    }

    public TW_UINT16[] getHuffmanMap()
    {
        return HuffmanMap;
    }

    public TW_MEMORY[] getHuffmanDC()
    {
        return HuffmanDC;
    }

    public TW_MEMORY[] getHuffmanAC()
    {
        return HuffmanAC;
    }


    public TW_JPEGCOMPRESSION setQuantMapValue(TW_UINT16 newValue, int which)
    {
        QuantMap[which] = newValue;
        return this;
    }

    public TW_JPEGCOMPRESSION setQuantTableValue(TW_MEMORY newValue, int which)
    {
        QuantTable[which] = newValue;
        return this;
    }

    public TW_JPEGCOMPRESSION setHuffmanMapValue(TW_UINT16 newValue, int which)
    {
        HuffmanMap[which] = newValue;
        return this;
    }

    public TW_JPEGCOMPRESSION setHuffmanDCValue(TW_MEMORY newValue, int which)
    {
        HuffmanDC[which] = newValue;
        return this;
    }

    public TW_JPEGCOMPRESSION setHuffmanACValue(TW_MEMORY newValue, int which)
    {
        HuffmanAC[which] = newValue;
        return this;
    }

    public TW_JPEGCOMPRESSION setColorSpace(TW_UINT16 colorSpace)
    {
        ColorSpace = colorSpace;
        return this;
    }

    public TW_JPEGCOMPRESSION setSubSampling(TW_UINT32 subSampling)
    {
        SubSampling = subSampling;
        return this;
    }

    public TW_JPEGCOMPRESSION setNumComponents(TW_UINT16 numComponents)
    {
        NumComponents = numComponents;
        return this;
    }

    public TW_JPEGCOMPRESSION setRestartFrequency(TW_UINT16 restartFrequency)
    {
        RestartFrequency = restartFrequency;
        return this;
    }

    public TW_JPEGCOMPRESSION setQuantMap(TW_UINT16[] quantMap)
    {
        QuantMap = quantMap;
        return this;
    }

    public TW_JPEGCOMPRESSION setQuantTable(TW_MEMORY[] quantTable)
    {
        QuantTable = quantTable;
        return this;
    }

    public TW_JPEGCOMPRESSION setHuffmanMap(TW_UINT16[] huffmanMap)
    {
        HuffmanMap = huffmanMap;
        return this;
    }

    public TW_JPEGCOMPRESSION setHuffmanDC(TW_MEMORY[] huffmanDC)
    {
        HuffmanDC = huffmanDC;
        return this;
    }

    public TW_JPEGCOMPRESSION setHuffmanAC(TW_MEMORY[] huffmanAC)
    {
        HuffmanAC = huffmanAC;
        return this;
    }

    public TW_JPEGCOMPRESSION setColorSpace(int colorSpace)
    {
        ColorSpace.setValue(colorSpace);
        return this;
    }

    public TW_JPEGCOMPRESSION setSubSampling(long subSampling)
    {
        SubSampling.setValue(subSampling);
        return this;
    }

    public TW_JPEGCOMPRESSION setNumComponents(int numComponents)
    {
        NumComponents.setValue(numComponents);
        return this;
    }

    public TW_JPEGCOMPRESSION setRestartFrequency(int restartFrequency)
    {
        RestartFrequency.setValue(restartFrequency);
        return this;
    }
}
