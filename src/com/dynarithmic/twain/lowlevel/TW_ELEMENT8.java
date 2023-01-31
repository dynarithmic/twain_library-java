package com.dynarithmic.twain.lowlevel;

public class TW_ELEMENT8 extends TwainLowLevel
{
    private TW_UINT8 Index = new TW_UINT8();
    private TW_UINT8 Channel1 = new TW_UINT8();
    private TW_UINT8 Channel2 = new TW_UINT8();
    private TW_UINT8 Channel3 = new TW_UINT8();

    public TW_ELEMENT8() {}

    public TW_UINT8 getIndex()
    {
        return Index;
    }

    public TW_UINT8 getChannel1()
    {
        return Channel1;
    }

    public TW_UINT8 getChannel2()
    {
        return Channel2;
    }

    public TW_UINT8 getChannel3()
    {
        return Channel3;
    }

    public TW_ELEMENT8 setIndex(TW_UINT8 index)
    {
        Index = index;
        return this;
    }

    public TW_ELEMENT8 setChannel1(TW_UINT8 channel1)
    {
        Channel1 = channel1;
        return this;
    }

    public TW_ELEMENT8 setChannel2(TW_UINT8 channel2)
    {
        Channel2 = channel2;
        return this;
    }

    public TW_ELEMENT8 setChannel3(TW_UINT8 channel3)
    {
        Channel3 = channel3;
        return this;
    }

    public TW_ELEMENT8 setIndex(int index)
    {
        Index.setValue(index);
        return this;
    }

    public TW_ELEMENT8 setChannel1(int channel1)
    {
        Channel1.setValue(channel1);
        return this;
    }

    public TW_ELEMENT8 setChannel2(int channel2)
    {
        Channel2.setValue(channel2);
        return this;
    }

    public TW_ELEMENT8 setChannel3(int channel3)
    {
        Channel3.setValue(channel3);
        return this;
    }
}
