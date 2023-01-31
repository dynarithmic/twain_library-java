package com.dynarithmic.twain.lowlevel;

public class TW_STATUS extends TwainLowLevel
{
    private TW_UINT16 ConditionCode = new TW_UINT16();
    private TW_UINT16 Data = new TW_UINT16();
    private final TW_UINT16 Reserved = new TW_UINT16();

    public TW_STATUS()
    {
    }

    public TW_UINT16 getConditionCode()
    {
        return ConditionCode;
    }

    public TW_UINT16 getData()
    {
        return Data;
    }

    public TW_UINT16 getReserved()
    {
        return Reserved;
    }

    public TW_STATUS setConditionCode(TW_UINT16 conditionCode)
    {
        ConditionCode = conditionCode;
        return this;
    }

    public TW_STATUS setData(TW_UINT16 data)
    {
        Data = data;
        return this;
    }

    public TW_STATUS setConditionCode(int conditionCode)
    {
        ConditionCode.setValue(conditionCode);
        return this;
    }

    public TW_STATUS setData(int data)
    {
        Data.setValue(data);
        return this;
    }

    public TW_STATUS setReserved(int data)
    {
        Reserved.setValue(data);
        return this;
    }
}
