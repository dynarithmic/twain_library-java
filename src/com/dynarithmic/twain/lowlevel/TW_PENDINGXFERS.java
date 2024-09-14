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

public class TW_PENDINGXFERS extends TwainLowLevel
{
    private TW_UINT16 Count = new TW_UINT16();
    private TW_UINT32 EOJ = new TW_UINT32();

    public TW_PENDINGXFERS() {}

    public TW_UINT16 getCount()
    {
        return Count;
    }

    public TW_UINT32 getEOJ()
    {
        return EOJ;
    }

    public TW_PENDINGXFERS setCount(TW_UINT16 count)
    {
        Count = count;
        return this;
    }

    public TW_PENDINGXFERS setEOJ(TW_UINT32 eOJ)
    {
        EOJ = eOJ;
        return this;
    }

    public TW_PENDINGXFERS setCount(int count)
    {
        Count.setValue(count);
        return this;
    }

    public TW_PENDINGXFERS setEOJ(long eOJ)
    {
        EOJ.setValue(eOJ);
        return this;
    }

}
