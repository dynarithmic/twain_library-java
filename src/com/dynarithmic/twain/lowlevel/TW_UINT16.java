package com.dynarithmic.twain.lowlevel;

public class TW_UINT16 extends TwainLowLevel
{
    protected int value = 0;

    private TW_UINT16 setValueImpl(int val)
    {
        value = val;
        return this;
    }

    public TW_UINT16()
    {}

    public TW_UINT16(int val)
    {
        setValue(val);
    }

    public int getValue()
    {
        return value;
    }

    public TW_UINT16 setValue(int val)
    {
        UnsignedUtils.checkUnsigned16(val);
        return setValueImpl(val);
    }
}
