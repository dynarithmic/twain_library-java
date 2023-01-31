package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

import java.util.ArrayList;
import java.util.List;
public class PatchcodeOptions
{
    private boolean enabled = false;
    private int maxRetries = defaultMaxRetries;
    private int maxSearchPriorities = defaultMaxSearchPriorities;
    private ICAP_PATCHCODESEARCHMODE searchMode = defaultSearchMode;
    private int timeOutValue = defaultTimeout;
    private List<ICAP_SUPPORTEDPATCHCODETYPES> listSearchPriorities = new ArrayList<>();

    public static final ICAP_PATCHCODESEARCHMODE defaultSearchMode = ICAP_PATCHCODESEARCHMODE.TWBD_DEFAULT;
    public static final int defaultTimeout = 0;
    public static final int defaultMaxRetries = 0;
    public static final int defaultMaxSearchPriorities = 0;

    public PatchcodeOptions enable(boolean bSet)
    {
        this.enabled = bSet; return this;
    }

    public PatchcodeOptions setMaxRetries(int num)
    {
        this.maxRetries = num;
        return this;
    }

    public PatchcodeOptions setMaxSearchPriorities(int num)
    {
        this.maxSearchPriorities = num;
        return this;
    }

    public PatchcodeOptions setSearchMode(ICAP_PATCHCODESEARCHMODE sm)
    {
        this.searchMode = sm;
        return this;
    }

    public PatchcodeOptions setTimeout(int num)
    {
        this.timeOutValue = num;
        return this;
    }

    public PatchcodeOptions setSearchPriorities(List<ICAP_SUPPORTEDPATCHCODETYPES> searches)
    {
        this.listSearchPriorities = searches;
        return this;
    }

    public boolean isEnabled() { return this.enabled; }
    public int getMaxRetries() { return this.maxRetries; }
    public int getMaxSearchPriorities() { return this.maxSearchPriorities; }
    public ICAP_PATCHCODESEARCHMODE getSearchMode() { return this.searchMode; }
    public List<ICAP_SUPPORTEDPATCHCODETYPES> getSearchPriorities() { return this.listSearchPriorities; }

    public List<Integer> getSearchPrioritiesAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (ICAP_SUPPORTEDPATCHCODETYPES sm : this.listSearchPriorities )
        {
            if ( sm != ICAP_SUPPORTEDPATCHCODETYPES.TWPCH_DEFAULT )
                intList.add(sm.ordinal());
        }
        return intList;
    }

    public int getTimeout() { return this.timeOutValue; }

    static protected final int affectedCaps[] = {   CAPS.ICAP_PATCHCODEDETECTIONENABLED,
                                                    CAPS.ICAP_SUPPORTEDPATCHCODETYPES,
                                                    CAPS.ICAP_PATCHCODEMAXRETRIES,
                                                    CAPS.ICAP_PATCHCODEMAXSEARCHPRIORITIES,
                                                    CAPS.ICAP_PATCHCODESEARCHMODE,
                                                    CAPS.ICAP_PATCHCODESEARCHPRIORITIES,
                                                    CAPS.ICAP_PATCHCODETIMEOUT};


    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
