package com.dynarithmic.twain.lowlevel;

public class TW_SETUPMEMXFER extends TwainLowLevel
{
    private TW_UINT32 MinBufSize = new TW_UINT32();
    private TW_UINT32 MaxBufSize = new TW_UINT32();
    private TW_UINT32 Preferred = new TW_UINT32();

    public TW_SETUPMEMXFER() {}

    public TW_UINT32 getMinBufSize()
    {
        return MinBufSize;
    }
    public TW_UINT32 getMaxBufSize()
    {
        return MaxBufSize;
    }
    public TW_UINT32 getPreferred()
    {
        return Preferred;
    }

    public TW_SETUPMEMXFER setMinBufSize(TW_UINT32 minBufSize)
    {
        MinBufSize = minBufSize;
        return this;
    }
    public TW_SETUPMEMXFER setMaxBufSize(TW_UINT32 maxBufSize)
    {
        MaxBufSize = maxBufSize;
        return this;
    }
    public TW_SETUPMEMXFER setPreferred(TW_UINT32 preferred)
    {
        Preferred = preferred;
        return this;
    }

    public TW_SETUPMEMXFER setMinBufSize(long minBufSize)
    {
        MinBufSize.setValue(minBufSize);
        return this;
    }

    public TW_SETUPMEMXFER setMaxBufSize(long maxBufSize)
    {
        MaxBufSize.setValue(maxBufSize);
        return this;
    }

    public TW_SETUPMEMXFER setPreferred(long preferred)
    {
        Preferred.setValue(preferred);
        return this;
    }
}
