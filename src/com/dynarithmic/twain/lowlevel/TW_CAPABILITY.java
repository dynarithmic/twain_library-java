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

public class TW_CAPABILITY extends TwainLowLevel
{
    private TW_UINT16  Cap = new TW_UINT16();
    private TW_UINT16  ConType = new TW_UINT16();
    private TW_HANDLE  hContainer = new TW_HANDLE();

    public TW_CAPABILITY() {}

    public TW_UINT16 getCap()
    {
        return Cap;
    }

    public TW_UINT16 getConType()
    {
        return ConType;
    }

    public TW_HANDLE gethContainer()
    {
        return hContainer;
    }

    public TW_CAPABILITY setCap(TW_UINT16 cap)
    {
        Cap = cap;
        return this;
    }

    public TW_CAPABILITY setCap(int cap)
    {
        Cap.setValue(cap);
        return this;
    }

    public TW_CAPABILITY setConType(TW_UINT16 conType)
    {
        ConType = conType;
        return this;
    }

    public TW_CAPABILITY setConType(int conType)
    {
        ConType.setValue(conType);
        return this;
    }

    public TW_CAPABILITY sethContainer(TW_HANDLE hContainer)
    {
        this.hContainer = hContainer;
        return this;
    }

    public TW_CAPABILITY sethContainer(long hContainer)
    {
        this.hContainer.setHandle(hContainer);
        return this;
    }
}
