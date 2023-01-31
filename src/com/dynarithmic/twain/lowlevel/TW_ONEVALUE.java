package com.dynarithmic.twain.lowlevel;

public class TW_ONEVALUE extends TwainLowLevel
{
    private TW_UINT16 ItemType = new TW_UINT16();
    private TW_UINT32 Item = new TW_UINT32();

    public TW_ONEVALUE()
    {
    }

    public TW_UINT16 getItemType()
    {
        return ItemType;
    }

    public TW_UINT32 getItem()
    {
        return Item;
    }

    public TW_ONEVALUE setItemType(TW_UINT16 itemType)
    {
        ItemType = itemType;
        return this;
    }

    public TW_ONEVALUE setItem(TW_UINT32 item)
    {
        Item = item;
        return this;
    }

    public TW_ONEVALUE setItemType(int itemType)
    {
        ItemType.setValue(itemType);
        return this;
    }

    public TW_ONEVALUE setItem(long item)
    {
        Item.setValue(item);
        return this;
    }

}
