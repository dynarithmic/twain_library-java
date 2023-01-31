package com.dynarithmic.twain.lowlevel;

public class TW_MEMREF extends TwainLowLevel
{
    private long handle = 0;

    public TW_MEMREF()
    {}

    public long getHandle()
    {
        return handle;
    }

    public long getValue()
    {
        return getHandle();
    }

    public TW_MEMREF setHandle(long handle)
    {
        this.handle = handle;
        return this;
    }

    public TW_MEMREF setValue(long handle)
    {
        return setHandle(handle);
    }
}
