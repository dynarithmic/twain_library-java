/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

import java.util.ArrayList;
import java.util.List;
public class PatchcodeDetectionOptions
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

    public PatchcodeDetectionOptions enable(boolean bSet)
    {
        this.enabled = bSet; return this;
    }

    public PatchcodeDetectionOptions setMaxRetries(int num)
    {
        this.maxRetries = num;
        return this;
    }

    public PatchcodeDetectionOptions setMaxSearchPriorities(int num)
    {
        this.maxSearchPriorities = num;
        return this;
    }

    public PatchcodeDetectionOptions setSearchMode(ICAP_PATCHCODESEARCHMODE sm)
    {
        this.searchMode = sm;
        return this;
    }

    public PatchcodeDetectionOptions setTimeout(int num)
    {
        this.timeOutValue = num;
        return this;
    }

    public PatchcodeDetectionOptions setSearchPriorities(List<ICAP_SUPPORTEDPATCHCODETYPES> searches)
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
