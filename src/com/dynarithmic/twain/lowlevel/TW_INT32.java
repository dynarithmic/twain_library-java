package com.dynarithmic.twain.lowlevel;

public class TW_INT32 extends TwainLowLevel
{
    protected int value = 0;

    public TW_INT32() {}

    public TW_INT32(int val)
    {
        value = val;
    }

    public TW_INT32 setValue(int val)
    {
        value = val;
        return this;
    }

    public int getValue()
    {
        return value;
    }
}
