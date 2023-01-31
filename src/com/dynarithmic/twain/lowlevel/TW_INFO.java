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
