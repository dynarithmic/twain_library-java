package com.dynarithmic.twain.lowlevel;

public class TW_INT16 extends TwainLowLevel
{
    protected short value = 0;

    public TW_INT16()
    {
        super();
    }

    public TW_INT16(short val)
    {
        value = val;
    }

    public short getValue()
    {
        return value;
    }

    public TW_INT16 setValue(short val)
    {
        value = val;
        return this;
    }
}
