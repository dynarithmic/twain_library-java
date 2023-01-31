package com.dynarithmic.twain.lowlevel;

public class TW_HANDLE extends TwainLowLevel
{
    private long handle = 0;

    public TW_HANDLE()
    {}

    public long getHandle()
    {
        return handle;
    }

    public long getValue()
    {
        return getHandle();
    }

    public TW_HANDLE setHandle(long handle)
    {
        this.handle = handle;
        return this;
    }

    public TW_HANDLE setValue(long handle)
    {
        return setHandle(handle);
    }
}
