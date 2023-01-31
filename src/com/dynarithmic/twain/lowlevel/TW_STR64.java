package com.dynarithmic.twain.lowlevel;

public class TW_STR64 extends TW_STRING
{
    public TW_STR64()
    {
        super(66, 64);
    }

    public TW_STR64(int val1, int val2)
    {
        this();
    }

    public String getValue()
    {
        return super.getValue();
    }

    public TW_STR64 setValue(String s)
    {
        super.setValue(s);
        return this;
    }

    public TW_STR64 setValue(TW_STR64 s)
    {
        super.setValue(s.getValue());
        return this;
    }
}
