package com.dynarithmic.twain.lowlevel;

public class TW_CIEPOINT extends TwainLowLevel
{
    private TW_FIX32 X = new TW_FIX32();
    private TW_FIX32 Y = new TW_FIX32();
    private TW_FIX32 Z = new TW_FIX32();

    public TW_CIEPOINT() {}

    public TW_FIX32 getX()
    {
        return X;
    }

    public TW_FIX32 getY()
    {
        return Y;
    }

    public TW_FIX32 getZ()
    {
        return Z;
    }

    public TW_CIEPOINT setX(TW_FIX32 x)
    {
        X = x;
        return this;
    }

    public TW_CIEPOINT setY(TW_FIX32 y)
    {
        Y = y;
        return this;
    }

    public TW_CIEPOINT setZ(TW_FIX32 z)
    {
        Z = z;
        return this;
    }

    public TW_CIEPOINT setX(double x)
    {
        X.setValue(x);
        return this;
    }

    public TW_CIEPOINT setY(double y)
    {
        Y.setValue(y);
        return this;
    }

    public TW_CIEPOINT setZ(double z)
    {
        Z.setValue(z);
        return this;
    }
}
