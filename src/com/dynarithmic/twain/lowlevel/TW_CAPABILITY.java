package com.dynarithmic.twain.lowlevel;

public class TW_CAPABILITY extends TwainLowLevel
{
    private TW_UINT16  Cap = new TW_UINT16();
    private TW_UINT16  ConType = new TW_UINT16();
    private TW_HANDLE  hContainer = new TW_HANDLE();

    public TW_CAPABILITY() {}

    public TW_UINT16 getCap()
    {
        return Cap;
    }

    public TW_UINT16 getConType()
    {
        return ConType;
    }

    public TW_HANDLE gethContainer()
    {
        return hContainer;
    }

    public TW_CAPABILITY setCap(TW_UINT16 cap)
    {
        Cap = cap;
        return this;
    }

    public TW_CAPABILITY setCap(int cap)
    {
        Cap.setValue(cap);
        return this;
    }

    public TW_CAPABILITY setConType(TW_UINT16 conType)
    {
        ConType = conType;
        return this;
    }

    public TW_CAPABILITY setConType(int conType)
    {
        ConType.setValue(conType);
        return this;
    }

    public TW_CAPABILITY sethContainer(TW_HANDLE hContainer)
    {
        this.hContainer = hContainer;
        return this;
    }

    public TW_CAPABILITY sethContainer(long hContainer)
    {
        this.hContainer.setHandle(hContainer);
        return this;
    }
}
