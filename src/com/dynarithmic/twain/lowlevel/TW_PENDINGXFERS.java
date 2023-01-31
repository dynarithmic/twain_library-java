package com.dynarithmic.twain.lowlevel;

public class TW_PENDINGXFERS extends TwainLowLevel
{
    private TW_UINT16 Count = new TW_UINT16();
    private TW_UINT32 EOJ = new TW_UINT32();

    public TW_PENDINGXFERS() {}

    public TW_UINT16 getCount()
    {
        return Count;
    }

    public TW_UINT32 getEOJ()
    {
        return EOJ;
    }

    public TW_PENDINGXFERS setCount(TW_UINT16 count)
    {
        Count = count;
        return this;
    }

    public TW_PENDINGXFERS setEOJ(TW_UINT32 eOJ)
    {
        EOJ = eOJ;
        return this;
    }

    public TW_PENDINGXFERS setCount(int count)
    {
        Count.setValue(count);
        return this;
    }

    public TW_PENDINGXFERS setEOJ(long eOJ)
    {
        EOJ.setValue(eOJ);
        return this;
    }

}
