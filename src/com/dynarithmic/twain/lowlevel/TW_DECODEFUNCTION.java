package com.dynarithmic.twain.lowlevel;

public class TW_DECODEFUNCTION extends TwainLowLevel
{
    private TW_FIX32 StartIn = new TW_FIX32();
    private TW_FIX32 BreakIn = new TW_FIX32();
    private TW_FIX32 EndIn = new TW_FIX32();
    private TW_FIX32 StartOut = new TW_FIX32();
    private TW_FIX32 BreakOut = new TW_FIX32();
    private TW_FIX32 EndOut = new TW_FIX32();
    private TW_FIX32 Gamma = new TW_FIX32();
    private TW_FIX32 SampleCount = new TW_FIX32();

    public TW_DECODEFUNCTION()
    {
    }

    public TW_FIX32 getStartIn()
    {
        return StartIn;
    }

    public TW_FIX32 getBreakIn()
    {
        return BreakIn;
    }

    public TW_FIX32 getEndIn()
    {
        return EndIn;
    }

    public TW_FIX32 getStartOut()
    {
        return StartOut;
    }

    public TW_FIX32 getBreakOut()
    {
        return BreakOut;
    }

    public TW_FIX32 getEndOut()
    {
        return EndOut;
    }

    public TW_FIX32 getGamma()
    {
        return Gamma;
    }

    public TW_FIX32 getSampleCount()
    {
        return SampleCount;
    }

    public TW_DECODEFUNCTION setStartIn(TW_FIX32 startIn)
    {
        StartIn = startIn;
        return this;
    }

    public TW_DECODEFUNCTION setBreakIn(TW_FIX32 breakIn)
    {
        BreakIn = breakIn;
        return this;
    }

    public TW_DECODEFUNCTION setEndIn(TW_FIX32 endIn)
    {
        EndIn = endIn;
        return this;
    }

    public TW_DECODEFUNCTION setStartOut(TW_FIX32 startOut)
    {
        StartOut = startOut;
        return this;
    }

    public TW_DECODEFUNCTION setBreakOut(TW_FIX32 breakOut)
    {
        BreakOut = breakOut;
        return this;
    }

    public TW_DECODEFUNCTION setEndOut(TW_FIX32 endOut)
    {
        EndOut = endOut;
        return this;
    }

    public TW_DECODEFUNCTION setGamma(TW_FIX32 gamma)
    {
        Gamma = gamma;
        return this;
    }

    public TW_DECODEFUNCTION setSampleCount(TW_FIX32 sampleCount)
    {
        SampleCount = sampleCount;
        return this;
    }

    public TW_DECODEFUNCTION setStartIn(double startIn)
    {
        StartIn.setValue(startIn);
        return this;
    }

    public TW_DECODEFUNCTION setBreakIn(double breakIn)
    {
        BreakIn.setValue(breakIn);
        return this;
    }

    public TW_DECODEFUNCTION setEndIn(double endIn)
    {
        EndIn.setValue(endIn);
        return this;
    }

    public TW_DECODEFUNCTION setStartOut(double startOut)
    {
        StartOut.setValue(startOut);
        return this;
    }

    public TW_DECODEFUNCTION setBreakOut(double breakOut)
    {
        BreakOut.setValue(breakOut);
        return this;
    }

    public TW_DECODEFUNCTION setEndOut(double endOut)
    {
        EndOut.setValue(endOut);
        return this;
    }

    public TW_DECODEFUNCTION setGamma(double gamma)
    {
        Gamma.setValue(gamma);
        return this;
    }

    public TW_DECODEFUNCTION setSampleCount(double sampleCount)
    {
        SampleCount.setValue(sampleCount);
        return this;
    }
}
