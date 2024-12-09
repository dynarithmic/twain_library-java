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

public class TW_ARRAY extends TwainLowLevel
{
    private final TW_UINT16 ItemType = new TW_UINT16();
    private final TW_UINT32 NumItems = new TW_UINT32();
    private TW_UINT8[] ItemList = new TW_UINT8[0];

    private TW_ARRAY setItems(long val)
    {
       NumItems.setValue(val);
       ItemList = new TW_UINT8[(int) val];
       for (int i = 0; i < val; ++i)
           ItemList[i] = new TW_UINT8();
       return this;
    }

    public TW_ARRAY()
    {}

    public TW_ARRAY setNumItems(long val)
    {
       return setItems(val);
    }

    public TW_ARRAY setNumItems(TW_UINT32 numItems)
    {
        return setNumItems(numItems.getValue());
    }

    public TW_ARRAY setItemType(TW_UINT16 itemType)
    {
        return setItemType(itemType.getValue());
    }

    public TW_ARRAY setItemType(int itemType)
    {
        ItemType.setValue(itemType);
        return this;
    }

    public TW_ARRAY setItemList(byte [] itemList)
    {
        setNumItems(itemList.length);
        for (int i = 0; i < itemList.length; ++i)
        {
            ItemList[i].setValue(itemList[i]);
        }
        return this;
    }

    public TW_ARRAY setItemList(TW_UINT8[] itemList)
    {
        setNumItems(itemList.length);
        ItemList =  itemList.clone();
        return this;
    }

    public TW_UINT32 getNumItems()
    {
       return NumItems;
    }

    public TW_UINT16 getItemType()
    {
        return ItemType;
    }

    public TW_UINT8[] getItemList()
    {
        return ItemList;
    }
}
