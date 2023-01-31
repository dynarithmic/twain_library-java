package com.dynarithmic.twain.lowlevel;

public class TW_UINT32 extends TwainLowLevel
{
    protected long value = 0;

    public TW_UINT32()
    {}

    public TW_UINT32(long val)
    {
        setValue(val);
    }

    public long getValue()
    {
        return value;
    }

    public TW_UINT32 setValue(long val)
    {
        UnsignedUtils.checkUnsigned32(val);
        value = val;
        return this;
    }

    public TW_UINT32 setValue(TW_UINT32 val)
    {
        return setValue(val.getValue());
    }

}
