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

public class TW_SETUPMEMXFER extends TwainLowLevel
{
    private TW_UINT32 MinBufSize = new TW_UINT32();
    private TW_UINT32 MaxBufSize = new TW_UINT32();
    private TW_UINT32 Preferred = new TW_UINT32();

    public TW_SETUPMEMXFER() {}

    public TW_UINT32 getMinBufSize()
    {
        return MinBufSize;
    }
    public TW_UINT32 getMaxBufSize()
    {
        return MaxBufSize;
    }
    public TW_UINT32 getPreferred()
    {
        return Preferred;
    }

    public TW_SETUPMEMXFER setMinBufSize(TW_UINT32 minBufSize)
    {
        MinBufSize = minBufSize;
        return this;
    }
    public TW_SETUPMEMXFER setMaxBufSize(TW_UINT32 maxBufSize)
    {
        MaxBufSize = maxBufSize;
        return this;
    }
    public TW_SETUPMEMXFER setPreferred(TW_UINT32 preferred)
    {
        Preferred = preferred;
        return this;
    }

    public TW_SETUPMEMXFER setMinBufSize(long minBufSize)
    {
        MinBufSize.setValue(minBufSize);
        return this;
    }

    public TW_SETUPMEMXFER setMaxBufSize(long maxBufSize)
    {
        MaxBufSize.setValue(maxBufSize);
        return this;
    }

    public TW_SETUPMEMXFER setPreferred(long preferred)
    {
        Preferred.setValue(preferred);
        return this;
    }
}
