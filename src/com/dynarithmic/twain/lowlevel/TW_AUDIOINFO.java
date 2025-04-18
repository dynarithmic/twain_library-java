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

public class TW_AUDIOINFO extends TwainLowLevel
{
   private TW_STR255  Name = new TW_STR255();
   private TW_UINT32  Reserved = new TW_UINT32();

    public TW_AUDIOINFO()
    {}

    public TW_STR255 getName()
    {
       return Name;
    }

    public TW_UINT32 getReserved()
    {
        return Reserved;
    }

    public TW_AUDIOINFO setReserved(TW_UINT32 reserved)
    {
        Reserved = reserved;
        return this;
    }
    public TW_AUDIOINFO setName(TW_STR255 name)
    {
        Name = name;
        return this;
    }

    public TW_AUDIOINFO setReserved(long reserved)
    {
        Reserved.setValue(reserved);
        return this;
    }

    public TW_AUDIOINFO setName(String name)
    {
        Name.setValue(name);
        return this;
    }
}
