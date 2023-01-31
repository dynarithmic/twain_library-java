package com.dynarithmic.twain.lowlevel;

public class TW_FIX32 extends TwainLowLevel
{
    private TW_INT16 Whole = new TW_INT16();
    private TW_UINT16 Frac = new TW_UINT16();

    public TW_FIX32() {}

    public double getValue()
    {
        return (double)Whole.getValue() + (double)Frac.getValue() / 65536.0;
    }

    public TW_FIX32 setValue(double fnum)
    {
        boolean sign = fnum < 0;
        int value = (int) (fnum * 65536.0 + (sign?(-0.5):0.5));
        Whole.setValue((short) (value >> 16));
        Frac.setValue((int)(value & 0x0000ffffL));
        return this;
    }

    public TW_FIX32 setValue(TW_FIX32 fnum)
    {
        Whole = fnum.Whole;
        Frac = fnum.Frac;
        return this;
    }

    public TW_INT16 getWhole()
    {
        return Whole;
    }

    public TW_UINT16 getFrac()
    {
        return Frac;
    }

    public TW_FIX32 setWhole(TW_INT16 whole)
    {
        Whole = whole;
        return this;
    }

    public TW_FIX32 setFrac(TW_UINT16 frac)
    {
        Frac = frac;
        return this;
    }

    public TW_FIX32 setWhole(short whole)
    {
        Whole.setValue(whole);
        return this;
    }

    public TW_FIX32 setFrac(short frac)
    {
        Frac.setValue(frac);
        return this;
    }

    public TW_FIX32 setFrac(int frac)
    {
        Frac.setValue(frac);
        return this;
    }

}
