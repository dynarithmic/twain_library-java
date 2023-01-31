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
