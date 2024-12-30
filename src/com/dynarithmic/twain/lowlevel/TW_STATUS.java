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

public class TW_STATUS extends TwainLowLevel
{
    private TW_UINT16 ConditionCode = new TW_UINT16();
    private TW_UINT16 Data = new TW_UINT16();
    private final TW_UINT16 Reserved = new TW_UINT16();

    public TW_STATUS()
    {
    }

    public TW_UINT16 getConditionCode()
    {
        return ConditionCode;
    }

    public TW_UINT16 getData()
    {
        return Data;
    }

    public TW_UINT16 getReserved()
    {
        return Reserved;
    }

    public TW_STATUS setConditionCode(TW_UINT16 conditionCode)
    {
        ConditionCode = conditionCode;
        return this;
    }

    public TW_STATUS setData(TW_UINT16 data)
    {
        Data = data;
        return this;
    }

    public TW_STATUS setConditionCode(int conditionCode)
    {
        ConditionCode.setValue(conditionCode);
        return this;
    }

    public TW_STATUS setData(int data)
    {
        Data.setValue(data);
        return this;
    }

    public TW_STATUS setReserved(int data)
    {
        Reserved.setValue(data);
        return this;
    }
}
