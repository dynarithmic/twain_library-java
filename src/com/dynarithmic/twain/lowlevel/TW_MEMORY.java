package com.dynarithmic.twain.lowlevel;

public class TW_MEMORY extends TwainLowLevel
{
    private TW_UINT32  Flags = new TW_UINT32();
    private TW_UINT32  Length = new TW_UINT32();
    private TW_MEMREF  TheMem = new TW_MEMREF();

    public TW_MEMORY() {}

    public TW_UINT32 getFlags()
    {
        return Flags;
    }

    public TW_UINT32 getLength()
    {
        return Length;
    }

    public TW_MEMREF getTheMem()
    {
        return TheMem;
    }

    public TW_MEMORY setFlags(TW_UINT32 flags)
    {
        Flags = flags;
        return this;
    }

    public TW_MEMORY setLength(TW_UINT32 length)
    {
        Length = length;
        return this;
    }

    public TW_MEMORY setFlags(long flags)
    {
        Flags.setValue(flags);
        return this;
    }

    public TW_MEMORY setLength(long length)
    {
        Length.setValue(length);
        return this;
    }

    public TW_MEMORY setTheMem(TW_MEMREF theMem)
    {
        TheMem = theMem;
        return this;
    }

    public TW_MEMORY setTheMem(long theMem)
    {
        TheMem.setValue(theMem);
        return this;
    }
}
