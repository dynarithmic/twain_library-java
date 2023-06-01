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

public class TW_SETUPFILEXFER extends TwainLowLevel
{
    private TW_STR255 FileName = new TW_STR255();
    private TW_UINT16 Format = new TW_UINT16();
    private TW_INT16  VRefNum = new TW_INT16();

    public TW_SETUPFILEXFER()
    {}

    public TW_STR255 getFileName()
    {
        return FileName;
    }

    public TW_UINT16 getFormat()
    {
        return Format;
    }

    public TW_INT16 getVRefNum()
    {
        return VRefNum;
    }

    public TW_SETUPFILEXFER setFileName(TW_STR255 fileName)
    {
        FileName = fileName;
        return this;
    }

    public TW_SETUPFILEXFER setFormat(TW_UINT16 format)
    {
        Format = format;
        return this;
    }

    public TW_SETUPFILEXFER setVRefNum(TW_INT16 vRefNum)
    {
        VRefNum = vRefNum;
        return this;
    }

    public TW_SETUPFILEXFER setFileName(String fileName)
    {
        FileName.setValue(fileName);
        return this;
    }

    public TW_SETUPFILEXFER setFormat(int format)
    {
        Format.setValue(format);
        return this;
    }

    public TW_SETUPFILEXFER setVRefNum(short vRefNum)
    {
        VRefNum.setValue(vRefNum);
        return this;
    }

}
