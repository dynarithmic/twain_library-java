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

import java.math.BigInteger;

public class TW_UINTPTR extends TwainLowLevel
{
    protected BigInteger value = BigInteger.valueOf(0);

    public TW_UINTPTR()
    {}

    public TW_UINTPTR(long val)
    {
        setValue(val);
    }

    public TW_UINTPTR(String val)
    {
        setValue(val);
    }

    public TW_UINTPTR(BigInteger val)
    {
        setValue(val);
    }

    public TW_UINTPTR setValue(BigInteger val)
    {
        UnsignedUtils.checkUnsigned64(val);
        value = new BigInteger(val.toString());
        return this;
    }

    public TW_UINTPTR setValue(long val)
    {
        UnsignedUtils.checkUnsigned64(val);
        value = new BigInteger(val + "");
        return this;
    }

    public TW_UINTPTR setValue(String s)
    {
        value = new BigInteger(s);
        return this;
    }

    public long getValue()
    {
        return value.longValue();
    }

    public BigInteger getValueAsUnsigned()
    {
        return value;
    }

    public String getValueAsString()
    {
        return value.toString();
    }
}
