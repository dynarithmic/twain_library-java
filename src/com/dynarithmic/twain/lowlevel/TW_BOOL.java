package com.dynarithmic.twain.lowlevel;

public class TW_BOOL extends TwainLowLevel
{
    private boolean value = false;

    public TW_BOOL() {}

    public boolean getValue()
    {
        return value;
    }

    public TW_BOOL setValue(boolean value)
    {
        this.value = value;
        return this;
    }

}
