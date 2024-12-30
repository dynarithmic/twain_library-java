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

public class TW_UINT32 extends TwainLowLevel
{
    protected long value = 0;

    public TW_UINT32()
    {}

    public TW_UINT32(long val)
    {
        setValue(val);
    }

    public long getValue()
    {
        return value;
    }

    public TW_UINT32 setValue(long val)
    {
        UnsignedUtils.checkUnsigned32(val);
        value = val;
        return this;
    }

    public TW_UINT32 setValue(TW_UINT32 val)
    {
        return setValue(val.getValue());
    }

}
