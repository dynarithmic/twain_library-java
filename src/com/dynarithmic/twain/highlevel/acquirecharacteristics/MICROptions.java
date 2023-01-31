package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants;

public class MICROptions
{
    private boolean enabled = false;

    public MICROptions enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    static protected final int affectedCaps[] = { TwainConstants.CAPS.CAP_MICRENABLED };

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
