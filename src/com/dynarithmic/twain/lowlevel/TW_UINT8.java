package com.dynarithmic.twain.lowlevel;

public class TW_UINT8 extends TwainLowLevel
{
    protected short value = 0;

    private TW_UINT8 setValueImpl(int val)
    {
        value = (short)val;
        return this;
    }

    public TW_UINT8()
    {}

    public TW_UINT8(int val)
    {
        setValue(val);
    }

    public int getValue()
    {
        return value;
    }

    public TW_UINT8 setValue(int val)
    {
        UnsignedUtils.checkUnsigned8(val);
        return setValueImpl(val);
    }
}
