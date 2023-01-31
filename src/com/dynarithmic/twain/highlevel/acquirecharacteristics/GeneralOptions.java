package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.PixelType;
import com.dynarithmic.twain.DTwainConstants.SourceStateAfterAcquire;

public class GeneralOptions
{
    private AcquireType acquireType = defaultAcquireType;
    private int maxPageCount = defaultMaxPageCount;
    private int maxAcquisitions = defaultMaxAcquisitions;
    private SourceStateAfterAcquire sourceStateAfterAcquire = defaultSourceStateAfterAcquire;
    private PixelType pixelType = defaultPixelType;

    public static final AcquireType defaultAcquireType = AcquireType.NATIVEFILE;
    public static final int defaultMaxPageCount = -1;
    public static final int defaultMaxAcquisitions = Integer.MIN_VALUE;
    public static final PixelType defaultPixelType = PixelType.DEFAULT;
    public static final SourceStateAfterAcquire defaultSourceStateAfterAcquire = SourceStateAfterAcquire.OPENED;

    public AcquireType getAcquireType()
    {
        return acquireType;
    }

    public int getMaxPageCount()
    {
        return maxPageCount;
    }

    public int getMaxAcquisitions()
    {
        return maxAcquisitions;
    }

    public SourceStateAfterAcquire getSourceStateAfterAcquire()
    {
        return sourceStateAfterAcquire;
    }

    public GeneralOptions setPixelType(PixelType pixelType)
    {
        this.pixelType = pixelType;
        return this;
    }

    public PixelType getPixelType()
    {
        return pixelType;
    }

    public GeneralOptions setAcquireType(AcquireType acquireType)
    {
        this.acquireType = acquireType;
        return this;
    }

    public GeneralOptions setMaxPageCount(int maxPageCount)
    {
        this.maxPageCount = maxPageCount;
        return this;
    }

    public GeneralOptions setMaxAcquisitions(int maxAcquisitions)
    {
        this.maxAcquisitions = maxAcquisitions;
        return this;
    }

    public GeneralOptions setSourceStateAfterAcquire(SourceStateAfterAcquire sourceStateAfterAcquire)
    {
        this.sourceStateAfterAcquire = sourceStateAfterAcquire;
        return this;
    }


}
