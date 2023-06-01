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

public class TW_METRICS extends TwainLowLevel
{
    private TW_UINT32 SizeOf = new TW_UINT32();
    private TW_UINT32 ImageCount = new TW_UINT32();
    private TW_UINT32 SheetCount = new TW_UINT32();

    public TW_METRICS()
    {
    }

    public TW_UINT32 getSizeOf()
    {
        return SizeOf;
    }

    public TW_UINT32 getImageCount()
    {
        return ImageCount;
    }

    public TW_UINT32 getSheetCount()
    {
        return SheetCount;
    }

    public TW_METRICS setSizeOf(TW_UINT32 sizeOf)
    {
        SizeOf = sizeOf;
        return this;
    }

    public TW_METRICS setImageCount(TW_UINT32 imageCount)
    {
        ImageCount = imageCount;
        return this;
    }

    public TW_METRICS setSheetCount(TW_UINT32 sheetCount)
    {
        SheetCount = sheetCount;
        return this;
    }

    public TW_METRICS setSizeOf(long sizeOf)
    {
        SizeOf.setValue(sizeOf);
        return this;
    }

    public TW_METRICS setImageCount(long imageCount)
    {
        ImageCount.setValue(imageCount);
        return this;
    }

    public TW_METRICS setSheetCount(long sheetCount)
    {
        SheetCount.setValue(sheetCount);
        return this;
    }
}
