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

public class TW_STATUSUTF8 extends TwainLowLevel
{
    private TW_STATUS Status = new TW_STATUS();
    private TW_UINT32 Size = new TW_UINT32();
    private TW_HANDLE UTF8string = new TW_HANDLE();

    public TW_STATUSUTF8()
    {
    }

    public TW_STATUS getStatus()
    {
        return Status;
    }

    public TW_UINT32 getSize()
    {
        return Size;
    }

    public TW_HANDLE getUTF8string()
    {
        return UTF8string;
    }

    public TW_STATUSUTF8 setStatus(TW_STATUS status)
    {
        Status = status;
        return this;
    }

    public TW_STATUSUTF8 setSize(TW_UINT32 size)
    {
        Size = size;
        return this;
    }

    public TW_STATUSUTF8 setUTF8string(TW_HANDLE uTF8string)
    {
        UTF8string = uTF8string;
        return this;
    }

    public TW_STATUSUTF8 setSize(long size)
    {
        Size.setValue(size);
        return this;
    }

}
