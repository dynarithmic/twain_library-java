package com.dynarithmic.twain.lowlevel;

public class TW_ENUMERATION extends TwainLowLevel
{
    private TW_UINT16 ItemType = new TW_UINT16();
    private final TW_UINT32 NumItems = new TW_UINT32();
    private TW_UINT32 CurrentIndex = new TW_UINT32();
    private TW_UINT32 DefaultIndex = new TW_UINT32();
    private TW_UINT8[] ItemList = new TW_UINT8[0];

    public TW_ENUMERATION() {}


    public TW_UINT16 getItemType()
    {
        return ItemType;
    }

    public TW_UINT32 getNumItems()
    {
        return NumItems;
    }

    public TW_UINT32 getCurrentIndex()
    {
        return CurrentIndex;
    }

    public TW_UINT32 getDefaultIndex()
    {
        return DefaultIndex;
    }

    public TW_UINT8[] getItemList()
    {
        return ItemList;
    }

    public TW_ENUMERATION setItemType(TW_UINT16 itemType)
    {
        ItemType = itemType;
        return this;
    }

    public TW_ENUMERATION setNumItems(long l)
    {
        if ( !UnsignedUtils.isUnsigned32(l))
            throw new IllegalArgumentException();
        if ( l != ItemList.length )
        {
            TW_UINT8 [] tempList = new TW_UINT8[(int) l];
            for (int i = 0; i < tempList.length; ++i )
                tempList[i] = new TW_UINT8();
            System.arraycopy(ItemList, 0, tempList, 0, ItemList.length);
            ItemList = tempList;
        }
        NumItems.setValue(l);
        return this;
    }

    public TW_ENUMERATION setNumItems(TW_UINT32 numItems)
    {
        return setNumItems(numItems.getValue());
    }

    public TW_ENUMERATION setCurrentIndex(TW_UINT32 currentIndex)
    {
        CurrentIndex = currentIndex;
        return this;
    }

    public TW_ENUMERATION setCurrentIndex(int currentIndex)
    {
        CurrentIndex.setValue(currentIndex);
        return this;
    }

    public TW_ENUMERATION setCurrentIndex(long currentIndex)
    {
        CurrentIndex.setValue(currentIndex);
        return this;
    }

    public TW_ENUMERATION setDefaultIndex(TW_UINT32 defaultIndex)
    {
        DefaultIndex = defaultIndex;
        return this;
    }

    public TW_ENUMERATION setDefaultIndex(int defaultIndex)
    {
        DefaultIndex.setValue(defaultIndex);
        return this;
    }

    public TW_ENUMERATION setDefaultIndex(long defaultIndex)
    {
        DefaultIndex.setValue(defaultIndex);
        return this;
    }

    public TW_ENUMERATION setItemList(TW_UINT8[] itemList)
    {
        ItemList = itemList;
        setNumItems(itemList.length);
        return this;
    }

    public TW_ENUMERATION setItemList(byte[] itemList)
    {
        setNumItems(itemList.length);
        for (int i = 0; i < itemList.length; ++i)
            ItemList[i].setValue(itemList[i]);
        return this;
    }
}
