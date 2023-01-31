package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants;

public class PowerMonitorOptions
{
    private int powerSaveTime = defaultPowerSaveTime;
    public static final int defaultPowerSaveTime = Integer.MAX_VALUE;

    public PowerMonitorOptions setPowerSaveTime(int powerSaveTime)
    {
        this.powerSaveTime = powerSaveTime;
        return this;
    }

    public int getPowerSaveTime()
    {
        return this.powerSaveTime;
    }

    static protected final int affectedCaps[] = { TwainConstants.CAPS.CAP_POWERSAVETIME };

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}