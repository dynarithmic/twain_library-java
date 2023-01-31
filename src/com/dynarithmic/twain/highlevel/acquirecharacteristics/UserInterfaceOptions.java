package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import java.util.ArrayList;
import java.util.List;

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class UserInterfaceOptions
{
    private boolean bshowUI = true;
    private boolean bshowUIOnly = false;
    private boolean bshowIndicators = true;
    private List<CAP_INDICATORSMODE> indicatorsMode = new ArrayList<>();

    public UserInterfaceOptions showUI(boolean show)
    {
        this.bshowUI = show;
        return this;
    }

    public UserInterfaceOptions showIndicators(boolean show)
    {
        this.bshowIndicators = show;
        return this;
    }

    public UserInterfaceOptions showUIOnly(boolean show)
    {
        this.bshowUIOnly = show;
        return this;
    }

    public UserInterfaceOptions setIndicatorsMode(List<CAP_INDICATORSMODE> indicatorsMode)
    {
        this.indicatorsMode = indicatorsMode;
        return this;
    }

    public boolean isShowUI()
    {
        return this.bshowUI;
    }

    public boolean isShowUIOnly()
    {
        return this.bshowUIOnly;
    }

    public boolean isShowIndicators()
    {
        return this.bshowIndicators;
    }

    public List<CAP_INDICATORSMODE> getIndicatorsMode()
    {
        return this.indicatorsMode;
    }

    public List<Integer> getIndicatorsModeAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (CAP_INDICATORSMODE mode : indicatorsMode)
        {
            if ( mode != CAP_INDICATORSMODE.TWCI_DEFAULT )
                intList.add(mode.ordinal());
        }
        return intList;
    }

    static protected final int affectedCaps[] = { CAPS.CAP_ENABLEDSUIONLY,
                                                  CAPS.CAP_UICONTROLLABLE,
                                                  CAPS.CAP_INDICATORS,
                                                  CAPS.CAP_INDICATORSMODE};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
