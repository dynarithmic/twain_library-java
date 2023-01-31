package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants;

public class ResolutionOptions
{
    private double xResolution = defaultXResolution;
    private double yResolution = defaultYResolution;

    public static final double defaultXResolution = Double.MAX_VALUE;
    public static final double defaultYResolution = Double.MAX_VALUE;

    public ResolutionOptions setXResolution(double xResolution)
    {
        this.xResolution = xResolution;
        return this;
    }

    public ResolutionOptions setYResolution(double yResolution)
    {
        this.yResolution = yResolution;
        return this;
    }

    public double getXResolution()
    {
        return this.xResolution;
    }

    public double getYResolution()
    {
        return this.yResolution;
    }

    static protected final int affectedCaps[] = { TwainConstants.CAPS.ICAP_XRESOLUTION,
                                                  TwainConstants.CAPS.ICAP_YRESOLUTION};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
