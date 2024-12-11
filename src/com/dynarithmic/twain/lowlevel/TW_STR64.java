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

public class TW_STR64 extends TW_STRING
{
    public TW_STR64()
    {
        super(66, 64);
    }

    public TW_STR64(int val1, int val2)
    {
        this();
    }

    public String getValue()
    {
        return super.getValue();
    }

    public TW_STR64 setValue(String s)
    {
        super.setValue(s);
        return this;
    }

    public TW_STR64 setValue(TW_STR64 s)
    {
        super.setValue(s.getValue());
        return this;
    }
}
