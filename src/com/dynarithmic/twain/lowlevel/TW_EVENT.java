package com.dynarithmic.twain.lowlevel;

public class TW_EVENT extends TwainLowLevel
{
    private TW_MEMREF pEvent = new TW_MEMREF();
    private TW_UINT16 TWMessage = new TW_UINT16();

    public TW_EVENT() {}

    public TW_MEMREF getpEvent()
    {
        return pEvent;
    }

    public TW_UINT16 getTWMessage()
    {
        return TWMessage;
    }

    public TW_EVENT setpEvent(TW_MEMREF pEvent)
    {
        this.pEvent = pEvent;
        return this;
    }

    public TW_EVENT setTWMessage(TW_UINT16 tWMessage)
    {
        TWMessage = tWMessage;
        return this;
    }

    public TW_EVENT setTWMessage(int tWMessage)
    {
        TWMessage.setValue(tWMessage);
        return this;
    }
}
