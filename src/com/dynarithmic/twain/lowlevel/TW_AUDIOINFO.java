package com.dynarithmic.twain.lowlevel;

public class TW_AUDIOINFO extends TwainLowLevel
{
   private TW_STR255  Name = new TW_STR255();
   private TW_UINT32  Reserved = new TW_UINT32();

    public TW_AUDIOINFO()
    {}

    public TW_STR255 getName()
    {
       return Name;
    }

    public TW_UINT32 getReserved()
    {
        return Reserved;
    }

    public TW_AUDIOINFO setReserved(TW_UINT32 reserved)
    {
        Reserved = reserved;
        return this;
    }
    public TW_AUDIOINFO setName(TW_STR255 name)
    {
        Name = name;
        return this;
    }

    public TW_AUDIOINFO setReserved(long reserved)
    {
        Reserved.setValue(reserved);
        return this;
    }

    public TW_AUDIOINFO setName(String name)
    {
        Name.setValue(name);
        return this;
    }
}
