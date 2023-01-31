package com.dynarithmic.twain.lowlevel;

public class TW_CUSTOMDSDATA extends TwainLowLevel
{
    private TW_UINT32  InfoLength = new TW_UINT32();
    private TW_HANDLE  hData = new TW_HANDLE();

    public TW_CUSTOMDSDATA() {}

    public TW_UINT32 getInfoLength()
    {
        return InfoLength;
    }

    public TW_HANDLE gethData()
    {
        return hData;
    }

    public TW_CUSTOMDSDATA setInfoLength(TW_UINT32 infoLength)
    {
        InfoLength = infoLength;
        return this;
    }

    public TW_CUSTOMDSDATA setInfoLength(long infoLength)
    {
        InfoLength.setValue(infoLength);
        return this;
    }

    public TW_CUSTOMDSDATA sethData(TW_HANDLE hData)
    {
        this.hData = hData;
        return this;
    }

    public TW_CUSTOMDSDATA sethData(long hData)
    {
        this.hData.setValue(hData);
        return this;
    }
}
