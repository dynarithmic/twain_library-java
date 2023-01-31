package com.dynarithmic.twain.lowlevel;

public class TW_STATUSUTF8 extends TwainLowLevel
{
    private TW_STATUS Status = new TW_STATUS();
    private TW_UINT32 Size = new TW_UINT32();
    private TW_HANDLE UTF8string = new TW_HANDLE();

    public TW_STATUSUTF8()
    {
    }

    public TW_STATUS getStatus()
    {
        return Status;
    }

    public TW_UINT32 getSize()
    {
        return Size;
    }

    public TW_HANDLE getUTF8string()
    {
        return UTF8string;
    }

    public TW_STATUSUTF8 setStatus(TW_STATUS status)
    {
        Status = status;
        return this;
    }

    public TW_STATUSUTF8 setSize(TW_UINT32 size)
    {
        Size = size;
        return this;
    }

    public TW_STATUSUTF8 setUTF8string(TW_HANDLE uTF8string)
    {
        UTF8string = uTF8string;
        return this;
    }

    public TW_STATUSUTF8 setSize(long size)
    {
        Size.setValue(size);
        return this;
    }

}
