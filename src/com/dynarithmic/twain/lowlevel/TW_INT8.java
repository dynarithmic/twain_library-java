package com.dynarithmic.twain.lowlevel;

public class TW_INT8 extends TwainLowLevel
{
    protected byte value = 0;

    public TW_INT8()
    {
    }

    public TW_INT8(byte val)
    {
        value = val;
    }

    public byte getValue()
    {
        return value;
    }

    public TW_INT8 setValue(byte b)
    {
        value = b;
        return this;
    }
}
