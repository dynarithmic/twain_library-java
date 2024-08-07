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

public class TW_INFO extends TwainLowLevel
{
    private TW_UINT16   InfoID = new TW_UINT16();
    private TW_UINT16   ItemType = new TW_UINT16();
    private TW_UINT16   NumItems = new TW_UINT16();
    private TW_UINT16   ReturnCode = new TW_UINT16();
    private TW_UINTPTR  Item = new TW_UINTPTR();

    public TW_INFO() {}

    public TW_UINT16 getInfoID()
    {
        return InfoID;
    }

    public TW_UINT16 getItemType()
    {
        return ItemType;
    }

    public TW_UINT16 getNumItems()
    {
        return NumItems;
    }

    public TW_UINT16 getReturnCode()
    {
        return ReturnCode;
    }

    public TW_UINTPTR getItem()
    {
        return Item;
    }

    public TW_INFO setInfoID(TW_UINT16 infoID)
    {
        InfoID = infoID;
        return this;
    }

    public TW_INFO setItemType(TW_UINT16 itemType)
    {
        ItemType = itemType;
        return this;
    }

    public TW_INFO setNumItems(TW_UINT16 numItems)
    {
        NumItems = numItems;
        return this;
    }

    public TW_INFO setReturnCode(TW_UINT16 returnCode)
    {
        ReturnCode = returnCode;
        return this;
    }

    public TW_INFO setItem(TW_UINTPTR item)
    {
        Item = item;
        return this;
    }

    public TW_INFO setInfoID(int infoID)
    {
        InfoID.setValue(infoID);
        return this;
    }

    public TW_INFO setItemType(int itemType)
    {
        ItemType.setValue(itemType);
        return this;
    }

    public TW_INFO setNumItems(int numItems)
    {
        NumItems.setValue(numItems);
        return this;
    }

    public TW_INFO setReturnCode(int returnCode)
    {
        ReturnCode.setValue(returnCode);
        return this;
    }

    public TW_INFO setItem(long item)
    {
        Item.setValue(item);
        return this;
    }

    public TW_INFO setItem(BigInteger item)
    {
        Item.setValue(item);
        return this;
    }
}
