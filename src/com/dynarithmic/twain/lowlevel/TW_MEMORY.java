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

public class TW_MEMORY extends TwainLowLevel
{
    private TW_UINT32  Flags = new TW_UINT32();
    private TW_UINT32  Length = new TW_UINT32();
    private TW_MEMREF  TheMem = new TW_MEMREF();

    public TW_MEMORY() {}

    public TW_UINT32 getFlags()
    {
        return Flags;
    }

    public TW_UINT32 getLength()
    {
        return Length;
    }

    public TW_MEMREF getTheMem()
    {
        return TheMem;
    }

    public TW_MEMORY setFlags(TW_UINT32 flags)
    {
        Flags = flags;
        return this;
    }

    public TW_MEMORY setLength(TW_UINT32 length)
    {
        Length = length;
        return this;
    }

    public TW_MEMORY setFlags(long flags)
    {
        Flags.setValue(flags);
        return this;
    }

    public TW_MEMORY setLength(long length)
    {
        Length.setValue(length);
        return this;
    }

    public TW_MEMORY setTheMem(TW_MEMREF theMem)
    {
        TheMem = theMem;
        return this;
    }

    public TW_MEMORY setTheMem(long theMem)
    {
        TheMem.setValue(theMem);
        return this;
    }
}
